package pe.ironbit.android.capstone.screen.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.bumptech.glide.request.target.AppWidgetTarget;

import pe.ironbit.android.capstone.R;
import pe.ironbit.android.capstone.data.broadcast.appwidget.BookNexusWidgetReceive;
import pe.ironbit.android.capstone.firebase.image.ImageLoader;
import pe.ironbit.android.capstone.firebase.storage.StorageService;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeFactory;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeParcelable;
import pe.ironbit.android.capstone.screen.activity.ReaderActivity;

public class ReaderAppWidget extends AppWidgetProvider {
    private static final String TAG = ReaderAppWidget.class.getSimpleName();

    private static final int BOOK_ID_NULL = -1;

    private static BookPrimeData book = new BookPrimeData();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.reader_app_widget);

        views.setTextViewText(R.id.reader_app_widget_book_name, book.getName());
        views.setTextViewText(R.id.reader_app_widget_book_author, book.getAuthor());

        if (book.getBookId() == BOOK_ID_NULL) {
            views.setImageViewResource(R.id.reader_app_widget_book_image, R.mipmap.ic_launcher);
        } else {
            AppWidgetTarget widgetTarget = new AppWidgetTarget(context, R.id.reader_app_widget_book_image, views, appWidgetId);

            StorageService storageService = new StorageService(context);

            ImageLoader.init(storageService)
                       .load(book.getBookId())
                       .target(widgetTarget);

            BookPrimeParcelable parcelable = BookPrimeFactory.create(book);
            Intent configIntent = new Intent(context, ReaderActivity.class);
            configIntent.putExtra(ReaderActivity.BOOK_PRIME_DATA_KEY, parcelable);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
            views.setOnClickPendingIntent(R.id.reader_app_widget_main, pendingIntent);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        BookNexusWidgetReceive receive = new BookNexusWidgetReceive(context, intent);
        if (receive.execute()) {
            BookPrimeData book = receive.getBook();
            if (book.getBookId() != BOOK_ID_NULL) {
                this.book = book;

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
                ComponentName thisWidget = new ComponentName(context.getApplicationContext(), ReaderAppWidget.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
                if (appWidgetIds != null && appWidgetIds.length > 0) {
                    onUpdate(context, appWidgetManager, appWidgetIds);
                }
            }
        }
    }
}
