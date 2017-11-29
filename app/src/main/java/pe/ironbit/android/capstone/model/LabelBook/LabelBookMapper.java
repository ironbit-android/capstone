package pe.ironbit.android.capstone.model.LabelBook;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import pe.ironbit.android.capstone.storage.contract.LabelBookContract.LabelBookEntry;

public class LabelBookMapper {
    private static String[] projection = {
            LabelBookEntry.LABEL_ID,
            LabelBookEntry.BOOK_ID
    };

    private LabelBookMapper() {
    }

    public static Loader<Cursor> query(Context context) {
        return new CursorLoader(context, LabelBookEntry.CONTENT_URI, projection, null, null, null);
    }

    public static Loader<Cursor> query(Context context, int labelId, int bookId) {
        Uri uri = LabelBookEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(labelId))
                                                        .appendPath(String.valueOf(bookId))
                                                        .build();

        return new CursorLoader(context, uri, projection, null, null , null);
    }

    public static Uri insert(ContentResolver resolver, LabelBookData data) {
        return resolver.insert(LabelBookEntry.CONTENT_URI, generateContentValues(data));
    }

    public static int delete(ContentResolver resolver, LabelBookData data) {
        Uri uri = LabelBookEntry.CONTENT_URI.buildUpon().appendPath(String.valueOf(data.getLabelId()))
                                                        .appendPath(String.valueOf(data.getBookId()))
                                                        .build();

        return resolver.delete(uri, null, null);
    }

    public static ArrayList<LabelBookData> generateListData(Cursor cursor) {
        ArrayList<LabelBookData> list = new ArrayList<>();
        LabelBookBuilder builder = new LabelBookBuilder();

        cursor.moveToPosition(LabelBookEntry.NULL_INDEX);
        while (cursor.moveToNext()) {
            list.add(getDataFromCursor(cursor, builder));
        }

        return list;
    }

    private static LabelBookData getDataFromCursor(Cursor cursor, LabelBookBuilder builder) {
        builder.clear();

        int index = cursor.getColumnIndex(LabelBookEntry.LABEL_ID);
        if (index != LabelBookEntry.NULL_INDEX) {
            builder.setLabelId(cursor.getInt(index));
        } else {
            builder.setLabelId(LabelBookEntry.NULL_INDEX);
        }

        index = cursor.getColumnIndex(LabelBookEntry.BOOK_ID);
        if (index != LabelBookEntry.NULL_INDEX) {
            builder.setBookId(cursor.getInt(index));
        } else {
            builder.setBookId(LabelBookEntry.NULL_INDEX);
        }

        return builder.build();
    }

    private static ContentValues generateContentValues(LabelBookData data) {
        ContentValues values = new ContentValues();

        values.put(LabelBookEntry.LABEL_ID, data.getLabelId());
        values.put(LabelBookEntry.BOOK_ID, data.getBookId());

        return values;
    }
}
