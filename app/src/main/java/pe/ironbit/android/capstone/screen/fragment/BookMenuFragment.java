package pe.ironbit.android.capstone.screen.fragment;

import android.os.Bundle;
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
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeFactory;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeParcelable;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeStatus;
import pe.ironbit.android.capstone.model.LabelBook.LabelBookData;
import pe.ironbit.android.capstone.util.DeviceMetaData;
import pe.ironbit.android.capstone.view.bookmenu.BookMenuAdapter;

public class BookMenuFragment extends Fragment {
    private static final String CURRENT_BOOK_PRIME_LIST = "CURRENT_BOOK_PRIME_LIST";

    private static final String COMPLETE_BOOK_PRIME_LIST = "COMPLETE_BOOK_PRIME_LIST";

    private static final int PHONE_PORTRAIT_MATRIX_SIZE = 2;

    private static final int PHONE_LANDSCAPE_MATRIX_SIZE = 3;

    private static final int TABLET_PORTRAIT_MATRIX_SIZE = 3;

    private static final int TABLET_LANDSCAPE_MATRIX_SIZE = 4;

    private Unbinder unbinder;

    private List<BookPrimeData> currentBookPrimeList;

    private List<BookPrimeData> completeBookPrimeList;

    private BookMenuAdapter adapter;

    @BindView(R.id.fragment_book_menu_recyclerview)
    RecyclerView recyclerView;

    public static BookMenuFragment newInstance(List<BookPrimeData> completeBookPrimeList) {
        Bundle bundle = new Bundle();
        storeData(bundle, completeBookPrimeList);

        BookMenuFragment fragment = new BookMenuFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public BookMenuFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            loadData(bundle);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_menu, container, false);

        unbinder = ButterKnife.bind(this, view);

        loadBookDataIntoScreen(view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);

        storeData(bundle, currentBookPrimeList, completeBookPrimeList);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);

        if (bundle != null) {
            loadData(bundle);
        }

        update();
    }

    public void updateModeGlobal() {
        currentBookPrimeList = completeBookPrimeList;
        update();
    }

    public void updateModelLocal() {
        currentBookPrimeList = new ArrayList<>();
        for (BookPrimeData book : completeBookPrimeList) {
            if (book.getStatus() == BookPrimeStatus.Local) {
                currentBookPrimeList.add(book);
            }
        }
        update();
    }

    public void updateModelLabel(List<LabelBookData> labelBookList) {
        currentBookPrimeList = new ArrayList<>();
        for (LabelBookData labelBook : labelBookList) {
            for (BookPrimeData bookPrime : completeBookPrimeList) {
                if (bookPrime.getBookId() == labelBook.getBookId()) {
                    currentBookPrimeList.add(bookPrime);
                }
            }
        }
        update();
    }

    private void loadBookDataIntoScreen(View view) {
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

        RecyclerView.LayoutManager manager = new GridLayoutManager(view.getContext(), size, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);

        StorageService service = new StorageService(view.getContext());
        BaseListener listener = new BaseListener() {
            @Override
            public void update(Object object) {
            }
        };

        adapter = new BookMenuAdapter(listener, service);
        recyclerView.setAdapter(adapter);
    }

    private void update() {
        adapter.setList(currentBookPrimeList);
    }

    private static void storeData(Bundle bundle, List<BookPrimeData> completeBookPrimeList) {
        bundle.putParcelableArrayList(COMPLETE_BOOK_PRIME_LIST, (ArrayList)BookPrimeFactory.createBookPrimeParcelableList(completeBookPrimeList));
    }

    private static void storeData(Bundle bundle, List<BookPrimeData> currentBookPrimeList, List<BookPrimeData> completeBookPrimeList) {
        bundle.putParcelableArrayList(COMPLETE_BOOK_PRIME_LIST, (ArrayList)BookPrimeFactory.createBookPrimeParcelableList(completeBookPrimeList));
        bundle.putParcelableArrayList(CURRENT_BOOK_PRIME_LIST, (ArrayList)BookPrimeFactory.createBookPrimeParcelableList(currentBookPrimeList));
    }

    private void loadData(Bundle bundle) {
        currentBookPrimeList = BookPrimeFactory.createBookPrimeDataList(bundle.<BookPrimeParcelable>getParcelableArrayList(CURRENT_BOOK_PRIME_LIST));
        completeBookPrimeList = BookPrimeFactory.createBookPrimeDataList(bundle.<BookPrimeParcelable>getParcelableArrayList(COMPLETE_BOOK_PRIME_LIST));

        if (currentBookPrimeList == null) {
            currentBookPrimeList = completeBookPrimeList;
        }
    }

    private boolean isDevicePhone() {
        return !DeviceMetaData.isDeviceTablet(getContext());
    }

    private boolean isOrientationPortrait() {
        return DeviceMetaData.isOrientationPortrait(getContext());
    }
}
