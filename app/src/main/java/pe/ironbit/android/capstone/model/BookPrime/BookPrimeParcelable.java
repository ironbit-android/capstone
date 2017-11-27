package pe.ironbit.android.capstone.model.BookPrime;

import android.os.Parcel;
import android.os.Parcelable;

public class BookPrimeParcelable implements Parcelable {
    private BookPrimeData data;

    public static final Parcelable.Creator<BookPrimeParcelable> CREATOR
            = new Parcelable.Creator<BookPrimeParcelable>() {
        @Override
        public BookPrimeParcelable createFromParcel(Parcel parcel) {
            return new BookPrimeParcelable(parcel);
        }

        @Override
        public BookPrimeParcelable[] newArray(int size) {
            return new BookPrimeParcelable[size];
        }
    };

    public BookPrimeParcelable(BookPrimeData data) {
        this.data = data;
    }

    public BookPrimeParcelable(Parcel parcel) {
        BookPrimeBuilder builder = new BookPrimeBuilder();

        builder.setBookId(parcel.readInt())
               .setName(parcel.readString())
               .setAuthor(parcel.readString())
               .setImage(parcel.readString())
               .setFile(parcel.readString())
               .setStatus(BookPrimeStatus.convertIntegerToStatus(parcel.readInt()));

        data = builder.build();
    }

    public BookPrimeData getBookPrime() {
        return data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(data.getBookId());
        parcel.writeString(data.getName());
        parcel.writeString(data.getAuthor());
        parcel.writeString(data.getImage());
        parcel.writeString(data.getFile());
        parcel.writeInt(BookPrimeStatus.convertStatusToInteger(data.getStatus()));
    }
}
