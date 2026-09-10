package com.learn.jvm.immutable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 不可变对象 + 防御性拷贝：
 * - 构造时 {@code new ArrayList<>(tags)}，切断与调用方 List 的共享
 * - 对外 {@code Collections.unmodifiableList}，调用方 add 会立刻失败
 *
 * 对照 {@link TagBag}：把内部 List 直接 return，外面一改里面也变。
 */
public final class UserProfile {
    private final String name;
    private final List<String> tags;

    public UserProfile(String name, List<String> tags) {
        this.name = Objects.requireNonNull(name, "name");
        if (name.isBlank()) {
            throw new IllegalArgumentException("name blank");
        }
        if (tags == null) {
            throw new IllegalArgumentException("tags required");
        }
        // 防御性拷贝：之后调用方再 tags.add(...)，不会污染本对象
        this.tags = Collections.unmodifiableList(new ArrayList<>(tags));
    }

    public String name() {
        return name;
    }

    public List<String> tags() {
        return tags;
    }

    public int tagCount() {
        return tags.size();
    }
}
