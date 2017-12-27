package pe.ironbit.android.capstone.generic;

public interface Action<Outcome> {
    void execute(Outcome outcome);
}
