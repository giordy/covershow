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

package com.novadart.android.covershow.container.fragment.appcompat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novadart.android.covershow.container.CovershowContainer;
import com.novadart.android.covershow.container.fragment.CovershowFragmentCore;
import com.novadart.android.covershow.cover.Cover;

import java.util.List;

public abstract class CovershowFragment extends Fragment implements CovershowContainer {

    private CovershowFragmentCore covershowFragmentCore = new CovershowFragmentCore(this);

    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(shouldDisplayCover()){
            View fragmentView = onDecoratedCreateView(inflater, container, savedInstanceState);
            return covershowFragmentCore.initContainer(getActivity(), fragmentView);

        } else {
            return onDecoratedCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        covershowFragmentCore.setFragmentVisibleToUser(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Cover> covers = buildCovers();
        covershowFragmentCore.setCovers(covers);
    }

    @Override
    public boolean isCovershowRunning() {
        return covershowFragmentCore.isCovershowRunning();
    }

    protected abstract View onDecoratedCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    @Override
    public void onCovershowPreparation() {}

    @Override
    public void onNextCover(Integer id) {}

    @Override
    public void onCovershowTermination() {}
}
