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

import android.app.Activity;
import android.view.View;

public class WidgetHighlightCover extends HighlightCover {

    public WidgetHighlightCover(int widgetId, Activity activity) {
        this(widgetId, activity, null);
    }

    public WidgetHighlightCover(int widgetId, Activity activity, Integer id) {
        super(activity, id);

        final View widget = activity.findViewById(widgetId);
        if(widget != null){
            widget.post(new Runnable() {
                @Override
                public void run() {
                    int[] winCoordinates = new int[2];
                    widget.getLocationOnScreen(winCoordinates);
                    setCoordinates(
                            winCoordinates[0] + (widget.getWidth() / 2),
                            winCoordinates[1]/2 + (widget.getHeight() / 2)
                    );
                }
            });
        }

    }


}