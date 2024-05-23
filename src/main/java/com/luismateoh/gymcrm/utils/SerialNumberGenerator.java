package com.luismateoh.gymcrm.utils;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

@Component
public class SerialNumberGenerator {

    private final AtomicLong serialNumber = new AtomicLong(1);

    public long getNextSerialNumber() {
        return serialNumber.getAndIncrement();
    }

    public long getCurrentSerialNumber() {
        return serialNumber.get();
    }
}