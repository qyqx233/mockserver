package me.zh.function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

//interface Comparator<T> {
//    int compare(T a, T b);
//}

class Apple {
    private double weight;
    private String color;

    Apple(String color, double weight) {
        this.color = color;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public String getColor() {
        return color;
    }
}

class Next {
    private Next next;
    private String name;

    public Next(String name) {
        this.name = name;
    }

    Next add(Next next) {
        this.next = next;
        return next;
    }

    public String getName() {
        return name;
    }

    public Next getNext() {
        return this.next;
    }
}

public class Lambda {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Lambda.class);

        Comparator<Integer> c = (Integer a, Integer b) -> 1;
        double unitPrice = 0.01;
        Apple apple = new Apple("red", 10.2);
        Function<Apple, Double> priceAppleFunction = a -> a.getWeight() * unitPrice;
        Function<Apple, Double> weigher = Apple::getWeight;
        weigher.apply(apple);
        Function<String, Integer> fun = Integer::parseInt;
        String s = "123";
        Function<String, Integer> func1 = String::length;
        func1.apply("a");
        Supplier<Integer> len = s::length;
        System.out.println(len.get());
        priceAppleFunction.apply(apple);
        List<Apple> apples = Arrays.asList(new Apple("red", 1), new Apple("red", 2));
        apples.sort(Comparator.comparing(Apple::getWeight));
        Next root = new Next("root");
        root.add(new Next("a")).add(new Next("b"));
//        System.out.println(root.getNext().getNext().getNext().getName());
        logger.error("----------------------");
        try {
            int a = 1 / 0;
        } catch (Exception e) {
            logger.error("{} {}", "aaa", 1, e);
        }
        logger.error("===========");
//        (Next next) -> {
//            if(next.getNext() == null) {
//                return next;
//            } else {
//                return
//            }
//        }

    }

}
