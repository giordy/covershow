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

package com.novadart.android.covershow.cover.impl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.novadart.android.covershow.cover.Cover;

public class StaticCover implements Cover {

    private final Integer id;
    private final int layoutId;
    private final Context context;

    public StaticCover(Context context, int layoutId) {
        this(context, null, layoutId);
    }

    public StaticCover(Context context, Integer id, int layoutId) {
        this.context = context;
        this.id = id;
        this.layoutId = layoutId;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public View buildView(final Handler handler) {
        View view = LayoutInflater.from(context).inflate(layoutId, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.onCoverExit();
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        return view;
    }

    @Override
    public void destroy() {}
}
