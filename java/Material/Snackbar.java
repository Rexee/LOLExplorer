package Material;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import tsingularity.lolexplorer.R;
import tsingularity.lolexplorer.REST_Service;
import tsingularity.lolexplorer.Util.S;

public class Snackbar {

    private static final String TAG            = Snackbar.class.getSimpleName() + " ";
    private static final long   AUTOHIDE_TIME  = 2000;
    private static final int    ANIMATION_TIME = 300;
    public static volatile Snackbar instance;
    public                 Activity mContext;
    public  boolean mIsShowing = false;
    private boolean mIsHiding  = false;
    private boolean mAutohide  = false;
    private View                 mSnackBarView;
    private TextView             mSnackbarText;
    private TextView             mSnackbarTextNext;
    private FloatingActionButton mFab;
    private Runnable mAutoHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide(true);
        }
    };

    public static Snackbar getInstance(Context context) {
        Snackbar localInstance = instance;
        if (instance == null) {
            synchronized (Snackbar.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Snackbar();
                }
            }
        }

        localInstance.setContext(context);
        return localInstance;
    }

    public static void show(final Context activity, final String text, final FloatingActionButton fab) {
        Handler UIThread = new Handler(Looper.getMainLooper());
        UIThread.post(new Runnable() {
            @Override
            public void run() {
                Snackbar sb = Snackbar.getInstance(activity);
                sb.mAutohide = true;
                sb.showText(text, "", false, fab, null);
            }
        });
    }

    public static void showError(final Context activity, final String text, final FloatingActionButton fab) {
        Handler UIThread = new Handler(Looper.getMainLooper());
        UIThread.post(new Runnable() {
            @Override
            public void run() {
                Snackbar sb = Snackbar.getInstance(activity);
                sb.mAutohide = true;
                sb.showText(text, "", true, fab, null);
            }
        });
    }

    public static void showAction(final Activity activity, final String text, final String actionText, final FloatingActionButton fab, final OnClickListener onClickListener) {
        Handler UIThread = new Handler(Looper.getMainLooper());
        UIThread.post(new Runnable() {
            @Override
            public void run() {
                Snackbar sb = Snackbar.getInstance(activity);
                sb.mAutohide = false;
                sb.showText(text, actionText, false, fab, onClickListener);
            }
        });
    }

    public static void hideAll() {
        Handler UIThread = new Handler(Looper.getMainLooper());
        UIThread.post(new Runnable() {
            @Override
            public void run() {
                Snackbar sb = Snackbar.getInstance(null);
                sb.mAutohide = true;
                sb.autoHide();
            }
        });
    }

    public void setContext(Context context) {

        if (context == null) {
            return;
        }

        if (context instanceof REST_Service) {
            return;
        }

        if (mContext != null && mContext != context) {
            removeView();
        }

        mContext = (Activity) context;
    }

    private void showText(final String text, String actionText, final boolean isError, FloatingActionButton fab, OnClickListener onClickListener) {
        S.L(TAG + "showText. text: " + text);

        stopHidding();
        mFab = fab;

        if (mIsShowing) {

            String prevText = (String) mSnackbarText.getText();
            if (!prevText.equals(text)) {
                setText(mSnackbarTextNext, text, isError);

//                mSnackbarText.clearAnimation();
//                mSnackbarTextNext.clearAnimation();
                Animation scrollUpFast = AnimationUtils.loadAnimation(mContext, R.anim.scroll_mid_to_up_fast);
                Animation scrollUpFastNext = AnimationUtils.loadAnimation(mContext, R.anim.scroll_down_to_mid_fast);

                scrollUpFast.setAnimationListener(new AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        mSnackbarTextNext.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mSnackbarTextNext.setVisibility(View.INVISIBLE);

                        setText(mSnackbarText, text, isError);

                        autoHide();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                mSnackbarText.setAnimation(scrollUpFast);
                mSnackbarTextNext.setAnimation(scrollUpFastNext);
            } else autoHide();

            return;
        }

        FrameLayout mRootView = (FrameLayout) mContext.findViewById(android.R.id.content);
        mSnackBarView = mContext.getLayoutInflater().inflate(R.layout.snackbar, mRootView, false);
        mSnackbarText = (TextView) mSnackBarView.findViewById(R.id.snackbarText);
        mSnackbarTextNext = (TextView) mSnackBarView.findViewById(R.id.snackbarTextNext);

        setActionText(actionText, onClickListener);
        setText(mSnackbarText, text, isError);

        mSnackBarView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAll();
            }
        });

        mSnackBarView.addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (mAutoHideRunnable != null) {
                    v.removeCallbacks(mAutoHideRunnable);
                }
            }
        });

        Animation alphaIn = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        mSnackbarText.setAnimation(alphaIn);

        mRootView.addView(mSnackBarView);
        mRootView.bringToFront();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mRootView.requestLayout();
            mRootView.invalidate();
        }
        mIsShowing = true;

        Animation scrollUp = AnimationUtils.loadAnimation(mContext, R.anim.scroll_up);
        scrollUp.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (mFab == null) return;

                int height = mSnackBarView.getHeight();
                mFab.animate().setDuration(ANIMATION_TIME).translationY(-height);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                autoHide();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mSnackBarView.startAnimation(scrollUp);
    }

    private void setActionText(String actionText, OnClickListener onClickListener) {
        if (actionText.isEmpty()) return;

        TextView actionTextTV = (TextView) mSnackBarView.findViewById(R.id.snackbarAction);
        actionTextTV.setText(actionText);
        actionTextTV.setOnClickListener(onClickListener);

    }

    private void setText(TextView snackbarText, String text, boolean isError) {
        int curColor = snackbarText.getCurrentTextColor();

        if (isError) {
            if (curColor != Color.RED) {
                snackbarText.setTextColor(Color.RED);
            }
        } else if (curColor != Color.WHITE) {
            snackbarText.setTextColor(Color.WHITE);
        }

        snackbarText.setText(text);
    }

    private void stopHidding() {
        if (mSnackBarView == null || mAutoHideRunnable == null) return;
        if (mAutohide || !mIsShowing || !mIsHiding) return;

        mSnackBarView.removeCallbacks(mAutoHideRunnable);
        mSnackBarView.clearAnimation();
        mIsHiding = false;
    }

    private void autoHide() {
        if (!mAutohide) return;
        if (mSnackBarView == null) return;

        mSnackBarView.post(new Runnable() {
            @Override
            public void run() {
                if (mAutoHideRunnable != null) mSnackBarView.removeCallbacks(mAutoHideRunnable);
                mSnackBarView.postDelayed(mAutoHideRunnable, AUTOHIDE_TIME);
            }
        });
    }

    private void hide(boolean withAnimation) {
        if (mIsHiding) return;

        mIsHiding = true;

        if (withAnimation) {

            mSnackBarView.animate().translationY(mSnackBarView.getHeight()).setDuration(ANIMATION_TIME).setListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationEnd(Animator animation) {
                    mSnackBarView.post(new Runnable() {
                        @Override
                        public void run() {
                            removeView();
                        }
                    });
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    if (mFab == null) return;

                    mFab.animate().setDuration(ANIMATION_TIME).translationY(0);
                }
            });

        } else removeView();
    }

    private void removeView() {
        if (mSnackBarView == null) return;

        mSnackBarView.clearAnimation();
        FrameLayout parent = (FrameLayout) mSnackBarView.getParent();

        if (parent != null) {
            parent.removeView(mSnackBarView);
        }
        mIsShowing = false;
        mIsHiding = false;
        mSnackBarView = null;
    }
}
