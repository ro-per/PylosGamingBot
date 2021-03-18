package be.kuleuven.pylos.main;


import java.util.ArrayList;
import java.util.List;


public class TestMain {
    public static void main(String[] args) {
        List<String> order;

        List<Object> list = new ArrayList<>();
        list.add("A1");
        list.add("A22");
        list.add("E");


        System.out.println();


        for (List permutation : Permutation.getPermutations(list)) {
            order = permutation;
            System.out.println(order);

        }
    }
}
