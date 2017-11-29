package pe.ironbit.android.capstone.model.LabelPrime;

public class LabelPrimeBuilder {
    private int labelId;

    private String labelName;

    public LabelPrimeBuilder() {
        clear();
    }

    public LabelPrimeData build() {
        return LabelPrimeFactory.create(labelId, labelName);
    }

    public void clear() {
        labelId = -1;
        labelName = "";
    }

    public LabelPrimeBuilder setLabelId(int labelId) {
        this.labelId = labelId;
        return this;
    }

    public LabelPrimeBuilder setLabelName(String labelName) {
        this.labelName = labelName;
        return this;
    }
}
