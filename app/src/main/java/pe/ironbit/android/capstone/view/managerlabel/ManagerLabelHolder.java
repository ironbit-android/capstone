package pe.ironbit.android.capstone.view.managerlabel;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.model.LabelPrime.LabelPrimeData;

public class ManagerLabelHolder extends RecyclerView.ViewHolder {
    private Integer position;

    @BindView(R.id.recyclerview_manager_label_name)
    TextView labelName;

    @BindView(R.id.recyclerview_manager_label_edit)
    View labelEdit;

    @BindView(R.id.recyclerview_manager_label_erase)
    View labelErase;

    public ManagerLabelHolder(View view, final ManagerLabelListener listener) {
        super(view);

        ButterKnife.bind(this, view);

        labelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.update(position, ManagerLabelListener.Action.Edit);
            }
        });

        labelErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.update(position, ManagerLabelListener.Action.Erase);
            }
        });
    }

    public void bind(int position, LabelPrimeData data) {
        this.position = position;
        labelName.setText(data.getLabelName());
    }
}
