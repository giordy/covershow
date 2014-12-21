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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.novadart.android.covershow.container.CovershowDirector;
import com.novadart.android.covershow.cover.Cover;

import java.util.List;

public abstract class CovershowActivity extends Activity implements CovershowDirector.Listener {

    private CovershowDirector covershowDirector;

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
            FrameLayout root = new FrameLayout(this);

            root.addView(view, params);

            FrameLayout coversContainer = new FrameLayout(this);
            root.addView(coversContainer);

            super.setContentView(
                    root,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT)
            );

            covershowDirector = new CovershowDirector(coversContainer);
            covershowDirector.setCovers( buildCovers() );
            covershowDirector.addListener(this);
            covershowDirector.start();

        } else {
            super.setContentView(view, params);
        }
    }


    protected abstract boolean shouldDisplayCover();

    protected abstract List<Cover> buildCovers();

    @Override
    public void onCovershowPreparation() {}

    @Override
    public void onNextCover(Integer id) {}

    @Override
    public void onCovershowTermination() {
        covershowDirector.removeListener(this);
        covershowDirector = null;
    }
}
