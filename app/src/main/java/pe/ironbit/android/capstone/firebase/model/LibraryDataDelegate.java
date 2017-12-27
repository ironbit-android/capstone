package pe.ironbit.android.capstone.firebase.model;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.firebase.storage.StoragePath;
import pe.ironbit.android.capstone.firebase.storage.StorageService;
import pe.ironbit.android.capstone.generic.Action;
import pe.ironbit.android.capstone.model.Library.LibraryData;

public class LibraryDataDelegate extends ModelDelegate {
    private static final String TAG = LibraryDataDelegate.class.getSimpleName();

    private LibraryData data;

    private Action action;

    private StoragePath storagePath;

    public LibraryDataDelegate(StorageService service) {
        super(Long.parseLong(service.getContext().getString(R.string.library_file_size)));
        data = null;
        action = null;
        generateStoragePath(service);
    }

    public LibraryDataDelegate(StorageService service, Action<LibraryData> action) {
        super(Long.parseLong(service.getContext().getString(R.string.library_file_size)));
        data = null;
        this.action = action;
        generateStoragePath(service);
    }

    public void setAction(Action<LibraryData> action) {
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
            data = mapper.readValue(bytes, LibraryData.class);
            if (action != null) {
                action.execute(data);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public LibraryData getData() {
        return data;
    }

    private void generateStoragePath(StorageService service) {
        Context context = service.getContext();
        storagePath = service.getStoragePath();
        storagePath.append(context.getString(R.string.library_file_name));
    }
}
