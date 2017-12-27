package pe.ironbit.android.capstone.model.BookPrime;

import pe.ironbit.android.capstone.storage.contract.BookPrimeContract.BookPrimeEntry;

public class BookPrimeData {
    private int bookId;

    private String name;

    private String author;

    private String image;

    private String file;

    private BookPrimeStatus status;

    public BookPrimeData() {
        bookId = -1;
        name = "";
        author = "";
        image = "";
        file = "";
        status = BookPrimeStatus.Global;
    }

    public BookPrimeData(int bookId, String name, String author, String image, String file, BookPrimeStatus status) {
        this.bookId = bookId;
        this.name = name;
        this.author = author;
        this.image = image;
        this.file = file;
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

    public String getFile() {
        return file;
    }

    public BookPrimeStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getClass().getSimpleName())
               .append('{')
               .append(BookPrimeEntry.BOOK_ID).append('=').append(bookId).append(',')
               .append(BookPrimeEntry.BOOK_NAME).append('=').append(name).append(',')
               .append(BookPrimeEntry.BOOK_AUTHOR).append('=').append(author).append(',')
               .append(BookPrimeEntry.BOOK_IMAGE).append('=').append(image).append(',')
               .append(BookPrimeEntry.BOOK_FILE).append('=').append(file).append(',')
               .append(BookPrimeEntry.BOOK_STATUS).append('=').append(status)
               .append('}');

        return builder.toString();
    }
}
