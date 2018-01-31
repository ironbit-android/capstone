package pe.ironbit.android.capstone.data.broadcast.appwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeFactory;

public class BookNexusWidgetSend {
    private BookPrimeData book;

    private Context context;

    public BookNexusWidgetSend(Context context) {
        this.context = context;
        book = new BookPrimeData();
    }

    public void setData(BookPrimeData book) {
        this.book = book;
    }

    public void execute() {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        intent.putExtra(BookNexusWidgetCore.BOOK_PRIME_DATA, BookPrimeFactory.create(book));

        context.sendBroadcast(intent);
    }
}
