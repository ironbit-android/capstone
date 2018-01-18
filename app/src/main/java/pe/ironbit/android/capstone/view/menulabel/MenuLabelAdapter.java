package pe.ironbit.android.capstone.view.menulabel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.event.base.BaseListener;
import pe.ironbit.android.capstone.model.LabelPrime.LabelPrimeData;

public class MenuLabelAdapter extends RecyclerView.Adapter<MenuLabelHolder> {
    BaseListener listener;

    List<LabelPrimeData> list;

    public MenuLabelAdapter(BaseListener listener) {
        this.listener = listener;
        list = new ArrayList<>();
    }

    @Override
    public MenuLabelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context)
                                  .inflate(R.layout.recyclerview_menu_label, parent, false);

        return new MenuLabelHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(MenuLabelHolder holder, int position) {
        holder.bind(position, list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public LabelPrimeData get(int index) {
        return list.get(index);
    }

    public void erase(int index) {
        list.remove(index);
        notifyDataSetChanged();
    }

    public void update(List list) {
        if (list == null) {
            return;
        }

        this.list = list;
        notifyDataSetChanged();
    }

    public void update(int index, LabelPrimeData data) {
        list.set(index, data);
        notifyDataSetChanged();
    }
}
