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

package com.novadart.android.covershow.container.activity.appcompat;


import android.support.v7.app.ActionBarActivity;

import com.novadart.android.covershow.container.CovershowAwareContainer;
import com.novadart.android.covershow.container.activity.ActivityCovershowManager;

public abstract class CovershowActionBarActivity<Identifier> extends ActionBarActivity implements CovershowAwareContainer<Identifier> {

    private ActivityCovershowManager<Identifier> activityCovershowManager = new ActivityCovershowManager<>(this);

    @Override
    protected void onStart() {
        super.onStart();
        activityCovershowManager.wrapActivityView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(shouldDisplayCover() && !activityCovershowManager.isCoversPresent()){
            activityCovershowManager.setCovers( buildCovers() );
        }
    }

    public boolean isCovershowRunning(){
        return activityCovershowManager.isCovershowRunning();
    }

    @Override
    public void onCovershowPreparation() {}

    @Override
    public void onNextCover(Identifier id) {}

    @Override
    public void onCovershowTermination() {}
}
