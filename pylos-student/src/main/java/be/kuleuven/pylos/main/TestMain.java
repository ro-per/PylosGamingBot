package be.kuleuven.pylos.main;


import java.util.ArrayList;
import java.util.List;


public class TestMain {
    public static void main(String[] args) {
        String order;

        List<Object> list = new ArrayList<>();
        list.add("A1");
        list.add("A22");
        list.add("E");


        System.out.println();

        for(Object o: Permutation.getPermutations(list)){
            System.out.println(o);
        }
    }
}
