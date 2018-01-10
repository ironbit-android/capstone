package pe.ironbit.android.capstone.view.bookmenu;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.event.base.BaseListener;
import pe.ironbit.android.capstone.firebase.image.ImageLoader;
import pe.ironbit.android.capstone.firebase.storage.StorageService;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;

public class BookMenuHolder extends RecyclerView.ViewHolder {
    private Integer position;

    private BaseListener listener;

    @BindView(R.id.recyclerview_book_menu_book_image)
    ImageView bookImage;

    @BindView(R.id.recyclerview_book_menu_book_name)
    TextView bookName;

    @BindView(R.id.recyclerview_book_menu_book_author)
    TextView bookAuthor;

    public BookMenuHolder(View view, final BaseListener listener) {
        super(view);

        ButterKnife.bind(this, view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.update(position);
            }
        });
    }

    public void bind(int position, BookPrimeData book, StorageService service) {
        this.position = position;

        bookName.setText(book.getName());

        bookAuthor.setText(book.getAuthor());

        ImageLoader.init(service)
                   .into(bookImage)
                   .load(book.getBookId());
    }
}
