package me.zh.invocation;

import java.lang.reflect.Proxy;

public class Demo {
    public static void main(String[] args) {
        InvocationImpl impl = new InvocationImpl();
        Speak speak = (Speak) Proxy.newProxyInstance(Speak.class.getClassLoader(), new Class[]{Speak.class}, impl);
        speak.ss();
        speak.ss1("hehe");
    }
}
