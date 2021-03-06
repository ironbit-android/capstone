package pe.ironbit.android.capstone.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public final class InternetStatus {
    private InternetStatus() {
        throw new AssertionError("No object instance for " + getClass().getSimpleName());
    }

    public static boolean verifyInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null) && (networkInfo.isConnectedOrConnecting());
    }
}
