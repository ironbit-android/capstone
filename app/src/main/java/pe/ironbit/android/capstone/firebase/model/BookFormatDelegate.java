package pe.ironbit.android.capstone.firebase.model;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.epub.EpubReader;
import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.firebase.storage.StoragePath;
import pe.ironbit.android.capstone.firebase.storage.StorageService;
import pe.ironbit.android.capstone.generic.Action;
import pe.ironbit.android.capstone.model.BookContent.BookContentBuilder;
import pe.ironbit.android.capstone.model.BookContent.BookContentData;
import pe.ironbit.android.capstone.model.BookTable.BookTableBuilder;
import pe.ironbit.android.capstone.model.BookTable.BookTableData;

public class BookFormatDelegate extends ModelDelegate {
    private static final String TAG = BookFormatDelegate.class.getSimpleName();

    private int bookId;

    private List<BookTableData> bookTableDataList;

    private List<BookContentData> bookContentDataList;

    private Action<List<BookTableData>> bookTableAction;

    private Action<List<BookContentData>> bookContentAction;

    private StoragePath storagePath;

    public BookFormatDelegate(StorageService service, int bookId, String ePubFile) {
        super(Long.parseLong(service.getContext().getString(R.string.book_epub_file_size)));
        this.bookId = bookId;
        bookTableAction = null;
        bookContentAction = null;
        bookTableDataList = new ArrayList<>();
        bookContentDataList = new ArrayList<>();
        generateStoragePath(service, bookId, ePubFile);
    }

    public BookFormatDelegate(StorageService service,
                              int bookId,
                              String ePubFile,
                              Action<List<BookTableData>> bookTableAction,
                              Action<List<BookContentData>> bookContentAction) {
        super(Long.parseLong(service.getContext().getString(R.string.book_epub_file_size)));
        this.bookId = bookId;
        this.bookTableAction = bookTableAction;
        this.bookContentAction = bookContentAction;
        bookTableDataList = new ArrayList<>();
        bookContentDataList = new ArrayList<>();
        generateStoragePath(service, bookId, ePubFile);
    }

    public void setBookTableAction(Action<List<BookTableData>> action) {
        bookTableAction = action;
    }

    public void setBookContentAction(Action<List<BookContentData>> action) {
        bookContentAction = action;
    }

    public List<BookTableData> getBookTableDataList() {
        return bookTableDataList;
    }

    public List<BookContentData> getBookContentDataList() {
        return bookContentDataList;
    }

    @Override
    public StoragePath getStoragePath() {
        return storagePath;
    }

    @Override
    public void process(byte[] bytes) {
        try {
            BookTableBuilder bookTableBuilder = new BookTableBuilder();
            BookContentBuilder bookContentBuilder = new BookContentBuilder();

            Book book = new EpubReader().readEpub(new ByteArrayInputStream(bytes));
            List<TOCReference> list = book.getTableOfContents().getTocReferences();
            for (int index = 0; index < list.size(); ++index) {
                TOCReference reference = list.get(index);
                generateBookTableData(index, reference, bookTableBuilder);
                generateBookContentData(index, reference, bookContentBuilder);
            }

            if (bookTableAction != null) {
                bookTableAction.execute(bookTableDataList);
            }

            if (bookContentAction != null) {
                bookContentAction.execute(bookContentDataList);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void generateBookTableData(int index, TOCReference reference, BookTableBuilder builder) {
        builder.clear();
        builder.setBookId(bookId);
        builder.setSection(index);
        builder.setValue(reference.getTitle());
        bookTableDataList.add(builder.build());
    }

    private void generateBookContentData(int index, TOCReference reference, BookContentBuilder builder) {
        try {
            builder.clear();
            builder.setBookId(bookId);
            builder.setSection(index);
            builder.setValue(new String(reference.getResource().getData()));
            bookContentDataList.add(builder.build());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private void generateStoragePath(StorageService service, int bookId, String ePubFile) {
        Context context = service.getContext();
        storagePath = service.getStoragePath();
        storagePath.append(context.getString(R.string.book_folder_path_prefix) + String.valueOf(bookId))
                   .append(ePubFile);
    }
}
