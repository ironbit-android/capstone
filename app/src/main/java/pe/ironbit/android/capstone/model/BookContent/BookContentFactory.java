package pe.ironbit.android.capstone.model.BookContent;

import java.util.ArrayList;
import java.util.List;

public class BookContentFactory {
    private BookContentFactory() {
    }

    public static BookContentData create(int bookId, int section, String value) {
        return new BookContentData(bookId, section, value);
    }

    public static BookContentParcelable create(BookContentData data) {
        return new BookContentParcelable(data);
    }

    public static List<BookContentData> createBookContentDataList(List<BookContentParcelable> input) {
        if (input == null) {
            return new ArrayList<>();
        }

        List<BookContentData> output = new ArrayList<>();
        for (BookContentParcelable data : input) {
            output.add(data.getBookContent());
        }

        return output;
    }

    public static List<BookContentParcelable> createBookContentParcelableList(List<BookContentData> input) {
        if (input == null) {
             return new ArrayList<>();
        }

        List<BookContentParcelable> output = new ArrayList<>();
        for (BookContentData data : input) {
            output.add(create(data));
        }

        return output;
    }
}
