package pe.ironbit.android.capstone.tools.download;

import android.app.Activity;
import android.os.Handler;

import java.util.List;

import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.firebase.model.BookFormatDelegate;
import pe.ironbit.android.capstone.firebase.storage.StorageService;
import pe.ironbit.android.capstone.generic.Action;
import pe.ironbit.android.capstone.model.BookContent.BookContentData;
import pe.ironbit.android.capstone.model.BookContent.BookContentMapper;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookTable.BookTableData;
import pe.ironbit.android.capstone.model.BookTable.BookTableMapper;

public class BookDownloader {
    private int state;

    private int position;

    private Activity activity;

    private StorageService service;

    private Action<Integer> action;

    private List<BookTableData> bookTableDataList;

    private List<BookContentData> bookContentDataList;

    private boolean isDownloadCancelled;

    private int delay;

    public BookDownloader(Activity activity, int position) {
        this.position = position;
        this.activity = activity;
        isDownloadCancelled = false;
        service = new StorageService(activity.getApplicationContext());
        delay = activity.getResources().getInteger(R.integer.book_download_time_seconds);
    }

    public void doOnDownloadFinished(Action<Integer> action) {
        this.action = action;
    }

    public void process(final BookPrimeData book) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                state = 0;
                BookFormatDelegate delegate = new BookFormatDelegate(service, book.getBookId(), book.getFile());

                delegate.setBookTableAction(new Action<List<BookTableData>>() {
                    @Override
                    public void execute(final List<BookTableData> bookTableData) {
                        bookTableDataList = bookTableData;
                        finishDownload();
                    }
                });

                delegate.setBookContentAction(new Action<List<BookContentData>>() {
                    @Override
                    public void execute(final List<BookContentData> bookContentData) {
                        bookContentDataList = bookContentData;
                        finishDownload();
                    }
                });

                service.process(delegate);
            }
        }, delay);
    }

    public synchronized boolean downloadCancelState(Boolean value) {
        if (value != null) {
            isDownloadCancelled = value;
        }
        return isDownloadCancelled;
    }

    private synchronized void finishDownload() {
        if (++state == 2) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    if (!downloadCancelState(null)) {
                        storeDownloadInformation();
                        action.execute(position);
                    }
                }
            });
        }
    }

    private void storeDownloadInformation() {
        for (BookTableData data : bookTableDataList) {
            BookTableMapper.insert(activity.getContentResolver(), data);
        }
        for (BookContentData data : bookContentDataList) {
            BookContentMapper.insert(activity.getContentResolver(), data);
        }
    }
}
