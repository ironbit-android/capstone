package pe.ironbit.android.capstone.view.menulabel;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.event.base.BaseListener;
import pe.ironbit.android.capstone.model.LabelPrime.LabelPrimeData;

public class MenuLabelHolder extends RecyclerView.ViewHolder {
    private Integer position;

    @BindView(R.id.recyclerview_menu_label_name)
    TextView label;

    public MenuLabelHolder(View view, final BaseListener listener) {
        super(view);

        ButterKnife.bind(this, view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.update(position);
            }
        });
    }

    public void bind(int position, LabelPrimeData data) {
        this.position = position;
        label.setText(data.getLabelName());
    }
}
