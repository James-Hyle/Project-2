package fa.dfa;

import fa.State;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class DFA implements DFAInterface {

    final short STATESSIZE = 5;
    final short ALPHABETSIZE = 3;
    StringBuilder sb = new StringBuilder();

    private Set<DFAState> states;
    private Set<DFAState> finalStates;
    private DFAState startState;
    private Set<Character> alphabet;
    private HashMap<DFAState, HashMap<Character, DFAState>> delta;

    public DFA() {
        this.states = new LinkedHashSet<>(STATESSIZE);
        this.alphabet = new LinkedHashSet<>(ALPHABETSIZE);
        this.delta = new HashMap<>();
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
        DFAState from = getState(fromState);
        DFAState to = getState(toState);
        if (from != null && to != null && alphabet.contains(onSymb)) {
            HashMap<Character, DFAState> transitions;
            if (delta.containsKey(from)) {
                transitions = delta.get(from);
                if (transitions.containsKey(onSymb)) {
                    transitions.replace(onSymb, to);
                }
                else {
                    transitions.put(onSymb, to);
                }
            }
            else {
                transitions = new HashMap<>();
                transitions.put(onSymb, to);
                delta.put(from, transitions);
            }
            return true;
        }
        return false;
    }

    @Override
    public DFA swap(char symb1, char symb2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'swap'");
    }

    public String toString() {
        sb.append(stringBuilderHelper("Q = " + states.toString()));
        sb.append(stringBuilderHelper("\nSigma = " + alphabet.toString()));
        sb.append("\ndelta =\n");
        for (char c : alphabet) {
            sb.append(stringBuilderHelper("\t" + c));
        }
        states.forEach(state -> {
            sb.append("\n" + state.getName() + "\t");
            HashMap<Character, DFAState> transitions = delta.get(state);
            for (char c : alphabet) {
                sb.append(transitions.get(c).toString() + "\t");
            }
        });
        sb.append("\nq0 = " + startState.toString());
        sb.append(stringBuilderHelper("\nF = " + finalStates.toString()) + "\n");

        return sb.toString();
    }

    // formats Q set, replaces bracket with curly brace, removes commas
    private String stringBuilderHelper(String s) {
        return s.replace("[", "{ ").replace(",", "").replace("]", " }");
    }

}
