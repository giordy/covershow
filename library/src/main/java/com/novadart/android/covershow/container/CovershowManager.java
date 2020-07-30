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

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.novadart.android.covershow.cover.Cover;
import com.novadart.android.covershow.director.CovershowDirector;
import com.novadart.android.covershow.director.impl.CovershowDirectorImpl;

import java.util.List;

public abstract class CovershowManager<Identifier> {
    private static final String COVERSHOW_CONTAINER_TAG = "COVERSHOW_CONTAINER_TAG";

    private CovershowDirector<Identifier> covershowDirector;

    private final Activity activity;
    private final CovershowContainer<Identifier> covershowContainer;
    private boolean autoStart = true;
    private boolean covershowCanStart = true;
    private boolean coversAreSet = false;

    public CovershowManager(Activity activity, CovershowContainer<Identifier> covershowContainer) {
        this.activity = activity;
        this.covershowContainer = covershowContainer;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
        this.covershowCanStart = autoStart;
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setCovershowCanStart(boolean covershowCanStart) {
        this.covershowCanStart = covershowCanStart;
    }

    public ViewGroup getCovershowContainer(Activity activity){
        final ViewGroup rootView = ((ViewGroup) activity.getWindow().getDecorView());

        FrameLayout coversContainer = null;

        int childCount = rootView.getChildCount();
        View child;
        Object tag;

        for (int i = 0; i < childCount; i++) {
            child = rootView.getChildAt(i);
            tag = child.getTag();
            if(tag != null && tag.equals(COVERSHOW_CONTAINER_TAG)){
                coversContainer = (FrameLayout) child;
                break;
            }
        }

        if(coversContainer == null) {
            final ViewGroup.LayoutParams fullscreenParams =
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            coversContainer = new FrameLayout(activity);
            coversContainer.setTag(COVERSHOW_CONTAINER_TAG);
            rootView.addView(coversContainer, fullscreenParams);
        }

        return coversContainer;
    }

    public void init(){
        if(covershowDirector == null) {
            covershowDirector = new CovershowDirectorImpl<>(getCovershowContainer(activity));
            covershowDirector.addListener(this.covershowContainer);
        }
    }

    public void setCovers(List<Cover<Identifier>> covers){
        init();
        if( coversAreSet = (covers != null && !covers.isEmpty()) ){
            covershowDirector.setCovers(covers);
        }
    }

    public void startCovershow(){
        init();
        if(covershowPreStartTest()) {
            covershowDirector.start();
        }
    }

    protected boolean covershowPreStartTest(){
        return coversAreSet && ( autoStart || covershowCanStart );
    }

}
