package me.zh;

class ExcpZero extends Exception {

}

public class Excp {
    static void foo() throws ExcpZero {
        throw new ExcpZero();
    }
    public static void main(String[] args) throws Exception {
        int i = 0;
        String b = i % 2 == 0 ? "" : "";
        try {
            foo();
        }catch(ExcpZero e) {
            throw new Exception(e);
        }
    }
}
