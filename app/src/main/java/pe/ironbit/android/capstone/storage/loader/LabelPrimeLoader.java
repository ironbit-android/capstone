package pe.ironbit.android.capstone.storage.loader;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import pe.ironbit.android.capstone.model.LabelPrime.LabelPrimeMapper;
import pe.ironbit.android.capstone.storage.contract.LabelPrimeContract.LabelPrimeEntry;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;

public class LabelPrimeLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private Context context;

    private int labelId;

    private OnStorageListener listener;

    public LabelPrimeLoader(Context context) {
        this.context = context;
        this.listener = null;
        loadList();
    }

    public LabelPrimeLoader setListener(OnStorageListener listener) {
        this.listener = listener;
        return this;
    }

    public LabelPrimeLoader loadList() {
        labelId = LabelPrimeEntry.NULL_INDEX;
        return this;
    }

    public LabelPrimeLoader LoadItem(int labelId, int bookId) {
        this.labelId = labelId;
        return this;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (labelId == LabelPrimeEntry.NULL_INDEX) {
            return LabelPrimeMapper.query(context);
        } else {
            return LabelPrimeMapper.query(context, labelId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && listener != null) {
            listener.onEvent(LabelPrimeMapper.generateListData(cursor));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (listener != null) {
            listener.onEvent(null);
        }
    }
}
