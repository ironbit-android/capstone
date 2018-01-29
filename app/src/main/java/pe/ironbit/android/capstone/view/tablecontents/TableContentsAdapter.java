package pe.ironbit.android.capstone.view.tablecontents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.event.base.BaseListener;
import pe.ironbit.android.capstone.model.BookTable.BookTableData;

public class TableContentsAdapter extends RecyclerView.Adapter<TableContentsHolder> {
    private BaseListener listener;

    private List<BookTableData> list;

    public TableContentsAdapter(final BaseListener listener) {
        this.listener = listener;
        list = new ArrayList<>();
    }

    @Override
    public TableContentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context)
                                  .inflate(R.layout.recyclerview_table_contents, parent, false);

        return new TableContentsHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(TableContentsHolder holder, int position) {
        BookTableData data = list.get(position);
        holder.bind(data.getSection(), data.getValue());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateList(List<BookTableData> list) {
        if (list == null) {
            list = new ArrayList<>();
        }

        this.list = list;
        notifyDataSetChanged();
    }
}
