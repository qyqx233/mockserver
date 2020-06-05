package me.zh.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvocationImpl implements InvocationHandler {

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        try {
            if ("ss".equals(method.getName())) {
                System.out.println("here to execute ss");
                System.out.println("ss finished");

            } else if ("ss1".equals(method.getName())) {

                System.out.println("here to execute ss1");
                System.out.println((String) objects[0]);
                System.out.println("ss1 finished");
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.out.println("执行后");
        return null;
    }
}
