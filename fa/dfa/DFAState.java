package fa.dfa;

import fa.State;

public class DFAState extends State {

    private boolean isFinal;
    private boolean isStart;

    public DFAState(String name) {
        super(name);
        this.isStart = false;
        this.isFinal = false;
    }

    public boolean setFinalState() {
        return this.isFinal = true;
    }

    public boolean getFinalState() {
        return this.isFinal;
    }

    public boolean setStartState() {
        return this.isStart = true;
    }

    public void removeStartState() {
        this.isStart = false;
    }

    public boolean getStartState() {
        return this.isStart;
    }
}