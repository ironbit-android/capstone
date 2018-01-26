package pe.ironbit.android.capstone.model.BookTable;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.ArrayList;

import pe.ironbit.android.capstone.storage.contract.BookTableContract.BookTableEntry;

public class BookTableMapper {
    private static String[] projection = {
            BookTableEntry.BOOK_ID,
            BookTableEntry.BOOK_SECTION,
            BookTableEntry.BOOK_VALUE
    };

    private BookTableMapper() {
    }

    public static Loader<Cursor> query(Context context) {
        return new CursorLoader(context, BookTableEntry.CONTENT_URI, projection, null, null, null);
    }

    public static Loader<Cursor> query(Context context, int bookId, int section) {
        Uri uri = BookTableEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(bookId))
                                                        .appendPath(String.valueOf(section))
                                                        .build();

        return new CursorLoader(context, uri, projection, null, null , null);
    }

    public static Uri insert(ContentResolver resolver, BookTableData data) {
        return resolver.insert(BookTableEntry.CONTENT_URI, generateContentValues(data));
    }

    public static int delete(ContentResolver resolver, BookTableData data) {
        Uri uri = BookTableEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(data.getBookId()))
                                                        .appendPath(String.valueOf(data.getSection()))
                                                        .build();

        return resolver.delete(uri, null, null);
    }

    public static int update(ContentResolver resolver, BookTableData data) {
        Uri uri = BookTableEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(data.getBookId()))
                                                        .appendPath(String.valueOf(data.getSection()))
                                                        .build();

        return resolver.update(uri, generateContentValues(data), null, null);
    }

    public static ArrayList<BookTableData> generateListData(Cursor cursor) {
        ArrayList<BookTableData> list = new ArrayList<>();
        BookTableBuilder builder = new BookTableBuilder();

        cursor.moveToPosition(BookTableEntry.NULL_INDEX);
        while (cursor.moveToNext()) {
            list.add(getDataFromCursor(cursor, builder));
        }

        return list;
    }

    private static BookTableData getDataFromCursor(Cursor cursor, BookTableBuilder builder) {
        builder.clear();

        int index = cursor.getColumnIndex(BookTableEntry.BOOK_ID);
        if (index != BookTableEntry.NULL_INDEX) {
            builder.setBookId(cursor.getInt(index));
        } else {
            builder.setBookId(BookTableEntry.NULL_INDEX);
        }

        index = cursor.getColumnIndex(BookTableEntry.BOOK_SECTION);
        if (index != BookTableEntry.NULL_INDEX) {
            builder.setSection(cursor.getInt(index));
        } else {
            builder.setSection(BookTableEntry.NULL_INDEX);
        }

        index = cursor.getColumnIndex(BookTableEntry.BOOK_VALUE);
        if (index != BookTableEntry.NULL_INDEX) {
            builder.setValue(cursor.getString(index));
        }

        return builder.build();
    }

    private static ContentValues generateContentValues(BookTableData data) {
        ContentValues values = new ContentValues();

        values.put(BookTableEntry.BOOK_ID, data.getBookId());
        values.put(BookTableEntry.BOOK_SECTION, data.getSection());
        values.put(BookTableEntry.BOOK_VALUE, data.getValue());

        return values;
    }
}
