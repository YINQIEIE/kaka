package com.lvr.library.headeview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvr.library.R;
import com.lvr.library.recyclerview.RefreshTrigger;

public class CustomGifHeader extends LinearLayout implements RefreshTrigger {

    private GifView gifView1;
    private GifView gifView2;
    private ImageView ivSuccess;
    private TextView mHintTextView;

    public CustomGifHeader(Context context) {
        super(context);
        setBackgroundColor(Color.parseColor("#f3f3f3"));
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public CustomGifHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_gif_header, this);
        gifView1 = (GifView) findViewById(R.id.gif1);
        ivSuccess = (ImageView) findViewById(R.id.ivSuccess);
        mHintTextView = (TextView) findViewById(R.id.gif_header_hint);
        gifView2 = (GifView) findViewById(R.id.gif2);
        gifView1.setMovieResource(R.raw.vertical);
        gifView2.setMovieResource(R.raw.horizontal);
        gifView2.setVisibility(View.GONE);
    }


    @Override
    public void onStart(boolean automatic, int headerHeight, int finalHeight) {
        mHintTextView.setText(R.string.refreshview_header_hint_normal);
        gifView1.setVisibility(View.VISIBLE);
        gifView2.setVisibility(View.GONE);
        gifView1.setPaused(false);
        gifView2.setPaused(true);
    }

    @Override
    public void onMove(boolean finished, boolean automatic, int movedHeight) {
        if (!finished) {
            if (movedHeight < getMeasuredHeight())
                mHintTextView.setText(R.string.refreshview_header_hint_normal);
            else
                mHintTextView.setText(R.string.refreshview_header_hint_ready);
        } else {
            mHintTextView.setText(R.string.refreshview_header_hint_loaded);
        }
    }

    @Override
    public void onRefresh() {
        mHintTextView.setText(R.string.refreshview_header_hint_refreshing);
        gifView1.setVisibility(View.GONE);
        gifView2.setVisibility(View.VISIBLE);
        gifView1.setPaused(true);
        gifView2.setPaused(false);
    }

    @Override
    public void onRelease() {
        mHintTextView.setText(R.string.refreshview_header_hint_refreshing);
    }

    @Override
    public void onComplete() {
        mHintTextView.setText(R.string.refreshview_header_hint_loaded);
//        gifView1.setVisibility(View.VISIBLE);
        gifView2.setVisibility(View.GONE);
        ivSuccess.setVisibility(VISIBLE);
        gifView2.setPaused(true);
    }

    @Override
    public void onReset() {
        ivSuccess.setVisibility(GONE);
    }
}
