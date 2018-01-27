package pe.ironbit.android.capstone.storage.contract;

import android.content.ContentResolver;
import android.net.Uri;

public class BookContentContract {
    public static final int LOADER_IDENTIFIER = 1;

    public static final String PATH_TABLE = "book_content";

    public static final int BOOK_CONTENT_LIST = 10;

    public static final int BOOK_CONTENT_ITEM = 11;

    public static final int BOOK_CONTENT_GROUP = 12;

    public static final String PATH_BOOK_CONTENT_GROUP = "by_id";

    private BookContentContract() {
    }

    public static final class BookContentEntry {
        public static final int NULL_INDEX = CapstoneStorageContract.NULL_INDEX;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(CapstoneStorageContract.BASE_CONTENT_URI, PATH_TABLE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CapstoneStorageContract.CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CapstoneStorageContract.CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String CONTENT_GROUP_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CapstoneStorageContract.CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String TABLE_NAME = "book_content";

        public static final String BOOK_ID = "book_id";

        public static final String BOOK_SECTION = "book_section";

        public static final String BOOK_VALUE = "book_value";
    }
}
