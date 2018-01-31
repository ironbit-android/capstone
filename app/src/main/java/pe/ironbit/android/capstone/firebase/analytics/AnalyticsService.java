package pe.ironbit.android.capstone.firebase.analytics;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsService {
    private static final String BOOK_ID = "book_id";

    private static final String BOOK_CHAPTER = "book_chapter";

    private static final String BOOK_EVENT = "book_event";

    private FirebaseAnalytics firebaseAnalytics;

    private Bundle bundle;

    public AnalyticsService(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        bundle = new Bundle();
    }

    public AnalyticsService setCurrentBook(int bookId) {
        bundle.putInt(BOOK_ID, bookId);
        return this;
    }

    public AnalyticsService setCurrentChapter(int chapter) {
        bundle.putInt(BOOK_CHAPTER, chapter);
        return this;
    }

    public void logEvent() {
        firebaseAnalytics.logEvent(BOOK_EVENT, bundle);
        bundle = new Bundle();
    }
}
