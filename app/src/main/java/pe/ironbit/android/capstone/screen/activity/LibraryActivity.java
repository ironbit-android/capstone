package pe.ironbit.android.capstone.screen.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.screen.fragment.BookMenuFragment;
import pe.ironbit.android.capstone.screen.fragment.MainMenuFragment;
import pe.ironbit.android.capstone.storage.contract.BookPrimeContract;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;
import pe.ironbit.android.capstone.storage.loader.BookPrimeLoader;
import pe.ironbit.android.capstone.util.InternetStatus;

public class LibraryActivity extends AppCompatActivity {
    private List<BookPrimeData> bookPrimeDataList;

    @BindView(R.id.activity_library_prime_screen)
    View primeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        ButterKnife.bind(this);

        initializeScreen();
        loadBooksFromStorage();
    }

    private void initializeScreen() {
        FragmentManager manager = getSupportFragmentManager();

        Fragment mainMenuFragment = MainMenuFragment.newInstance();
        manager.beginTransaction()
               .add(R.id.layout_library_main_menu, mainMenuFragment, MainMenuFragment.class.getSimpleName())
               .commit();

        Fragment bookMenuFragment = BookMenuFragment.newInstance();
        manager.beginTransaction()
               .add(R.id.layout_library_book_menu, bookMenuFragment, BookMenuFragment.class.getSimpleName())
               .commit();
    }

    private void loadBooksFromStorage() {
        BookPrimeLoader loader = new BookPrimeLoader(getApplicationContext());
        loader.loadList()
              .setListener(new OnStorageListener() {
                  @Override
                  public void onEvent(List list) {
                      bookPrimeDataList = list;
                      updateBookMenuScreen();
                  }
              });

        getLoaderManager().initLoader(BookPrimeContract.LOADER_IDENTIFIER, null, loader);
    }

    private void updateBookMenuScreen() {
        if (bookPrimeDataList.size() == 0) {
            if (isInternetWorking()) {
                loadBookDataFromFirebase();
            } else {
                showMessageInternetProblem();
            }
        } else {
            loadBookDataIntoBookMenuScreen();
        }
    }

    private void showMessageInternetProblem() {
        String message = getString(R.string.notify_internet_not_working);
        Snackbar.make(primeView, message, Snackbar.LENGTH_LONG).show();
    }

    private void loadBookDataFromFirebase() {
    }

    private void loadBookDataIntoBookMenuScreen() {
    }

    private boolean isInternetWorking() {
        return InternetStatus.verifyInternetConnection(getApplicationContext());
    }
}
