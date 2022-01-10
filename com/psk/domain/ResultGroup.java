package com.psk.domain;

import java.util.Objects;
import java.util.Set;

public class ResultGroup {
    private final Integer votesNumber;
    private Set<Server> groupElements;

    public ResultGroup(Integer votesNumber, Set<Server> groupElements) {
        this.votesNumber = votesNumber;
        this.groupElements = groupElements;
    }

    public Integer getVotesNumber() {
        return votesNumber;
    }

    public Set<Server> getGroupElements() {
        return groupElements;
    }

    public void setGroupElements(Set<Server> elements) {
        this.groupElements = elements;
    }

    @Override
    public String toString() {
        return "ResultGroup{" +
                "votesNumber=" + votesNumber +
                ", groupElements=" + groupElements +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultGroup that = (ResultGroup) o;
        return Objects.equals(votesNumber, that.votesNumber) && Objects.equals(groupElements, that.groupElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(votesNumber, groupElements);
    }
}
