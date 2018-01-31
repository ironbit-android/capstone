package pe.ironbit.android.capstone.screen.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.model.LabelBook.LabelBookMapper;
import pe.ironbit.android.capstone.model.LabelPrime.LabelPrimeData;
import pe.ironbit.android.capstone.model.LabelPrime.LabelPrimeFactory;
import pe.ironbit.android.capstone.model.LabelPrime.LabelPrimeMapper;
import pe.ironbit.android.capstone.screen.activity.LibraryActivity;
import pe.ironbit.android.capstone.screen.dialog.CreateLabelDialog;
import pe.ironbit.android.capstone.screen.dialog.DeleteLabelDialog;
import pe.ironbit.android.capstone.screen.dialog.EditLabelDialog;
import pe.ironbit.android.capstone.storage.contract.LabelPrimeContract;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;
import pe.ironbit.android.capstone.storage.loader.LabelPrimeLoader;
import pe.ironbit.android.capstone.util.DeviceMetaData;
import pe.ironbit.android.capstone.view.managerlabel.ManagerLabelAdapter;
import pe.ironbit.android.capstone.view.managerlabel.ManagerLabelListener;

public class ManagerLabelFragment extends BaseFragment {
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
        setHasOptionsMenu(true);

        loadScreen();
        loadData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.manager_label_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.manager_label_menu_add_label) {
            performCreateLabelDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void performCreateLabelDialog() {
        int newLabelId = 1;
        for (LabelPrimeData data : adapter.getList()) {
            if (newLabelId <= data.getLabelId()) {
                newLabelId = data.getLabelId() + 1;
            }
        }

        DialogFragment dialog = CreateLabelDialog.newInstance(newLabelId);
        dialog.show(getFragmentManager(), CreateLabelDialog.class.getSimpleName());
    }

    @Override
    public boolean doOnBackPressed() {
        if (!isHidden()) {
            return true;
        }
        return super.doOnBackPressed();
    }

    private void onEditLabelAction(final Integer index) {
        LabelPrimeData data = adapter.getItem(index);
        DialogFragment dialog = EditLabelDialog.newInstance(index, data.getLabelName());
        dialog.show(getFragmentManager(), EditLabelDialog.class.getSimpleName());
    }

    public void doOnEditLabelCancelAction() {
        View view = ((LibraryActivity)getActivity()).getPrimeView();
        Snackbar.make(view, getString(R.string.manager_label_edit_cancelled_message), Snackbar.LENGTH_LONG).show();
    }

    public void doOnEditLabelAcceptAction(final int index, final String labelName) {
        boolean valid = false;
        LabelPrimeData data = adapter.getItem(index);
        if ((labelName != null) && (!labelName.isEmpty())) {
            if (!TextUtils.equals(labelName, data.getLabelName())) {
                valid = true;
            }
        }

        String oldLabelName = data.getLabelName();
        View view = ((LibraryActivity)getActivity()).getPrimeView();
        if (valid) {
            LabelPrimeData newData = LabelPrimeFactory.create(data.getLabelId(), labelName);
            adapter.update(index, newData);
            LabelPrimeMapper.update(getActivity().getContentResolver(), newData);
            verifyCurrentTitle(labelName, oldLabelName);
            Snackbar.make(view, getString(R.string.manager_label_edit_label_accept_message), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(view, getString(R.string.manager_label_edit_label_problem_message), Snackbar.LENGTH_LONG).show();
        }
    }

    private void verifyCurrentTitle(String newLabelName, String oldLabelName) {
        ((LibraryActivity)getActivity()).changeTitle(newLabelName, oldLabelName);
    }

    private void onEraseLabelAction(final Integer index) {
        DialogFragment dialog = DeleteLabelDialog.newInstance(index);
        dialog.show(getFragmentManager(), DeleteLabelDialog.class.getSimpleName());
    }

    public void doOnEraseLabelCancelAction() {
        View view = ((LibraryActivity)getActivity()).getPrimeView();
        Snackbar.make(view, getString(R.string.manager_label_erase_cancel_message), Snackbar.LENGTH_LONG).show();
    }

    public void doOnEraseLabelAcceptAction(final Integer index) {
        LabelPrimeData data = adapter.eraseItem(index);
        LabelPrimeMapper.delete(getActivity().getContentResolver(), data.getLabelId());
        LabelBookMapper.deleteByLabelIdentifier(getActivity().getContentResolver(), data.getLabelId());

        View view = ((LibraryActivity)getActivity()).getPrimeView();
        Snackbar.make(view, getString(R.string.manager_label_erase_accept_message), Snackbar.LENGTH_LONG).show();
    }

    public void doOnCreateLabelCancelAction() {
        View view = ((LibraryActivity)getActivity()).getPrimeView();
        Snackbar.make(view, getString(R.string.manager_label_create_cancelled_message), Snackbar.LENGTH_LONG).show();
    }

    public void doOnCreateLabelAcceptAction(final Integer labelId, final String labelName) {
        boolean valid = true;
        if ((labelName != null) && (!labelName.isEmpty())) {
            for (LabelPrimeData data : adapter.getList()) {
                if (TextUtils.equals(data.getLabelName(), labelName)) {
                    valid = false;
                    break;
                }
            }
        } else {
            valid = false;
        }

        View view = ((LibraryActivity)getActivity()).getPrimeView();
        if (valid) {
            LabelPrimeMapper.insert(getActivity().getContentResolver(), LabelPrimeFactory.create(labelId, labelName));
            Snackbar.make(view, getString(R.string.manager_label_create_label_accept_message), Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(view, getString(R.string.manager_label_create_label_problem_message), Snackbar.LENGTH_LONG).show();
        }
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

    private boolean isDevicePhone() {
        return !DeviceMetaData.isDeviceTablet(getContext());
    }
}
