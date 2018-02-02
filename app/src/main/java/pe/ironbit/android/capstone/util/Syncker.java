package pe.ironbit.android.capstone.util;

public class Syncker {
    private enum State {
        Init,
        Halt,
        Proceed
    }

    private State state;

    public Syncker() {
        state = State.Init;
    }

    public synchronized void halt() throws InterruptedException {
        if (state == State.Init) {
            state = State.Halt;
            wait();
        }
    }

    public synchronized void proceed() {
        if (state == State.Init) {
            state = State.Proceed;
        } else {
            notify();
        }
    }
}
