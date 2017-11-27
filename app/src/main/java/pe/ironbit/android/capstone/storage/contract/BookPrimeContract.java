package pe.ironbit.android.capstone.storage.contract;

import android.content.ContentResolver;
import android.net.Uri;

public class BookPrimeContract {
    public static final int LOADER_IDENTIFIER = 2;

    public static final String PATH_TABLE = "book_prime";

    public static final int BOOK_PRIME_LIST = 20;

    public static final int BOOK_PRIME_ITEM = 21;

    private BookPrimeContract() {
    }

    public static final class  BookPrimeEntry {
        public static final int NULL_INDEX = CapstoneStorageContract.NULL_INDEX;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(CapstoneStorageContract.BASE_CONTENT_URI, PATH_TABLE);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CapstoneStorageContract.CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CapstoneStorageContract.CONTENT_AUTHORITY + "/" + PATH_TABLE;

        public static final String TABLE_NAME = "book_prime";

        public static final String BOOK_ID = "book_id";

        public static final String BOOK_NAME = "book_name";

        public static final String BOOK_AUTHOR = "book_author";

        public static final String BOOK_IMAGE = "book_image";

        public static final String BOOK_FILE = "book_file";

        public static final String BOOK_STATUS = "book_status";
    }
}
