package pe.ironbit.android.capstone.view.managerlabel;

public interface ManagerLabelListener {
    enum Action {
        Edit,
        Erase
    }

    void update(Integer position, Action action);
}
