package me.zh.invocation;

public class Dog implements Speak {
    @Override
    public void ss() {
        System.out.println("zhiHu ss");
    }

    @Override
    public void ss1(String name) {
        System.out.println("zhiHu ss1");
    }
}
