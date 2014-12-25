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
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;

import com.novadart.android.covershow.R;
import com.novadart.android.covershow.Utils;
import com.novadart.android.covershow.cover.Cover;

public class HighlightCover<Identifier> implements Cover<Identifier> {
    public static final int DEFAULT_STYLE_ID = R.style.DefaultHighlightCoverStyle;

    private final Identifier id;
    private final Context context;
    private HighlightView view;

    private float x = 0;
    private float y = 0;

    private int styleId = DEFAULT_STYLE_ID;
    private String title;
    private String description;

    public HighlightCover(Context context) {
        this(context, null);
    }

    public HighlightCover(Context context, Identifier id) {
        this.id = id;
        this.context = context;
    }

    public void setCoordinates(float centerX, float centerY){
        x = centerX;
        y = centerY;
    }

    public void setText(String title, String description){
        this.title = title;
        this.description = description;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }

    @Override
    public void buildView(final Handler<Identifier> handler) {
        view = new HighlightView(context);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.onCoverExit(id);
                }
                return true; // TODO allow touching the highlighted UI in future
            }
        });
    }

    @Override
    public HighlightView getView() {
        return view;
    }

    @Override
    public void destroy() {
        view.destroy();
        view = null;
    }

    protected class HighlightView extends View {

        private final Paint paint;
        private Bitmap bitmap;

        public HighlightView(Context context) {
            super(context);
            paint = new Paint();
        }

        protected void drawBackground(Canvas canvas, TypedArray style){
            // fill the canvas
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(style.getColor(R.styleable.HighlightCoverStyle_csBackgroundColor,
                    Color.parseColor("#FFFFFFFF")));
            canvas.drawPaint(paint);
        }

        protected void drawCircle(Canvas canvas, TypedArray style){
            float innerCircleDiam = style.getDimension(R.styleable.HighlightCoverStyle_csRingDiameter, Utils.dipToPixels(context, 50f));
            float stroke = style.getDimension(R.styleable.HighlightCoverStyle_csRingStroke, Utils.dipToPixels(context, 2f));
            float outerCircleDiam = innerCircleDiam + stroke;

            //draw the outer circle
            paint.reset();
            paint.setAntiAlias(true);
            paint.setColor( style.getColor(R.styleable.HighlightCoverStyle_csRingColor, Color.parseColor("#FF000000")) );
            canvas.drawCircle(x, y,  outerCircleDiam, paint);

            //draw the inner transparent circle
            paint.setColor(Color.TRANSPARENT);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawCircle(x, y, innerCircleDiam, paint);
        }

        protected void drawText(Canvas canvas, TypedArray style){
            int canvasHeight = canvas.getHeight();
            boolean textPositionOnTop = y >= canvasHeight / 2;
            boolean hasTitle = title != null && !title.isEmpty();
            boolean hasDescription = description != null && !description.isEmpty();

            StaticLayout titleLayout = null;
            StaticLayout descLayout = null;

            float topPadding = style.getDimension(R.styleable.HighlightCoverStyle_csTopPadding, Utils.dipToPixels(context, 20f));
            float bottomPadding = style.getDimension(R.styleable.HighlightCoverStyle_csBottomPadding, Utils.dipToPixels(context, 20f));
            float leftPadding = style.getDimension(R.styleable.HighlightCoverStyle_csLeftPadding, Utils.dipToPixels(context, 20f));
            float rightPadding = style.getDimension(R.styleable.HighlightCoverStyle_csRightPadding, Utils.dipToPixels(context, 20f));


            float titlePadding = style.getDimension(R.styleable.HighlightCoverStyle_csTitlePadding, Utils.dipToPixels(context, 5f));
            float descPadding = style.getDimension(R.styleable.HighlightCoverStyle_csDescriptionPadding, Utils.dipToPixels(context, 5f));

            if(hasTitle) {
                boolean titleBold = style.getBoolean(R.styleable.HighlightCoverStyle_csTitleBold, true);
                int titleColor = style.getColor(R.styleable.HighlightCoverStyle_csTitleColor, Color.parseColor("#FF000000"));
                float titleSize = style.getDimension(R.styleable.HighlightCoverStyle_csTitleSize, Utils.dipToPixels(context, 22f));

                TextPaint titlePaint = new TextPaint();
                titlePaint.setAntiAlias(true);
                titlePaint.setColor(titleColor);
                Typeface tf = Typeface.create(Typeface.SANS_SERIF, titleBold ? Typeface.BOLD : Typeface.NORMAL);
                titlePaint.setTextSize(Utils.dipToPixels(context, titleSize));
                titlePaint.setTypeface(tf);

                titleLayout = new StaticLayout( title, titlePaint, (int) (canvas.getWidth()-(2*titlePadding)-(leftPadding+rightPadding) ),
                        Layout.Alignment.ALIGN_NORMAL, 1f, 2f, false);
            }

            if(hasDescription) {
                boolean descBold = style.getBoolean(R.styleable.HighlightCoverStyle_csDescriptionBold, false);
                int descColor = style.getColor(R.styleable.HighlightCoverStyle_csDescriptionColor, Color.parseColor("#FF000000"));
                float descSize = style.getDimension(R.styleable.HighlightCoverStyle_csDescriptionSize, Utils.dipToPixels(context, 16f));


                TextPaint descPaint = new TextPaint();
                descPaint.setAntiAlias(true);
                descPaint.setColor(descColor);
                Typeface tf = Typeface.create(Typeface.SANS_SERIF, descBold ? Typeface.BOLD : Typeface.NORMAL);
                descPaint.setTextSize(Utils.dipToPixels(context, descSize));
                descPaint.setTypeface(tf);

                descLayout = new StaticLayout( description, descPaint, (int) (canvas.getWidth()-(2*descPadding)-(leftPadding+rightPadding) ),
                        Layout.Alignment.ALIGN_NORMAL, 1f, 2f, false);
            }

            float titleHeight = hasTitle ? titlePadding*2 + titleLayout.getHeight() : 0;
            float descHeight = hasDescription ? descPadding*2 + descLayout.getHeight() : 0;
            float top = textPositionOnTop ? topPadding : canvas.getHeight()-titleHeight-descHeight-bottomPadding;

            if(hasTitle){
                // save state
                canvas.save();

                canvas.translate(leftPadding+titlePadding, top+titlePadding);
                titleLayout.draw(canvas);

                // restore state
                canvas.restore();
            }

            if(hasDescription){
                // save state
                canvas.save();

                canvas.translate(leftPadding+descPadding, top+titleHeight+descPadding);
                descLayout.draw(canvas);

                // restore state
                canvas.restore();
            }

        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            if(bitmap != null) {
                bitmap.recycle();
            }
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            TypedArray style = context.getTheme().obtainStyledAttributes(styleId, R.styleable.HighlightCoverStyle);

            drawBackground(canvas, style);

            drawCircle(canvas, style);

            if(title != null || description != null){
                drawText(canvas, style);
            }

            style.recycle();
            paint.reset();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }

        protected void destroy(){
            if(bitmap != null) {
                bitmap.recycle();
                bitmap = null;
            }
        }
    }



}
