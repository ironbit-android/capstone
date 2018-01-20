package pe.ironbit.android.capstone.util;

import java.util.ArrayList;
import java.util.List;

public class Collection {
    private Collection() {
    }

    public static final float[] convertFloatCollection(final List<Float> input) {
        float[] output = new float[input.size()];

        for (int index = 0; index < input.size(); ++index) {
            output[index] = input.get(index);
        }

        return output;
    }

    public static final List<Float> convertFloatCollection(final float[] input) {
        List<Float> output = new ArrayList<>();

        for (int index = 0; index < input.length; ++index) {
            output.add(input[index]);
        }

        return output;
    }

    public static <Type> void assign(List<Type> list, Type value) {
        for (int index = 0; index < list.size(); ++index) {
            list.set(index, value);
        }
    }

    public static <Type> void assign(List<Type> list, int size, Type value) {
        if ((list != null) && (list.size() == size)) {
            for (int index = 0; index < size; ++index) {
                list.set(index, value);
            }
        } else {
            list = new ArrayList<>();
            for (int index = 0; index < size; ++index) {
                list.add(value);
            }
        }
    }

    public static <Type> List<Type> initialize(int size, Type value) {
        List<Type> list = new ArrayList<>();

        while (0 < size--) {
            list.add(value);
        }

        return list;
    }
}
