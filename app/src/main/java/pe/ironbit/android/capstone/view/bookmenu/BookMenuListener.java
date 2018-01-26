package pe.ironbit.android.capstone.view.bookmenu;

public interface BookMenuListener {
    enum ClickType {
        Short,
        Long,
        Cancel
    }

    void update(int position, ClickType type);
}
