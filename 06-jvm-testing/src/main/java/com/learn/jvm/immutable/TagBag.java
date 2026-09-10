package com.learn.jvm.immutable;

import java.util.ArrayList;
import java.util.List;

/**
 * 可变标签袋：外部拿到引用后可以继续 add，演示「没做防御性拷贝」的坑。
 * 对照 {@link UserProfile}。
 */
public final class TagBag {
    private final List<String> tags = new ArrayList<>();

    public void add(String tag) {
        if (tag == null || tag.isBlank()) {
            throw new IllegalArgumentException("tag required");
        }
        tags.add(tag);
    }

    /** 把内部可变 List 直接交出去——调用方 add 会改到本对象。 */
    public List<String> tags() {
        return tags;
    }

    public int size() {
        return tags.size();
    }
}
