package pe.ironbit.android.capstone.model.BookTable;

public class BookTableBuilder {
    private int bookId;

    private int section;

    private String value;

    public BookTableBuilder() {
        clear();
    }

    public BookTableData build() {
        return BookTableFactory.create(bookId, section, value);
    }

    public void clear() {
        bookId = -1;
        section = -1;
        value = "";
    }

    public BookTableBuilder setBookId(int bookId) {
        this.bookId = bookId;
        return this;
    }

    public BookTableBuilder setSection(int section) {
        this.section = section;
        return this;
    }

    public BookTableBuilder setValue(String value) {
        this.value = value;
        return this;
    }
}
