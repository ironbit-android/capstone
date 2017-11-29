package pe.ironbit.android.capstone.model.LabelBook;

import pe.ironbit.android.capstone.storage.contract.LabelBookContract.LabelBookEntry;

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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(LabelBookEntry.LABEL_ID).append(':').append(labelId).append(',')
               .append(LabelBookEntry.BOOK_ID).append(':').append(bookId).append(';');

        return builder.toString();
    }
}
