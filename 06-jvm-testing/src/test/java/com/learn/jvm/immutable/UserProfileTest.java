package com.learn.jvm.immutable;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserProfileTest {

    @Test
    void constructorDefensiveCopy() {
        List<String> shared = new ArrayList<>();
        shared.add("vip");
        UserProfile profile = new UserProfile("Ada", shared);
        shared.add("hacked");
        assertEquals(List.of("vip"), profile.tags());
        assertEquals(1, profile.tagCount());
    }

    @Test
    void returnedListRejectsMutation() {
        UserProfile profile = new UserProfile("Ada", List.of("vip"));
        assertThrows(UnsupportedOperationException.class, () -> profile.tags().add("hacked"));
    }

    @Test
    void tagBagLeaksMutableList() {
        TagBag bag = new TagBag();
        bag.add("vip");
        bag.tags().add("hacked");
        assertEquals(2, bag.size());
    }
}
