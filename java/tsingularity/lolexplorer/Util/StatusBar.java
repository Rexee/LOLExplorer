package tsingularity.lolexplorer.Util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout.LayoutParams;

public class StatusBar {

    public static final  int    DEFAULT_BACKGROUND_COLOR   = 0x00000000;
    private static final String STATUS_BAR_HEIGHT_RES_NAME = "status_bar_height";

    private boolean mStatusBarAvailable;
    private View    mStatusBarBackgroundView;

    public StatusBar(Activity activity) {

        mStatusBarAvailable = false;
        Window win = activity.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // check theme attrs
            int[] attrs = {android.R.attr.windowTranslucentStatus};
            TypedArray a = activity.obtainStyledAttributes(attrs);
            try {
                mStatusBarAvailable = a.getBoolean(0, false);
            } finally {
                a.recycle();
            }

            // check window flags
            WindowManager.LayoutParams winParams = win.getAttributes();
            int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if ((winParams.flags & bits) != 0) {
                mStatusBarAvailable = true;
            }
        }

        if (mStatusBarAvailable) {
            ViewGroup decorViewGroup = (ViewGroup) win.getDecorView();
            enableStatusBarBackground(activity, decorViewGroup);
        }

    }

    public static void setAlpha(Activity activity, float alpha) {
        StatusBar statusBar = new StatusBar(activity);
        statusBar.setAlpha(alpha);
    }

    public static void setStatusBarBackgroundColor(Activity activity, int color) {
        StatusBar statusBar = new StatusBar(activity);
        statusBar.setStatusBarBackgroundColor(color);
    }

    public void setStatusBarBackgroundColor(int color) {
        if (mStatusBarAvailable) {
            mStatusBarBackgroundView.setBackgroundColor(color);
        }
    }

    public void setAlpha(float alpha) {
        if (mStatusBarAvailable) {
            mStatusBarBackgroundView.setAlpha(alpha);
        }
    }

    public void setStatusBarBackgroundResource(int res) {
        if (mStatusBarAvailable) {
            mStatusBarBackgroundView.setBackgroundResource(res);
        }
    }

    public void setStatusBarBackgroundDrawable(Drawable drawable) {
        if (mStatusBarAvailable) {
            mStatusBarBackgroundView.setBackground(drawable);
        }
    }

    private void enableStatusBarBackground(Context context, ViewGroup decorViewGroup) {

        final int mStatusBarHeight = getStatusBarHeight(context);

        mStatusBarBackgroundView = new View(context);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mStatusBarHeight);
        params.gravity = Gravity.TOP;

        mStatusBarBackgroundView.setLayoutParams(params);
        mStatusBarBackgroundView.setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
        decorViewGroup.addView(mStatusBarBackgroundView);
    }

    public int getStatusBarHeight(Context context) {
        Resources res = context.getResources();
        int result = 0;
        int resourceId = res.getIdentifier(STATUS_BAR_HEIGHT_RES_NAME, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }
}