package pe.ironbit.android.capstone.screen.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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
import pe.ironbit.android.capstone.firebase.storage.StorageService;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeParcelable;
import pe.ironbit.android.capstone.util.DeviceMetaData;
import pe.ironbit.android.capstone.view.bookmenu.BookMenuAdapter;

public class BookMenuFragment extends Fragment {
    public static final String BOOK_MENU_LIST_KEY = "BOOK_MENU_LIST_KEY";

    public static final String RECYCLER_VIEW_PARCELABLE_KEY = "RECYCLER_VIEW_PARCELABLE_KEY";

    private static final int PHONE_PORTRAIT_MATRIX_SIZE = 2;

    private static final int PHONE_LANDSCAPE_MATRIX_SIZE = 3;

    private static final int TABLET_PORTRAIT_MATRIX_SIZE = 3;

    private static final int TABLET_LANDSCAPE_MATRIX_SIZE = 4;

    private List<BookPrimeParcelable> books;

    private BookMenuAdapter adapter;

    private Unbinder unbinder;

    @BindView(R.id.fragment_book_menu_recyclerview)
    RecyclerView recyclerView;

    public static BookMenuFragment newInstance(ArrayList<BookPrimeParcelable> list) {
        Bundle argument = new Bundle();
        argument.putParcelableArrayList(BOOK_MENU_LIST_KEY, list);

        BookMenuFragment fragment = new BookMenuFragment();
        fragment.setArguments(argument);

        return fragment;
    }

    public BookMenuFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            books = bundle.getParcelableArrayList(BOOK_MENU_LIST_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_menu, container, false);

        unbinder = ButterKnife.bind(this, view);

        loadBookDataIntoFragmentScreen(view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        Parcelable parcelable = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(RECYCLER_VIEW_PARCELABLE_KEY, parcelable);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            Parcelable parcelable = savedInstanceState.getParcelable(RECYCLER_VIEW_PARCELABLE_KEY);
            recyclerView.getLayoutManager().onRestoreInstanceState(parcelable);
        }
    }

    private void loadBookDataIntoFragmentScreen(View view) {
        int size = 0;
        if (isDevicePhone()) {
            if (isOrientationPortrait()) {
                size = PHONE_PORTRAIT_MATRIX_SIZE;
            } else {
                size = PHONE_LANDSCAPE_MATRIX_SIZE;
            }
        } else {
            if (isOrientationPortrait()) {
                size = TABLET_PORTRAIT_MATRIX_SIZE;
            } else {
                size = TABLET_LANDSCAPE_MATRIX_SIZE;
            }
        }

        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), size, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        StorageService service = new StorageService(getContext());
        BaseListener listener = new BaseListener() {
            @Override
            public void update(Object o) {
            }
        };

        adapter = new BookMenuAdapter(listener, service);
        recyclerView.setAdapter(adapter);

        adapter.setList(createBookPrimeDataList(books));
    }

    private List<BookPrimeData> createBookPrimeDataList(List<BookPrimeParcelable> inputList) {
        List<BookPrimeData> outputList = new ArrayList<>();

        for (BookPrimeParcelable parcelable : inputList) {
            outputList.add(parcelable.getBookPrime());
        }

        return outputList;
    }

    private boolean isDevicePhone() {
        return !DeviceMetaData.isDeviceTablet(getContext());
    }

    private boolean isOrientationPortrait() {
        return DeviceMetaData.isOrientationPortrait(getContext());
    }
}
