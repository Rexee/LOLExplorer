package tsingularity.lolexplorer.Util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public class S {

    public static final String TAG = "myLOLLog";

    public static void L(Object object) {
        Log.d(TAG, "" + object);
    }

    public static void E(Context context, String s) {
        Toast.makeText(context, "" + s, Toast.LENGTH_LONG);
    }
}