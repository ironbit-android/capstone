package pe.ironbit.android.capstone.model.LabelPrime;

public class LabelPrimeBuilder {
    private int labelId;

    private String name;

    public LabelPrimeBuilder() {
        clear();
    }

    public LabelPrimeData build() {
        return LabelPrimeFactory.create(labelId, name);
    }

    public void clear() {
        labelId = -1;
        name = "";
    }

    public LabelPrimeBuilder setLabelId(int labelId) {
        this.labelId = labelId;
        return this;
    }

    public LabelPrimeBuilder setName(String name) {
        this.name = name;
        return this;
    }
}
