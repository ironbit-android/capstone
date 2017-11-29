package pe.ironbit.android.capstone.storage.contract;

import android.content.ContentResolver;
import android.net.Uri;

public class LabelPrimeContract {
    public static final int LOADER_IDENTIFIER = 5;

    public static final String PATH_TABLE = "label_prime";

    public static final int LABEL_PRIME_LIST = 50;

    public static final int LABEL_PRIME_ITEM = 51;

    private LabelPrimeContract() {
    }

    public static final class LabelPrimeEntry {
        public static final int NULL_INDEX = CapstoneStorageContract.NULL_INDEX;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(CapstoneStorageContract.BASE_CONTENT_URI, PATH_TABLE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CapstoneStorageContract.CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CapstoneStorageContract.CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String TABLE_NAME = "label_prime";

        public static final String LABEL_ID = "label_id";

        public static final String LABEL_NAME = "label_name";
    }
}
