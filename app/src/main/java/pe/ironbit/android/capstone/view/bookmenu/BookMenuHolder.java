package pe.ironbit.android.capstone.view.bookmenu;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.firebase.image.ImageLoader;
import pe.ironbit.android.capstone.firebase.storage.StorageService;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeStatus;

public class BookMenuHolder extends RecyclerView.ViewHolder {
    private Integer position;

    @BindView(R.id.recyclerview_book_menu_book_image)
    ImageView bookImage;

    @BindView(R.id.recyclerview_book_menu_book_name)
    TextView bookName;

    @BindView(R.id.recyclerview_book_menu_book_author)
    TextView bookAuthor;

    @BindView(R.id.recyclerview_book_menu_book_icon)
    ImageView bookIcon;

    @BindView(R.id.recyclerview_book_menu_cardview)
    CardView cardView;

    public BookMenuHolder(View view, final BookMenuListener listener) {
        super(view);

        ButterKnife.bind(this, view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.update(position, BookMenuListener.ClickType.Short);
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.update(position, BookMenuListener.ClickType.Long);
                return true;
            }
        });
    }

    public void bind(StorageService service, int position, BookPrimeData book, float alpha) {
        this.position = position;

        bookName.setText(book.getName());

        bookAuthor.setText(book.getAuthor());

        ImageLoader.init(service)
                   .into(bookImage)
                   .load(book.getBookId());

        if (book.getStatus() != BookPrimeStatus.Global) {
            bookIcon.setVisibility(View.GONE);
        }

        cardView.setAlpha(alpha);
    }
}
