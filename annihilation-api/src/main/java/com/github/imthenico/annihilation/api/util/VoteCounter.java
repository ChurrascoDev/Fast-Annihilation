package com.github.imthenico.annihilation.api.util;

import java.util.*;

public abstract class VoteCounter<K, V> {

    private final Map<V, List<K>> votes;

    public VoteCounter() {
        this.votes = new LinkedHashMap<>();
    }

    public void vote(K voter, V candidate) {
        Objects.requireNonNull(voter, "voter");
        Objects.requireNonNull(candidate, "candidate");

        for (List<K> value : votes.values()) {
            value.remove(voter);
        }

        List<K> voters = votes.computeIfAbsent(candidate, k -> new ArrayList<>());

        voters.add(voter);
    }

    public V getVote(K voter) {
        Objects.requireNonNull(voter);

        for (Map.Entry<V, List<K>> entry : votes.entrySet()) {
            List<K> votes = entry.getValue();

            if (votes.contains(voter))
                return entry.getKey();
        }

        return null;
    }

    public V mostVoted() {
        Pair<V, Integer> mostVoted = new Pair<>(null, 0);

        for (Map.Entry<V, List<K>> entry : votes.entrySet()) {
            List<K> votes = entry.getValue();

            int count = votes.size();
            if (mostVoted.getLeft() == null || mostVoted.getRight() >= count)
                mostVoted = new Pair<>(entry.getKey(), count);
        }

        return mostVoted.getLeft();
    }

    public boolean isCandidate(V possibleCandidate) {
        return getCandidates().contains(possibleCandidate);
    }

    public abstract List<V> getCandidates();
}