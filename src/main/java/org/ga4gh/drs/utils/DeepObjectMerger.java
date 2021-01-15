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
        "java.util.Date",
        "java.util.List"
    ));

    public DeepObjectMerger() {

    }

    public void merge(Object source, Object target) {
        System.out.println("Inside the merge method");

        Class<?> objectClass = source.getClass();
        Field[] fields = objectClass.getDeclaredFields();
        
        System.out.println("---");
        System.out.println("Object Class: " + objectClass.getName());

        for (Field field: fields) {
            Class<?> fieldClass = field.getType();
            String fieldClassName = fieldClass.getName();
            String fieldName = field.getName();
            String getterName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            String setterName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            System.out.println("-");
            System.out.println("Field name: " + fieldName);
            System.out.println("Field class name: " + fieldClassName);
            System.out.println("Getter name: " + getterName);

            Method getter = null;
            try {
                System.out.println("LOOKING FOR METHOD: " + getterName);
                getter = objectClass.getDeclaredMethod(getterName);
                System.out.println("FOUND METHOD");
                Object sourceProperty = getter.invoke(source, (Object[]) null);
                Object targetProperty = getter.invoke(target, (Object[]) null);

                if (primitives.contains(fieldClassName)) {
                    if (sourceProperty != null) {
                        System.out.println("Setting for: " + fieldName);
                        System.out.println("Looking up setter");
                        Method setter = objectClass.getDeclaredMethod(setterName, fieldClass);
                        System.out.println("FOUND SETTER");
                        System.out.println("The source property:");
                        System.out.println(sourceProperty);
                        System.out.println("The target property");
                        System.out.println(targetProperty);
                        setter.invoke(target, sourceProperty);
                    }


                    
                } else {
                    System.out.println("Recursing for: " + fieldName);
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

    /*
    public Class<T> getTypeClass() {
        return typeClass;
    }
    */
    
}
