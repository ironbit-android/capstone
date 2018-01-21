package pe.ironbit.android.capstone.screen.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.firebase.storage.StorageService;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeFactory;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeParcelable;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeStatus;
import pe.ironbit.android.capstone.model.LabelBook.LabelBookData;
import pe.ironbit.android.capstone.screen.activity.LibraryActivity;
import pe.ironbit.android.capstone.screen.dialog.AddBookIntoLabelDialog;
import pe.ironbit.android.capstone.util.Collection;
import pe.ironbit.android.capstone.util.DeviceMetaData;
import pe.ironbit.android.capstone.view.bookmenu.BookMenuAdapter;
import pe.ironbit.android.capstone.view.bookmenu.BookMenuListener;

public class BookMenuFragment extends Fragment {
    private static final float ALPHA_DEFAULT = 1.0f;

    private static final String CURRENT_BOOK_PRIME_LIST = "CURRENT_BOOK_PRIME_LIST";

    private static final String COMPLETE_BOOK_PRIME_LIST = "COMPLETE_BOOK_PRIME_LIST";

    private static final String BOOK_SELECTION_LIST = "BOOK_SELECTION_LIST";

    private static final String ALPHA_SELECTION_LIST = "ALPHA_SELECTION_LIST";

    private static final String IS_SELECT_OPTION_ACTIVE = "IS_SELECT_OPTION_ACTIVE";

    private static final int PHONE_PORTRAIT_MATRIX_SIZE = 2;

    private static final int PHONE_LANDSCAPE_MATRIX_SIZE = 3;

    private static final int TABLET_PORTRAIT_MATRIX_SIZE = 3;

    private static final int TABLET_LANDSCAPE_MATRIX_SIZE = 4;

    private Unbinder unbinder;

    private List<BookPrimeData> currentBookPrimeList;

    private List<BookPrimeData> completeBookPrimeList;

    private List<BookPrimeData> bookSelectionList;

    private List<Float> alphaList;

    private float alphaLongClick;

    private boolean isSelectOptionActive = false;

    private BookMenuAdapter adapter;

    @BindView(R.id.fragment_book_menu_recyclerview)
    RecyclerView recyclerView;

    public static BookMenuFragment newInstance(List<BookPrimeData> completeBookPrimeList) {
        Bundle bundle = new Bundle();
        storeData(bundle, COMPLETE_BOOK_PRIME_LIST, completeBookPrimeList);

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
            completeBookPrimeList = loadData(bundle, COMPLETE_BOOK_PRIME_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_menu, container, false);

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
        super.onSaveInstanceState(bundle);

        storeData(bundle, BOOK_SELECTION_LIST, bookSelectionList);
        storeData(bundle, CURRENT_BOOK_PRIME_LIST, currentBookPrimeList);
        storeData(bundle, COMPLETE_BOOK_PRIME_LIST, completeBookPrimeList);
        bundle.putBoolean(IS_SELECT_OPTION_ACTIVE, isSelectOptionActive);
        bundle.putFloatArray(ALPHA_SELECTION_LIST, Collection.convertFloatCollection(alphaList));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);

        if (bundle != null) {
            bookSelectionList = loadData(bundle, BOOK_SELECTION_LIST);
            currentBookPrimeList = loadData(bundle, CURRENT_BOOK_PRIME_LIST);
            completeBookPrimeList = loadData(bundle, COMPLETE_BOOK_PRIME_LIST);
            isSelectOptionActive = bundle.getBoolean(IS_SELECT_OPTION_ACTIVE);
            alphaList = Collection.convertFloatCollection(bundle.getFloatArray(ALPHA_SELECTION_LIST));
        } else {
            isSelectOptionActive = false;
            if (bookSelectionList == null) {
                bookSelectionList = new ArrayList<>();
            }
            if (currentBookPrimeList == null) {
                currentBookPrimeList = completeBookPrimeList;
            }
            alphaList = Collection.initialize(currentBookPrimeList.size(), ALPHA_DEFAULT);
        }

        {
            if (isSelectOptionActive) {
                if (isDevicePhone()) {
                    updateActionBar(true);
                }
            }
        }
        {
            TypedValue valueType = new TypedValue();
            getResources().getValue(R.dimen.general_long_click_alpha, valueType, true);
            alphaLongClick = valueType.getFloat();
        }

        loadBookDataIntoScreen(getView());
        setHasOptionsMenu(isSelectOptionActive);

        updateView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_selection_book, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_selection_book_erase_book) {
            //performEraseBookDialog();
            return true;
        }
        if (item.getItemId() == R.id.menu_selection_book_group_book) {
            performAddBookIntoLabelDialog();
            return true;
        }
        if (item.getItemId() == R.id.menu_selection_book_group_cancel) {
            performEndSelection();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void performAddBookIntoLabelDialog() {
        AddBookIntoLabelDialog dialog = AddBookIntoLabelDialog.newInstance(bookSelectionList);
        dialog.show(getFragmentManager(), AddBookIntoLabelDialog.class.getSimpleName());
    }

    public boolean performEndSelection() {
        boolean outcome = isSelectOptionActive;
        if (outcome) {
            performOnFinishedAddBookIntoLabelDialog();
        }
        return outcome;
    }

    public void doOnCloseSelectionMode() {
        resetAlphaList();
        if (isSelectOptionActive) {
            resetSelectionBar();
            bookSelectionList.clear();
            isSelectOptionActive = false;
            setHasOptionsMenu(isSelectOptionActive);
            updateActionBar(false);
        }
        setActivityMode(LibraryActivity.ActivityMode.BookMenu);
    }

    public void performOnFinishedAddBookIntoLabelDialog() {
        doOnCloseSelectionMode();
        updatePreviousTitle();

        updateView();
    }

    public void updateModeGlobal() {
        currentBookPrimeList = completeBookPrimeList;
        doOnCloseSelectionMode();
        updateView();
    }

    public void updateModelLocal() {
        currentBookPrimeList = new ArrayList<>();
        for (BookPrimeData book : completeBookPrimeList) {
            if (book.getStatus() == BookPrimeStatus.Local) {
                currentBookPrimeList.add(book);
            }
        }
        doOnCloseSelectionMode();
        updateView();
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
        doOnCloseSelectionMode();
        updateView();
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
                    doOnShortClickInBookItem(position);
                }
                if (type == ClickType.Long) {
                    doOnLongClickInBookItem(position);
                }
            }
        };

        adapter = new BookMenuAdapter(listener, service);
        recyclerView.setAdapter(adapter);
    }

    private void doOnShortClickInBookItem(int position) {
        if (isSelectOptionActive) {
            performOnFinishedAddBookIntoLabelDialog();
        }
    }

    private void doOnLongClickInBookItem(int position) {
        BookPrimeData book = currentBookPrimeList.get(position);
        for (BookPrimeData data : bookSelectionList) {
            if (data.getBookId() == book.getBookId()) {
                return;
            }
        }

        bookSelectionList.add(book);
        alphaList.set(position, alphaLongClick);
        adapter.setAlpha(position, alphaLongClick);
        updateSelectionBar(bookSelectionList.size());

        if (!isSelectOptionActive) {
            isSelectOptionActive = true;
            setHasOptionsMenu(isSelectOptionActive);
            updateTitle(getString(R.string.menu_selection_book_title));
            updateActionBar(true);
            setActivityMode(LibraryActivity.ActivityMode.BookSelection);
        }
    }

    public void updateView() {
        adapter.update(currentBookPrimeList, alphaList);
    }

    private void setActivityMode(LibraryActivity.ActivityMode activityMode) {
        ((LibraryActivity)getActivity()).setActivityMode(activityMode);
    }

    private void resetAlphaList() {
        alphaList = Collection.assign(alphaList, currentBookPrimeList.size(), ALPHA_DEFAULT);
    }

    private void updateActionBar(boolean value) {
        ((LibraryActivity) getActivity()).configureActionBar(value);
    }

    private void updatePreviousTitle() {
        ((LibraryActivity) getActivity()).setPreviousTitle();
    }

    private void updateTitle(String value) {
        ((LibraryActivity) getActivity()).setTitle(value);
    }

    private void resetSelectionBar() {
        ((LibraryActivity) getActivity()).resetSelectionBar();
    }

    private void updateSelectionBar(int value) {
        ((LibraryActivity) getActivity()).setSelectionBar(value);
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
