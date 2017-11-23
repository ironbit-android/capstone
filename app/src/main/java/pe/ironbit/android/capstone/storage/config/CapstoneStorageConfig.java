package pe.ironbit.android.capstone.storage.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pe.ironbit.android.capstone.storage.contract.BookContentContract.BookContentEntry;

public class CapstoneStorageConfig extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "capstone.db";

    private static final int DATABASE_VERSION = 1;

    public CapstoneStorageConfig(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createBookContextTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    protected String createBookContextTable() {
        return "CREATE TABLE " + BookContentEntry.TABLE_NAME + " ("
               + BookContentEntry.BOOK_ID + " INTEGER NOT NULL, "
               + BookContentEntry.BOOK_SECTION + " INTEGER NOT NULL, "
               + BookContentEntry.BOOK_VALUE + " TEXT NOT NULL, "
               + "PRIMARY KEY (" + BookContentEntry.BOOK_ID + ", " + BookContentEntry.BOOK_SECTION + "));";
    }
}
