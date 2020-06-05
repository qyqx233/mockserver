package me.zh.invocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InterceptorJdkProxy implements InvocationHandler {
    private Object target;
    private String interceptorClass = null;

    public InterceptorJdkProxy(Object target, String interceptorClass) {
        this.target = target;
        this.interceptorClass = interceptorClass;
    }

    public static Object bind(Object target, String interceptorClass) {
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),
                new InterceptorJdkProxy(target, interceptorClass));
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        return null;
    }
}
