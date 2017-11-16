package pe.ironbit.android.capstone.model.BookTable;

public class BookTableFactory {
    private BookTableFactory() {
    }

    public static BookTableData create(int bookId, int section, String value) {
        return new BookTableData(bookId, section, value);
    }
}
