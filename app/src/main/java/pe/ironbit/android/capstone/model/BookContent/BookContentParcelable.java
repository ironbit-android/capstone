package pe.ironbit.android.capstone.model.BookContent;

import android.os.Parcel;
import android.os.Parcelable;

public class BookContentParcelable implements Parcelable {
    private BookContentData data;

    public static final Parcelable.Creator<BookContentParcelable> CREATOR
            = new Parcelable.Creator<BookContentParcelable>() {
        @Override
        public BookContentParcelable createFromParcel(Parcel parcel) {
            return new BookContentParcelable(parcel);
        }

        @Override
        public BookContentParcelable[] newArray(int size) {
            return new BookContentParcelable[size];
        }
    };

    public BookContentParcelable(BookContentData data) {
        this.data = data;
    }

    public BookContentParcelable(Parcel parcel) {
        BookContentBuilder builder = new BookContentBuilder();

        builder.setBookId(parcel.readInt())
               .setSection(parcel.readInt())
               .setValue(parcel.readString());

        data = builder.build();
    }

    public BookContentData getBookContent() {
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
