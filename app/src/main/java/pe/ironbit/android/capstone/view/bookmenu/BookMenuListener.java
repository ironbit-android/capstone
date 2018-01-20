package pe.ironbit.android.capstone.view.bookmenu;

public interface BookMenuListener {
    enum ClickType {
        Short,
        Long
    }

    void update(int position, ClickType type);
}
