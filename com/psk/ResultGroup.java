package com.psk;

import java.util.Set;

public class ResultGroup {
    private final Integer votesNumber;
    private Set<Long> groupElements;

    public ResultGroup(Integer votesNumber, Set<Long> groupElements) {
        this.votesNumber = votesNumber;
        this.groupElements = groupElements;
    }

    public Integer getVotesNumber() {
        return votesNumber;
    }

    public Set<Long> getGroupElements() {
        return groupElements;
    }

    public void setGroupElements(Set<Long> elements) {
        this.groupElements = elements;
    }

    @Override
    public String toString() {
        return "ResultGroup{" +
                "votesNumber=" + votesNumber +
                ", groupElements=" + groupElements +
                '}';
    }
}
