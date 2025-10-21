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
    private Set<NFAState> startState;
    private Set<NFAState> finalStates;
    private Set<Character> alphabet;
    private HashMap<NFAState, HashMap<Character, NFAState>> delta;

    public NFA() {
        this.states = new LinkedHashSet<>(STATESSIZE);
        this.finalStates = new LinkedHashSet<>(STATESSIZE);
        this.alphabet = new LinkedHashSet<>(ALPHABETSIZE);
        this.delta = new HashMap<>();
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eClosure'");
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
    public boolean addTransition(String fromState, Set<String> toStates, char onSymb) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addTransition'");
    }

    @Override
    public boolean addTransition(String fromState, String toState, char onSymb) {
        NFAState from = getState(fromState);
        NFAState to = getState(toState);
        Character s = Character.valueOf(onSymb);

        if (from != null && to != null && alphabet.contains(onSymb)) {
            // access delta's inner map locally to maintain memory integrity
            HashMap<Character, NFAState> transitions;
            // search transition table for the from state, if found get its transitions, if
            // not create new transitions map
            if (delta.containsKey(from)) {
                transitions = delta.get(from);
                // search transitions for character, if found replace current transition if not
                // set new transition
                if (transitions.containsKey(s)) {
                    transitions.replace(s, to);
                } else {
                    transitions.put(s, to);
                }
            } else {
                transitions = new HashMap<>();
                transitions.put(s, to);
                delta.put(from, transitions);
            }
            return true;
        }
        return false;
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getToState'");
    }

    @Override
    public int maxCopies(String s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'maxCopies'");
    }

    @Override
    public NFA swap(char symb1, char symb2) {
        try {
            // create a deep copy of this NFA by serialization
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            ObjectOutputStream oOut = new ObjectOutputStream(bOut);
            oOut.writeObject(this);
            oOut.flush();
            oOut.close();

            ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());
            ObjectInputStream oIn = new ObjectInputStream(bIn);
            // copy NFA to new memory addresses, no references to original
            NFA newNFA = (NFA) oIn.readObject();
            oIn.close();

            for (NFAState state : newNFA.delta.keySet()) {
                // access deltas inner map locally to ensure memory integrity
                HashMap<Character, NFAState> transitions = newNFA.delta.get(state);
                if (transitions.containsKey(symb1) || transitions.containsKey(symb2)) {
                    NFAState state1 = transitions.get(symb1);
                    NFAState state2 = transitions.get(symb2);

                    // swap states for symb1
                    if (state1 != null) {
                        transitions.put(symb2, state1);
                    } else {
                        transitions.remove(symb2);
                    }

                    if (state2 != null) {
                        transitions.put(symb1, state2);
                    } else {
                        transitions.remove(symb1);
                    }
                }
            }
            return newNFA;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

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
