package pe.ironbit.android.capstone.model.BookContent;

public class BookContentFactory {
    private BookContentFactory() {
    }

    public static BookContentData create(int bookId, int section, String value) {
        return new BookContentData(bookId, section, value);
    }
}
