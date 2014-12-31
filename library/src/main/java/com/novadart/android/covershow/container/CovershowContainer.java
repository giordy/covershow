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

package com.novadart.android.covershow.container;

import com.novadart.android.covershow.cover.Cover;
import com.novadart.android.covershow.director.CovershowDirector;

import java.util.List;

public interface CovershowContainer<Identifier> extends CovershowDirector.Listener<Identifier> {
    public static final String ARG_AUTOSTART = "ARG_AUTOSTART";

    void startCovershow();
    void disableAutoStart();
    boolean shouldStartCovershow();
    void onPreCovershow();
    void buildCoverList(AsyncHandler<Identifier> handler);


    public static interface AsyncHandler<Identifier>{
        void setCovers(List<Cover<Identifier>> covers);
    }
}
