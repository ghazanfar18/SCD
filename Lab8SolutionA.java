package com.mycompany.lab8solutiona;
//AliGhazanfar 2023F-BSE-103
import java.util.*;
public class Lab8SolutionA {

    // Generic method: works with Integer[], Double[], Float[] etc.
    public static <T extends Number> List<Double> consecutiveDifferences(T[] input) {
        List<Double> result = new ArrayList<>();
        if (input == null || input.length < 2) return result;

        for (int i = 0; i < input.length - 1; i++) {
            double next = input[i + 1].doubleValue();
            double curr = input[i].doubleValue();
            result.add(next - curr);
        }
        return result;
    }
    public static void main(String[] args) {
        Integer[] intArray   = {5, 7, 3, 9, 19};
        Double[]  doubleArray= {1.5, 2.0, 3.25, 1.10};
        Float[]   floatArray = {2.5f, 2.0f, 5.0f, 4.5f};

        System.out.println("Input  (Integer): " + Arrays.toString(intArray));
        System.out.println("Output (Double) : " + consecutiveDifferences(intArray));
        System.out.println();

        System.out.println("Input  (Double) : " + Arrays.toString(doubleArray));
        System.out.println("Output (Double) : " + consecutiveDifferences(doubleArray));
        System.out.println();

        System.out.println("Input  (Float)  : " + Arrays.toString(floatArray));
               System.out.println("Output (Double) : " + consecutiveDifferences(floatArray));
    }}

