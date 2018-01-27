package pe.ironbit.android.capstone.screen.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.model.BookContent.BookContentData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeFactory;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeParcelable;
import pe.ironbit.android.capstone.model.BookTable.BookTableData;
import pe.ironbit.android.capstone.storage.contract.BookContentContract;
import pe.ironbit.android.capstone.storage.contract.BookTableContract;
import pe.ironbit.android.capstone.storage.listener.OnStorageListener;
import pe.ironbit.android.capstone.storage.loader.BookContentLoader;
import pe.ironbit.android.capstone.storage.loader.BookTableLoader;

public class ReaderActivity extends AppCompatActivity {
    public static final String BOOK_PRIME_DATA_KEY = "BOOK_PRIME_DATA_KEY";

    private static final String SAVE_BOOK_ID = "SAVE_BOOK_ID";

    private static final String SAVE_BOOK_CHAPTER = "SAVE_BOOK_CHAPTER";

    private BookPrimeData book;

    private List<BookTableData> bookTableList;

    private List<BookContentData> bookContentList;

    private int chapter;

    @BindView(R.id.activity_reader_toolbar_book_name)
    TextView bookNameView;

    @BindView(R.id.activity_reader_toolbar_book_author)
    TextView bookAuthorView;

    @BindView(R.id.activity_reader_webview)
    WebView webView;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_reader);

        ButterKnife.bind(this);

        configureVariables(bundle);
        loadSavedInformation();
        configureScreen();

        loadBookTableInformation(book.getBookId());
        loadBookContentInformation(book.getBookId());
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putParcelable(BOOK_PRIME_DATA_KEY, BookPrimeFactory.create(book));
        super.onSaveInstanceState(bundle);
    }

    private void configureVariables(Bundle bundle) {
        if (bundle != null) {
            book = ((BookPrimeParcelable)bundle.getParcelable(BOOK_PRIME_DATA_KEY)).getBookPrime();
        } else {
            book = ((BookPrimeParcelable)getIntent().getParcelableExtra(BOOK_PRIME_DATA_KEY)).getBookPrime();
        }
    }

    private void configureScreen() {
        bookNameView.setText(book.getName());
        bookAuthorView.setText(book.getAuthor());
    }

    private void loadSavedInformation() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        int bookId = preferences.getInt(SAVE_BOOK_ID, -1);
        int chapter = preferences.getInt(SAVE_BOOK_CHAPTER, -1);

        if (book.getBookId() != bookId) {
            this.chapter = 2;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(SAVE_BOOK_ID, book.getBookId());
            editor.putInt(SAVE_BOOK_CHAPTER, this.chapter);
            return;
        }
        if (chapter == -1) {
            this.chapter = 2;
        } else {
            this.chapter = chapter;
        }
    }

    private void loadBookTableInformation(int bookId) {
        BookTableLoader loader = new BookTableLoader(getApplicationContext());
        loader.load(bookId);
        loader.setListener(new OnStorageListener() {
            @Override
            public void onEvent(final List list) {
                bookTableList = (List<BookTableData>)list;
            }
        });

        getSupportLoaderManager().initLoader(BookTableContract.LOADER_IDENTIFIER, null, loader);
    }

    private void loadBookContentInformation(int bookId) {
        BookContentLoader loader = new BookContentLoader(getApplicationContext());
        loader.load(bookId);
        loader.setListener(new OnStorageListener() {
            @Override
            public void onEvent(List list) {
                bookContentList = (List<BookContentData>)list;
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        loadBookContentInformation();
                    }
                });
            }
        });

        getSupportLoaderManager().initLoader(BookContentContract.LOADER_IDENTIFIER, null, loader);
    }

    private void loadBookContentInformation() {
        webView.loadData(bookContentList.get(chapter).getValue(), "text/html; charset=UTF-8", null);
    }
}
