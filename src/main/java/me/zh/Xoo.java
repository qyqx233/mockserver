package me.zh;

public enum Xoo {
    X(1, "12");
    private int a;
    private String s;

    Xoo(int a, String s) {
        this.a = a;
        this.s = s;
    }

    public static void main(String[] args) {
        System.out.println(Xoo.X.a);
        System.out.println(Xoo.X.s);
    }

}
