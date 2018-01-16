package pe.ironbit.android.capstone.screen.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.model.LabelBook.LabelBookMapper;
import pe.ironbit.android.capstone.model.LabelPrime.LabelPrimeData;
import pe.ironbit.android.capstone.model.LabelPrime.LabelPrimeMapper;
import pe.ironbit.android.capstone.screen.dialog.DeleteLabelDialog;
import pe.ironbit.android.capstone.storage.contract.LabelPrimeContract;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;
import pe.ironbit.android.capstone.storage.loader.LabelPrimeLoader;
import pe.ironbit.android.capstone.view.managerlabel.ManagerLabelAdapter;
import pe.ironbit.android.capstone.view.managerlabel.ManagerLabelListener;

public class ManagerLabelFragment extends Fragment {
    private ManagerLabelAdapter adapter;

    private Unbinder unbinder;

    @BindView(R.id.fragment_manager_label_recyclerview_labels)
    RecyclerView recyclerView;

    public ManagerLabelFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager_label, container, false);

        unbinder = ButterKnife.bind(this, view);

        loadScreen();
        loadData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    private void onAddLabelAction() {
    }

    private void onEditLabelAction(Integer position) {
    }

    private void onEraseLabelAction(final Integer index) {
        DialogFragment dialog = DeleteLabelDialog.newInstance(index);
        dialog.show(getFragmentManager(), DeleteLabelDialog.class.getSimpleName());
    }

    public void doOnEraseLabelAcceptAction(final Integer index) {
        LabelPrimeData data = adapter.eraseItem(index);
        LabelPrimeMapper.delete(getActivity().getContentResolver(), data.getLabelId());
        LabelBookMapper.deleteByLabelIdentifier(getActivity().getContentResolver(), data.getLabelId());
    }

    private void update(List list) {
        adapter.update(list);
    }

    private void loadData() {
        LabelPrimeLoader loader = new LabelPrimeLoader(getContext());
        loader.setListener(new OnStorageListener() {
            @Override
            public void onEvent(List list) {
                update(list);
            }
        });
        getLoaderManager().initLoader(LabelPrimeContract.LOADER_IDENTIFIER, null, loader);
    }

    private void loadScreen() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        adapter = new ManagerLabelAdapter(new ManagerLabelListener() {
            @Override
            public void update(Integer position, Action action) {
                if (action == Action.Edit) {
                    onEditLabelAction(position);
                } else {
                    onEraseLabelAction(position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
