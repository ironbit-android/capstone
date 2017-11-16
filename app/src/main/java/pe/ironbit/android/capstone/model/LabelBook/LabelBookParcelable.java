package pe.ironbit.android.capstone.model.LabelBook;

import android.os.Parcel;
import android.os.Parcelable;

public class LabelBookParcelable implements Parcelable {
    private LabelBookData data;

    public static final Parcelable.Creator<LabelBookParcelable> CREATOR
            = new Parcelable.Creator<LabelBookParcelable>() {
        @Override
        public LabelBookParcelable createFromParcel(Parcel parcel) {
            return new LabelBookParcelable(parcel);
        }

        @Override
        public LabelBookParcelable[] newArray(int size) {
            return new LabelBookParcelable[size];
        }
    };

    public LabelBookParcelable(LabelBookData data) {
        this.data = data;
    }

    public LabelBookParcelable(Parcel parcel) {
        LabelBookBuilder builder = new LabelBookBuilder();

        builder.setLabelId(parcel.readInt())
               .setBookId(parcel.readInt());

        data = builder.build();
    }

    public LabelBookData getLabelBook() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(data.getLabelId());
        parcel.writeInt(data.getBookId());
    }
}
