package pe.ironbit.android.capstone.screen.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.event.base.BaseListener;
import pe.ironbit.android.capstone.model.BookTable.BookTableData;
import pe.ironbit.android.capstone.model.BookTable.BookTableFactory;
import pe.ironbit.android.capstone.model.BookTable.BookTableParcelable;
import pe.ironbit.android.capstone.screen.activity.ReaderActivity;
import pe.ironbit.android.capstone.view.tablecontents.TableContentsAdapter;

public class TableContentFragment extends Fragment {
    public static final String TABLE_CONTENT_KEY = "TABLE_CONTENT_KEY";

    private Unbinder unbinder;

    private TableContentsAdapter adapter;

    private List<BookTableData> bookTableDataList;

    @BindView(R.id.fragment_table_content_recyclerview)
    RecyclerView recyclerView;

    public static TableContentFragment newInstance(List<BookTableData> data) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(TABLE_CONTENT_KEY, (ArrayList)BookTableFactory.createBookTableParcelableList(data));

        TableContentFragment fragment = new TableContentFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public TableContentFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            bookTableDataList = BookTableFactory.createBookTableDataList(bundle.<BookTableParcelable>getParcelableArrayList(TABLE_CONTENT_KEY));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_table_contents, container, false);

        unbinder = ButterKnife.bind(this, view);

        return  view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadScreen();
    }

    private void loadScreen() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        adapter = new TableContentsAdapter(new BaseListener<Integer>() {
            @Override
            public void update(Integer chapter) {
                changeChapter(chapter);
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.updateList(bookTableDataList);
    }

    private void changeChapter(int chapter) {
        ((ReaderActivity)getActivity()).changeChapter(chapter);
    }
}
