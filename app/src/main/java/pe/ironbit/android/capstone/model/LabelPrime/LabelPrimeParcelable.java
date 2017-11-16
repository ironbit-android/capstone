package pe.ironbit.android.capstone.model.LabelPrime;

import android.os.Parcel;
import android.os.Parcelable;

public class LabelPrimeParcelable implements Parcelable {
    private LabelPrimeData data;

    public static final Parcelable.Creator<LabelPrimeParcelable> CREATOR
            = new Parcelable.Creator<LabelPrimeParcelable>() {
        @Override
        public LabelPrimeParcelable createFromParcel(Parcel parcel) {
            return new LabelPrimeParcelable(parcel);
        }

        @Override
        public LabelPrimeParcelable[] newArray(int size) {
            return new LabelPrimeParcelable[size];
        }
    };

    public LabelPrimeParcelable(LabelPrimeData data) {
        this.data = data;
    }

    public LabelPrimeParcelable(Parcel parcel) {
        LabelPrimeBuilder builder = new LabelPrimeBuilder();

        builder.setLabelId(parcel.readInt())
               .setName(parcel.readString());

        data = builder.build();
    }

    public LabelPrimeData getLocalPrime() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(data.getLabelId());
        parcel.writeString(data.getName());
    }
}
