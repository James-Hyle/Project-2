package fa.nfa;

import fa.State;

import java.util.LinkedHashMap;
import java.util.Set;

public class NFAState extends State {

    private boolean isFinal;
    private boolean isStart;
    private LinkedHashMap<Character, Set<NFAState>> delta;
    //

    /**
     * Constructor for a DFA state, initializes to non-start and non-final
     * 
     * @param name is the label of state
     */
    public NFAState(String name) {
        super(name);
        this.isStart = false;
        this.isFinal = false;
        delta = new LinkedHashMap<>();
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

    /**
     * Gets the transitions for a particular character
     *
     * @return list of to states
     */
    public Set<NFAState> getTransitions(char c) { return delta.get(c); }

    /**
     * Set the transitions for the state
     */
    public void addTransitions(char c, Set<NFAState> s) { delta.put(c, s); }
}