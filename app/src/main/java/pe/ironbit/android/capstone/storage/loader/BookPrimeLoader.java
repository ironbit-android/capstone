package pe.ironbit.android.capstone.storage.loader;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

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

    public void setListener(OnStorageListener listener) {
        this.listener = listener;
    }

    public void loadList() {
        bookId = BookContentEntry.NULL_INDEX;
    }

    public void loadItem(int bookId) {
        this.bookId = bookId;
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
