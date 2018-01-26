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
    private static final int DOWNLOAD_ACTIVE = 1;

    private BookMenuListener listener;

    private StorageService service;

    private List<BookPrimeData> bookList;

    private List<Integer> downloadList;

    private List<Float> alphaList;

    public BookMenuAdapter(BookMenuListener listener, StorageService service) {
        this.listener = listener;
        this.service = service;
        bookList = new ArrayList<>();
        alphaList = new ArrayList<>();
        downloadList = new ArrayList<>();
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
        boolean download = downloadList.get(position) == DOWNLOAD_ACTIVE;
        holder.bind(service, position, book, alpha, download);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public void updateList(List<BookPrimeData> bookList, List<Float> alphaList, List<Integer> downloadList) {
        this.bookList = bookList;
        this.alphaList = alphaList;
        this.downloadList = downloadList;
        notifyDataSetChanged();
    }

    public void updateItem(int position, BookPrimeData book, float alpha, int download) {
        bookList.set(position, book);
        alphaList.set(position, alpha);
        downloadList.set(position, download);
        notifyItemChanged(position);
    }
}
