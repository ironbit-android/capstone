package pe.ironbit.android.capstone.screen.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import pe.ironbit.android.capstone.generic.Action;
import pe.ironbit.android.capstone.model.BookContent.BookContentData;
import pe.ironbit.android.capstone.model.BookContent.BookContentMapper;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeFactory;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeMapper;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeParcelable;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeStatus;
import pe.ironbit.android.capstone.model.BookTable.BookTableData;
import pe.ironbit.android.capstone.model.BookTable.BookTableMapper;
import pe.ironbit.android.capstone.model.LabelBook.LabelBookData;
import pe.ironbit.android.capstone.screen.activity.LibraryActivity;
import pe.ironbit.android.capstone.screen.activity.ReaderActivity;
import pe.ironbit.android.capstone.screen.dialog.AddBookIntoLabelDialog;
import pe.ironbit.android.capstone.screen.dialog.DeleteBookDialog;
import pe.ironbit.android.capstone.storage.contract.BookContentContract;
import pe.ironbit.android.capstone.storage.contract.BookTableContract;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;
import pe.ironbit.android.capstone.storage.loader.BookContentLoader;
import pe.ironbit.android.capstone.storage.loader.BookTableLoader;
import pe.ironbit.android.capstone.tools.download.BookDownloader;
import pe.ironbit.android.capstone.util.Collection;
import pe.ironbit.android.capstone.util.DeviceMetaData;
import pe.ironbit.android.capstone.view.bookmenu.BookMenuAdapter;
import pe.ironbit.android.capstone.view.bookmenu.BookMenuListener;

public class BookMenuFragment extends BaseFragment {
    private static final float ALPHA_DEFAULT = 1.0f;

    private static final int DOWNLOAD_ACTIVE = 1;

    private static final int DOWNLOAD_INACTIVE = 0;

    private static final String CURRENT_BOOK_PRIME_LIST = "CURRENT_BOOK_PRIME_LIST";

    private static final String COMPLETE_BOOK_PRIME_LIST = "COMPLETE_BOOK_PRIME_LIST";

    private static final String BOOK_SELECTION_LIST = "BOOK_SELECTION_LIST";

    private static final String ALPHA_SELECTION_LIST = "ALPHA_SELECTION_LIST";

    private static final String IS_SELECT_OPTION_ACTIVE = "IS_SELECT_OPTION_ACTIVE";

    private static final String DOWNLOAD_LIST = "DOWNLOAD_LIST";

    private static final int PHONE_PORTRAIT_MATRIX_SIZE = 2;

    private static final int PHONE_LANDSCAPE_MATRIX_SIZE = 3;

    private static final int TABLET_PORTRAIT_MATRIX_SIZE = 3;

    private static final int TABLET_LANDSCAPE_MATRIX_SIZE = 4;

    private Unbinder unbinder;

    private List<BookPrimeData> currentBookPrimeList;

    private List<BookPrimeData> completeBookPrimeList;

    private List<BookPrimeData> bookSelectionList;

    private List<Float> alphaList;

    private List<Integer> downloadList;

    private float alphaLongClick;

    private boolean isSelectOptionActive = false;

    private BookMenuAdapter adapter;

    private class BookDownloaderHelper {
        public BookDownloaderHelper() {
            position = 0;
            cancel = false;
            downloader = null;
        }

        public int position;
        public boolean cancel;
        public BookDownloader downloader;
    }

    private List<BookDownloaderHelper> bookDownloaderHelperList;

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
        bundle.putIntegerArrayList(DOWNLOAD_LIST, (ArrayList)downloadList);
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
            downloadList = bundle.getIntegerArrayList(DOWNLOAD_LIST);
        } else {
            isSelectOptionActive = false;
            if (bookSelectionList == null) {
                bookSelectionList = new ArrayList<>();
            }
            if (currentBookPrimeList == null) {
                currentBookPrimeList = completeBookPrimeList;
            }
            bookDownloaderHelperList = new ArrayList<>();
            alphaList = Collection.initialize(currentBookPrimeList.size(), ALPHA_DEFAULT);
            downloadList = Collection.initialize(currentBookPrimeList.size(), DOWNLOAD_INACTIVE);
        }

        {
            if (isSelectOptionActive) {
                updateSelectionBar(bookSelectionList.size());
                setActivityMode(LibraryActivity.ActivityMode.BookSelection);
            }
        }
        {
            TypedValue valueType = new TypedValue();
            getResources().getValue(R.dimen.general_long_click_alpha, valueType, true);
            alphaLongClick = valueType.getFloat();
        }

        loadBookDataIntoScreen(getView());
        setHasOptionsMenu(isSelectOptionActive);

        updateViewList();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_selection_book, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_selection_book_erase_book) {
            performEraseBookDialog();
            return true;
        }
        if (item.getItemId() == R.id.menu_selection_book_group_book) {
            performAddBookIntoLabelDialog();
            return true;
        }
        if (item.getItemId() == R.id.menu_selection_book_group_cancel) {
            performOnFinishAction();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doOnShowFragment() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void doOnHideFragment() {
        doOnCloseSelectionMode();
    }

    @Override
    public boolean doOnBackPressed() {
        if (!isHidden()) {
            return doOnCloseSelectionMode();
        }
        return super.doOnBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRecyclerViewLayout(getView());
    }

    private void performEraseBookDialog() {
        DeleteBookDialog dialog = new DeleteBookDialog();
        dialog.show(getFragmentManager(), DeleteBookDialog.class.getSimpleName());
    }

    private void performAddBookIntoLabelDialog() {
        AddBookIntoLabelDialog dialog = AddBookIntoLabelDialog.newInstance(bookSelectionList);
        dialog.show(getFragmentManager(), AddBookIntoLabelDialog.class.getSimpleName());
    }

    public boolean doOnCloseSelectionMode() {
        boolean outcome = isSelectOptionActive;

        resetAlphaList();
        bookSelectionList.clear();
        isSelectOptionActive = false;
        setHasOptionsMenu(isSelectOptionActive);

        return outcome;
    }

    public void performOnFinishAction() {
        doOnCloseSelectionMode();
        updatePreviousTitle();
        setActivityMode(LibraryActivity.ActivityMode.BookMenu);
    }

    public void updateModeGlobal() {
        currentBookPrimeList = completeBookPrimeList;
        doOnCloseSelectionMode();
        updateViewList();
    }

    public void updateModelLocal() {
        currentBookPrimeList = new ArrayList<>();
        for (BookPrimeData book : completeBookPrimeList) {
            if (book.getStatus() == BookPrimeStatus.Local) {
                currentBookPrimeList.add(book);
            }
        }
        doOnCloseSelectionMode();
        updateViewList();
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
        updateViewList();
    }

    private void loadBookDataIntoScreen(View view) {
        setRecyclerViewLayout(view);

        StorageService service = new StorageService(view.getContext());
        BookMenuListener listener = new BookMenuListener() {
            @Override
            public void update(int position, ClickType type) {
                if (type == ClickType.Short) {
                    doOnShortClickInBookItem(position);
                    return;
                }
                if (type == ClickType.Long) {
                    doOnLongClickInBookItem(position);
                    return;
                }
                if (type == ClickType.Cancel) {
                    doOnCancelDownloadBook(position);
                    return;
                }
            }
        };

        adapter = new BookMenuAdapter(listener, service);
        recyclerView.setAdapter(adapter);
    }

    private void doOnShortClickInBookItem(final int position) {
        if (isSelectOptionActive) {
            performOnFinishAction();
            return;
        }

        if (currentBookPrimeList.get(position).getStatus() == BookPrimeStatus.Global) {
            doOnClickDownloadBook(position);
            return;
        }

        BookPrimeParcelable parcelable = BookPrimeFactory.create(currentBookPrimeList.get(position));
        Intent intent = new Intent(getContext(), ReaderActivity.class);
        intent.putExtra(ReaderActivity.BOOK_PRIME_DATA_KEY, parcelable);
        startActivity(intent);
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
        updateViewList();
        updateSelectionBar(bookSelectionList.size());

        if (!isSelectOptionActive) {
            isSelectOptionActive = true;
            setHasOptionsMenu(isSelectOptionActive);
            setActivityMode(LibraryActivity.ActivityMode.BookSelection);
        }
    }

    private synchronized void doOnClickDownloadBook(int position) {
        for (BookDownloaderHelper helper : bookDownloaderHelperList) {
            if (helper.position == position) {
                return;
            }
        }

        BookDownloaderHelper helper = new BookDownloaderHelper();
        helper.position = position;
        bookDownloaderHelperList.add(helper);

        BookDownloader downloader = new BookDownloader(getActivity(), position);
        helper.downloader = downloader;

        downloader.doOnDownloadFinished(new Action<Integer>() {
            @Override
            public void execute(Integer position) {
                BookPrimeData oldBook = currentBookPrimeList.get(position);
                BookPrimeData newBook = BookPrimeFactory.create(oldBook.getBookId(),
                                                                oldBook.getName(),
                                                                oldBook.getAuthor(),
                                                                oldBook.getImage(),
                                                                oldBook.getFile(),
                                                                BookPrimeStatus.Local);

                currentBookPrimeList.set(position, newBook);
                BookPrimeMapper.update(getActivity().getContentResolver(), newBook);

                doOnFinishDownloadBook(position);

                setSnackBarMessage(R.string.fragment_book_menu_download_book_accepted);
            }
        });

        alphaList.set(position, alphaLongClick);
        downloadList.set(position, DOWNLOAD_ACTIVE);
        updateViewItem(position);

        downloader.process(currentBookPrimeList.get(position));
    }

    private void doOnCancelDownloadBook(int position) {
        for (BookDownloaderHelper helper : bookDownloaderHelperList) {
            if (helper.position == position) {
                if (helper.cancel == false) {
                    helper.cancel = true;
                    helper.downloader.downloadCancelState(true);

                    doOnFinishDownloadBook(position);

                    setSnackBarMessage(R.string.fragment_book_menu_download_book_cancelled);
                }
                break;
            }
        }
    }

    private synchronized void doOnFinishDownloadBook(int position) {
        downloadList.set(position, DOWNLOAD_INACTIVE);
        alphaList.set(position, ALPHA_DEFAULT);
        adapter.updateItem(position, currentBookPrimeList.get(position), alphaList.get(position), downloadList.get(position));
        for (int index = 0; index < bookDownloaderHelperList.size(); ++index) {
            if (bookDownloaderHelperList.get(index).position == position) {
                bookDownloaderHelperList.remove(index);
            }
        }
    }

    public void doOnEraseBookAccepted() {
        List<BookPrimeData> eraseList = new ArrayList<>();
        for (BookPrimeData book : bookSelectionList) {
            if (book.getStatus() == BookPrimeStatus.Local) {
                eraseList.add(book);
            }
        }

        if (eraseList.size() != 0) {
            updateBookPrimeData(eraseList);
            eraseBookTableData(eraseList);
            eraseBookContentData(eraseList);
        }

        performOnFinishAction();
        setSnackBarMessage(R.string.fragment_book_menu_erase_book_accepted);
    }

    public void doOnEraseBookCancelled() {
        performOnFinishAction();
        setSnackBarMessage(R.string.fragment_book_menu_erase_book_cancelled);
    }

    private void updateBookPrimeData(final List<BookPrimeData> eraseList) {
        for (BookPrimeData book : eraseList) {
            for (int index = 0; index < currentBookPrimeList.size(); ++index) {
                if (book.getBookId() == currentBookPrimeList.get(index).getBookId()) {
                    BookPrimeData newBook = BookPrimeFactory.create(book.getBookId(),
                                                                    book.getName(),
                                                                    book.getAuthor(),
                                                                    book.getImage(),
                                                                    book.getFile(),
                                                                    BookPrimeStatus.Global);

                    currentBookPrimeList.set(index, newBook);
                    BookPrimeMapper.update(getActivity().getContentResolver(), newBook);
                }
            }
        }
    }

    private void eraseBookTableData(final List<BookPrimeData> eraseList) {
        final ContentResolver resolver = getActivity().getContentResolver();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                BookTableLoader loader = new BookTableLoader(getContext());
                loader.setListener(new OnStorageListener() {
                    @Override
                    public void onEvent(final List list) {
                        getLoaderManager().destroyLoader(BookTableContract.LOADER_IDENTIFIER);
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                List<BookTableData> dataList = (List<BookTableData>) list;
                                for (BookPrimeData book : eraseList) {
                                    for (BookTableData data : dataList) {
                                        if (data.getBookId() == book.getBookId()) {
                                            BookTableMapper.delete(resolver, data);
                                        }
                                    }
                                }
                            }
                        });
                    }
                });

                getLoaderManager().initLoader(BookTableContract.LOADER_IDENTIFIER, null, loader);
            }
        });
    }

    private void eraseBookContentData(final List<BookPrimeData> eraseList) {
        final ContentResolver resolver = getActivity().getContentResolver();
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                BookContentLoader loader = new BookContentLoader(getContext());
                loader.setListener(new OnStorageListener() {
                    @Override
                    public void onEvent(final List list) {
                        getLoaderManager().destroyLoader(BookContentContract.LOADER_IDENTIFIER);
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                List<BookContentData> dataList = (List<BookContentData>) list;
                                for (BookPrimeData book : eraseList) {
                                    for (BookContentData data : dataList) {
                                        if (data.getBookId() == book.getBookId()) {
                                            BookContentMapper.delete(resolver, data);
                                        }
                                    }
                                }
                            }
                        });
                    }
                });

                getLoaderManager().initLoader(BookContentContract.LOADER_IDENTIFIER, null, loader);
            }
        });
    }

    private void setRecyclerViewLayout(View view) {
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
    }

    public void updateViewList() {
        adapter.updateList(currentBookPrimeList, alphaList, downloadList);
    }

    public void updateViewItem(int position) {
        adapter.updateItem(position, currentBookPrimeList.get(position), alphaList.get(position), downloadList.get(position));
    }

    private void setSnackBarMessage(int resource) {
        ((LibraryActivity)getActivity()).setSnackBarMessage(resource);
    }

    private void setActivityMode(LibraryActivity.ActivityMode activityMode) {
        ((LibraryActivity)getActivity()).setActivityMode(activityMode);
    }

    private void resetAlphaList() {
        alphaList = Collection.assign(alphaList, currentBookPrimeList.size(), ALPHA_DEFAULT);
    }

    private void updatePreviousTitle() {
        ((LibraryActivity) getActivity()).setPreviousTitle();
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
