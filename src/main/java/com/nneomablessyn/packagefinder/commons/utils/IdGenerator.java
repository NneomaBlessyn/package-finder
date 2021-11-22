package com.nneomablessyn.packagefinder.commons.utils;

import java.util.Objects;
import java.util.UUID;

public class IdGenerator {

    public static String randomCode(Integer length) {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, length);
    }

    public static String randomCode(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString();
    }

    public static String generatePackageCode(String name, Integer length) {
        int prefixLength = 2;
        String prefix = name.substring(0, prefixLength).toUpperCase();
        return (Objects.nonNull(name) | name.isEmpty()) ? prefix + UUID.randomUUID().toString()
                .replaceAll("-", "").substring(0, length - prefixLength).toUpperCase()
                : UUID.randomUUID().toString().substring(0, length)
                .toUpperCase();
    }

    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}

