package pe.ironbit.android.capstone.screen.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import pe.ironbit.android.capstone.screen.fragment.BaseFragment;
import pe.ironbit.android.capstone.screen.fragment.BookMenuFragment;
import pe.ironbit.android.capstone.screen.fragment.BookSearchFragment;
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

    private static final String ACTIVITY_LIBRARY_SCROLL = "ACTIVITY_LIBRARY_SCROLL";

    private static final String ACTIVITY_LIBRARY_BOOK_PRIME = "ACTIVITY_LIBRARY_BOOK_PRIME";

    private static final String ACTIVITY_LIBRARY_CURRENT_TITLE = "ACTIVITY_LIBRARY_CURRENT_TITLE";

    private static final String ACTIVITY_LIBRARY_PREVIOUS_TITLE = "ACTIVITY_LIBRARY_PREVIOUS_TITLE";

    private static final String ACTIVITY_LIBRARY_SELECTION_BAR = "ACTIVITY_LIBRARY_SELECTION_BAR";

    private static final String IS_BACK_ICON_ACTIVE = "IS_BACK_ICON_ACTIVE";

    private static final String ACTIVITY_MODE = "ACTIVITY_MODE";

    public enum ActivityMode {
        None,
        BookMenu,
        BookSearch,
        BookSelection,
        ManagerLabel
    }

    private Map<ActivityMode, String> fragmentTagMap;

    private int totalBooksLoaded;

    private int totalBooksLibrary;

    private StorageService storageService;

    private List<BookPrimeData> bookPrimeDataList;

    private String currentTitle;

    private String previousTitle;

    private String selectionBarValue;

    private boolean isBackIconActive;

    private ActivityMode activityMode;

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

    @BindView(R.id.activity_library_toolbar_selection)
    TextView selectionBarView;

    @BindView(R.id.activity_library_toolbar_left_icon)
    ImageView toolbarLeftIconView;

    @BindView(R.id.activity_library_toolbar_right_icon)
    ImageView toolbarRightIconView;

    @BindView(R.id.activity_library_book_search)
    EditText bookSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        ButterKnife.bind(this);

        configureVariables(savedInstanceState);

        loadMainMenuFragment();
        configActivityMode();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(ACTIVITY_MODE, activityMode);
        outState.putBoolean(IS_BACK_ICON_ACTIVE, isBackIconActive);
        outState.putString(ACTIVITY_LIBRARY_CURRENT_TITLE, currentTitle);
        outState.putString(ACTIVITY_LIBRARY_PREVIOUS_TITLE, previousTitle);
        outState.putString(ACTIVITY_LIBRARY_SELECTION_BAR, selectionBarValue);
        outState.putParcelableArrayList(ACTIVITY_LIBRARY_BOOK_PRIME, (ArrayList)BookPrimeFactory.createBookPrimeParcelableList(bookPrimeDataList));
        outState.putIntArray(ACTIVITY_LIBRARY_SCROLL, new int[] {scrollView.getScrollX(), scrollView.getScrollY()});
        super.onSaveInstanceState(outState);
    }

    private void configureVariables(Bundle bundle) {
        if (bundle != null) {
            activityMode = (ActivityMode) bundle.get(ACTIVITY_MODE);
            isBackIconActive = bundle.getBoolean(IS_BACK_ICON_ACTIVE);
            currentTitle = bundle.getString(ACTIVITY_LIBRARY_CURRENT_TITLE);
            previousTitle = bundle.getString(ACTIVITY_LIBRARY_PREVIOUS_TITLE);
            selectionBarValue = bundle.getString(ACTIVITY_LIBRARY_SELECTION_BAR);
            bookPrimeDataList = BookPrimeFactory.createBookPrimeDataList(bundle.<BookPrimeParcelable>getParcelableArrayList(ACTIVITY_LIBRARY_BOOK_PRIME));
            final int[] position = bundle.getIntArray(ACTIVITY_LIBRARY_SCROLL);
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.scrollTo(position[0], position[1]);
                }
            });
        } else {
            activityMode = ActivityMode.None;
            isBackIconActive = false;
            selectionBarValue = "";
            currentTitle = getString(R.string.cloud);
            previousTitle = currentTitle;
            bookPrimeDataList = new ArrayList<>();
        }

        totalBooksLoaded = 0;
        totalBooksLibrary = 0;
        storageService = new StorageService(getApplicationContext());

        fragmentTagMap = new HashMap<>();
        fragmentTagMap.put(ActivityMode.BookMenu, BookMenuFragment.class.getSimpleName());
        fragmentTagMap.put(ActivityMode.BookSearch, BookSearchFragment.class.getSimpleName());
        fragmentTagMap.put(ActivityMode.ManagerLabel, ManagerLabelFragment.class.getSimpleName());

        setSupportActionBar(toolbar);
        titleView.setText(currentTitle);
    }

    @Override
    public void onBackPressed() {
        if (doOnBackPressed()) {
            setTitle(previousTitle);
            setActivityMode(ActivityMode.BookMenu);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void onClickToolbarLeftMenu(View view) {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    public void onClickToolbarRightMenu(View view) {
        if (activityMode == ActivityMode.BookMenu) {
            setActivityMode(ActivityMode.BookSearch);
        }
    }

    public void onClickManagerOption(View view) {
        closeNavigationDrawer();
        setActivityMode(ActivityMode.ManagerLabel);
        setTitle(getString(R.string.manager_label_title));
    }

    public void onClickMainMenuCloudOption(View view) {
        closeNavigationDrawer();
        setActivityMode(ActivityMode.BookMenu);
        loadBookMenuGlobalModel();
        setTitle(getString(R.string.cloud));
    }

    public void onClickMainMenuLocalOption(View view) {
        closeNavigationDrawer();
        setActivityMode(ActivityMode.BookMenu);
        loadBookMenuLocalModel();
        setTitle(getString(R.string.local));
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

        showFragment(ActivityMode.BookMenu);
        setActivityMode(ActivityMode.BookMenu);

        closeNavigationDrawer();
        setTitle(labelPrime.getLabelName());
        getSupportLoaderManager().initLoader(LabelBookContract.LOADER_IDENTIFIER, null, loader);
    }

    public View getPrimeView() {
        return primeView;
    }

    public void setSnackBarMessage(int resource) {
        Snackbar.make(primeView, resource, Snackbar.LENGTH_LONG).show();
    }

    public void setSnackBarMessage(String message) {
        Snackbar.make(primeView, message, Snackbar.LENGTH_LONG).show();
    }

    public String getQuery() {
        return bookSearchView.getText().toString();
    }

    public void clearQuery() {
        bookSearchView.setText("");
    }

    private void closeNavigationDrawer() {
        if (isDevicePhone()) {
            drawerLayout.closeDrawers();
        }
    }

    public void configureActionBar(boolean value) {
        if (isDevicePhone()) {
            isBackIconActive = value;
            getSupportActionBar().setDisplayHomeAsUpEnabled(value);
            getSupportActionBar().setDisplayShowHomeEnabled(value);
        }
    }

    public void setPreviousTitle() {
        currentTitle = previousTitle;
        titleView.setText(currentTitle);
    }

    public void setTitle(String title) {
        String selection = getString(R.string.menu_selection_book_title);
        if (!TextUtils.equals(currentTitle, selection)) {
            previousTitle = currentTitle;
        }
        currentTitle = title;
        titleView.setText(title);
    }

    public void setSelectionBar(int value) {
        selectionBarValue = String.valueOf(value);
        selectionBarView.setText(selectionBarValue);
    }

    public void setActivityMode(ActivityMode activityMode) {
        this.activityMode = activityMode;
        configActivityMode();
    }

    private void configActivityMode() {
        if (activityMode == ActivityMode.None) {
            performInitialization();
            return;
        }
        if (activityMode == ActivityMode.BookMenu) {
            loadBookMenuFragment();
            return;
        }
        if (activityMode == ActivityMode.BookSelection) {
            loadBookMenuSelectionFunctionality();
            return;
        }
        if (activityMode == ActivityMode.ManagerLabel) {
            loadManagerLabelFragment();
            return;
        }
        if (activityMode == ActivityMode.BookSearch) {
            loadBookSearchFragment();
            return;
        }
    }

    private void loadBookMenuSelectionFunctionality() {
        titleView.setVisibility(View.VISIBLE);
        bookSearchView.setVisibility(View.GONE);
        selectionBarView.setVisibility(View.VISIBLE);
        titleView.setVisibility(View.VISIBLE);
        toolbarLeftIconView.setVisibility(View.GONE);
        toolbarRightIconView.setVisibility(View.GONE);

        configureActionBar(true);
        setTitle(getString(R.string.menu_selection_book_title));
    }

    private void loadBookMenuFragment() {
        loadBookMenuBaseFragment();
        showFragment(ActivityMode.BookMenu);

        selectionBarValue = "";
        titleView.setVisibility(View.VISIBLE);
        bookSearchView.setVisibility(View.GONE);
        selectionBarView.setVisibility(View.GONE);
        if (isDevicePhone()) {
            toolbarLeftIconView.setVisibility(View.VISIBLE);
        } else {
            toolbarLeftIconView.setVisibility(View.GONE);
        }
        toolbarRightIconView.setVisibility(View.VISIBLE);

        configureActionBar(false);
    }

    private void loadManagerLabelFragment() {
        loadManagerLabelBaseFragment();
        showFragment(ActivityMode.ManagerLabel);

        selectionBarValue = "";
        titleView.setVisibility(View.VISIBLE);
        bookSearchView.setVisibility(View.GONE);
        selectionBarView.setVisibility(View.GONE);
        toolbarLeftIconView.setVisibility(View.GONE);
        toolbarRightIconView.setVisibility(View.GONE);

        configureActionBar(true);
    }

    private void loadBookSearchFragment() {
        loadBookSearchBaseFragment();
        showFragment(ActivityMode.BookSearch);

        selectionBarValue = "";
        titleView.setVisibility(View.GONE);
        bookSearchView.setVisibility(View.VISIBLE);
        selectionBarView.setVisibility(View.VISIBLE);
        toolbarLeftIconView.setVisibility(View.GONE);
        toolbarRightIconView.setVisibility(View.GONE);

        configureActionBar(true);
        selectionBarView.setText(selectionBarValue);
    }

    private void loadBookMenuBaseFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(BookMenuFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = BookMenuFragment.newInstance(bookPrimeDataList);
            manager.beginTransaction()
                   .add(R.id.activity_library_main_screen, fragment, BookMenuFragment.class.getSimpleName())
                   .commit();
        }
    }

    private void loadManagerLabelBaseFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(ManagerLabelFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = new ManagerLabelFragment();
            manager.beginTransaction()
                   .add(R.id.activity_library_main_screen, fragment, ManagerLabelFragment.class.getSimpleName())
                   .commit();
        }
    }

    private void loadBookSearchBaseFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(BookSearchFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = BookSearchFragment.newInstance(bookPrimeDataList);
            manager.beginTransaction()
                   .add(R.id.activity_library_main_screen, fragment, BookSearchFragment.class.getSimpleName())
                   .commit();
        }
    }

    private void loadMainMenuFragment() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentByTag(MainMenuFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = MainMenuFragment.newInstance();
            manager.beginTransaction()
                   .add(R.id.layout_library_main_menu, fragment, MainMenuFragment.class.getSimpleName())
                   .commit();
        }
    }

    private void showFragment(ActivityMode key) {
        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction transaction = manager.beginTransaction();
        for (Map.Entry<ActivityMode, String> entry : fragmentTagMap.entrySet()) {
            Fragment fragment = manager.findFragmentByTag(entry.getValue());
            if (fragment == null) {
                continue;
            }
            if (key == entry.getKey()) {
                transaction.show(fragment);
                ((BaseFragment) fragment).doOnShowFragment();
            } else {
                if (!fragment.isHidden()) {
                    transaction.hide(fragment);
                    ((BaseFragment) fragment).doOnHideFragment();
                }
            }
        }
        transaction.commit();
    }

    private boolean doOnBackPressed() {
        FragmentManager manager = getSupportFragmentManager();

        boolean outcome = false;
        for (Map.Entry<ActivityMode, String> entry : fragmentTagMap.entrySet()) {
            Fragment fragment = manager.findFragmentByTag(entry.getValue());
            if (fragment == null) {
                continue;
            }
            outcome = ((BaseFragment)fragment).doOnBackPressed();
            if (outcome) {
                return outcome;
            }
        }

        return outcome;
    }

    private void loadBookMenuGlobalModel() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment bookMenuFragment = manager.findFragmentByTag(BookMenuFragment.class.getSimpleName());
        ((BookMenuFragment)bookMenuFragment).updateModeGlobal();
    }

    private void loadBookMenuLocalModel() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment bookMenuFragment = manager.findFragmentByTag(BookMenuFragment.class.getSimpleName());
        ((BookMenuFragment)bookMenuFragment).updateModelLocal();
    }

    private void performInitialization() {
        BookPrimeLoader loader = new BookPrimeLoader(getApplicationContext());
        loader.loadList()
              .setListener(new OnStorageListener() {
                  @Override
                  public void onEvent(List list) {
                      getLoaderManager().destroyLoader(BookPrimeContract.LOADER_IDENTIFIER);

                      if (list.size() == 0) {
                          if (isInternetWorking()) {
                              loadLibraryDataFromFirebase();
                          } else {
                              showMessageInternetProblem();
                          }
                      } else {
                          bookPrimeDataList = list;
                          new Handler().post(new Runnable() {
                              @Override
                              public void run() {
                                  setTitle(currentTitle);
                                  setActivityMode(ActivityMode.BookMenu);
                              }
                          });
                      }
                  }
              });

        getLoaderManager().initLoader(BookPrimeContract.LOADER_IDENTIFIER, null, loader);
    }

    private void loadLibraryDataFromFirebase() {
        LibraryDataDelegate delegate = new LibraryDataDelegate(storageService);
        delegate.setAction(new Action<LibraryData>() {
            @Override
            public void execute(LibraryData libraryData) {
                totalBooksLoaded = 0;
                totalBooksLibrary = libraryData.getTotal();
                for (String identifier : libraryData.getBooks()) {
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
        });

        storageService.process(delegate);
    }

    private synchronized void finishedLoadBookDataFromFirebase() {
        if (totalBooksLibrary == ++totalBooksLoaded) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    setTitle(currentTitle);
                    setActivityMode(ActivityMode.BookMenu);
                }
            });
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
