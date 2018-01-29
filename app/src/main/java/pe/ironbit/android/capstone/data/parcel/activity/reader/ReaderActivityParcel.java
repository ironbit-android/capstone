package pe.ironbit.android.capstone.data.parcel.activity.reader;

import android.os.Parcel;
import android.os.Parcelable;

import pe.ironbit.android.capstone.model.BookPrime.BookPrimeData;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeFactory;
import pe.ironbit.android.capstone.model.BookPrime.BookPrimeParcelable;

public class ReaderActivityParcel implements Parcelable {
    public static final String READER_ACTIVITY_KEY = "READER_ACTIVITY_KEY";

    private ReaderActivityData data;

    public static final Parcelable.Creator<ReaderActivityParcel> CREATOR
            = new Parcelable.Creator<ReaderActivityParcel>() {
        @Override
        public ReaderActivityParcel createFromParcel(Parcel parcel) {
            return new ReaderActivityParcel(parcel);
        }

        @Override
        public ReaderActivityParcel[] newArray(int size) {
            return new ReaderActivityParcel[size];
        }
    };

    public ReaderActivityParcel(ReaderActivityData data) {
        this.data = data;
    }

    public ReaderActivityParcel(Parcel parcel) {
        data = new ReaderActivityData();
        data.setFirstChapter(parcel.readInt());
        data.setLastChapter(parcel.readInt());
        data.setCurrentChapter(parcel.readInt());
        data.setCurrentBook(((BookPrimeParcelable)parcel.readParcelable(BookPrimeData.class.getClassLoader())).getBookPrime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(data.getFirstChapter());
        parcel.writeInt(data.getLastChapter());
        parcel.writeInt(data.getCurrentChapter());
        parcel.writeParcelable(BookPrimeFactory.create(data.getCurrentBook()), flags);
    }

    public ReaderActivityData getData() {
        return data;
    }
}
