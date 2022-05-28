package com.divinity.hlspells.spell;

import java.util.Objects;
import java.util.function.Consumer;

// Technically we don't need this, we can use predicate in conjunction with consumer, but added this for sake of consistency
public interface SpellConsumer<T> {

    boolean accept(T t);

    default Consumer<T> andThenIfCast(Consumer<? super T> after) {
        Objects.requireNonNull(after);
        return t -> {
            if (accept(t)) {
                after.accept(t);
            }
        };
    }
}
