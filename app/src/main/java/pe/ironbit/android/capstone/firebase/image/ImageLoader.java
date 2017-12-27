package pe.ironbit.android.capstone.firebase.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.firebase.storage.StoragePath;
import pe.ironbit.android.capstone.firebase.storage.StorageService;

public class ImageLoader {
    private static final int NILL = -1;

    private int bookId;

    private ImageView view;

    private StorageService service;

    private ImageLoader(StorageService service) {
        bookId = NILL;
        view = null;
        this.service = service;
    }

    public static ImageLoader init(StorageService service) {
        return new ImageLoader(service);
    }

    public ImageLoader load(int bookId) {
        this.bookId = bookId;

        if (view != null) {
            execute();
        }

        return this;
    }

    public ImageLoader into(ImageView view) {
        this.view = view;

        if (bookId != NILL) {
            execute();
        }

        return this;
    }

    private void execute() {
        Context context = service.getContext();
        StoragePath storagePath = service.getStoragePath();

        String folder = context.getString(R.string.book_folder_path_prefix) + String.valueOf(bookId);
        String file = folder + context.getString(R.string.image_file_path_suffix);

        storagePath.append(folder).append(file);

        Glide.with(service.getContext())
             .load(storagePath.getReference())
             .into(view);
    }
}
