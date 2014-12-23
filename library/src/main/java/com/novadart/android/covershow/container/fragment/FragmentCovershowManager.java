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
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.novadart.android.covershow.container.CovershowAwareContainer;
import com.novadart.android.covershow.cover.Cover;
import com.novadart.android.covershow.director.CovershowDirector;
import com.novadart.android.covershow.director.impl.CovershowDirectorImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentCovershowManager<Identifier> implements CovershowDirector.Listener<Identifier> {

    private Map<String, CovershowDirector<Identifier>> directors;
    private Map<String, Boolean> fragmentsVisibility;

    private String fragmentInCovershow = null;
    private FrameLayout coversContainer;

    public void wrapActivityView(Activity activity){
        directors = new HashMap<>();
        fragmentsVisibility = new HashMap<>();

        ViewGroup.LayoutParams fullscreenParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        coversContainer = new FrameLayout(activity);

        final ViewGroup rootView = ((ViewGroup) activity.getWindow().getDecorView().getRootView());
        rootView.post(new Runnable() {
            @Override
            public void run() {
                rootView.addView(coversContainer);
            }
        });
    }

    public void registerFragment(Class<?> fragment, CovershowAwareContainer<Identifier> container){
        CovershowDirector<Identifier> covershowDirector = new CovershowDirectorImpl<>(coversContainer);
        covershowDirector.addListener(this);
        covershowDirector.addListener(container);
        directors.put(fragment.getCanonicalName(), covershowDirector);
    }

    public void setCovers(Class<?> fragment, List<Cover<Identifier>> covers){
        String canonicalName = fragment.getCanonicalName();
        CovershowDirector<Identifier> director = directors.get(canonicalName);
        director.setCovers(covers);
        tryStart(canonicalName);
    }

    public void setFragmentVisibleToUser(Class<?> fragment, boolean isVisibleToUser) {
        String canonicalName = fragment.getCanonicalName();
        fragmentsVisibility.put(canonicalName, isVisibleToUser);
        if(isVisibleToUser) {
            tryStart(canonicalName);
        }
    }

    private synchronized void tryStart(String fragmentCanonicalName){
        CovershowDirector director = directors.get(fragmentCanonicalName);
        if(director == null){
            return;
        }

        boolean hasCovers = director.hasCovers();
        Boolean isFragmentVisibleToUser = fragmentsVisibility.get(fragmentCanonicalName);

        if(fragmentInCovershow == null && isFragmentVisibleToUser != null && isFragmentVisibleToUser && hasCovers){
            fragmentInCovershow = fragmentCanonicalName;
            director.start();
        }
    }

    public boolean isCovershowRunning(Class<?> fragment) {
        return fragmentInCovershow.equals(fragment.getCanonicalName());
    }


    @Override
    public void onCovershowPreparation() {}

    @Override
    public void onNextCover(Identifier id) {}

    @Override
    public void onCovershowTermination() {
        fragmentInCovershow = null;
    }
}
