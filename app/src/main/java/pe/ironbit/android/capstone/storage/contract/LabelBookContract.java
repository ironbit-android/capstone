package pe.ironbit.android.capstone.storage.contract;

import android.content.ContentResolver;
import android.net.Uri;

public class LabelBookContract {
    public static final int LOADER_IDENTIFIER = 4;

    public static final String PATH_TABLE = "label_book";

    public static final int LABEL_BOOK_LIST = 40;

    public static final int LABEL_BOOK_ITEM = 41;

    public static final int LABEL_BOOK_LABEL_IDEN = 42;

    public static final int LABEL_BOOK_BOOK_IDEN = 43;

    public static final String PATH_LABEL_BOOK_LABEL_IDEN = "label_iden";

    public static final String PATH_LABEL_BOOK_BOOK_IDEN = "book_iden";

    private LabelBookContract() {
    }

    public static final class LabelBookEntry {
        public static final int NULL_INDEX = CapstoneStorageContract.NULL_INDEX;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(CapstoneStorageContract.BASE_CONTENT_URI, PATH_TABLE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CapstoneStorageContract.CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CapstoneStorageContract.CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String CONTENT_LABEL_IDEN_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CapstoneStorageContract.CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String CONTENT_BOOK_IDEN_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CapstoneStorageContract.CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String TABLE_NAME = "label_book";

        public static final String LABEL_ID = "label_id";

        public static final String BOOK_ID = "book_id";
    }
}
