package org.ga4gh.drs.utils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.*;

public class DeepObjectMerger {

    private static final Set<Class<?>> classesToSet = new HashSet<>(Arrays.asList(
        List.class,
        ArrayList.class,
        String.class,
        LocalDateTime.class,
        boolean.class,
        byte.class,
        short.class,
        char.class,
        int.class,
        long.class,
        float.class,
        double.class
    ));

    public static void merge(Object source, Object target) {
        // Through reflection, get all fields associated with object to be merged
        Class<?> objectClass = source.getClass();
        Field[] fields = objectClass.getDeclaredFields();

        for (Field field : fields) {
            // Get field and set as accessible so it can be written
            Class<?> fieldClass = field.getType();
            field.setAccessible(true);

            try {
                Object sourceProperty = field.get(source);
                Object targetProperty = field.get(target);

                // If the field is a 'primitive' type, then the target field
                // can be set with the source field value (provided source
                // field value is not null)
                if (classesToSet.contains(fieldClass)) {
                    if (sourceProperty != null) {
                        field.set(target, sourceProperty);
                    }
                } else {
                    // If the field is a complex class (i.e. non-primitive),
                    // and both source and target are non-null, then
                    // this method is recursively called on the field value
                    if (sourceProperty != null && targetProperty != null) {
                        merge(sourceProperty, targetProperty);
                    }
                    // Null target field can be set with source field value
                    else if (sourceProperty != null) {
                        field.set(target, sourceProperty);
                    }
                }
            } catch (IllegalAccessException e) {
                System.out.println("illegal access exception");
            }
        }
    }
}
