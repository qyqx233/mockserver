package me.zh.function;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

@FunctionalInterface
interface CheckedFunction2<T1, T2, R> {
    R apply(T1 t1, T2 t2) throws Throwable;
}

@FunctionalInterface
interface CheckedFunction3<T1, T2, T3, R> {
    R apply(T1 t1, T2 t2, T3 t3) throws Throwable;
}


@FunctionalInterface
interface CheckedFunction<T, R> {
    R apply(T t) throws Throwable;
}

@FunctionalInterface
interface CheckedSupplier<T> {
    T get() throws Throwable;
}

@FunctionalInterface
interface CheckedNoneSupplier {
    void get() throws Throwable;
}

public interface Attempt {


    static boolean fxx() throws Exception {
        throw new Exception("aaa");
    }

    static boolean fxx(int i) throws Exception {
        return false;
    }

    static <T> boolean exitWhenFailOrException(CheckedSupplier<T> function, Predicate<T> predicate, Consumer<T> fail,
                                               Consumer<Throwable> exception) {
        Objects.requireNonNull(function);

        try {
            T t = function.get();
            if (!predicate.test(t)) {
                fail.accept(t);
                return true;
            }
            return false;
        } catch (Throwable ex) {
            exception.accept(ex);
            return true;
        }
    }

    static void log(Throwable throwable) {

    }

    static <T> boolean exitException(CheckedNoneSupplier function, Consumer<Throwable> exception) {
        Objects.requireNonNull(function);
//        System.out.println("exitException");
        try {
            function.get();
            return false;
        } catch (Throwable ex) {
//            System.out.println("exitException failed");
            exception.accept(ex);
            return true;
        }
    }

    public static void main(String[] args) {
//        if (Attempt.exitWhenFailOrException(() -> fxx(), t -> true, t -> {
//        }, Attempt::log)) return;

//        System.out.println("saasd");
        if (Attempt.exitException(() -> Files.lines(Paths.get("d:\\1.txt")), e -> {
            System.out.println("read 1.txt failed");
        })) return;
    }
}
