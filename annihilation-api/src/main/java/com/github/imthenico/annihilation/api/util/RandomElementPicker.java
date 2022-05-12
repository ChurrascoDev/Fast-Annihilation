package com.github.imthenico.annihilation.api.util;

import java.util.List;
import java.util.Random;

public interface RandomElementPicker {

    Random RANDOM = new Random();

    static <E> E pickRandom(E[] array) {
        return array[RANDOM.nextInt(array.length - 1)];
    }

    static <E> E pickRandom(List<E> list) {
        return list.get(RANDOM.nextInt(list.size() - 1));
    }
}