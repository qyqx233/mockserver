package me.zh.function;

import java.util.Objects;

@FunctionalInterface
public interface Function1<T, R> {
    R apply(T var1);

    default <V> java.util.function.Function<V, R> compose(java.util.function.Function<? super V, ? extends T> before) {
        Objects.requireNonNull(before);
        return (v) -> {
            return this.apply(before.apply(v));
        };
    }
//
//    default <V> java.util.function.Function<T, V> andThen(java.util.function.Function<? super R, ? extends V> after) {
//        Objects.requireNonNull(after);
//        return (t) -> {
//            return after.apply(this.apply(t));
//        };
//    }
//
//    static <T> java.util.function.Function<T, T> identity() {
//        return (t) -> {
//            return t;
//        };
//    }
}
