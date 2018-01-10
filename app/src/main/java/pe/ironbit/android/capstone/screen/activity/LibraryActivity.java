package pe.ironbit.android.capstone.screen.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeMapper;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeParcelable;
import pe.ironbit.android.capstone.model.Library.LibraryData;
import pe.ironbit.android.capstone.screen.fragment.BookMenuFragment;
import pe.ironbit.android.capstone.storage.contract.BookPrimeContract;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;
import pe.ironbit.android.capstone.storage.loader.BookPrimeLoader;
import pe.ironbit.android.capstone.util.InternetStatus;

public class LibraryActivity extends AppCompatActivity {
    private int totalBooksLoaded;

    private int totalBooksLibrary;

    private StorageService storageService;

    private List<BookPrimeData> bookPrimeDataList;

    @BindView(R.id.activity_library_prime_screen)
    View primeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        configureVariables();

        loadBookDataForBookMenu();
    }

    private void configureVariables() {
        totalBooksLoaded = 0;
        totalBooksLibrary = 0;
        bookPrimeDataList = new ArrayList<>();
        storageService = new StorageService(getApplicationContext());
        ButterKnife.bind(this);
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

        Fragment bookMenuFragment = manager.findFragmentByTag(BookMenuFragment.class.getSimpleName());
        if (bookMenuFragment == null) {
            bookMenuFragment = BookMenuFragment.newInstance(createBookParcelableList(bookPrimeDataList));
            manager.beginTransaction()
                   .add(R.id.layout_library_book_menu, bookMenuFragment, BookMenuFragment.class.getSimpleName())
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

//    private void initializeScreen() {
//        FragmentManager manager = getSupportFragmentManager();
//
//        Fragment mainMenuFragment = MainMenuFragment.newInstance();
//        manager.beginTransaction()
//                .add(R.id.layout_library_main_menu, mainMenuFragment, MainMenuFragment.class.getSimpleName())
//                .commit();
//
//        Fragment bookMenuFragment = BookMenuFragment.newInstance();
//        manager.beginTransaction()
//                .add(R.id.layout_library_book_menu, bookMenuFragment, BookMenuFragment.class.getSimpleName())
//                .commit();
//    }

    private ArrayList<BookPrimeParcelable> createBookParcelableList(List<BookPrimeData> inputList) {
        ArrayList<BookPrimeParcelable> outputList = new ArrayList<>();

        for (BookPrimeData data : inputList) {
            BookPrimeParcelable parcel = new BookPrimeParcelable(data);
            outputList.add(parcel);
        }

        return outputList;
    }

    private void showMessageInternetProblem() {
        String message = getString(R.string.notify_internet_not_working);
        Snackbar.make(primeView, message, Snackbar.LENGTH_LONG).show();
    }

    private boolean isInternetWorking() {
        return InternetStatus.verifyInternetConnection(getApplicationContext());
    }
}
