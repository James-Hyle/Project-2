package fa.dfa;

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
public class DFA implements DFAInterface, Serializable {

    final short STATESSIZE = 5;
    final short ALPHABETSIZE = 3;
    StringBuilder sb = new StringBuilder();

    private Set<DFAState> states;
    private DFAState startState;
    private Set<DFAState> finalStates;
    private Set<Character> alphabet;
    private HashMap<DFAState, HashMap<Character, DFAState>> delta;

    public DFA() {
        this.states = new LinkedHashSet<>(STATESSIZE);
        this.finalStates = new LinkedHashSet<>(STATESSIZE);
        this.alphabet = new LinkedHashSet<>(ALPHABETSIZE);
        this.delta = new HashMap<>();
    }

    @Override
    public boolean addState(String name) {
        if (!states.contains(getState(name))) {
            DFAState s = new DFAState(name);
            return states.add(s);
        }
        return false;
    }

    @Override
    public boolean setFinal(String name) {
        DFAState s = getState(name);
        if (states.contains(s)) {
            s.setFinalState();
            return finalStates.add(s);
        }
        return false;
    }

    @Override
    public boolean setStart(String name) {
        if (states.contains(getState(name))) {
            states.forEach(state -> {
                if (isStart(state.getName()) && !state.equals(getState(name))) {
                    state.removeStartState();
                }
            });
            getState(name).setStartState();
            startState = states.stream().filter(state -> state.equals(getState(name))).findFirst().get();
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
        DFAState current = states.stream().filter(state -> isStart(state.getName())).findFirst().get();
        for (char c : s.toCharArray()) {
            if (!alphabet.contains(c)) {
                return false;
            }
            HashMap<Character, DFAState> stateTransitions = delta.get(current);
            if (stateTransitions.containsKey(c)) {
                current = stateTransitions.get(c);
            } else {
                return false;
            }
        }
        return current.getFinalState();
    }

    @Override
    public Set<Character> getSigma() {
        return alphabet;
    }

    @Override
    public DFAState getState(String name) {
        for (DFAState state : states) {
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
    public boolean addTransition(String fromState, String toState, char onSymb) {
        DFAState from = getState(fromState);
        DFAState to = getState(toState);
        Character s = Character.valueOf(onSymb);

        if (from != null && to != null && alphabet.contains(onSymb)) {
            // access delta's inner map locally to maintain memory integrity
            HashMap<Character, DFAState> transitions;
            if (delta.containsKey(from)) {
                transitions = delta.get(from);
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
    public DFA swap(char symb1, char symb2) {
        try {
            // create a deep copy of this DFA by serialization
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            ObjectOutputStream oOut = new ObjectOutputStream(bOut);
            oOut.writeObject(this);
            oOut.flush();
            oOut.close();

            ByteArrayInputStream bIn = new ByteArrayInputStream(bOut.toByteArray());
            ObjectInputStream oIn = new ObjectInputStream(bIn);
            // copy DFA to new memory addresses, no references to original
            DFA newDFA = (DFA) oIn.readObject();
            oIn.close();

            for (DFAState state : newDFA.delta.keySet()) {
                // access deltas inner map locally to ensure memory integrity
                HashMap<Character, DFAState> transitions = newDFA.delta.get(state);
                if (transitions.containsKey(symb1) || transitions.containsKey(symb2)) {
                    DFAState state1 = transitions.get(symb1);
                    DFAState state2 = transitions.get(symb2);

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
            return newDFA;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
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
     * Helper method to format sets in toString by replaceing brackets and commas
     * 
     * @param s
     * 
     * @return formatted string
     */
    private String stringBuilderHelper(String s) {
        return s.replace("[", "{ ").replace(",", "").replace("]", " }");
    }

}
