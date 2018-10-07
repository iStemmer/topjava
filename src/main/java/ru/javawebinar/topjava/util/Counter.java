package ru.javawebinar.topjava.util;

public class Counter {
    private Long recordId = 0L;

    public synchronized Long getValue() {
        return recordId;
    }

    public synchronized Long getIncrement() {
        return ++recordId;
    }
}
