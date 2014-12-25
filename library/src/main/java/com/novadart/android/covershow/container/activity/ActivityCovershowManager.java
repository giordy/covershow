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

import com.novadart.android.covershow.container.CovershowContainer;
import com.novadart.android.covershow.container.CovershowManager;
import com.novadart.android.covershow.cover.Cover;

import java.util.List;

public class ActivityCovershowManager<Identifier> extends CovershowManager<Identifier> {


    public ActivityCovershowManager(Activity activity, CovershowContainer<Identifier> covershowContainer) {
        super(activity, covershowContainer);
    }

    @Override
    public void setCovers(List<Cover<Identifier>> covers) {
        super.setCovers(covers);
        startCovershow();
    }

}
