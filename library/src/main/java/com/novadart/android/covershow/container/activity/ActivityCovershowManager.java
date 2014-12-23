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

package com.novadart.android.covershow.container.activity;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.novadart.android.covershow.container.CovershowAwareContainer;
import com.novadart.android.covershow.cover.Cover;
import com.novadart.android.covershow.director.CovershowDirector;
import com.novadart.android.covershow.director.impl.CovershowDirectorImpl;

import java.util.HashMap;
import java.util.List;

public class ActivityCovershowManager<Identifier> implements CovershowDirector.Listener<Identifier> {

    private CovershowDirector<Identifier> covershowDirector;
    private boolean covershowRunning = false;
    private boolean coversPresent = false;
    private final CovershowAwareContainer<Identifier> covershowAwareContainer;

    public ActivityCovershowManager(CovershowAwareContainer<Identifier> covershowAwareContainer) {
        this.covershowAwareContainer = covershowAwareContainer;
    }

    public void wrapActivityView(Activity activity){
        ViewGroup.LayoutParams fullscreenParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final FrameLayout coversContainer = new FrameLayout(activity);

        final ViewGroup rootView = ((ViewGroup) activity.getWindow().getDecorView().getRootView());
        rootView.post(new Runnable() {
            @Override
            public void run() {
                rootView.addView(coversContainer);
            }
        });

        covershowDirector = new CovershowDirectorImpl<>(coversContainer);
        covershowDirector.addListener(this);
        covershowDirector.addListener(this.covershowAwareContainer);
        covershowDirector.start();
    }

    public boolean isCoversPresent() {
        return coversPresent;
    }

    public void setCovers(List<Cover<Identifier>> covers){
        covershowDirector.setCovers(covers);
        coversPresent = true;
        covershowDirector.start();
    }

    public boolean isCovershowRunning(){
        return covershowRunning;
    }

    @Override
    public void onCovershowPreparation() {}

    @Override
    public void onNextCover(Identifier identifier) {}


    @Override
    public void onCovershowTermination() {
        covershowRunning = false;
    }
}
