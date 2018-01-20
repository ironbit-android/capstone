package pe.ironbit.android.capstone.view.bookmenu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.firebase.storage.StorageService;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;

public class BookMenuAdapter extends RecyclerView.Adapter<BookMenuHolder> {
    private BookMenuListener listener;

    private StorageService service;

    private List<Float> alphaList;

    private List<BookPrimeData> bookList;

    public BookMenuAdapter(BookMenuListener listener, StorageService service) {
        this.listener = listener;
        this.service = service;
        bookList = new ArrayList<>();
        alphaList = new ArrayList<>();
    }

    @Override
    public BookMenuHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context)
                                  .inflate(R.layout.recyclerview_book_menu, parent, false);

        BookMenuHolder viewHolder = new BookMenuHolder(view, listener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BookMenuHolder holder, int position) {
        float alpha = alphaList.get(position);
        BookPrimeData book = bookList.get(position);
        holder.bind(service, position, book, alpha);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void update(List<BookPrimeData> bookList, List<Float> alphaList) {
        if ((bookList == null) || (alphaList == null)) {
            return;
        }

        this.bookList = bookList;
        this.alphaList = alphaList;
        notifyDataSetChanged();
    }

    public void setAlpha(int index, float alpha) {
        if (index >= alphaList.size()) {
            return;
        }

        alphaList.set(index, alpha);
        notifyDataSetChanged();
    }
}
