package pe.ironbit.android.capstone.view.tablecontents;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.event.base.BaseListener;

public class TableContentsHolder extends RecyclerView.ViewHolder {
    private Integer chapter;

    @BindView(R.id.recyclerview_table_contents_item)
    TextView label;

    public TableContentsHolder(View view, final BaseListener listener) {
        super(view);

        ButterKnife.bind(this, view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.update(chapter);
            }
        });
    }

    public void bind(int chapter, String value) {
        this.chapter = chapter;
        label.setText(value);
    }
}
