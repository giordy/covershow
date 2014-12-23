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

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;

import com.novadart.android.covershow.container.CovershowAwareContainer;
import com.novadart.android.covershow.cover.Cover;

import java.util.List;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
public abstract class CovershowFragment<Identifier> extends Fragment implements CovershowAwareContainer<Identifier> {

    private FragmentCovershowManager<Identifier> fragmentCovershowManager;

    public void setFragmentCovershowManager(FragmentCovershowManager<Identifier> fragmentCovershowManager) {
        this.fragmentCovershowManager = fragmentCovershowManager;
        fragmentCovershowManager.registerFragment(getClass(), this);
    }

    protected FragmentCovershowManager<Identifier> getFragmentCovershowManager() {
        return fragmentCovershowManager;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        fragmentCovershowManager.setFragmentVisibleToUser(getClass(), isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(shouldDisplayCover()) {
            List<Cover<Identifier>> covers = buildCovers();
            fragmentCovershowManager.setCovers(getClass(), covers);
        }
    }

    @Override
    public boolean isCovershowRunning() {
        return fragmentCovershowManager.isCovershowRunning(getClass());
    }


    @Override
    public void onCovershowPreparation() {}

    @Override
    public void onNextCover(Identifier id) {}

    @Override
    public void onCovershowTermination() {}
}
