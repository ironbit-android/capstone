package pe.ironbit.android.capstone.model.LabelBook;

public class LabelBookData {
    private int labelId;

    private int bookId;

    public LabelBookData(int labelId, int bookId) {
        this.labelId = labelId;
        this.bookId = bookId;
    }

    public int getLabelId() {
        return labelId;
    }

    public int getBookId() {
        return bookId;
    }
}
