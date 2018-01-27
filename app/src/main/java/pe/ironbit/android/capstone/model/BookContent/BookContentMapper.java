package pe.ironbit.android.capstone.model.BookContent;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.ArrayList;

import pe.ironbit.android.capstone.storage.contract.BookContentContract;
import pe.ironbit.android.capstone.storage.contract.BookContentContract.BookContentEntry;

public class BookContentMapper {
    private static String[] projection = {
            BookContentEntry.BOOK_ID,
            BookContentEntry.BOOK_SECTION,
            BookContentEntry.BOOK_VALUE
    };

    private BookContentMapper() {
    }

    public static Loader<Cursor> query(Context context) {
        return new CursorLoader(context, BookContentEntry.CONTENT_URI, projection, null, null, null);
    }

    public static Loader<Cursor> query(Context context, int bookId) {
        Uri uri = BookContentEntry.CONTENT_URI.buildUpon().appendPath(BookContentContract.PATH_BOOK_CONTENT_GROUP)
                                                          .appendPath(String.valueOf(bookId))
                                                          .build();

        return new CursorLoader(context, uri, projection, null, null , null);
    }

    public static Loader<Cursor> query(Context context, int bookId, int section) {
        Uri uri = BookContentEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(bookId))
                                                          .appendPath(String.valueOf(section))
                                                          .build();

        return new CursorLoader(context, uri, projection, null, null , null);
    }

    public static Uri insert(ContentResolver resolver, BookContentData data) {
        return resolver.insert(BookContentEntry.CONTENT_URI, generateContentValues(data));
    }

    public static int delete(ContentResolver resolver, BookContentData data) {
        Uri uri = BookContentEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(data.getBookId()))
                                                          .appendPath(String.valueOf(data.getSection()))
                                                          .build();

        return resolver.delete(uri, null, null);
    }

    public static int update(ContentResolver resolver, BookContentData data) {
        Uri uri = BookContentEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(data.getBookId()))
                                                          .appendPath(String.valueOf(data.getSection()))
                                                          .build();

        return resolver.update(uri, generateContentValues(data), null, null);
    }

    public static ArrayList<BookContentData> generateListData(Cursor cursor) {
        ArrayList<BookContentData> list = new ArrayList<>();
        BookContentBuilder builder = new BookContentBuilder();

        cursor.moveToPosition(BookContentEntry.NULL_INDEX);
        while (cursor.moveToNext()) {
            list.add(getDataFromCursor(cursor, builder));
        }

        return list;
    }

    private static BookContentData getDataFromCursor(Cursor cursor, BookContentBuilder builder) {
        builder.clear();

        int index = cursor.getColumnIndex(BookContentEntry.BOOK_ID);
        if (index != BookContentEntry.NULL_INDEX) {
            builder.setBookId(cursor.getInt(index));
        } else {
            builder.setBookId(BookContentEntry.NULL_INDEX);
        }

        index = cursor.getColumnIndex(BookContentEntry.BOOK_SECTION);
        if (index != BookContentEntry.NULL_INDEX) {
            builder.setSection(cursor.getInt(index));
        } else {
            builder.setSection(BookContentEntry.NULL_INDEX);
        }

        index = cursor.getColumnIndex(BookContentEntry.BOOK_VALUE);
        if (index != BookContentEntry.NULL_INDEX) {
            builder.setValue(cursor.getString(index));
        }

        return builder.build();
    }

    private static ContentValues generateContentValues(BookContentData data) {
        ContentValues values = new ContentValues();

        values.put(BookContentEntry.BOOK_ID, data.getBookId());
        values.put(BookContentEntry.BOOK_SECTION, data.getSection());
        values.put(BookContentEntry.BOOK_VALUE, data.getValue());

        return values;
    }
}
