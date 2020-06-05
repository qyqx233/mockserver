package me.ifling;

import java.util.ArrayList;

public class Util {
    public static String getStackTraceString(Throwable ex) {//(Exception ex) {
        StackTraceElement[] traceElements = ex.getStackTrace();

        StringBuilder traceBuilder = new StringBuilder();

        if (traceElements != null && traceElements.length > 0) {
            for (StackTraceElement traceElement : traceElements) {
                traceBuilder.append(traceElement.toString());
                traceBuilder.append("\n");
            }
        }

        return traceBuilder.toString();
    }

    public static void main(String[] args) {
        Object[] args1 = new Object[]{"1", 1};
        ArrayList<String> lst = new ArrayList<String>();
        lst.add("");
    }
}
