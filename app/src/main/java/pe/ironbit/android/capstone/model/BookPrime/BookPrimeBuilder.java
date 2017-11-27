package pe.ironbit.android.capstone.model.BookPrime;

public class BookPrimeBuilder {
    private int bookId;

    private String name;

    private String author;

    private String image;

    private String file;

    private BookPrimeStatus status;

    public BookPrimeBuilder() {
        clear();
    }

    public BookPrimeData build() {
        return BookPrimeFactory.create(bookId, name, author, image, file, status);
    }

    public void clear() {
        bookId = -1;
        name = "";
        author = "";
        image = "";
        file = "";
        status = BookPrimeStatus.Null;
    }

    public BookPrimeBuilder setBookId(int bookId) {
        this.bookId = bookId;
        return this;
    }

    public BookPrimeBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public BookPrimeBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public BookPrimeBuilder setImage(String image) {
        this.image = image;
        return this;
    }

    public BookPrimeBuilder setFile(String file) {
        this.file = file;
        return this;
    }

    public BookPrimeBuilder setStatus(BookPrimeStatus status) {
        this.status = status;
        return this;
    }
}
