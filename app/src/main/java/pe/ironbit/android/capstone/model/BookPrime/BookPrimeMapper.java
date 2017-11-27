package pe.ironbit.android.capstone.model.BookPrime;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import pe.ironbit.android.capstone.storage.contract.BookPrimeContract.BookPrimeEntry;

public class BookPrimeMapper {
    private static String[] projection = {
            BookPrimeEntry.BOOK_ID,
            BookPrimeEntry.BOOK_NAME,
            BookPrimeEntry.BOOK_AUTHOR,
            BookPrimeEntry.BOOK_IMAGE,
            BookPrimeEntry.BOOK_FILE,
            BookPrimeEntry.BOOK_STATUS
    };

    private BookPrimeMapper() {
    }

    public static Loader<Cursor> query(Context context) {
        return new CursorLoader(context, BookPrimeEntry.CONTENT_URI, projection, null, null, null);
    }

    public static Loader<Cursor> query(Context context, int bookId) {
        Uri uri = ContentUris.withAppendedId(BookPrimeEntry.CONTENT_URI, bookId);
        return new CursorLoader(context, uri, projection, null, null, null);
    }

    public static Uri insert(ContentResolver resolver, BookPrimeData data) {
        return resolver.insert(BookPrimeEntry.CONTENT_URI, generateContentValues(data));
    }

    public static int delete(ContentResolver resolver, int bookId) {
        Uri uri = ContentUris.withAppendedId(BookPrimeEntry.CONTENT_URI, bookId);
        return resolver.delete(uri, null, null);
    }

    public static int update(ContentResolver resolver, BookPrimeData data) {
        Uri uri = ContentUris.withAppendedId(BookPrimeEntry.CONTENT_URI, data.getBookId());
        return resolver.update(uri, generateContentValues(data), null, null);
    }

    public static ArrayList<BookPrimeData> generateListData(Cursor cursor) {
        ArrayList<BookPrimeData> list = new ArrayList<>();
        BookPrimeBuilder builder = new BookPrimeBuilder();

        cursor.moveToPosition(BookPrimeEntry.NULL_INDEX);
        while (cursor.moveToNext()) {
            list.add(getDataFromCursor(cursor, builder));
        }

        return list;
    }

    private static BookPrimeData getDataFromCursor(Cursor cursor, BookPrimeBuilder builder) {
        builder.clear();

        int index = cursor.getColumnIndex(BookPrimeEntry.BOOK_ID);
        if (index != BookPrimeEntry.NULL_INDEX) {
            builder.setBookId(cursor.getInt(index));
        } else {
            builder.setBookId(BookPrimeEntry.NULL_INDEX);
        }

        index = cursor.getColumnIndex(BookPrimeEntry.BOOK_NAME);
        if (index != BookPrimeEntry.NULL_INDEX) {
            builder.setName(cursor.getString(index));
        }

        index = cursor.getColumnIndex(BookPrimeEntry.BOOK_AUTHOR);
        if (index != BookPrimeEntry.NULL_INDEX) {
            builder.setAuthor(cursor.getString(index));
        }

        index = cursor.getColumnIndex(BookPrimeEntry.BOOK_IMAGE);
        if (index != BookPrimeEntry.NULL_INDEX) {
            builder.setImage(cursor.getString(index));
        }

        index = cursor.getColumnIndex(BookPrimeEntry.BOOK_FILE);
        if (index != BookPrimeEntry.NULL_INDEX) {
            builder.setFile(cursor.getString(index));
        }

        index = cursor.getColumnIndex(BookPrimeEntry.BOOK_STATUS);
        if (index != BookPrimeEntry.NULL_INDEX) {
            builder.setStatus(BookPrimeStatus.convertIntegerToStatus(cursor.getInt(index)));
        }

        return builder.build();
    }

    private static ContentValues generateContentValues(BookPrimeData data) {
        ContentValues values = new ContentValues();

        values.put(BookPrimeEntry.BOOK_ID, data.getBookId());
        values.put(BookPrimeEntry.BOOK_NAME, data.getName());
        values.put(BookPrimeEntry.BOOK_AUTHOR, data.getAuthor());
        values.put(BookPrimeEntry.BOOK_IMAGE, data.getImage());
        values.put(BookPrimeEntry.BOOK_FILE, data.getFile());
        values.put(BookPrimeEntry.BOOK_STATUS, BookPrimeStatus.convertStatusToInteger(data.getStatus()));

        return values;
    }
}
