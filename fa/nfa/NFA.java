package fa.nfa;

import java.io.Serializable;
import java.util.*;

/**
 * State machine that accepts or rejects input strings over a input language
 * 
 * @author James Hyle, Rebecca Berg
 */
public class NFA implements NFAInterface, Serializable {

    final short STATESSIZE = 10;
    final short ALPHABETSIZE = 10;
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
        if (symbol != 'e') {
            alphabet.add(symbol);
        }
    }

    @Override
    public boolean accepts(String s) {
        // start epsilon-closure of states
        Set<NFAState> currentStates = new HashSet<>();
        currentStates.addAll(eClosure(startState));
        if (s.isEmpty()) {
            for (NFAState state : currentStates) {
                if (this.finalStates.contains(state)) {
                    return true;
                }
            }
            return false;
        }
        // iterate through each char in input string
        for (char symbol : s.toCharArray()) {
            Set<NFAState> nextStates = new HashSet<>();
            Queue<NFAState> queue = new LinkedList<>(currentStates);
            while (!queue.isEmpty()) {
                NFAState current = queue.poll();
                Set<NFAState> transitions = getToState(current, symbol);
                if (transitions != null && !transitions.isEmpty()) {
                    nextStates.addAll(transitions);
                }
            }
            // e-closure after processing char
            currentStates = setEClosure(nextStates);
        }
        // check if any state is final state
        for (NFAState state : currentStates) {
            if (this.finalStates.contains(state)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Set<NFAState> eClosure(NFAState s) {
        Set<NFAState> closures = new LinkedHashSet<>();
        // create stack to hold reachable states
        Stack<NFAState> statesStack = new Stack<>();
        statesStack.push(s);
        // until every reachable state has been checked loop
        while (!statesStack.isEmpty()) {
            NFAState current = statesStack.pop();
            // add state if not already in closures
            closures.add(current);
            Set<NFAState> transitions = current.getTransitions('e');
            // loop through valid epsilon transitions if any and add to the stack
            if (transitions != null) {
                for (NFAState transition : transitions) {
                    if (!statesStack.contains(transition)) {
                        statesStack.push(transition);
                    }
                }
            }
        }
        return closures;
    }

    /**
     * find all e-closures of input set of states
     * 
     * @param s
     * @return e-closures of states
     */
    public Set<NFAState> setEClosure(Set<NFAState> s) {
        Set<NFAState> closures = new HashSet<>();
        for (NFAState nS : s) {
            closures.addAll(eClosure(nS));
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
        if (!finalStates.contains(getState(name))) {
            return false;
        }
        return getState(name).getFinalState();
    }

    @Override
    public boolean isStart(String name) {
        if (!states.contains(getState(name))) {
            return false;
        }
        return getState(name).getStartState();
    }

    @Override
    public boolean addTransition(String fromState, Set<String> toState, char onSymb) {
        NFAState from = getState(fromState);
        // if from state isn't in system stop and return false
        if (!states.contains(from)) {
            return false;
        }
        Set<NFAState> to = new LinkedHashSet<>(STATESSIZE);
        for (String state : toState) {
            // if any toStates are not in system, stop and return false
            if (!states.contains(getState(state))) {
                return false;
            }
            to.add(getState(state));
        }
        Character s = Character.valueOf(onSymb);

        // if symbol is not in system alphabet or any states to or from are empty, stop and return false,
        // otherwise add transitions for state
        if (from != null && !to.isEmpty() && (alphabet.contains(onSymb) || onSymb == 'e')) {
            from.addTransitions(s, to);
            return true;
        }
        return false;
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        Set<NFAState> transitions = from.getTransitions(onSymb);
        if (transitions == null) {
            return new HashSet<>();
        }
        return transitions;
    }

    @Override
    public int maxCopies(String s) {
        int maxCopies = 0;
        Set<NFAState> current = new HashSet<>();

        current.add(startState);
        current = setEClosure(current);

        maxCopies = Math.max(maxCopies, current.size());
        for (char c : s.toCharArray()) {

            Set<NFAState> nextStates = new HashSet<>();
            Queue<NFAState> q = new LinkedList<>(current);

            while (!q.isEmpty()) {
                NFAState fromState = q.poll();
                Set<NFAState> transitions = getToState(fromState, c);
                nextStates.addAll(transitions);
            }
            current = setEClosure(nextStates);
            if (current.isEmpty()) {
                return maxCopies;
            }
            maxCopies = Math.max(maxCopies, current.size());
        }
        return maxCopies;
    }

    @Override
    public boolean isDFA() {
        for (NFAState state : states) {
            // check each state for any epsilon transitions and if any found return false
            if (state.getTransitions('e') != null) {
                return false;
            }
            // check each state for any transitions with multiple toStates, if more than one found return false
            for (Character c : alphabet) {
                if (state.getTransitions(c) != null && state.getTransitions(c).toArray().length > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        sb.append(stringBuilderHelper("Q = " + states.toString()));
        sb.append(stringBuilderHelper("\nSigma = " + alphabet.toString()));
        sb.append("\ndelta =\n");
        for (char c : alphabet) {
            sb.append(stringBuilderHelper("\t\t" + c));
        }
        sb.append("\t\te");
        states.forEach(state -> {
            sb.append("\n" + state.getName() + "\t");
            for (char c : alphabet) {
                if (state.getTransitions(c) != null) {
                    sb.append(stringBuilderHelper(state.getTransitions(c).toString() + "\t"));
                } else {
                    sb.append("\t\t");
                }
            }
            if (state.getTransitions('e') != null) {
                sb.append(stringBuilderHelper(state.getTransitions('e').toString() + "\t"));
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
