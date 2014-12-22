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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novadart.android.covershow.container.CovershowContainer;
import com.novadart.android.covershow.container.activity.CovershowActivityCore;

public abstract class CovershowActivity extends ActionBarActivity implements CovershowContainer {

    private CovershowActivityCore covershowActivityCore = new CovershowActivityCore(this);

    @Override
    public void setContentView(int layoutResID) {
        setContentView(LayoutInflater.from(this).inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        setContentView(
                view,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        );
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if(shouldDisplayCover()){
            View root = covershowActivityCore.init(this, view, params);
            super.setContentView(
                    root,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT)
            );
        } else {
            super.setContentView(view, params);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(shouldDisplayCover() && !covershowActivityCore.isCoversPresent()){
            covershowActivityCore.setCovers( buildCovers() );
        }
    }

    public boolean isCovershowRunning(){
        return covershowActivityCore.isCovershowRunning();
    }

    @Override
    public void onCovershowPreparation() {}

    @Override
    public void onNextCover(Integer id) {}

    @Override
    public void onCovershowTermination() {}
}
