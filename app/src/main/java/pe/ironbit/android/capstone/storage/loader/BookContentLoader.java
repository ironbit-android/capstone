package pe.ironbit.android.capstone.storage.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import pe.ironbit.android.capstone.model.BookContent.BookContentMapper;
import pe.ironbit.android.capstone.storage.contract.BookContentContract.BookContentEntry;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;

public class BookContentLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;

    private int bookId;

    private int section;

    private OnStorageListener listener;

    public BookContentLoader(Context context) {
        this.context = context;
        this.listener = null;
        loadList();
    }

    public BookContentLoader setListener(OnStorageListener listener) {
        this.listener = listener;
        return this;
    }

    public BookContentLoader loadList() {
        bookId = BookContentEntry.NULL_INDEX;
        section = BookContentEntry.NULL_INDEX;
        return this;
    }

    public BookContentLoader load(int bookId) {
        this.bookId = bookId;
        section = BookContentEntry.NULL_INDEX;
        return this;
    }

    public BookContentLoader loadItem(int bookId, int section) {
        this.bookId = bookId;
        this.section = section;
        return this;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (bookId == BookContentEntry.NULL_INDEX) {
            return BookContentMapper.query(context);
        }
        if (section == BookContentEntry.NULL_INDEX) {
            return BookContentMapper.query(context);
        }
        return BookContentMapper.query(context, bookId, section);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && listener != null) {
            listener.onEvent(BookContentMapper.generateListData(cursor));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (listener != null) {
            listener.onEvent(null);
        }
    }
}
