package pe.ironbit.android.capstone.model.LabelPrime;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.util.ArrayList;

import pe.ironbit.android.capstone.storage.contract.LabelPrimeContract.LabelPrimeEntry;

public class LabelPrimeMapper {
    private static String[] projection = {
            LabelPrimeEntry.LABEL_ID,
            LabelPrimeEntry.LABEL_NAME
    };

    private LabelPrimeMapper() {
    }

    public static Loader<Cursor> query(Context context) {
        return new CursorLoader(context, LabelPrimeEntry.CONTENT_URI, projection, null, null, null);
    }

    public static Loader<Cursor> query(Context context, int labelId) {
        Uri uri = ContentUris.withAppendedId(LabelPrimeEntry.CONTENT_URI, labelId);
        return new CursorLoader(context, uri, projection, null, null, null);
    }

    public static Uri insert(ContentResolver resolver, LabelPrimeData data) {
        return resolver.insert(LabelPrimeEntry.CONTENT_URI, generateContentValues(data));
    }

    public static int delete(ContentResolver resolver, int labelId) {
        Uri uri = ContentUris.withAppendedId(LabelPrimeEntry.CONTENT_URI, labelId);
        return resolver.delete(uri, null, null);
    }

    public static int update(ContentResolver resolver, LabelPrimeData data) {
        Uri uri = ContentUris.withAppendedId(LabelPrimeEntry.CONTENT_URI, data.getLabelId());
        return resolver.update(uri, generateContentValues(data), null, null);
    }

    public static ArrayList<LabelPrimeData> generateListData(Cursor cursor) {
        ArrayList<LabelPrimeData> list = new ArrayList<>();
        LabelPrimeBuilder builder = new LabelPrimeBuilder();

        cursor.moveToPosition(LabelPrimeEntry.NULL_INDEX);
        while (cursor.moveToNext()) {
            list.add(getDataFromCursor(cursor, builder));
        }

        return list;
    }

    private static LabelPrimeData getDataFromCursor(Cursor cursor, LabelPrimeBuilder builder) {
        builder.clear();

        int index = cursor.getColumnIndex(LabelPrimeEntry.LABEL_ID);
        if (index != LabelPrimeEntry.NULL_INDEX) {
            builder.setLabelId(cursor.getInt(index));
        } else {
            builder.setLabelId(LabelPrimeEntry.NULL_INDEX);
        }

        index = cursor.getColumnIndex(LabelPrimeEntry.LABEL_NAME);
        if (index != LabelPrimeEntry.NULL_INDEX) {
            builder.setLabelName(cursor.getString(index));
        }

        return builder.build();
    }

    private static ContentValues generateContentValues(LabelPrimeData data) {
        ContentValues values = new ContentValues();

        values.put(LabelPrimeEntry.LABEL_ID, data.getLabelId());
        values.put(LabelPrimeEntry.LABEL_NAME, data.getLabelName());

        return values;
    }
}
