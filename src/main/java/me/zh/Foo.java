package me.zh;

enum Color {
    RED, GREEN
}

class XA {
    int add(int... args) {
        int n = 0;
        for (int i : args) {
            n += i;
        }
        return n;
    }
}


public enum Foo {
    X(1), Y(2);

//    Y(2);

    private int a;

    Foo(int a) {
        this.a = a;
    }


    public static void main(String[] args) {
//        System.out.println(Foo.X);
//        Foo f = Foo.X;
//        f.a = 100;
        Color color;
        color = Color.GREEN;
        color = Color.RED;
        new XA().add(1, 2, 3);
    }
};