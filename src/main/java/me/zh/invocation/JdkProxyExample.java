package me.zh.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkProxyExample implements InvocationHandler {
    private Object target = null;

    public Object bind(Object target) {
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        System.out.println("enter proxy method: " + method.getName());
        Object obj = method.invoke(target, args); // 可以这么玩。。
        System.out.println("exit proxy method: " + method.getName());
        return null;
    }

    public static void main(String[] args) {

        JdkProxyExample jdk = new JdkProxyExample();
        Speak proxy = (Speak) jdk.bind(new Dog());
        proxy.ss();
    }
}
