package pe.ironbit.android.capstone.screen.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.firebase.model.BookPrimeDelegate;
import pe.ironbit.android.capstone.firebase.model.LibraryDataDelegate;
import pe.ironbit.android.capstone.firebase.storage.StorageService;
import pe.ironbit.android.capstone.generic.Action;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeFactory;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeMapper;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeParcelable;
import pe.ironbit.android.capstone.model.LabelPrime.LabelPrimeData;
import pe.ironbit.android.capstone.model.Library.LibraryData;
import pe.ironbit.android.capstone.screen.fragment.BookMenuFragment;
import pe.ironbit.android.capstone.screen.fragment.MainMenuFragment;
import pe.ironbit.android.capstone.screen.fragment.ManagerLabelFragment;
import pe.ironbit.android.capstone.storage.contract.BookPrimeContract;
import pe.ironbit.android.capstone.storage.contract.LabelBookContract;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;
import pe.ironbit.android.capstone.storage.loader.BookPrimeLoader;
import pe.ironbit.android.capstone.storage.loader.LabelBookLoader;
import pe.ironbit.android.capstone.util.DeviceMetaData;
import pe.ironbit.android.capstone.util.InternetStatus;

public class LibraryActivity extends AppCompatActivity {
    private static final String TAG = LibraryActivity.class.getSimpleName();

    private static final String ACTIVITY_LIBRARY_TITLE = "ACTIVITY_LIBRARY_TITLE";

    private static final String ACTIVITY_LIBRARY_SCROLL = "ACTIVITY_LIBRARY_SCROLL";

    private static final String ACTIVITY_LIBRARY_BOOK_PRIME = "ACTIVITY_LIBRARY_BOOK_PRIME";

    private int totalBooksLoaded;

    private int totalBooksLibrary;

    private StorageService storageService;

    private List<BookPrimeData> bookPrimeDataList;

    private String currentTitle;

    @BindView(R.id.activity_library_prime_screen)
    View primeView;

    @Nullable
    @BindView(R.id.activity_library_normal_drawerlayout)
    DrawerLayout drawerLayout;

    @BindView(R.id.activity_library_toolbar)
    Toolbar toolbar;

    @BindView(R.id.activity_library_title)
    TextView titleView;

    @BindView(R.id.activity_library_main_scroll)
    NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        ButterKnife.bind(this);

        configureVariables(savedInstanceState);
        configureActivity();

        loadMainMenu();
        loadBookDataForBookMenu();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ACTIVITY_LIBRARY_TITLE, currentTitle);
        outState.putParcelableArrayList(ACTIVITY_LIBRARY_BOOK_PRIME, (ArrayList)BookPrimeFactory.createBookPrimeParcelableList(bookPrimeDataList));
        outState.putIntArray(ACTIVITY_LIBRARY_SCROLL, new int[] {scrollView.getScrollX(), scrollView.getScrollY()});
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        FragmentManager manager = getSupportFragmentManager();

        Fragment managerLabelFragment = manager.findFragmentByTag(ManagerLabelFragment.class.getSimpleName());
        if ((managerLabelFragment != null) && (!managerLabelFragment.isHidden())) {
            if (isDevicePhone()) {
                configureActionBar(false);
            }

            Fragment bookMenuFragment = manager.findFragmentByTag(BookMenuFragment.class.getSimpleName());
            manager.beginTransaction()
                   .hide(managerLabelFragment)
                   .show(bookMenuFragment)
                   .commitNow();

            setTitle(getString(R.string.local));
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void onClickManagerOption(View view) {
        closeNavigationDrawer();
        changeScreenToManagerLabel();
    }

    public void onClickMainMenuCloud(View view) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(BookMenuFragment.class.getSimpleName());
        if (fragment != null) {
            ((BookMenuFragment)fragment).updateModeGlobal();
        }

        closeNavigationDrawer();
        setTitle(getString(R.string.cloud));
    }

    public void onClickMainMenuLocal(View view) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(BookMenuFragment.class.getSimpleName());
        if (fragment != null) {
            ((BookMenuFragment)fragment).updateModelLocal();
        }

        closeNavigationDrawer();
        setTitle(getString(R.string.local));
    }

    public View getPrimeView() {
        return primeView;
    }

    private void closeNavigationDrawer() {
        if (isDevicePhone()) {
            drawerLayout.closeDrawers();
        }
    }

    private void changeScreenToManagerLabel() {
        FragmentManager manager = getSupportFragmentManager();

        Fragment managerLabelFragment = manager.findFragmentByTag(ManagerLabelFragment.class.getSimpleName());
        if (managerLabelFragment == null) {
            managerLabelFragment = new ManagerLabelFragment();
            manager.beginTransaction()
                   .add(R.id.activity_library_main_screen, managerLabelFragment, ManagerLabelFragment.class.getSimpleName())
                   .commit();
        } else if (!managerLabelFragment.isHidden()) {
            return;
        }

        if (isDevicePhone()) {
            configureActionBar(true);
        }
        setTitle(getString(R.string.manager_label_title));

        Fragment bookMenuFragment = manager.findFragmentByTag(BookMenuFragment.class.getSimpleName());

        manager.beginTransaction()
               .hide(bookMenuFragment)
               .show(managerLabelFragment)
               .commit();
    }

    private void configureVariables(Bundle bundle) {
        if (bundle != null) {
            currentTitle = bundle.getString(ACTIVITY_LIBRARY_TITLE);
            bookPrimeDataList = BookPrimeFactory.createBookPrimeDataList(bundle.<BookPrimeParcelable>getParcelableArrayList(ACTIVITY_LIBRARY_BOOK_PRIME));
            final int[] position = bundle.getIntArray(ACTIVITY_LIBRARY_SCROLL);
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(position[0], position[1]);
                }
            });
        } else {
            currentTitle = getString(R.string.cloud);
            bookPrimeDataList = new ArrayList<>();
        }

        totalBooksLoaded = 0;
        totalBooksLibrary = 0;
        storageService = new StorageService(getApplicationContext());
    }

    private void configureActivity() {
        setTitle(currentTitle);
        setSupportActionBar(toolbar);
    }

    public void configureActionBar(boolean value) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(value);
        getSupportActionBar().setDisplayShowHomeEnabled(value);
    }

    public void setTitle(String title) {
        this.currentTitle = title;
        titleView.setText(title);
    }

    public void updateBookMenuScreen(LabelPrimeData labelPrime) {
        LabelBookLoader loader = new LabelBookLoader(getApplicationContext());
        loader.LoadLabelList(labelPrime.getLabelId());
        loader.setListener(new OnStorageListener() {
            @Override
            public void onEvent(List list) {
                getSupportLoaderManager().destroyLoader(LabelBookContract.LOADER_IDENTIFIER);

                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = manager.findFragmentByTag(BookMenuFragment.class.getSimpleName());
                if (fragment != null) {
                    ((BookMenuFragment)fragment).updateModelLabel(list);
                }
            }
        });

        closeNavigationDrawer();
        setTitle(labelPrime.getLabelName());
        getSupportLoaderManager().initLoader(LabelBookContract.LOADER_IDENTIFIER, null, loader);
    }

    private void loadMainMenu() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(MainMenuFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = MainMenuFragment.newInstance();
            manager.beginTransaction()
                   .add(R.id.layout_library_main_menu, fragment, MainMenuFragment.class.getSimpleName())
                   .commit();
        }
    }

    private void loadBookDataForBookMenu() {
        BookPrimeLoader loader = new BookPrimeLoader(getApplicationContext());
        loader.loadList()
              .setListener(new OnStorageListener() {
                  @Override
                  public void onEvent(List list) {
                      executeBookDataLogic(list);
                  }
              });

        getLoaderManager().initLoader(BookPrimeContract.LOADER_IDENTIFIER, null, loader);
    }

    private void executeBookDataLogic(List list) {
        getLoaderManager().destroyLoader(BookPrimeContract.LOADER_IDENTIFIER);

        if (list.size() == 0) {
            if (isInternetWorking()) {
                loadLibraryDataFromFirebase();
            } else {
                showMessageInternetProblem();
            }
        } else {
            bookPrimeDataList = list;
            loadBookMenuScreen();
        }
    }

    private void loadBookMenuScreen() {
        FragmentManager manager = getSupportFragmentManager();

        Fragment fragment = manager.findFragmentByTag(BookMenuFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = BookMenuFragment.newInstance(bookPrimeDataList);
            manager.beginTransaction()
                   .add(R.id.activity_library_main_screen, fragment, BookMenuFragment.class.getSimpleName())
                   .commit();
        }
    }

    private void loadLibraryDataFromFirebase() {
        LibraryDataDelegate delegate = new LibraryDataDelegate(storageService);
        delegate.setAction(new Action<LibraryData>() {
            @Override
            public void execute(LibraryData libraryData) {
                totalBooksLoaded = 0;
                totalBooksLibrary = libraryData.getTotal();
                loadBookDataFromFirebase(libraryData);
            }
        });

        storageService.process(delegate);
    }

    private void loadBookDataFromFirebase(LibraryData library) {
        for (String identifier : library.getBooks()) {
            BookPrimeDelegate delegate = new BookPrimeDelegate(storageService, Integer.parseInt(identifier));

            delegate.setAction(new Action<BookPrimeData>() {
                @Override
                public void execute(BookPrimeData bookPrimeData) {
                    bookPrimeDataList.add(bookPrimeData);
                    BookPrimeMapper.insert(getContentResolver(), bookPrimeData);
                    finishedLoadBookDataFromFirebase();
                }
            });

            storageService.process(delegate);
        }
    }

    private synchronized void finishedLoadBookDataFromFirebase() {
        if (totalBooksLibrary == ++totalBooksLoaded) {
            loadBookMenuScreen();
        }
    }

    private void showMessageInternetProblem() {
        String message = getString(R.string.notify_internet_not_working);
        Snackbar.make(primeView, message, Snackbar.LENGTH_LONG).show();
    }

    private boolean isInternetWorking() {
        return InternetStatus.verifyInternetConnection(getApplicationContext());
    }

    private boolean isDevicePhone() {
        return !DeviceMetaData.isDeviceTablet(getApplicationContext());
    }
}
