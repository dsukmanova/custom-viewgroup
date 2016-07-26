package ru.yandex.yamblz.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by dsukmanova on 26.07.16.
 */

public class CustomViewGroup extends ViewGroup {
    public CustomViewGroup(Context context) {
        super(context);
    }

    public CustomViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int elementsWidth = 0;
        View matchParentElement = null;

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) continue;
            LayoutParams layoutParams = childView.getLayoutParams();
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);

            if (layoutParams.width != LayoutParams.MATCH_PARENT) {
                measureChild(childView, widthMeasureSpec, heightMeasureSpec);
                elementsWidth += childView.getMeasuredWidth();
            } else {
                matchParentElement = childView;
            }
        }
        if (matchParentElement != null) {
            int mpWidthSpec = MeasureSpec.makeMeasureSpec(Math.max(0, width - elementsWidth), MeasureSpec.EXACTLY);
            int mpHeightSpec = MeasureSpec.makeMeasureSpec(matchParentElement.getMeasuredHeight(), MeasureSpec.EXACTLY);
            matchParentElement.measure(mpWidthSpec, mpHeightSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int elementStart = getPaddingLeft();
        int elementTop = getPaddingTop();
        int newElementStart;

        for (int i = 0; i < count; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() == GONE) continue;
            newElementStart = elementStart + childView.getMeasuredWidth();
            childView.layout(elementStart, elementTop, newElementStart, elementTop + childView.getMeasuredHeight());
            elementStart = newElementStart;
        }
    }
}
