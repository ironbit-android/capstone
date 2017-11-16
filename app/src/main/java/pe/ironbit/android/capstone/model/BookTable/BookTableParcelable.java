package pe.ironbit.android.capstone.model.BookTable;

import android.os.Parcel;
import android.os.Parcelable;

public class BookTableParcelable implements Parcelable {
    private BookTableData data;

    public static final Parcelable.Creator<BookTableParcelable> CREATOR
            = new Parcelable.Creator<BookTableParcelable>() {
        @Override
        public BookTableParcelable createFromParcel(Parcel parcel) {
            return new BookTableParcelable(parcel);
        }

        @Override
        public BookTableParcelable[] newArray(int size) {
            return new BookTableParcelable[size];
        }
    };

    public BookTableParcelable(BookTableData data) {
        this.data = data;
    }

    public BookTableParcelable(Parcel parcel) {
        BookTableBuilder builder = new BookTableBuilder();

        builder.setBookId(parcel.readInt())
               .setSection(parcel.readInt())
               .setValue(parcel.readString());

        data = builder.build();
    }

    public BookTableData getBookTable() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(data.getBookId());
        parcel.writeInt(data.getSection());
        parcel.writeString(data.getValue());
    }
}
