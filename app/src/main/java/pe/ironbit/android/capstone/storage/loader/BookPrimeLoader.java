package pe.ironbit.android.capstone.storage.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import pe.ironbit.android.capstone.model.BookPrime.BookPrimeMapper;
import pe.ironbit.android.capstone.storage.contract.BookContentContract.BookContentEntry;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;

public class BookPrimeLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;

    private int bookId;

    private OnStorageListener listener;

    public BookPrimeLoader(Context context) {
        this.context = context;
        bookId = BookContentEntry.NULL_INDEX;
        listener = null;
    }

    public BookPrimeLoader setListener(OnStorageListener listener) {
        this.listener = listener;
        return this;
    }

    public BookPrimeLoader loadList() {
        bookId = BookContentEntry.NULL_INDEX;
        return this;
    }

    public BookPrimeLoader loadItem(int bookId) {
        this.bookId = bookId;
        return this;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (bookId == BookContentEntry.NULL_INDEX) {
            return BookPrimeMapper.query(context);
        } else {
            return BookPrimeMapper.query(context, bookId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && listener != null) {
            listener.onEvent(BookPrimeMapper.generateListData(cursor));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (listener != null) {
            listener.onEvent(null);
        }
    }
}
