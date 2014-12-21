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

package com.novadart.android.covershow.container;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.novadart.android.covershow.cover.Cover;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CovershowDirector implements Cover.Handler{
    public static final int DEFAULT_FADING_TIME_MS = 500;
    public static final int DISABLE_FADING = -1;

    public static interface Listener {
        public void onCovershowPreparation();
        public void onNextCover(Integer id);
        public void onCovershowTermination();
    }

    private List<Cover> covers = new ArrayList<>();
    private int coverIndex = -1;
    private int fadingTime = DEFAULT_FADING_TIME_MS;

    private final Set<Listener> listeners = new HashSet<>();

    private final ViewGroup container;

    public CovershowDirector(ViewGroup container) {
        this.container = container;
    }

    public void addListener(Listener listener){
        listeners.add(listener);
    }

    public void removeListener(Listener listener){
        listeners.remove(listener);
    }

    public int getFadingTime() {
        return fadingTime;
    }

    public void setFadingTime(int fadingTime) {
        this.fadingTime = fadingTime;
    }

    public void setCovers(List<Cover> covers) {
        if(covers != null) {
            this.covers = covers;
        }
    }

    public void start(){
        for (Listener listener : listeners) {
            listener.onCovershowPreparation();
        }

        container.post(new Runnable() {
            @Override
            public void run() {
                displayNextCover();
            }
        });

    }

    @Override
    public void onCoverExit() {
        displayNextCover();
    }

    protected void displayNextCover(){
        if(fadingTime != DISABLE_FADING){
            displayNextCoverWithFading();
        } else {
            displayNextCoverWithoutFading();
        }
    }

    private void displayNextCoverWithoutFading(){
        int currentCoverIndex = coverIndex;
        int nextCoverIndex = ++coverIndex;

        if(nextCoverIndex < covers.size()){

            // remove the previous one, if present, add the new one
            container.removeAllViews();

            // free the resources occupied by the previous cover BEFORE building the new one
            if(currentCoverIndex >= 0){
                covers.get(currentCoverIndex).destroy();
            }

            // retrieve the next cover
            Cover cover = covers.get(nextCoverIndex);
            // build the view
            View coverView = cover.buildView(this);

            container.addView(coverView);

            // notify listeners about the change
            for (Listener listener : listeners) {
                listener.onNextCover(cover.getId());
            }

        } else {
            cleanup(currentCoverIndex);
        }
    }


    private void displayNextCoverWithFading(){
        if(coverIndex+1 < covers.size()){

            View oldView = container.getChildAt(0);
            animateChangeView(oldView);

        } else {
            cleanup(coverIndex);
        }
    }


    private class ViewsSwapper {
        protected View swapViews(){
            int currentCoverIndex = coverIndex;
            int nextCoverIndex = ++coverIndex;

            // remove the previous one, if present, add the new one
            container.removeAllViews();

            // free the resources occupied by the previous cover BEFORE building the new one
            if(currentCoverIndex >= 0){
                covers.get(currentCoverIndex).destroy();
            }

            // retrieve the next cover
            Cover cover = covers.get(nextCoverIndex);
            // build the view
            View coverView = cover.buildView(CovershowDirector.this);

            setAlphaOnView(coverView, 0f);
            container.addView(coverView);

            // notify listeners about the change
            for (Listener listener : listeners) {
                listener.onNextCover(cover.getId());
            }

            return coverView;
        }
    }


    private void animateChangeView(View oldView){

        if(Build.VERSION.SDK_INT < 12){
            if(oldView != null) {
                AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f);
                alphaAnimation.setDuration(fadingTime);
                alphaAnimation.setAnimationListener(new FadeInAlphaAnimationListener());
                oldView.startAnimation(alphaAnimation);
            } else {
                new FadeInAlphaAnimationListener().onAnimationEnd(null);
            }

        } else {
            if(oldView != null) {
                oldView.animate()
                        .alpha(0f)
                        .setDuration(fadingTime)
                        .setListener(new FadeInAlphaAnimatorListenerAdapter());
            } else {
                new FadeInAlphaAnimatorListenerAdapter().onAnimationEnd(null);
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

    protected void cleanup(int currentCoverIndex){
        container.setVisibility(View.GONE);
        container.removeAllViews();

        for (Listener listener : listeners) {
            listener.onCovershowTermination();
        }

        // free the resources occupied by the previous cover
        if(currentCoverIndex >= 0){
            covers.get(currentCoverIndex).destroy();
        }
    }


    private class FadeInAlphaAnimationListener extends ViewsSwapper implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {}
        @Override
        public void onAnimationRepeat(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {

            View coverView = swapViews();

            AlphaAnimation alphaAnimation = new AlphaAnimation(0f, 1f);
            alphaAnimation.setDuration(fadingTime);
            alphaAnimation.setAnimationListener(null);
            coverView.startAnimation(alphaAnimation);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private class FadeInAlphaAnimatorListenerAdapter extends ViewsSwapper implements Animator.AnimatorListener {
        @Override
        public void onAnimationCancel(Animator animation) {}
        @Override
        public void onAnimationRepeat(Animator animation) {}
        @Override
        public void onAnimationStart(Animator animation) {}

        @Override
        public void onAnimationEnd(Animator animation) {
            View coverView = swapViews();

            coverView.animate()
                    .alpha(1f)
                    .setDuration(fadingTime)
                    .setListener(null);
        }
    }

}
