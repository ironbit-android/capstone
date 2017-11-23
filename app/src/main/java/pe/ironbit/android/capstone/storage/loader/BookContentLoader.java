package pe.ironbit.android.capstone.storage.loader;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import pe.ironbit.android.capstone.model.BookContent.BookContentMapper;
import pe.ironbit.android.capstone.storage.contract.BookContentContract;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;

public class BookContentLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;

    private int bookId;

    private int section;

    private OnStorageListener listener;

    public BookContentLoader(Context context, OnStorageListener listener) {
        this.context = context;
        this.listener = listener;
        loadList();
    }

    public void loadList() {
        bookId = BookContentContract.BookContentEntry.NULL_INDEX;
        section = BookContentContract.BookContentEntry.NULL_INDEX;
    }

    public void loadItem(int bookId, int section) {
        this.bookId = bookId;
        this.section = section;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (bookId == BookContentContract.BookContentEntry.NULL_INDEX) {
            return BookContentMapper.query(context);
        } else {
            return BookContentMapper.query(context, bookId, section);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            listener.onEvent(BookContentMapper.generateListData(cursor));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        listener.onEvent(null);
    }
}
