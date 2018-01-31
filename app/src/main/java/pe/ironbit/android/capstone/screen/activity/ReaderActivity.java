package pe.ironbit.android.capstone.screen.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.data.parcel.activity.reader.ReaderActivityData;
import pe.ironbit.android.capstone.data.parcel.activity.reader.ReaderActivityParcel;
import pe.ironbit.android.capstone.data.shared.BookSharedData;
import pe.ironbit.android.capstone.firebase.analytics.AnalyticsService;
import pe.ironbit.android.capstone.model.BookContent.BookContentData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeFactory;
import pe.ironbit.android.capstone.model.BookTable.BookTableData;
import pe.ironbit.android.capstone.screen.fragment.ReaderMainMenuFragment;
import pe.ironbit.android.capstone.screen.fragment.TableContentFragment;
import pe.ironbit.android.capstone.storage.contract.BookContentContract;
import pe.ironbit.android.capstone.storage.contract.BookPrimeContract;
import pe.ironbit.android.capstone.storage.contract.BookTableContract;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;
import pe.ironbit.android.capstone.storage.loader.BookContentLoader;
import pe.ironbit.android.capstone.storage.loader.BookPrimeLoader;
import pe.ironbit.android.capstone.storage.loader.BookTableLoader;
import pe.ironbit.android.capstone.util.DeviceMetaData;

public class ReaderActivity extends AppCompatActivity {
    public static final String BOOK_PRIME_DATA_KEY ="BOOK_PRIME_DATA_KEY";

    private static final int INIT_CHAPTER = 2;

    private static final float ALPHA_ENABLE = 1.0f;

    private static final float ALPHA_DISABLE = 0.25f;

    private BookSharedData shared;

    private ReaderActivityData data;

    private List<BookTableData> bookTableList;

    private List<BookContentData> bookContentList;

    private AnalyticsService analyticsService;

    private int configReady = 0;

    @Nullable
    @BindView(R.id.activity_reader_drawerlayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.activity_reader_appbar_layout)
    AppBarLayout appBarLayout;

    @BindView(R.id.activity_reader_toolbar_book_name)
    TextView bookNameView;

    @BindView(R.id.activity_reader_toolbar_book_author)
    TextView bookAuthorView;

    @BindView(R.id.activity_reader_webview)
    WebView webView;

    @BindView(R.id.activity_reader_navigator_left_icon)
    View navigatorLeftIconView;

    @BindView(R.id.activity_reader_navigator_right_icon)
    View navigatorRightIconView;

    @BindView(R.id.activity_reader_main_scroll)
    NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_reader);

        ButterKnife.bind(this);

        configureVariables();
        loadSavedInformation();
        loadMainMenuFragment();
        executeAnalyticsService();
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable(ReaderActivityParcel.READER_ACTIVITY_KEY, new ReaderActivityParcel(data));
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void configureVariables() {
        data = new ReaderActivityData();
        data.setCurrentChapter(INIT_CHAPTER);
        data.setCurrentBook(BookPrimeFactory.create(getIntent().getParcelableExtra(BOOK_PRIME_DATA_KEY)));
    }

    private void loadSavedInformation() {
        shared = new BookSharedData(getPreferences(Context.MODE_PRIVATE));
        shared.load();

        BookPrimeData book = data.getCurrentBook();
        if (book.getBookId() == BookPrimeData.NULL_INDEX) {
            loadBookInformation(shared.getBookId());
            data.setCurrentChapter(shared.getCurrentChapter());
        } else if (book.getBookId() == shared.getBookId()) {
            loadBookInformation(book.getBookId());
            data.setCurrentChapter(shared.getCurrentChapter());
        } else {
            loadBookInformation(book.getBookId());
            shared.setBookId(book.getBookId());
            shared.setCurrentChapter(data.getCurrentChapter());
            shared.save();
        }
    }

    public void onClickToolbarBackIcon(View view) {
        returnToLibraryActivity();
    }

    public void onClickLibraryOption(View view) {
        returnToLibraryActivity();
    }

    public void onClickTableOfContentOption(View view) {
        loadTableOfContentFragment();
    }

    public void onClickNavigatorLeftIcon(View view) {
        int chapter = data.getCurrentChapter();
        if (chapter == data.getFirstChapter()) {
            return;
        }

        chapter--;
        data.setCurrentChapter(chapter);
        shared.setCurrentChapter(chapter);
        shared.save();

        loadViewInitSettings();
        loadBookContentInformation();
        updateNavigatorMenu(chapter);

        executeAnalyticsService();
    }

    public void onClickNavigatorRightIcon(View view) {
        int chapter = data.getCurrentChapter();
        if (chapter == data.getLastChapter()) {
            return;
        }

        chapter++;
        data.setCurrentChapter(chapter);
        shared.setCurrentChapter(chapter);
        shared.save();

        loadViewInitSettings();
        loadBookContentInformation();
        updateNavigatorMenu(chapter);

        executeAnalyticsService();
    }

    public void onClickNavigatorMenuIcon(View view) {
        loadMainMenuFragment();
        if (isDevicePhone()) {
            drawerLayout.openDrawer(Gravity.LEFT);
        }
    }

    public void changeChapter(int chapter) {
        if (chapter != data.getCurrentChapter()) {
            data.setCurrentChapter(chapter);
            shared.setCurrentChapter(chapter);
            shared.save();

            loadBookContentInformation();

            updateNavigatorMenu(chapter);

            executeAnalyticsService();
        }
        if (isDevicePhone()) {
            drawerLayout.closeDrawers();
        }
    }

    private void configureScreen() {
        BookPrimeData book = data.getCurrentBook();
        bookNameView.setText(book.getName());
        bookAuthorView.setText(book.getAuthor());
    }

    private void updateNavigatorMenu(int chapter) {
        if (chapter == data.getFirstChapter()) {
            navigatorLeftIconView.setAlpha(ALPHA_DISABLE);
            navigatorRightIconView.setAlpha(ALPHA_ENABLE);
        } else if (chapter == data.getLastChapter()) {
            navigatorLeftIconView.setAlpha(ALPHA_ENABLE);
            navigatorRightIconView.setAlpha(ALPHA_DISABLE);
        } else {
            navigatorLeftIconView.setAlpha(ALPHA_ENABLE);
            navigatorRightIconView.setAlpha(ALPHA_ENABLE);
        }
    }

    private void loadMainMenuFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(ReaderMainMenuFragment.class.getSimpleName());
        if ((fragment == null) || fragment.isHidden()) {
            fragment = new ReaderMainMenuFragment();
            manager.beginTransaction()
                   .replace(R.id.layout_reader_main_menu, fragment, ReaderMainMenuFragment.class.getSimpleName())
                   .commit();
        }
    }

    private void loadTableOfContentFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(TableContentFragment.class.getSimpleName());
        if ((fragment == null) || fragment.isHidden()) {
            fragment = TableContentFragment.newInstance(bookTableList.subList(INIT_CHAPTER, bookTableList.size()));
            manager.beginTransaction()
                   .replace(R.id.layout_reader_main_menu, fragment, TableContentFragment.class.getSimpleName())
                   .commit();
        }
    }

    private void loadBookInformation(final int bookId) {
        loadBookDataInformation(bookId);
        loadBookTableInformation(bookId);
        loadBookContentInformation(bookId);
    }

    private void loadBookDataInformation(final int bookId) {
        BookPrimeLoader loader = new BookPrimeLoader(getApplicationContext());
        loader.loadItem(bookId);
        loader.setListener(new OnStorageListener() {
            @Override
            public void onEvent(List list) {
                getSupportLoaderManager().destroyLoader(BookPrimeContract.LOADER_IDENTIFIER);

                if (list == null) {
                    return;
                }
                data.setCurrentBook((BookPrimeData)list.get(0));
                configBookInformation();
            }
        });

        getSupportLoaderManager().initLoader(BookPrimeContract.LOADER_IDENTIFIER, null, loader);
    }

    private void loadBookTableInformation(final int bookId) {
        BookTableLoader loader = new BookTableLoader(getApplicationContext());
        loader.load(bookId);
        loader.setListener(new OnStorageListener() {
            @Override
            public void onEvent(final List list) {
                getSupportLoaderManager().destroyLoader(BookTableContract.LOADER_IDENTIFIER);

                if (list == null) {
                    return;
                }
                bookTableList = (List<BookTableData>)list;
                configBookInformation();
            }
        });

        getSupportLoaderManager().initLoader(BookTableContract.LOADER_IDENTIFIER, null, loader);
    }

    private void loadBookContentInformation(final int bookId) {
        BookContentLoader loader = new BookContentLoader(getApplicationContext());
        loader.load(bookId);
        loader.setListener(new OnStorageListener() {
            @Override
            public void onEvent(List list) {
                getSupportLoaderManager().destroyLoader(BookContentContract.LOADER_IDENTIFIER);

                if (list == null) {
                    return;
                }
                bookContentList = (List<BookContentData>)list;
                configBookInformation();
            }
        });

        getSupportLoaderManager().initLoader(BookContentContract.LOADER_IDENTIFIER, null, loader);
    }

    private synchronized void configBookInformation() {
        if (++configReady == 3) {
            configReady = 0;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    data.setFirstChapter(INIT_CHAPTER);
                    data.setLastChapter(bookTableList.size() - 1);

                    configureScreen();

                    loadBookContentInformation();
                    updateNavigatorMenu(data.getCurrentChapter());
                }
            });
        }
    }

    private void loadViewInitSettings() {
        appBarLayout.setExpanded(true);
        int[] position = new int[] {0, 0};
        scrollView.scrollTo(position[0], position[1]);
    }

    private void loadBookContentInformation() {
        if ((bookContentList != null) && !bookContentList.isEmpty()) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    webView.loadDataWithBaseURL(null, bookContentList.get(data.getCurrentChapter()).getValue(), "text/html", "UTF-8", null);
                }
            });
        }
    }

    private void executeAnalyticsService() {
        if (analyticsService == null) {
            analyticsService = new AnalyticsService(getApplicationContext());
        }

        analyticsService.setCurrentBook(data.getCurrentBook().getBookId())
                        .setCurrentChapter(data.getCurrentChapter())
                        .logEvent();
    }

    private void returnToLibraryActivity() {
        finish();
    }

    private boolean isDevicePhone() {
        return !DeviceMetaData.isDeviceTablet(getApplicationContext());
    }
}
