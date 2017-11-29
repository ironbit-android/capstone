package pe.ironbit.android.capstone.storage.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pe.ironbit.android.capstone.storage.contract.BookContentContract.BookContentEntry;
import pe.ironbit.android.capstone.storage.contract.BookPrimeContract.BookPrimeEntry;
import pe.ironbit.android.capstone.storage.contract.BookTableContract.BookTableEntry;
import pe.ironbit.android.capstone.storage.contract.LabelBookContract.LabelBookEntry;

public class CapstoneStorageConfig extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "capstone.db";

    private static final int DATABASE_VERSION = 1;

    public CapstoneStorageConfig(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createBookContextTable());
        sqLiteDatabase.execSQL(createBookPrimeTable());
        sqLiteDatabase.execSQL(createBookTableTable());
        sqLiteDatabase.execSQL(createLabelBookTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    protected String createBookContextTable() {
        return "CREATE TABLE " + BookContentEntry.TABLE_NAME + " (" +
               BookContentEntry.BOOK_ID + " INTEGER NOT NULL, " +
               BookContentEntry.BOOK_SECTION + " INTEGER NOT NULL, " +
               BookContentEntry.BOOK_VALUE + " TEXT NOT NULL, " +
               "PRIMARY KEY (" + BookContentEntry.BOOK_ID + ", " + BookContentEntry.BOOK_SECTION + "));";
    }

    protected String createBookPrimeTable() {
        return "CREATE TABLE " + BookPrimeEntry.TABLE_NAME + " (" +
               BookPrimeEntry.BOOK_ID + " INTEGER PRIMARY KEY, " +
               BookPrimeEntry.BOOK_NAME + " TEXT NOT NULL, " +
               BookPrimeEntry.BOOK_AUTHOR + " TEXT NOT NULL, " +
               BookPrimeEntry.BOOK_IMAGE + " TEXT NOT NULL, " +
               BookPrimeEntry.BOOK_FILE + " TEXT NOT NULL, " +
               BookPrimeEntry.BOOK_STATUS + " INTEGER NOT NULL);";
    }

    protected String createBookTableTable() {
        return "CREATE TABLE " + BookTableEntry.TABLE_NAME + " (" +
                BookTableEntry.BOOK_ID + " INTEGER NOT NULL, " +
                BookTableEntry.BOOK_SECTION + " INTEGER NOT NULL, " +
                BookTableEntry.BOOK_VALUE + " TEXT NOT NULL, " +
                "PRIMARY KEY (" + BookTableEntry.BOOK_ID + ", " + BookTableEntry.BOOK_SECTION + "));";
    }

    protected String createLabelBookTable() {
        return "CREATE TABLE " + LabelBookEntry.TABLE_NAME + " (" +
                LabelBookEntry.LABEL_ID + " INTEGER NOT NULL, " +
                LabelBookEntry.BOOK_ID + " INTEGER NOT NULL, " +
                "PRIMARY KEY (" + LabelBookEntry.LABEL_ID + ", " + LabelBookEntry.BOOK_ID + "));";
    }
}
