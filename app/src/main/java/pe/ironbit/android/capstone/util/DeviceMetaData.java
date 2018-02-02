package pe.ironbit.android.capstone.util;

import android.content.Context;
import android.content.res.Configuration;

public final class DeviceMetaData {
    private DeviceMetaData() {
        throw new AssertionError("No object instance for " + getClass().getSimpleName());
    }

    public static boolean isOrientationPortrait(Context context) {
        return (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }

    public static boolean isDeviceTablet(Context context) {
        return ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE);
    }
}
