package pe.ironbit.android.capstone.view.managerlabel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.model.LabelPrime.LabelPrimeData;

public class ManagerLabelAdapter extends RecyclerView.Adapter<ManagerLabelHolder> {
    ManagerLabelListener listener;

    List<LabelPrimeData> list;

    public ManagerLabelAdapter(ManagerLabelListener listener) {
        this.listener = listener;
        list = new ArrayList<>();
    }

    @Override
    public ManagerLabelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View view = LayoutInflater.from(context)
                                  .inflate(R.layout.recyclerview_manager_label, parent, false);

        ManagerLabelHolder viewHolder = new ManagerLabelHolder(view, listener);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ManagerLabelHolder holder, int position) {
        holder.bind(position, list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public LabelPrimeData eraseItem(int index) {
        LabelPrimeData data = list.remove(index);
        notifyDataSetChanged();
        return data;
    }

    public LabelPrimeData getItem(int index) {
        return list.get(index);
    }

    public List<LabelPrimeData> getList() {
        return list;
    }

    public void update(List<LabelPrimeData> list) {
        if (list == null) {
            return;
        }

        this.list = list;
        notifyDataSetChanged();
    }
}
