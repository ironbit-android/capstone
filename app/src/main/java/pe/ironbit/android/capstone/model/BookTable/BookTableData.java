package pe.ironbit.android.capstone.model.BookTable;

public class BookTableData {
    int bookId;

    int section;

    String value;

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
}
