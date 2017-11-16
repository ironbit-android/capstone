package pe.ironbit.android.capstone.model.LabelPrime;

public class LabelPrimeData {
    private int labelId;

    private String name;

    public LabelPrimeData(int labelId, String name) {
        this.labelId = labelId;
        this.name = name;
    }

    public int getLabelId() {
        return labelId;
    }

    public String getName() {
        return name;
    }
}
