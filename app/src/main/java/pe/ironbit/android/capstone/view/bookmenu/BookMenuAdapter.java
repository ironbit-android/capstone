package pe.ironbit.android.capstone.view.bookmenu;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.event.base.BaseListener;
import pe.ironbit.android.capstone.firebase.storage.StorageService;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;

public class BookMenuAdapter extends RecyclerView.Adapter<BookMenuHolder> {
    BaseListener listener;

    StorageService service;

    List<BookPrimeData> list;

    public BookMenuAdapter(BaseListener listener, StorageService service) {
        this.listener = listener;
        this.service = service;
        list = new ArrayList<>();
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
        BookPrimeData book = list.get(position);
        holder.bind(position, book, service);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<BookPrimeData> list) {
        if (list == null) {
            return;
        }

        this.list = list;
        notifyDataSetChanged();
    }

    public void setItem(BookPrimeData data, int index) {
        if (index >= list.size()) {
            return;
        }

        list.set(index, data);
        notifyDataSetChanged();
    }
}
