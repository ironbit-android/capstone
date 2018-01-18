package pe.ironbit.android.capstone.screen.fragment;

import android.os.Bundle;
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
import pe.ironbit.android.capstone.event.base.BaseListener;
import pe.ironbit.android.capstone.screen.activity.LibraryActivity;
import pe.ironbit.android.capstone.storage.contract.LabelPrimeContract;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;
import pe.ironbit.android.capstone.storage.loader.LabelPrimeLoader;
import pe.ironbit.android.capstone.view.menulabel.MenuLabelAdapter;

public class MainMenuFragment extends Fragment {
    private Unbinder unbinder;

    private MenuLabelAdapter adapter;

    @BindView(R.id.fragment_main_menu_label_list)
    RecyclerView labelList;

    public MainMenuFragment() {
    }

    public static MainMenuFragment newInstance() {
        MainMenuFragment fragment = new MainMenuFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

        unbinder = ButterKnife.bind(this, view);

        loadData();
        loadScreen();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
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
        labelList.setLayoutManager(manager);
        adapter = new MenuLabelAdapter(new BaseListener<Integer>() {
            @Override
            public void update(Integer index) {
                ((LibraryActivity)getActivity()).updateBookMenuScreen(adapter.get(index));
            }
        });
        labelList.setAdapter(adapter);
    }
}
