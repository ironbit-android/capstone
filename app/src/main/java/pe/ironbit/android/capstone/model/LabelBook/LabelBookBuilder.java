package pe.ironbit.android.capstone.model.LabelBook;

public class LabelBookBuilder {
    private int labelId;

    private int bookId;

    public LabelBookBuilder() {
        clear();
    }

    public LabelBookData build() {
        return LabelBookFactory.create(labelId, bookId);
    }

    void clear() {
        labelId = -1;
        bookId = -1;
    }

    public LabelBookBuilder setLabelId(int labelId) {
        this.labelId = labelId;
        return this;
    }

    public LabelBookBuilder setBookId(int bookId) {
        this.bookId = bookId;
        return this;
    }
}
