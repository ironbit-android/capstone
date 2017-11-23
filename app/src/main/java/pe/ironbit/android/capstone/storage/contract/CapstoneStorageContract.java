package pe.ironbit.android.capstone.storage.contract;

import android.net.Uri;

public class CapstoneStorageContract {
    /**
     * Name of Content Provider
     */
    public static final String CONTENT_AUTHORITY = "pe.ironbit.android.capstone";

    /**
     * Base Uri to contact with the content provider
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    private CapstoneStorageContract() {
    }
}
