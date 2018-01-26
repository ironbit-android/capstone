package pe.ironbit.android.capstone.storage.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import pe.ironbit.android.capstone.model.BookTable.BookTableMapper;
import pe.ironbit.android.capstone.storage.contract.BookTableContract.BookTableEntry;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;

public class BookTableLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;

    private int bookId;

    private int section;

    private OnStorageListener listener;

    public BookTableLoader(Context context) {
        this.context = context;
        this.listener = null;
        loadList();
    }

    public BookTableLoader setListener(OnStorageListener listener) {
        this.listener = listener;
        return this;
    }

    public BookTableLoader loadList() {
        bookId = BookTableEntry.NULL_INDEX;
        section = BookTableEntry.NULL_INDEX;
        return this;
    }

    public BookTableLoader loadItem(int bookId, int section) {
        this.bookId = bookId;
        this.section = section;
        return this;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (bookId == BookTableEntry.NULL_INDEX) {
            return BookTableMapper.query(context);
        } else {
            return BookTableMapper.query(context, bookId, section);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && listener != null) {
            listener.onEvent(BookTableMapper.generateListData(cursor));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (listener != null) {
            listener.onEvent(null);
        }
    }
}
