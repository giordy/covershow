/*
 * Copyright 2015 Giordano Battilana, Novadart
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.novadart.android.covershow.director.impl;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.novadart.android.covershow.cover.Cover;
import com.novadart.android.covershow.director.CovershowDirector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CovershowDirectorImpl<Identifier> implements CovershowDirector<Identifier> {
    public static final int DEFAULT_FADING_TIME_MS = 500;

    private int fadingTime = DEFAULT_FADING_TIME_MS;
    private final Set<Listener<Identifier>> listeners = new HashSet<>();
    private final ViewGroup container;
    private int coverIndex = -1;
    private List<Cover<Identifier>> covers = new ArrayList<>();

    public CovershowDirectorImpl(ViewGroup container) {
        this.container = container;
    }

    @Override
    public void setCovers(List<Cover<Identifier>> covers) {
        if(covers != null) {
            this.covers = covers;
        }
    }

    @Override
    public void start(){
        getContainer().post(new Runnable() {
            @Override
            public void run() {
                displayNextCover();
            }
        });
    }

    @Override
    public void onCoverExit(Identifier identifier) {
        displayNextCover();
    }

    protected void displayNextCover(){
        if(coverIndex+1 < covers.size()){

            swapCovers(
                    coverIndex >= 0 ? covers.get(coverIndex) : null,
                    covers.get(++coverIndex)
            );

        } else {

            hideLastCover( coverIndex >= 0 ? covers.get(coverIndex) : null );
        }
    }


    protected void cleanup(Cover lastCover){
        getContainer().removeAllViews();

        for (Listener listener : listeners) {
            listener.onPostCovershow();
        }

        // free the resources occupied by the previous cover
        if(lastCover != null){
            lastCover.destroy();
        }

        covers.clear();
    }



    @Override
    public void addListener(Listener<Identifier> listener){
        listeners.add(listener);
    }

    public int getFadingTime() {
        return fadingTime;
    }

    protected ViewGroup getContainer() {
        return container;
    }

    public void setFadingTime(int fadingTime) {
        this.fadingTime = fadingTime;
    }

    private class CoverSwapper {
        private final Cover<Identifier> oldCover;
        private final Cover<Identifier> newCover;

        private CoverSwapper(Cover<Identifier> oldCover, Cover<Identifier> newCover) {
            this.oldCover = oldCover;
            this.newCover = newCover;
        }

        protected View swapCovers(){

            // remove the previous one, if present, add the new one
            container.removeAllViews();

            // free the resources occupied by the previous cover BEFORE building the new one
            if(oldCover != null){
                oldCover.destroy();
            }

            // build the view
            newCover.buildView(CovershowDirectorImpl.this);
            View coverView = newCover.getView();

            setAlphaOnView(coverView, 0f);
            container.addView(coverView);

            // notify listeners about the change
            for (Listener<Identifier> listener : listeners) {
                listener.onNextCover(newCover.getIdentifier());
            }

            return coverView;
        }

    }

    protected void hideLastCover(final Cover lastCover){
        if(Build.VERSION.SDK_INT < 12){
            AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
            alphaAnimation.setDuration(fadingTime);
            alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationRepeat(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    cleanup(lastCover);
                }

            });
            lastCover.getView().startAnimation(alphaAnimation);

        } else {
            lastCover.getView().animate()
                    .alpha(0f)
                    .setDuration(fadingTime)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            cleanup(lastCover);
                        }
                    });
        }
    }

    protected void swapCovers(Cover<Identifier> oldCover, Cover<Identifier> newCover){

        if(Build.VERSION.SDK_INT < 12){
            if(oldCover != null) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
                alphaAnimation.setDuration(fadingTime);
                alphaAnimation.setAnimationListener(new FadeInAlphaAnimationListener(oldCover, newCover));
                oldCover.getView().startAnimation(alphaAnimation);
            } else {
                new FadeInAlphaAnimationListener(null, newCover).onAnimationEnd(null);
            }

        } else {
            if(oldCover != null) {
                oldCover.getView().animate()
                        .alpha(0f)
                        .setDuration(fadingTime)
                        .setListener(new FadeInAlphaAnimatorListenerAdapter(oldCover, newCover));
            } else {
                new FadeInAlphaAnimatorListenerAdapter(null, newCover).onAnimationEnd(null);
            }
        }
    }

    private void setAlphaOnView(View view, float alpha){
        if(Build.VERSION.SDK_INT < 11){
            AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
            alphaAnimation.setDuration(0);
            alphaAnimation.setFillAfter(true);
            view.startAnimation(alphaAnimation);
        } else {
            view.setAlpha(0f);
        }
    }


    private class FadeInAlphaAnimationListener extends CoverSwapper implements Animation.AnimationListener {

        private FadeInAlphaAnimationListener(Cover<Identifier> oldCover, Cover<Identifier> newCover) {
            super(oldCover, newCover);
        }

        @Override
        public void onAnimationStart(Animation animation) {}
        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {

            View coverView = swapCovers();

            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
            alphaAnimation.setDuration(fadingTime);
            alphaAnimation.setAnimationListener(null);
            coverView.startAnimation(alphaAnimation);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private class FadeInAlphaAnimatorListenerAdapter extends CoverSwapper implements Animator.AnimatorListener {

        private FadeInAlphaAnimatorListenerAdapter(Cover<Identifier> oldCover, Cover<Identifier> newCover) {
            super(oldCover, newCover);
        }

        @Override
        public void onAnimationCancel(Animator animation) {}
        @Override
        public void onAnimationRepeat(Animator animation) {}
        @Override
        public void onAnimationStart(Animator animation) {}

        @Override
        public void onAnimationEnd(Animator animation) {
            View coverView = swapCovers();

            coverView.animate()
                    .alpha(1f)
                    .setDuration(fadingTime)
                    .setListener(null);
        }
    }

}
