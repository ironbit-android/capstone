package pe.ironbit.android.capstone.firebase.model;

import com.google.android.gms.tasks.Task;

import pe.ironbit.android.capstone.firebase.storage.StorageDelegate;

public abstract class ModelDelegate implements StorageDelegate {
    private Task<byte[]> task;

    private long fileMaxSize;

    public ModelDelegate(long fileMaxSize) {
        task = null;
        this.fileMaxSize = fileMaxSize;
    }

    @Override
    public long getSize() {
        return fileMaxSize;
    }

    @Override
    public void setTask(Task<byte[]> task) {
        this.task = task;
    }

    public Task<byte[]> getTask() {
        return task;
    }

    public byte[] getOutcome() {
        return task.getResult();
    }

    public boolean isComplete() {
        return task.isComplete();
    }

    public boolean isSuccessful() {
        return task.isSuccessful();
    }
}
