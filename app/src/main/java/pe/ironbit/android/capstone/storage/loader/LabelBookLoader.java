package pe.ironbit.android.capstone.storage.loader;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import pe.ironbit.android.capstone.model.LabelBook.LabelBookMapper;
import pe.ironbit.android.capstone.storage.contract.LabelBookContract.LabelBookEntry;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;

public class LabelBookLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;

    private int labelId;

    private int bookId;

    private OnStorageListener listener;

    public LabelBookLoader(Context context) {
        this.context = context;
        this.listener = null;
        loadList();
    }

    public LabelBookLoader setListener(OnStorageListener listener) {
        this.listener = listener;
        return this;
    }

    public LabelBookLoader loadList() {
        labelId = LabelBookEntry.NULL_INDEX;
        bookId = LabelBookEntry.NULL_INDEX;
        return this;
    }

    public LabelBookLoader LoadItem(int labelId, int bookId) {
        this.labelId = labelId;
        this.bookId = bookId;
        return this;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (labelId == LabelBookEntry.NULL_INDEX) {
            return LabelBookMapper.query(context);
        } else {
            return LabelBookMapper.query(context, labelId, bookId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && listener != null) {
            listener.onEvent(LabelBookMapper.generateListData(cursor));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (listener != null) {
            listener.onEvent(null);
        }
    }
}
