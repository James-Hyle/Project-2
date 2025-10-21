package fa.dfa;

import fa.State;

public class DFAState extends State {

    private boolean isFinal;
    private boolean isStart;

    /**
     * Constructor for a DFA state, initializes to non-start and non-final
     * 
     * @param name is the label of state
     */
    public DFAState(String name) {
        super(name);
        this.isStart = false;
        this.isFinal = false;
    }

    /**
     * Sets this state as a final state of the DFA
     * 
     * @return true if successful
     */
    public boolean setFinalState() {
        return this.isFinal = true;
    }

    /**
     * Returns true if this state is a final state
     * 
     * @return true if final state
     */
    public boolean getFinalState() {
        return this.isFinal;
    }

    /**
     * Sets this state as the start state of the DFA
     * 
     * @return true if successful
     */
    public boolean setStartState() {
        return this.isStart = true;
    }

    /**
     * Removes this state's start designation, making it a normal state
     */
    public void removeStartState() {
        this.isStart = false;
    }

    /**
     * Gets whether this state is the start state
     * 
     * @return true if is the start state
     */
    public boolean getStartState() {
        return this.isStart;
    }
}