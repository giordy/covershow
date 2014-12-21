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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.novadart.android.covershow.container.CovershowDirector;
import com.novadart.android.covershow.cover.Cover;

import java.util.List;

public abstract class CovershowFragment extends android.support.v4.app.Fragment implements CovershowDirector.Listener {

    private CovershowDirector covershowDirector;

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(shouldDisplayCover()){
            View fragmentView = onDecoratedCreateView(inflater, container, savedInstanceState);

            FrameLayout root = new FrameLayout(getActivity());

            root.addView(fragmentView);

            FrameLayout coversContainer = new FrameLayout(getActivity());
            root.addView(coversContainer);

            covershowDirector = new CovershowDirector(coversContainer);
            covershowDirector.setCovers( buildCovers() );
            covershowDirector.addListener(this);

            return root;

        } else {
            return onDecoratedCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            covershowDirector.start();
        }
    }

    protected abstract View onDecoratedCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);


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
