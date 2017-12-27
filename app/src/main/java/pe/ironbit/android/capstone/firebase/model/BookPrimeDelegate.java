package pe.ironbit.android.capstone.firebase.model;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.firebase.storage.StoragePath;
import pe.ironbit.android.capstone.firebase.storage.StorageService;
import pe.ironbit.android.capstone.generic.Action;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;

public class BookPrimeDelegate extends ModelDelegate {
    private static final String TAG = BookPrimeDelegate.class.getSimpleName();

    private BookPrimeData data;

    private Action action;

    private StoragePath storagePath;

    public BookPrimeDelegate(StorageService service, int bookId) {
        super(Long.parseLong(service.getContext().getString(R.string.book_metadata_file_size)));
        data = null;
        action = null;
        generateStoragePath(bookId, service);
    }

    public BookPrimeDelegate(StorageService service, int bookId, Action<BookPrimeData> action) {
        super(Long.parseLong(service.getContext().getString(R.string.book_metadata_file_size)));
        data = null;
        this.action = action;
        generateStoragePath(bookId, service);
    }

    public void setAction(Action<BookPrimeData> action) {
        this.action = action;
    }

    @Override
    public StoragePath getStoragePath() {
        return storagePath;
    }

    @Override
    public void process(byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            data = mapper.readValue(bytes, BookPrimeData.class);
            if (action != null) {
                action.execute(data);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public BookPrimeData getData() {
        return data;
    }

    private void generateStoragePath(int bookId, StorageService service) {
        Context context = service.getContext();
        storagePath = service.getStoragePath();
        storagePath.append(context.getString(R.string.book_folder_path_prefix) + String.valueOf(bookId))
                   .append(context.getString(R.string.book_metadata_file_name));
    }
}
