package pe.ironbit.android.capstone.model.Library;

import java.util.ArrayList;

public class LibraryData {
    private int total;

    private ArrayList<String> books;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<String> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<String> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(getClass().getSimpleName())
               .append('{')
               .append("total=").append(total).append(',');

        for (String book : books) {
            builder.append("book=").append(book).append(',');
        }

        builder.setCharAt(builder.length() - 1, '}');

        return builder.toString();
    }
}
