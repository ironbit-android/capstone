package pe.ironbit.android.capstone.storage.contract;

import android.net.Uri;

public final class CapstoneStorageContract {
    /**
     * Name of Content Provider
     */
    public static final String CONTENT_AUTHORITY = "pe.ironbit.android.capstone";

    /**
     * Base Uri to contact with the content provider
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Initial index value of cursor class.
     * It is used for invalid value for primary keys.
     */
    public static final int NULL_INDEX = -1;

    private CapstoneStorageContract() {
    }
}
