package pe.ironbit.android.capstone.model.BookPrime;

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
}
