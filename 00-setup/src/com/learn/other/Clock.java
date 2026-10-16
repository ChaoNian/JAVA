package com.learn.other;

/** 另一个包里的 Clock：演示同名类要靠包名区分。 */
public final class Clock {
    public String now() {
        return "other.Clock";
    }
}
