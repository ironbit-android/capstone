package pe.ironbit.android.capstone.model.BookContent;

import pe.ironbit.android.capstone.storage.contract.BookContentContract.BookContentEntry;

public class BookContentData {
    private int bookId;

    private int section;

    private String value;

    public BookContentData(int bookId, int section, String value) {
        this.bookId = bookId;
        this.section = section;
        this.value = value;
    }

    public int getBookId() {
        return bookId;
    }

    public int getSection() {
        return section;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(BookContentEntry.BOOK_ID).append(':').append(bookId).append(',')
               .append(BookContentEntry.BOOK_SECTION).append(':').append(section).append(',')
               .append(BookContentEntry.BOOK_VALUE).append(':').append(value).append(';');

        return builder.toString();
    }
}
