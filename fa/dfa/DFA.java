package fa.dfa;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class DFA implements DFAInterface {

    final short STATESSIZE = 5;
    final short ALPHABETSIZE = 3;
    StringBuilder sb = new StringBuilder();

    private Set<DFAState> states;
    private Set<DFAState> finalStates;
    private Set<DFAState> startStates;
    private Set<Character> alphabet;
    private HashMap<DFAState, Character> delta;

    public DFA() {
        this.states = new LinkedHashSet<>(STATESSIZE);
        this.alphabet = new LinkedHashSet<>(ALPHABETSIZE);
        this.delta = new HashMap<>();
        this.startStates = new LinkedHashSet<>(STATESSIZE);
        this.finalStates = new LinkedHashSet<>(STATESSIZE);
    }

    @Override
    public boolean addState(String name) {
        DFAState s = new DFAState(name);
        if (!states.contains(getState(name))) {
            return states.add(s);
        }
        return false;
    }

    @Override
    public boolean setFinal(String name) {
        if (states.contains(getState(name))) {
            getState(name).setFinalState();
            return finalStates.add(getState(name));
        }
        return false;
    }

    @Override
    public boolean setStart(String name) {
        if (states.contains(getState(name))) {
            getState(name).setStartState();
            return startStates.add(getState(name));
        }
        return false;
    }

    @Override
    public void addSigma(char symbol) {
        alphabet.add(symbol);
    }

    @Override
    public boolean accepts(String s) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accepts'");
    }

    @Override
    public Set<Character> getSigma() {
        LinkedHashSet<Character> sigma = new LinkedHashSet<>(alphabet);
        return sigma;
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addTransition'");
    }

    @Override
    public DFA swap(char symb1, char symb2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'swap'");
    }

    public String toString() {
        sb.append(stringBuilderHelper("Q = " + states.toString()));
        sb.append(stringBuilderHelper("\nSigma = " + alphabet.toString()));
        // TODO: Delta
        sb.append("\ndelta = ");
        sb.append("\nq0 = " + startStates.toString().replace("[", "").replace(",", "").replace("]", ""));
        sb.append(stringBuilderHelper("\nF = " + finalStates.toString()));

        return sb.toString();
    }

    // formats set, replaces bracket with curly brace, removes commas
    private String stringBuilderHelper(String s) {
        return s.replace("[", "{ ").replace(",", "").replace("]", " }");
    }

}
