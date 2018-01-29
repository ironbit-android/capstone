package pe.ironbit.android.capstone.model.BookTable;

import java.util.ArrayList;
import java.util.List;

public class BookTableFactory {
    private BookTableFactory() {
    }

    public static BookTableData create(int bookId, int section, String value) {
        return new BookTableData(bookId, section, value);
    }

    public static BookTableParcelable create(BookTableData data) {
        return new BookTableParcelable(data);
    }

    public static List<BookTableData> createBookTableDataList(List<BookTableParcelable> input) {
        if (input == null) {
            return new ArrayList<>();
        }

        List<BookTableData> output = new ArrayList<>();
        for (BookTableParcelable data : input) {
            output.add(data.getBookTable());
        }

        return output;
    }

    public static List<BookTableParcelable> createBookTableParcelableList(List<BookTableData> input) {
        if (input == null) {
            return new ArrayList<>();
        }

        List<BookTableParcelable> output = new ArrayList<>();
        for (BookTableData data : input) {
            output.add(create(data));
        }

        return output;
    }
}
