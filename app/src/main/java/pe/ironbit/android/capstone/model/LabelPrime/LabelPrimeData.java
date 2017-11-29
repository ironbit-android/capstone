package pe.ironbit.android.capstone.model.LabelPrime;

import pe.ironbit.android.capstone.storage.contract.LabelPrimeContract.LabelPrimeEntry;

public class LabelPrimeData {
    private int labelId;

    private String labelName;

    public LabelPrimeData(int labelId, String labelName) {
        this.labelId = labelId;
        this.labelName = labelName;
    }

    public int getLabelId() {
        return labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(LabelPrimeEntry.LABEL_ID).append(':').append(labelId).append(',')
               .append(LabelPrimeEntry.LABEL_NAME).append(':').append(labelName).append(';');

        return builder.toString();
    }
}
