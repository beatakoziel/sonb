package com.psk;

import java.time.LocalDateTime;

public class Server {
    private final Short id;
    private LocalDateTime time;
    private Integer weight;

    public Server(Short id) {
        this.id = id;
    }

    public Short getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
