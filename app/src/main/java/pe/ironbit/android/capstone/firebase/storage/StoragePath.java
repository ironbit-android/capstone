package pe.ironbit.android.capstone.firebase.storage;

import com.google.firebase.storage.StorageReference;

public class StoragePath {
    private StringBuilder builder;
    private StorageReference reference;

    public StoragePath(StorageReference base) {
        reference = base;
        builder = new StringBuilder();
    }

    public StoragePath append(String value) {
        if (builder.length() == 0) {
            builder.append(value);
        } else {
            builder.append('/').append(value);
        }
        return this;
    }

    public StoragePath erase() {
        builder.setLength(0);
        return this;
    }

    public StoragePath eraseLast() {
        int index = builder.toString().lastIndexOf('/');
        if (index == -1) {
            builder.setLength(0);
        } else {
            builder.setLength(index);
        }
        return this;
    }

    public String getPath() {
        return builder.toString();
    }

    public StorageReference getReference() {
        return reference.child(builder.toString());
    }
}
