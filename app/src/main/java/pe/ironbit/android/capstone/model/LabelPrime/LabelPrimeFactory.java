package pe.ironbit.android.capstone.model.LabelPrime;

public class LabelPrimeFactory {
    private LabelPrimeFactory() {
    }

    public static LabelPrimeData create(int labelId, String name) {
        return new LabelPrimeData(labelId, name);
    }
}
