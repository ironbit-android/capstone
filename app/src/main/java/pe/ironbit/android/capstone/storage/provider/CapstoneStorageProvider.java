package pe.ironbit.android.capstone.storage.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import pe.ironbit.android.capstone.storage.config.CapstoneStorageConfig;
import pe.ironbit.android.capstone.storage.contract.BookContentContract;
import pe.ironbit.android.capstone.storage.contract.BookContentContract.BookContentEntry;
import pe.ironbit.android.capstone.storage.contract.BookPrimeContract;
import pe.ironbit.android.capstone.storage.contract.BookPrimeContract.BookPrimeEntry;
import pe.ironbit.android.capstone.storage.contract.BookTableContract;
import pe.ironbit.android.capstone.storage.contract.BookTableContract.BookTableEntry;
import pe.ironbit.android.capstone.storage.contract.CapstoneStorageContract;
import pe.ironbit.android.capstone.storage.contract.LabelBookContract;
import pe.ironbit.android.capstone.storage.contract.LabelBookContract.LabelBookEntry;
import pe.ironbit.android.capstone.storage.contract.LabelPrimeContract;
import pe.ironbit.android.capstone.storage.contract.LabelPrimeContract.LabelPrimeEntry;

public class CapstoneStorageProvider extends ContentProvider {
    private static final String TAG = CapstoneStorageProvider.class.getSimpleName();

    private CapstoneStorageConfig capstoneStorageConfig;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY,
                           BookContentContract.PATH_TABLE,
                           BookContentContract.BOOK_CONTENT_LIST);

        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY,
                           BookContentContract.PATH_TABLE + "/#/#",
                           BookContentContract.BOOK_CONTENT_ITEM);

        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY,
                           BookContentContract.PATH_TABLE + "/" + BookContentContract.PATH_BOOK_CONTENT_GROUP + "/#",
                           BookContentContract.BOOK_CONTENT_GROUP);

        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY,
                           BookPrimeContract.PATH_TABLE,
                           BookPrimeContract.BOOK_PRIME_LIST);

        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY,
                           BookPrimeContract.PATH_TABLE + "/#",
                           BookPrimeContract.BOOK_PRIME_ITEM);

        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY,
                           BookTableContract.PATH_TABLE,
                           BookTableContract.BOOK_TABLE_LIST);

        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY,
                           BookTableContract.PATH_TABLE + "/#/#",
                           BookTableContract.BOOK_TABLE_ITEM);

        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY,
                           BookTableContract.PATH_TABLE + "/" + BookTableContract.PATH_BOOK_TABLE_GROUP + "/#",
                           BookTableContract.BOOK_TABLE_GROUP);

        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY,
                           LabelBookContract.PATH_TABLE,
                           LabelBookContract.LABEL_BOOK_LIST);

        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY,
                           LabelBookContract.PATH_TABLE + "/" + LabelBookContract.PATH_LABEL_BOOK_LABEL_IDEN + "/#",
                           LabelBookContract.LABEL_BOOK_LABEL_IDEN);

        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY,
                           LabelBookContract.PATH_TABLE + "/" + LabelBookContract.PATH_LABEL_BOOK_BOOK_IDEN + "/#",
                           LabelBookContract.LABEL_BOOK_BOOK_IDEN);

        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY,
                           LabelBookContract.PATH_TABLE + "/#/#",
                           LabelBookContract.LABEL_BOOK_ITEM);

        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY,
                           LabelPrimeContract.PATH_TABLE,
                           LabelPrimeContract.LABEL_PRIME_LIST);

        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY,
                           LabelPrimeContract.PATH_TABLE + "/#",
                           LabelPrimeContract.LABEL_PRIME_ITEM);
    }

    @Override
    public boolean onCreate() {
        capstoneStorageConfig = new CapstoneStorageConfig(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BookContentContract.BOOK_CONTENT_ITEM:
                return BookContentEntry.CONTENT_ITEM_TYPE;
            case BookContentContract.BOOK_CONTENT_LIST:
                return BookContentEntry.CONTENT_LIST_TYPE;
            case BookContentContract.BOOK_CONTENT_GROUP:
                return BookContentEntry.CONTENT_GROUP_TYPE;

            case BookPrimeContract.BOOK_PRIME_ITEM:
                return BookPrimeEntry.CONTENT_ITEM_TYPE;
            case BookPrimeContract.BOOK_PRIME_LIST:
                return BookPrimeEntry.CONTENT_LIST_TYPE;

            case BookTableContract.BOOK_TABLE_ITEM:
                return BookTableEntry.CONTENT_ITEM_TYPE;
            case BookTableContract.BOOK_TABLE_LIST:
                return BookTableEntry.CONTENT_LIST_TYPE;
            case BookTableContract.BOOK_TABLE_GROUP:
                return BookTableEntry.CONTENT_GROUP_TYPE;

            case LabelBookContract.LABEL_BOOK_ITEM:
                return LabelBookEntry.CONTENT_ITEM_TYPE;
            case LabelBookContract.LABEL_BOOK_LIST:
                return LabelBookEntry.CONTENT_LIST_TYPE;
            case LabelBookContract.LABEL_BOOK_LABEL_IDEN:
                return LabelBookEntry.CONTENT_LABEL_IDEN_TYPE;
            case LabelBookContract.LABEL_BOOK_BOOK_IDEN:
                return LabelBookEntry.CONTENT_BOOK_IDEN_TYPE;

            case LabelPrimeContract.LABEL_PRIME_ITEM:
                return LabelPrimeEntry.CONTENT_ITEM_TYPE;
            case LabelPrimeContract.LABEL_PRIME_LIST:
                return LabelPrimeEntry.CONTENT_LIST_TYPE;

            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String tableName = "";
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BookContentContract.BOOK_CONTENT_ITEM:
                selection = BookContentEntry.BOOK_ID + "=? AND " + BookContentEntry.BOOK_SECTION + "=?";
                String[] array = uri.getPath().split("/");
                selectionArgs = new String[] {array[array.length - 2], array[array.length - 1]};
            case BookContentContract.BOOK_CONTENT_LIST:
                tableName = BookContentEntry.TABLE_NAME;
                break;
            case BookContentContract.BOOK_CONTENT_GROUP:
                selection = BookContentEntry.BOOK_ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
                tableName = BookContentEntry.TABLE_NAME;
                break;

            case BookPrimeContract.BOOK_PRIME_ITEM:
                selection = BookPrimeEntry.BOOK_ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
            case BookPrimeContract.BOOK_PRIME_LIST:
                tableName = BookPrimeEntry.TABLE_NAME;
                break;

            case BookTableContract.BOOK_TABLE_ITEM:
                selection = BookTableEntry.BOOK_ID + "=? AND " + BookTableEntry.BOOK_SECTION + "=?";
                array = uri.getPath().split("/");
                selectionArgs = new String[]{array[array.length - 2], array[array.length - 1]};
            case BookTableContract.BOOK_TABLE_LIST:
                tableName = BookTableEntry.TABLE_NAME;
                break;
            case BookTableContract.BOOK_TABLE_GROUP:
                selection = BookTableEntry.BOOK_ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
                tableName = BookTableEntry.TABLE_NAME;
                break;

            case LabelBookContract.LABEL_BOOK_ITEM:
                selection = LabelBookEntry.LABEL_ID + "=? AND " + LabelBookEntry.BOOK_ID + "=?";
                array = uri.getPath().split("/");
                selectionArgs = new String[]{array[array.length - 2], array[array.length - 1]};
            case LabelBookContract.LABEL_BOOK_LIST:
                tableName = LabelBookEntry.TABLE_NAME;
                break;
            case LabelBookContract.LABEL_BOOK_LABEL_IDEN:
                selection = LabelBookEntry.LABEL_ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
                tableName = LabelBookEntry.TABLE_NAME;
                break;
            case LabelBookContract.LABEL_BOOK_BOOK_IDEN:
                selection = LabelBookEntry.BOOK_ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
                tableName = LabelBookEntry.TABLE_NAME;
                break;

            case LabelPrimeContract.LABEL_PRIME_ITEM:
                selection = LabelPrimeEntry.LABEL_ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
            case LabelPrimeContract.LABEL_PRIME_LIST:
                tableName = LabelPrimeEntry.TABLE_NAME;
                break;

            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        SQLiteDatabase database = capstoneStorageConfig.getReadableDatabase();
        Cursor cursor = database.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        String tableName = "";

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BookContentContract.BOOK_CONTENT_LIST:
                tableName = BookContentEntry.TABLE_NAME;
                break;

            case BookPrimeContract.BOOK_PRIME_LIST:
                tableName = BookPrimeEntry.TABLE_NAME;
                break;

            case BookTableContract.BOOK_TABLE_LIST:
                tableName = BookTableEntry.TABLE_NAME;
                break;

            case LabelBookContract.LABEL_BOOK_LIST:
                tableName = LabelBookEntry.TABLE_NAME;
                break;

            case LabelPrimeContract.LABEL_PRIME_LIST:
                tableName = LabelPrimeEntry.TABLE_NAME;
                break;

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

        SQLiteDatabase database = capstoneStorageConfig.getWritableDatabase();
        long outcome = database.insert(tableName, null, contentValues);
        if (outcome == -1) {
            Log.e(TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, outcome);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = "";

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BookContentContract.BOOK_CONTENT_ITEM:
                selection = BookContentEntry.BOOK_ID + "=? AND " + BookContentEntry.BOOK_SECTION + "=?";
                String[] array = uri.getPath().split("/");
                selectionArgs = new String[] {array[array.length - 2], array[array.length - 1]};
            case BookContentContract.BOOK_CONTENT_LIST:
                tableName = BookContentEntry.TABLE_NAME;
                break;

            case BookPrimeContract.BOOK_PRIME_ITEM:
                selection = BookPrimeEntry.BOOK_ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
            case BookPrimeContract.BOOK_PRIME_LIST:
                tableName = BookPrimeEntry.TABLE_NAME;
                break;

            case BookTableContract.BOOK_TABLE_ITEM:
                selection = BookTableEntry.BOOK_ID + "=? AND " + BookTableEntry.BOOK_SECTION + "=?";
                array = uri.getPath().split("/");
                selectionArgs = new String[] {array[array.length - 2], array[array.length - 1]};
            case BookTableContract.BOOK_TABLE_LIST:
                tableName = BookTableEntry.TABLE_NAME;
                break;

            case LabelBookContract.LABEL_BOOK_ITEM:
                selection = LabelBookEntry.LABEL_ID + "=? AND " + LabelBookEntry.BOOK_ID + "=?";
                array = uri.getPath().split("/");
                selectionArgs = new String[]{array[array.length - 2], array[array.length - 1]};
            case LabelBookContract.LABEL_BOOK_LIST:
                tableName = LabelBookEntry.TABLE_NAME;
                break;

            case LabelBookContract.LABEL_BOOK_LABEL_IDEN:
                selection = LabelBookEntry.LABEL_ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
                tableName = LabelBookEntry.TABLE_NAME;
                break;
            case LabelBookContract.LABEL_BOOK_BOOK_IDEN:
                selection = LabelBookEntry.BOOK_ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
                tableName = LabelBookEntry.TABLE_NAME;
                break;

            case LabelPrimeContract.LABEL_PRIME_ITEM:
                selection = LabelPrimeEntry.LABEL_ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
            case LabelPrimeContract.LABEL_PRIME_LIST:
                tableName = LabelPrimeEntry.TABLE_NAME;
                break;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        SQLiteDatabase database = capstoneStorageConfig.getWritableDatabase();
        int outcome = database.delete(tableName, selection, selectionArgs);
        if (outcome != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return outcome;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        String tableName = "";

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case BookContentContract.BOOK_CONTENT_ITEM:
                selection = BookContentEntry.BOOK_ID + "=? AND " + BookContentEntry.BOOK_SECTION + "=?";
                String[] array = uri.getPath().split("/");
                selectionArgs = new String[] {array[array.length - 2], array[array.length - 1]};
            case BookContentContract.BOOK_CONTENT_LIST:
                tableName = BookContentEntry.TABLE_NAME;
                break;

            case BookPrimeContract.BOOK_PRIME_ITEM:
                selection = BookPrimeEntry.BOOK_ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
            case BookPrimeContract.BOOK_PRIME_LIST:
                tableName = BookPrimeEntry.TABLE_NAME;
                break;

            case BookTableContract.BOOK_TABLE_ITEM:
                selection = BookTableEntry.BOOK_ID + "=? AND " + BookTableEntry.BOOK_SECTION + "=?";
                array = uri.getPath().split("/");
                selectionArgs = new String[] {array[array.length - 2], array[array.length - 1]};
            case BookTableContract.BOOK_TABLE_LIST:
                tableName = BookTableEntry.TABLE_NAME;
                break;

            case LabelPrimeContract.LABEL_PRIME_ITEM:
                selection = LabelPrimeEntry.LABEL_ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
            case LabelPrimeContract.LABEL_PRIME_LIST:
                tableName = LabelPrimeEntry.TABLE_NAME;
                break;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        SQLiteDatabase database = capstoneStorageConfig.getWritableDatabase();
        int outcome = database.update(tableName, contentValues, selection, selectionArgs);
        if (outcome != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return outcome;
    }
}
