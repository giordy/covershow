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

import com.novadart.android.covershow.cover.Cover;

public class WidgetHighlightCover<Identifier> extends HighlightCover<Identifier> {

    private View widget;

    public WidgetHighlightCover(int widgetId, Activity activity) {
        this(widgetId, activity, null);
    }

    public WidgetHighlightCover(View widget, Activity activity) {
        this(widget, activity, null);
    }

    public WidgetHighlightCover(int widgetId, Activity activity, Identifier id) {
        this(activity.findViewById(widgetId), activity, id);
    }

    public WidgetHighlightCover(final View widget, Activity activity, Identifier id) {
        super(activity, id);
        this.widget = widget;
    }

    @Override
    public void buildView(Handler<Identifier> handler) {
        if(widget != null){
            int[] winCoordinates = new int[2];
            widget.getLocationInWindow(winCoordinates);
            setCoordinates(
                    winCoordinates[0] + (widget.getWidth() / 2),
                    winCoordinates[1] + (widget.getHeight() / 2)
            );
        }

        super.buildView(handler);
    }

    @Override
    public void destroy() {
        super.destroy();
        widget = null;
    }
}
