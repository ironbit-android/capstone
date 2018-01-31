package pe.ironbit.android.capstone.data.broadcast.appwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeFactory;

public class BookNexusWidgetReceive {
    private BookPrimeData book;

    private Context context;

    private Intent intent;

    public BookNexusWidgetReceive(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        book = new BookPrimeData();
    }

    public boolean execute() {
        if (!TextUtils.equals(intent.getAction(), AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            return false;
        }

        book = BookPrimeFactory.create(intent.getParcelableExtra(BookNexusWidgetCore.BOOK_PRIME_DATA));
        return true;
    }

    public BookPrimeData getBook() {
        return book;
    }

    public int getBookId() {
        return book.getBookId();
    }

    public String getBookName() {
        return book.getName();
    }

    public String getBookAuthor() {
        return book.getAuthor();
    }
}
