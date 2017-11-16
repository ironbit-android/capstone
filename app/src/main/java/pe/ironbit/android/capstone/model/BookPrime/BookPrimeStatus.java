package pe.ironbit.android.capstone.model.BookPrime;

public enum BookPrimeStatus {
    Null,
    Local,
    Global;

    public static final int convertStatusToInteger(BookPrimeStatus status) {
        return status.ordinal();
    }

    public static final BookPrimeStatus convertIntegerToStatus(final int index) {
        if (index == 1) {
            return BookPrimeStatus.Local;
        }
        if (index == 2) {
            return BookPrimeStatus.Global;
        }
        return BookPrimeStatus.Null;
    }
}
