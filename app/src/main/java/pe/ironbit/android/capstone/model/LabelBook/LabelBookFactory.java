package pe.ironbit.android.capstone.model.LabelBook;

public class LabelBookFactory {
    private LabelBookFactory() {
    }

    public static LabelBookData create(int labelId, int bookId) {
        return new LabelBookData(labelId, bookId);
    }

    public static LabelBookParcelable create(LabelBookData data) {
        return new LabelBookParcelable(data);
    }
}
