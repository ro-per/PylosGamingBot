package be.kuleuven.pylos.main;

import java.util.ArrayList;
import java.util.List;

public class Permutation {
    private static List<List<Object>> allPermutations = new ArrayList<>();

    public static List<List<Object>> getPermutations(List<Object> array) {
        helper(array, 0);
        return allPermutations;
    }

    private static List<Object> helper(List<Object> list, int pos) {
        List<Object> permutation = new ArrayList<>();
        int size = list.size();
        if (pos >= size - 1) {
            for (int i = 0; i < size - 1; i++) {
                Object o = list.get(i);
                permutation.add(o);
            }
            if (size > 0) {
                Object o = list.get(size - 1);
                permutation.add(o);
            }
        }

        for (int i = pos; i < size; i++) {

            Object o_pos = list.get(pos);
            Object o_i = list.get(i);

            list.set(pos, o_i);
            list.set(i, o_pos);


            if (permutation.size() != 0) allPermutations.add(permutation);

            helper(list, pos + 1);

            o_pos = list.get(pos);
            o_i = list.get(i);

            list.set(pos, o_i);
            list.set(i, o_pos);
        }
        return permutation;
    }
}
