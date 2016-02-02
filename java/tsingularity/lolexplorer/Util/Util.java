package tsingularity.lolexplorer.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build.VERSION_CODES;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Util {

    public static float density = 1;

    public static String normalize(String name) {
        return name.toLowerCase().replaceAll("\\s+", "");
    }

    public static String getUTimeStr(long revisionDate) {

        Date date = new Date(revisionDate);

        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy HH:mm", Locale.getDefault());
        return sdf.format(date).toString();

    }

    public static String secondsToTime(long matchDuration) {
        long Sec = matchDuration % 60;
        long Min = (matchDuration - Sec) / 60;

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%02d", Min));
        sb.append(":");
        sb.append(String.format("%02d", Sec));

        return sb.toString();
    }

    public static Bitmap crop(Bitmap bm, int profileIconId) {
        //crop: 0-5 8-9 10-22 24-27 502
        if ((profileIconId >= 0 && profileIconId <= 5) ||
                (profileIconId >= 8 && profileIconId <= 9) ||
                (profileIconId >= 10 && profileIconId <= 22) ||
                (profileIconId >= 24 && profileIconId <= 27) ||
                profileIconId == 502) {
            int width = bm.getWidth();
            int height = bm.getHeight();
            return Bitmap.createBitmap(bm, 4, 4, width - 8, height - 8);
        }

        return bm;
    }

    public static Bitmap blur(Context context, Bitmap bm) {

        if (android.os.Build.VERSION.SDK_INT <= VERSION_CODES.JELLY_BEAN) {
            return bm;
        }

        int radius = 4;

        Bitmap bitmap = bm.copy(bm.getConfig(), true);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        Allocation tmpIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation tmpOut = Allocation.createTyped(rs, tmpIn.getType());
        theIntrinsic.setRadius(radius);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(bitmap);

        return bitmap;
    }

    public static SpannedString BuildSpannable(String s1, int games, String s2, int percent, String s3) {

        Spannable spanGames = new SpannableString("" + games);
        spanGames.setSpan(new StyleSpan(Typeface.BOLD), 0, spanGames.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        Spannable spanPercent = new SpannableString("" + percent);
        spanPercent.setSpan(new StyleSpan(Typeface.BOLD), 0, spanPercent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        int fgColor = 0;

        if (percent > 70) {
            fgColor = 0xFFD32F2F;
        } else if (percent > 60) {
            fgColor = 0xFFff9999;
        } else if (percent > 50) {
            fgColor = 0xFFffcc00;
        } else if (percent < 40) {
            fgColor = 0xFFFFA000;
        } else if (percent < 50) {
            fgColor = 0xFFCDDC39;
        }

        if (fgColor != 0) spanPercent.setSpan(new ForegroundColorSpan(fgColor), 0, spanPercent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return (SpannedString) TextUtils.concat(s1, spanGames, s2, spanPercent, s3);
    }

    public static int dpToPix(float value, float mDensity) {
        return (int) Math.ceil(mDensity * value);
    }

}
