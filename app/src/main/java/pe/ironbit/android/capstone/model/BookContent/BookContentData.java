package pe.ironbit.android.capstone.model.BookContent;

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
}