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

package com.novadart.test;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.novadart.android.covershow.container.activity.CovershowActivity;
import com.novadart.android.covershow.cover.Cover;
import com.novadart.android.covershow.cover.impl.HighlightCover;
import com.novadart.android.covershow.cover.impl.WidgetHighlightCover;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends CovershowActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean shouldDisplayCover() {
        return true;
    }


    @Override
    protected List<Cover> buildCovers() {
        List<Cover> covers = new ArrayList<>();

        WidgetHighlightCover sc = new WidgetHighlightCover(R.id.aa, this);
        sc.setText("a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a a", "DDADADADA");

        covers.add(sc);
        covers.add(sc);
        covers.add(sc);
        covers.add(sc);covers.add(sc);covers.add(sc);covers.add(sc);

        return covers;
    }
}
