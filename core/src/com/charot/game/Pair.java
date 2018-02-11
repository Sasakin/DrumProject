package com.charot.game;
/*
 * Author: Piotr Likus, SpaceInSoft, 2015
 * License: MIT
 */

/**
 * Implements generic pair class that can hold pair of any objects.
 * Class is immutable, so setting values is done by creation of new object.
 *
 * @author Piotr Likus
 */
final public class Pair<T, U> {

    private final T first_;
    private final U second_;

    public Pair() {
        first_ = null;
        second_ = null;
    }

    public Pair(Pair<T, U> src) {
        first_ = src.first_;
        second_ = src.second_;
    }

    public Pair(T firstValue, U secondValue) {
        first_ = firstValue;
        second_ = secondValue;
    }

    public Pair first(T newValue) {
        return new Pair(newValue, second_);
    }

    public T first() {
        return first_;
    }

    public Pair second(T newValue) {
        return new Pair(first_, newValue);
    }

    public U second() {
        return second_;
    }

    public String toString() {
        return "(" + first_ + ", " + second_ + ")";
    }

}
