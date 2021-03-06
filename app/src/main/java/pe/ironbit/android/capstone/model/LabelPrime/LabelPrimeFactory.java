package pe.ironbit.android.capstone.model.LabelPrime;

public class LabelPrimeFactory {
    private LabelPrimeFactory() {
    }

    public static LabelPrimeData create(int labelId, String labelName) {
        return new LabelPrimeData(labelId, labelName);
    }

    public static LabelPrimeParcelable create(LabelPrimeData data) {
        return new LabelPrimeParcelable(data);
    }
}
