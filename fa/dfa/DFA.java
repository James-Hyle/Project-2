package fa.dfa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class DFA implements DFAInterface {

    private Set<DFAState> states;
    private Set<Character> alphabet;
    private Set<DFAState> finalStates;
    private Set<DFAState> startStates;
    private HashMap<DFA, DFAState> delta;

    public DFA() {
        this.states = new LinkedHashSet<>();
        this.alphabet = new LinkedHashSet<>();
        this.startStates = new HashSet<>();
        this.finalStates = new HashSet<>();
    }

    @Override
    public boolean addState(String name) {
        DFAState s = new DFAState(name);
        if (states.contains(getState(name))) {
            return false;
        }
        return states.add(s);
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
            if (state.getName().equals(name)) return state;
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
        StringBuilder sb = new StringBuilder();

        sb.append(formattedString("Q = " + states.toString()));
        sb.append(formattedString("\nSigma = " + alphabet.toString()));
        sb.append("\ndelta = ");
        sb.append("\nq0 = " + startStates.toString().replace("[", "").replace(",","").replace("]", ""));
        sb.append(formattedString("\nF = " + finalStates.toString()));

        return sb.toString();
    }

    private String formattedString(String s) {
        return s.replace("[", "{ ").replace(",", "").replace("]", " }");
    }
 
}
