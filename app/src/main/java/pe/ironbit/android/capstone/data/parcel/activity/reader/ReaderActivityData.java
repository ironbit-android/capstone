package pe.ironbit.android.capstone.data.parcel.activity.reader;

import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeParcelable;

public class ReaderActivityData {
    private int firstChapter;

    private int lastChapter;

    private int currentChapter;

    private BookPrimeData currentBook;

    public ReaderActivityData() {
    }

    public int getFirstChapter() {
        return firstChapter;
    }

    public void setFirstChapter(int firstChapter) {
        this.firstChapter = firstChapter;
    }

    public int getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(int lastChapter) {
        this.lastChapter = lastChapter;
    }

    public int getCurrentChapter() {
        return currentChapter;
    }

    public void setCurrentChapter(int currentChapter) {
        this.currentChapter = currentChapter;
    }

    public BookPrimeData getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(BookPrimeData currentBook) {
        this.currentBook = currentBook;
    }

    public void setCurrentBook(BookPrimeParcelable currentBook) {
        this.currentBook = currentBook.getBookPrime();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("\"ReaderActivityData\":{")
               .append("\"firstChapter\":").append(firstChapter).append(",")
               .append("\"lastChapter\":").append(lastChapter).append(",")
               .append("\"currentChapter\":").append(currentChapter).append(",")
               .append(currentBook.toString())
               .append("}");

        return builder.toString();
    }
}
