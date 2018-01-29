package pe.ironbit.android.capstone.data.shared;

import android.content.SharedPreferences;

public class BookSharedData {
    private static final int NULL_VALUE = -1;

    private static final String SHARED_BOOK_ID_KEY = "SHARED_BOOK_ID_KEY";

    private static final String SHARED_CURRENT_CHAPTER_KEY = "SHARED_CURRENT_CHAPTER_KEY";

    private int bookId;

    private int currentChapter;

    private SharedPreferences preferences;

    public BookSharedData(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void load() {
        bookId = preferences.getInt(SHARED_BOOK_ID_KEY, NULL_VALUE);
        currentChapter = preferences.getInt(SHARED_CURRENT_CHAPTER_KEY, NULL_VALUE);
    }

    public void save() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(SHARED_BOOK_ID_KEY, bookId);
        editor.putInt(SHARED_CURRENT_CHAPTER_KEY, currentChapter);
        editor.commit();
    }

    public boolean isBookIdValid() {
        return bookId != NULL_VALUE;
    }

    public boolean isCurrentChapterValid() {
        return currentChapter != NULL_VALUE;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getCurrentChapter() {
        return currentChapter;
    }

    public void setCurrentChapter(int currentChapter) {
        this.currentChapter = currentChapter;
    }

    @Override
    public String toString() {
        return "BookSharedData:{" +
                "bookId=" + bookId +
                ", currentChapter=" + currentChapter +
                '}';
    }
}
