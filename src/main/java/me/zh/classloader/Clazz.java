package me.zh.classloader;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class A {
    A(String name) {

    }
}

class AA extends  A {

    AA(String name) {
        super(name);
    }
}

class Base {
    static int num = 1;

    static {
        System.out.println("Base " + num);
    }
}

class Derived extends Base {
}

class Person implements Serializable {

    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

// get/set方法
}

public class Clazz {
    public static void main(String[] args) throws ClassNotFoundException, IntrospectionException, InvocationTargetException, IllegalAccessException {
        // 不会初始化静态块
        Class clazz1 = Base.class;
        System.out.println("------");
        // 会初始化
//        Class clazz2 = Class.forName("me.zh.classloader.Base");
        Class clazz3 = Class.forName("me.zh.classloader.Derived");

        Person person = new Person("luoxn28", 23);
        Class clazz = person.getClass();


        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String key = field.getName();
            System.out.println(key);
            PropertyDescriptor descriptor = new PropertyDescriptor(key, clazz);
            Method method = descriptor.getReadMethod();
            Object value = method.invoke(person);
            System.out.println(key + ":" + value);

        }
    }
}