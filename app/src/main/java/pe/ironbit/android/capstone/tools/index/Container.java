package pe.ironbit.android.capstone.tools.index;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Container {
    private Set<Integer> spaces;

    private Map<String, Map<Integer, Set<Integer>>> container;

    public Container() {
        spaces = new TreeSet<>();
        container = new HashMap<>();
    }

    public Set<Integer> verify(String query) {
        Set<String> tokens = new TreeSet<>(Arrays.asList(query.toLowerCase().trim().split("\\s+")));

        Set<Integer> outcome = new TreeSet<>();
        for (Integer space : spaces) {
            boolean init = true;
            Set<Integer> partial = new TreeSet<>();
            for (String token : tokens) {
                if (!container.containsKey(token)) {
                    partial.clear();
                    break;
                }
                if (!container.get(token).containsKey(space)) {
                    break;
                }

                Set<Integer> set = container.get(token).get(space);
                if (init) {
                    init = false;
                    partial = set;
                    continue;
                }
                partial.retainAll(set);
            }
            outcome.addAll(partial);
        }
        return outcome;
    }

    public void set(String input, int space, int bookId) {
        Set<String> tokens = new TreeSet<>(Arrays.asList(input.toLowerCase().trim().split("\\s+")));

        for (String token : tokens) {
            if (!container.containsKey(token)) {
                container.put(token, new HashMap<Integer, Set<Integer>>());
            }
            Map<Integer, Set<Integer>> map = container.get(token);

            if (!map.containsKey(space)) {
                map.put(space, new TreeSet<Integer>());
            }
            Set<Integer> set = map.get(space);

            set.add(bookId);
            spaces.add(space);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append("container:{");
        for (Map.Entry<String, Map<Integer, Set<Integer>>> map : container.entrySet()) {
            builder.append(map.getKey()).append(":{");
            for (Map.Entry<Integer, Set<Integer>> map2 : map.getValue().entrySet()) {
                builder.append(map2.getKey()).append(":{");
                for (Integer integer : map2.getValue()) {
                    builder.append(integer).append(",");
                }
                builder.setLength(builder.length() - 1);
                builder.append("},");
            }
            builder.setLength(builder.length() - 1);
            builder.append("},");
        }
        builder.setLength(builder.length() - 1);
        builder.append("}");

        return builder.toString();
    }
}
