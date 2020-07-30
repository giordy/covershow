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
import android.os.Bundle;

import com.novadart.android.covershow.container.CovershowContainer;
import com.novadart.android.covershow.container.CovershowManager;
import com.novadart.android.covershow.cover.Cover;

import java.util.List;

public abstract class CovershowActivity<Identifier> extends Activity implements CovershowContainer<Identifier> {

    private CovershowManager<Identifier> covershowManager;
    private boolean autoStart = true;

    @Override
    public void disableAutoStart() {
        this.autoStart = false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(ARG_AUTOSTART, autoStart);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        autoStart = savedInstanceState==null || savedInstanceState.getBoolean(ARG_AUTOSTART, true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(shouldStartCovershow()){

            covershowManager = new ActivityCovershowManager<>(this, this);
            covershowManager.setAutoStart(autoStart);

            onPreCovershow();

            buildCoverList(new AsyncHandler<Identifier>() {
                @Override
                public void setCovers(List<Cover<Identifier>> covers) {
                    covershowManager.setCovers(covers);
                }
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        covershowManager = null;
    }

    @Override
    public void startCovershow() {
        if(covershowManager != null && !covershowManager.isAutoStart()){
            covershowManager.setCovershowCanStart(true);
            covershowManager.startCovershow();
        }
    }

    @Override
    public void onPreCovershow() {}

    @Override
    public void onNextCover(Identifier id) {}

    @Override
    public void onPostCovershow() {}
}
