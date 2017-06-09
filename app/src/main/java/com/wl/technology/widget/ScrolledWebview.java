package com.wl.technology.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by wanglin  on 2017/5/31 10:07.
 */

public class ScrolledWebview extends WebView {
    private OnScrollChangedCallback mOnScrollChangedCallback;

    public ScrolledWebview(Context context) {
        super(context);
    }

    public ScrolledWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrolledWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l, t);
        }

    }

    public void setmOnScrollChangedCallback(OnScrollChangedCallback mOnScrollChangedCallback) {
        this.mOnScrollChangedCallback = mOnScrollChangedCallback;
    }

    public interface OnScrollChangedCallback {
        void onScroll(int dx, int dy);
    }
}
