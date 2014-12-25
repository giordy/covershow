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
import com.novadart.android.covershow.container.CovershowManager;
import com.novadart.android.covershow.cover.Cover;

import java.util.List;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
public abstract class CovershowFragment<Identifier> extends Fragment implements CovershowAwareContainer<Identifier> {

    private CovershowManager<Identifier> covershowManager;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(covershowManager != null){
            ((FragmentCovershowManager) covershowManager).setUserVisibleHint(isVisibleToUser);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(shouldDisplayCovershow() && covershowManager == null) {
            buildCovers(new AsyncHandler<Identifier>() {
                @Override
                public void setCovers(List<Cover<Identifier>> covers) {
                    FragmentCovershowManager<Identifier> manager = new FragmentCovershowManager<>(getActivity(), CovershowFragment.this);
                    covershowManager = manager;
                    manager.setUserVisibleHint( getUserVisibleHint() );
                    manager.setCovers(covers);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        covershowManager = null;
    }

    @Override
    public void onPreCovershow() {}

    @Override
    public void onNextCover(Identifier id) {}

    @Override
    public void onPostCovershow() {}
}
