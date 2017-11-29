package pe.ironbit.android.capstone.model.BookTable;

import pe.ironbit.android.capstone.storage.contract.BookTableContract.BookTableEntry;

public class BookTableData {
    private int bookId;

    private int section;

    private String value;

    public BookTableData(int bookId, int section, String value) {
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

        builder.append(BookTableEntry.BOOK_ID).append(':').append(bookId).append(',')
               .append(BookTableEntry.BOOK_SECTION).append(':').append(section).append(',')
               .append(BookTableEntry.BOOK_VALUE).append(':').append(value).append(';');

        return builder.toString();
    }
}
