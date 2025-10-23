package fa.nfa;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

/**
 * State machine that accepts or rejects input strings over a input language
 * 
 * @author James Hyle, Rebecca Berg
 */
public class NFA implements NFAInterface, Serializable {

    final short STATESSIZE = 5;
    final short ALPHABETSIZE = 3;
    StringBuilder sb = new StringBuilder();

    private Set<NFAState> states;
    private NFAState startState;
    private Set<NFAState> finalStates;
    private Set<Character> alphabet;

    public NFA() {
        this.states = new LinkedHashSet<>(STATESSIZE);
        this.finalStates = new LinkedHashSet<>(STATESSIZE);
        this.alphabet = new LinkedHashSet<>(ALPHABETSIZE);
    }

    @Override
    public boolean addState(String name) {
        // check states to prevent duplicate before creating and adding new state
        if (!states.contains(getState(name))) {
            NFAState s = new NFAState(name);
            return states.add(s);
        }
        return false;
    }

    @Override
    public boolean setFinal(String name) {
        NFAState s = getState(name);
        // check for valid state before setting it as a final state
        if (states.contains(s)) {
            s.setFinalState();
            return finalStates.add(s);
        }
        return false;
    }

    @Override
    public boolean setStart(String name) {
        NFAState temp = getState(name);
        // check for valid state
        if (states.contains(temp)) {
            // loop through all states to remove any previously set starting states before
            // setting new start
            states.forEach(state -> {
                if (isStart(state.getName()) && !state.equals(temp)) {
                    state.removeStartState();
                }
            });
            temp.setStartState();
            startState = temp;
            return true;
        }
        return false;
    }

    @Override
    public void addSigma(char symbol) {
        alphabet.add(symbol);
    }

    //TODO: rework accepts from DFA to NFA structure
    @Override
    public boolean accepts(String s) {
        // find set current state to start
        NFAState current = startState;
        // convert string to character array and loop through each character
        for (char c : s.toCharArray()) {
            // ensure each character is contained in the alphabet
            if (!alphabet.contains(c)) {
                return false;
            }
            // find the transitions for the current state
            HashMap<Character, NFAState> stateTransitions = delta.get(current);
            // if the character is a transition for the state, retrieve the new state and
            // set it as current
            if (stateTransitions.containsKey(c)) {
                current = stateTransitions.get(c);
            } else {
                return false;
            }
        }
        // return whether the current state is a final state after consuming array
        return current.getFinalState();
    }

    @Override
    public Set<NFAState> eClosure(NFAState s) {
        Set<NFAState> closures = new LinkedHashSet<>();
        Stack<NFAState> statesStack = new Stack<>();
        statesStack.push(s);
        while (!statesStack.isEmpty()) {
            NFAState current = statesStack.pop();
            closures.add(current);
            Set<NFAState> transitions = current.getTransitions('e');
            for (NFAState transition : transitions) {
                if (!statesStack.contains(transition)) {
                    statesStack.push(transition);
                }
            }
        }
        return closures;
    }

    @Override
    public Set<Character> getSigma() {
        return alphabet;
    }

    @Override
    public NFAState getState(String name) {
        // loop through all states until a matching state name is found and return that
        // state, otherwise return null
        for (NFAState state : states) {
            if (state.getName().equals(name))
                return state;
        }
        return null;
    }

    @Override
    public boolean isFinal(String name) {
        return getState(name).getFinalState();
    }

    @Override
    public boolean isStart(String name) {
        return getState(name).getStartState();
    }

    @Override
    public boolean addTransition(String fromState, Set<String> toState, char onSymb) {
        NFAState from = getState(fromState);
        Set<NFAState> to = new LinkedHashSet<>(STATESSIZE);
        for(String state: toState) {
            to.add(getState(state));
        }
        Character s = Character.valueOf(onSymb);

        if (from != null && !to.isEmpty() && alphabet.contains(onSymb)) {
            from.addTransitions(s, to);
            return true;
        }

        return false;
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.getTransitions(onSymb);
    }

    @Override
    public int maxCopies(String s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'maxCopies'");
    }

    //TODO: this can probably be a slightly reworked version of the DFA Accepts method
    @Override
    public boolean isDFA() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isNFA'");
    }

    @Override
    public String toString() {
        sb.append(stringBuilderHelper("Q = " + states.toString()));
        sb.append(stringBuilderHelper("\nSigma = " + alphabet.toString()));
        sb.append("\ndelta =\n");
        for (char c : alphabet) {
            sb.append(stringBuilderHelper("\t" + c));
        }
        states.forEach(state -> {
            sb.append("\n" + state.getName() + "\t");
            for (char c : alphabet) {
                sb.append(delta.get(state).get(c).toString() + "\t");
            }
        });
        sb.append("\nq0 = " + startState.toString());
        sb.append(stringBuilderHelper("\nF = " + finalStates.toString()) + "\n");

        return sb.toString();
    }

    /**
     * Helper method to format sets in toString by replacing brackets and commas
     * 
     * @param s
     * 
     * @return formatted string
     */
    private String stringBuilderHelper(String s) {
        return s.replace("[", "{ ").replace(",", "").replace("]", " }");
    }
}
