package pe.ironbit.android.capstone.screen.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.firebase.storage.StorageService;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeFactory;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeParcelable;
import pe.ironbit.android.capstone.screen.activity.LibraryActivity;
import pe.ironbit.android.capstone.tools.index.Container;
import pe.ironbit.android.capstone.util.Collection;
import pe.ironbit.android.capstone.util.DeviceMetaData;
import pe.ironbit.android.capstone.view.bookmenu.BookMenuAdapter;
import pe.ironbit.android.capstone.view.bookmenu.BookMenuListener;

public class BookSearchFragment extends BaseFragment {
    private static final int ONE_SECOND = 1000;

    private static final float ALPHA_DEFAULT = 1.0f;

    private static final int DOWNLOAD_ACTIVE = 1;

    private static final int DOWNLOAD_INACTIVE = 0;

    private static final String BOOK_NUMBER = "BOOK_NUMBER";

    private static final String BOOK_SEARCH = "BOOK_SEARCH";

    private static final String CURRENT_BOOK_LIST = "CURRENT_BOOK_LIST";

    private static final String COMPLETE_BOOK_LIST = "COMPLETE_BOOK_LIST";

    private static final int PHONE_PORTRAIT_MATRIX_SIZE = 2;

    private static final int PHONE_LANDSCAPE_MATRIX_SIZE = 3;

    private static final int TABLET_PORTRAIT_MATRIX_SIZE = 3;

    private static final int TABLET_LANDSCAPE_MATRIX_SIZE = 4;

    private Unbinder unbinder;

    private int bookNumber;

    private String bookSearch;

    private List<BookPrimeData> currentBookList;

    private List<BookPrimeData> completeBookList;

    private BookMenuAdapter adapter;

    private Container container;

    @BindView(R.id.fragment_book_search_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.fragment_book_search_progressbar)
    ProgressBar progressBarView;

    public static BookSearchFragment newInstance(List<BookPrimeData> list) {
        Bundle bundle = new Bundle();
        storeData(bundle, COMPLETE_BOOK_LIST, list);

        BookSearchFragment fragment = new BookSearchFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    public BookSearchFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            completeBookList = loadData(bundle, COMPLETE_BOOK_LIST);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_search, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        bundle.putInt(BOOK_NUMBER, bookNumber);
        bundle.putString(BOOK_SEARCH, bookSearch);
        storeData(bundle, CURRENT_BOOK_LIST, currentBookList);
        storeData(bundle, COMPLETE_BOOK_LIST, completeBookList);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);

        if (bundle != null) {
            bookNumber = bundle.getInt(BOOK_NUMBER);
            bookSearch = bundle.getString(BOOK_SEARCH);
            currentBookList = loadData(bundle, CURRENT_BOOK_LIST);
            completeBookList = loadData(bundle, COMPLETE_BOOK_LIST);
        } else {
            bookNumber = 0;
            bookSearch = "";
            currentBookList = new ArrayList<>();
        }

        createInvertedIndex();
        loadBookDataIntoScreen(getView());
        updateView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_book_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_book_search_enter) {
            searchBookAction();
            return true;
        }
        if (item.getItemId() == R.id.menu_book_search_cancel) {
            searchBookClear();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doOnShowFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void doOnHideFragment() {
        setHasOptionsMenu(false);

        InputMethodManager manager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean doOnBackPressed() {
        if (!isHidden()) {
            clearQuery();
            currentBookList.clear();
            updateView();
            return true;
        }
        return super.doOnBackPressed();
    }

    private void searchBookClear() {
        clearQuery();
    }

    private void searchBookAction() {
        currentBookList.clear();
        updateView();

        progressBarView.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBarView.setVisibility(View.GONE);

                Set<Integer> outcome = container.verify(getQuery());
                if (outcome.isEmpty()) {
                    String message = getString(R.string.menu_book_search_message);
                    Snackbar.make(((LibraryActivity)getActivity()).getPrimeView(), message, Snackbar.LENGTH_LONG).show();
                    return;
                }

                currentBookList.clear();
                for (Integer id : outcome) {
                    for (BookPrimeData data : completeBookList) {
                        if (data.getBookId() == id) {
                            currentBookList.add(data);
                            break;
                        }
                    }
                }

                updateView();
            }
        }, ONE_SECOND);
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
        BookMenuListener listener = new BookMenuListener() {
            @Override
            public void update(int position, ClickType type) {
                if (type == ClickType.Short) {
                    //doOnShortClickInBookItem(position);
                }
            }
        };

        adapter = new BookMenuAdapter(listener, service);
        recyclerView.setAdapter(adapter);
    }

    private void updateView() {
        List<Float> alphaList = Collection.initialize(currentBookList.size(), ALPHA_DEFAULT);
        List<Integer> downloadList = Collection.initialize(currentBookList.size(), DOWNLOAD_INACTIVE);
        adapter.updateList(currentBookList, alphaList, downloadList);
    }

    private void createInvertedIndex() {
        container = new Container();
        for (BookPrimeData book : completeBookList) {
            container.set(book.getName(), 0, book.getBookId());
            container.set(book.getAuthor(), 1, book.getBookId());
        }
    }

    private String getQuery() {
        return ((LibraryActivity)getActivity()).getQuery();
    }

    private void clearQuery() {
        ((LibraryActivity)getActivity()).clearQuery();
    }

    private static void storeData(Bundle bundle, String key, List<BookPrimeData> list) {
        bundle.putParcelableArrayList(key, (ArrayList)BookPrimeFactory.createBookPrimeParcelableList(list));
    }

    private static List<BookPrimeData> loadData(Bundle bundle, String key) {
        return BookPrimeFactory.createBookPrimeDataList(bundle.<BookPrimeParcelable>getParcelableArrayList(key));
    }

    private boolean isDevicePhone() {
        return !DeviceMetaData.isDeviceTablet(getContext());
    }

    private boolean isOrientationPortrait() {
        return DeviceMetaData.isOrientationPortrait(getContext());
    }
}
