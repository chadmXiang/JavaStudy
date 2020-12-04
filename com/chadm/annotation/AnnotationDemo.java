/*
 * Copyright (c)  chadm All Rights Reserved.
 * @author: chadm.xiang@hotmail.com
 * @version: 1.0
 */

package com.chadm.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

/**
 * @author chadm
 * @date 2020-12-04 21:21
 */
public class AnnotationDemo {

    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    static @interface MyAnnotation {
        String author() default "chadm";
        String date();
    }

    @MyAnnotation(author = "chadm",date = "20201204")
    class JavaBook {
        @MyAnnotation(author = "Jack",date = "20201204")
        public void translation() {
            System.out.println(JavaBook.class.getSimpleName());
        }
    }

    public static void main(String[] args) {
        Class<JavaBook> aClass = JavaBook.class;
        if(aClass.isAnnotationPresent(MyAnnotation.class)) {
            MyAnnotation annotation = aClass.getAnnotation(MyAnnotation.class);
            System.out.println("Annotation in Class");
            System.out.println(annotation.author());
            System.out.println(annotation.date());
        }
        try {
            Method method = aClass.getDeclaredMethod("translation",null);
            if(method.isAnnotationPresent(MyAnnotation.class)) {
                MyAnnotation annotation = method.getAnnotation(MyAnnotation.class);
                System.out.println("Annotation in Method");
                System.out.println(annotation.author());
                System.out.println(annotation.date());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
