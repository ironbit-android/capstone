package pe.ironbit.android.capstone.firebase.storage;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashSet;
import java.util.Set;

import pe.ironbit.android.capstone.R;

public class StorageService {
    private static final String TAG = StorageService.class.getSimpleName();

    private static Set<Context> contextCollection = new HashSet<Context>();

    private FirebaseStorage firebaseStorage;

    private Context context;

    public StorageService(Context context) {
        this.context = context;
        initializeFirebase(context);
        String bucket = context.getString(R.string.storage_bucket);
        firebaseStorage = FirebaseStorage.getInstance(bucket);
    }

    public void process(final StorageDelegate delegate) {
        Task<byte[]> task = delegate.getStoragePath().getReference().getBytes(delegate.getSize());
        delegate.setTask(task);

        task.addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                delegate.process(bytes);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    public Context getContext() {
        return context;
    }

    public StoragePath getStoragePath() {
        return new StoragePath(firebaseStorage.getReference());
    }

    private synchronized void initializeFirebase(Context context) {
        if (!contextCollection.contains(context)) {
            contextCollection.add(context);
            FirebaseApp.initializeApp(context);
        }
    }
}
