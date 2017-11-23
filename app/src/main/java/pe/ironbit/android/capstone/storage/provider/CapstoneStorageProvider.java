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
import pe.ironbit.android.capstone.storage.contract.CapstoneStorageContract;

public class CapstoneStorageProvider extends ContentProvider {
    private static final String TAG = CapstoneStorageProvider.class.getSimpleName();

    private CapstoneStorageConfig capstoneStorageConfig;

    private static final int BOOK_CONTENT_LIST = 100;

    private static final int BOOK_CONTENT_ITEM = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY, BookContentContract.PATH_TABLE, BOOK_CONTENT_LIST);

        sUriMatcher.addURI(CapstoneStorageContract.CONTENT_AUTHORITY, BookContentContract.PATH_TABLE + "/#/#", BOOK_CONTENT_ITEM);
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
            case BOOK_CONTENT_LIST:
                return BookContentEntry.CONTENT_LIST_TYPE;
            case BOOK_CONTENT_ITEM:
                return BookContentEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = capstoneStorageConfig.getReadableDatabase();

        Cursor cursor = null;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case BOOK_CONTENT_ITEM:
                selection = BookContentEntry.BOOK_ID + "=? AND " + BookContentEntry.BOOK_SECTION + "=?";
                String[] array = uri.getPath().split("/");
                selectionArgs = new String[] {array[array.length - 2], array[array.length - 1]};
            case BOOK_CONTENT_LIST:
                cursor = database.query(BookContentEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        if (match != BOOK_CONTENT_LIST) {
            throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

        SQLiteDatabase database = capstoneStorageConfig.getWritableDatabase();
        long outcome = database.insert(BookContentEntry.TABLE_NAME, null, contentValues);
        if (outcome == -1) {
            Log.e(TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, outcome);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted = 0;
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase database = capstoneStorageConfig.getWritableDatabase();

        switch (match) {
            case BOOK_CONTENT_ITEM:
                selection = BookContentEntry.BOOK_ID + "=? AND " + BookContentEntry.BOOK_SECTION + "=?";
                String[] array = uri.getPath().split("/");
                selectionArgs = new String[] {array[array.length - 2], array[array.length - 1]};
            case BOOK_CONTENT_LIST:
                deleted = database.delete(BookContentEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (deleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updated = 0;
        final int match = sUriMatcher.match(uri);
        SQLiteDatabase database = capstoneStorageConfig.getWritableDatabase();

        switch (match) {
            case BOOK_CONTENT_ITEM:
                selection = BookContentEntry.BOOK_ID + "=? AND " + BookContentEntry.BOOK_SECTION + "=?";
                String[] array = uri.getPath().split("/");
                selectionArgs = new String[] {array[array.length - 2], array[array.length - 1]};
            case BOOK_CONTENT_LIST:
                updated = database.update(BookContentEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (updated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updated;
    }
}
