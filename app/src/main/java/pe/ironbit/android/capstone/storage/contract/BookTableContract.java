package pe.ironbit.android.capstone.storage.contract;

import android.content.ContentResolver;
import android.net.Uri;

public class BookTableContract {
    public static final int LOADER_IDENTIFIER = 3;

    public static final String PATH_TABLE = "book_table";

    public static final int BOOK_TABLE_LIST = 30;

    public static final int BOOK_TABLE_ITEM = 31;

    private BookTableContract() {
    }

    public static final class BookTableEntry {
        public static final int NULL_INDEX = CapstoneStorageContract.NULL_INDEX;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(CapstoneStorageContract.BASE_CONTENT_URI, PATH_TABLE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CapstoneStorageContract.CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CapstoneStorageContract.CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String TABLE_NAME = "book_table";

        public static final String BOOK_ID = "book_id";

        public static final String BOOK_SECTION = "book_section";

        public static final String BOOK_VALUE = "book_value";
    }
}
