package me.ifling;

public class Interfaces {
    static void fx() {
        System.out.println("fx");
    }

    void gx() {

    }

    public static void main(String[] args) {
        Interfaces ifs = new Interfaces();
        run r = Interfaces::fx;
        run r1 = ifs::gx;
        run r2 = () -> {
        };
        r.handle();
        r1.handle();
    }
}


@FunctionalInterface
interface run {
    void handle();
}



