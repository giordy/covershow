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

package com.novadart.android.covershow.container.fragment;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import com.novadart.android.covershow.container.CovershowContainer;
import com.novadart.android.covershow.container.CovershowDirector;
import com.novadart.android.covershow.cover.Cover;

import java.util.List;

public class CovershowFragmentCore implements CovershowDirector.Listener {

    private CovershowDirector covershowDirector;
    private boolean covershowRunning = false;
    private boolean isFragmentVisibleToUser = false;
    private boolean areCoversPresent = false;
    private final CovershowContainer covershowContainer;

    public CovershowFragmentCore(CovershowContainer covershowContainer) {
        this.covershowContainer = covershowContainer;
    }

    public View initContainer(Activity activity, View fragmentView){
        covershowRunning = true;

        FrameLayout root = new FrameLayout(activity);

        root.addView(fragmentView);

        FrameLayout coversContainer = new FrameLayout(activity);
        root.addView(coversContainer);

        covershowDirector = new CovershowDirector(coversContainer);
        covershowDirector.addListener(this);
        covershowDirector.addListener(this.covershowContainer);

        return root;
    }

    public void setCovers(List<Cover> covers){
        covershowDirector.setCovers( covers );
        areCoversPresent = true;
        tryStart();
    }

    public void setFragmentVisibleToUser(boolean isVisibleToUser) {
        this.isFragmentVisibleToUser = isVisibleToUser;
        tryStart();
    }

    private void tryStart(){
        if(covershowDirector != null && this.isFragmentVisibleToUser && this.areCoversPresent ){
            covershowDirector.start();
        }
    }


    public boolean isCovershowRunning() {
        return covershowRunning;
    }


    @Override
    public void onCovershowPreparation() {}

    @Override
    public void onNextCover(Integer id) {}

    @Override
    public void onCovershowTermination() {
        covershowDirector = null;
        covershowRunning = false;
    }
}
