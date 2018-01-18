package pe.ironbit.android.capstone.model.BookPrime;

import java.util.ArrayList;
import java.util.List;

public class BookPrimeFactory {
    private  BookPrimeFactory() {
    }

    public static BookPrimeData create(int bookId,
                                       String name,
                                       String author,
                                       String image,
                                       String file,
                                       BookPrimeStatus status) {
        return new BookPrimeData(bookId, name, author, image, file, status);
    }

    public static BookPrimeParcelable create(BookPrimeData data) {
        return new BookPrimeParcelable(data);
    }

    public static List<BookPrimeData> createBookPrimeDataList(List<BookPrimeParcelable> input) {
        if (input == null) {
            return null;
        }

        List<BookPrimeData> output = new ArrayList<>();
        for (BookPrimeParcelable data : input) {
            output.add(data.getBookPrime());
        }

        return output;
    }

    public static List<BookPrimeParcelable> createBookPrimeParcelableList(List<BookPrimeData> input) {
        if (input == null) {
            return null;
        }

        ArrayList<BookPrimeParcelable> output = new ArrayList<>();
        for (BookPrimeData data : input) {
            output.add(create(data));
        }

        return output;
    }
}
