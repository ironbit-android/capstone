package pe.ironbit.android.capstone.model.BookContent;

public class BookContentBuilder {
    private int bookId;

    private int section;

    private String value;

    public BookContentBuilder() {
        clear();
    }

    public BookContentData build() {
        return BookContentFactory.create(bookId, section, value);
    }

    public void clear() {
        bookId = -1;
        section = -1;
        value = "";
    }

    public BookContentBuilder setBookId(int bookId) {
        this.bookId = bookId;
        return this;
    }

    public BookContentBuilder setSection(int section) {
        this.section = section;
        return this;
    }

    public BookContentBuilder setValue(String value) {
        this.value = value;
        return this;
    }
}
