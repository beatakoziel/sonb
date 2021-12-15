package com.psk;

import java.util.Objects;

public class Server {
    private final Short id;
    private Long time;
    private Integer weight ;

    public Server(Short id, Integer weight) {
        this.id = id;
        this.weight = weight;
    }

    public Short getId() {
        return id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return Objects.equals(id, server.id) && Objects.equals(time, server.time) && Objects.equals(weight, server.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, weight);
    }
}
