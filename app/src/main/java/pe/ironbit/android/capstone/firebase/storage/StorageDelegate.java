package pe.ironbit.android.capstone.firebase.storage;

import com.google.android.gms.tasks.Task;

public interface StorageDelegate {
    long getSize();

    StoragePath getStoragePath();

    void process(byte[] bytes);

    void setTask(Task<byte[]> task);
}
