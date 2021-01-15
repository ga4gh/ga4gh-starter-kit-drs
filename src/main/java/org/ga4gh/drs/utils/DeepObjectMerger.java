package org.ga4gh.drs.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class DeepObjectMerger {

    private static final Set<String> primitives = new HashSet<>(Arrays.asList(
        "java.lang.String",
        "java.time.LocalDateTime",
        "java.util.List"
    ));

    public DeepObjectMerger() {

    }

    public void merge(Object source, Object target) {
        // through reflection, get all fields associated with object to be 
        // merged
        Class<?> objectClass = source.getClass();
        Field[] fields = objectClass.getDeclaredFields();
        
        for (Field field: fields) {
            // get field name, class, and associated getter and setter
            Class<?> fieldClass = field.getType();
            String fieldClassName = fieldClass.getName();
            String fieldName = field.getName();
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            try {
                // call the getter to get the value for both source and target
                // objects
                Method getter = objectClass.getDeclaredMethod(getterName);
                Object sourceProperty = getter.invoke(source, (Object[]) null);
                Object targetProperty = getter.invoke(target, (Object[]) null);

                // if the field is a 'primitive' type, then the target field
                // can be set with the source field value (provided source
                // field value is not null)
                if (primitives.contains(fieldClassName)) {
                    if (sourceProperty != null) {
                        Method setter = objectClass.getDeclaredMethod(setterName, fieldClass);
                        setter.invoke(target, sourceProperty);
                    }
                // if the field is a complex class (i.e. non-primitive), then
                // this method is recursively called on the field value
                } else {
                    merge(sourceProperty, targetProperty);
                }
            } catch (NoSuchMethodException e) {
                System.out.println("no such method");
            } catch (InvocationTargetException e) {
                System.out.println("invocation target exception");
            } catch (IllegalAccessException e) {
                System.out.println("illegal access exception");
            }
        }
    }
}
