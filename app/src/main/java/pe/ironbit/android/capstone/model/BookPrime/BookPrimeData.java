package pe.ironbit.android.capstone.model.BookPrime;

public class BookPrimeData {
    private int bookId;

    private String name;

    private String author;

    private String image;

    private String book;

    private BookPrimeStatus status;

    public BookPrimeData(int bookId, String name, String author, String image, String book, BookPrimeStatus status) {
        this.bookId = bookId;
        this.name = name;
        this.author = author;
        this.image = image;
        this.book = book;
        this.status = status;
    }

    public int getBookId() {
        return bookId;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public String getBook() {
        return book;
    }

    public BookPrimeStatus getStatus() {
        return status;
    }
}
