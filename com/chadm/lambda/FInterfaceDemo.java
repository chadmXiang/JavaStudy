/*
 * Copyright (c)  chadm All Rights Reserved.
 * @author: chadm.xiang@hotmail.com
 * @version: 1.0
 */

package com.chadm.functioninterface;

import java.util.function.*;

/**
 * @author chadm
 * @date 2020-11-28 20:59
 */
public class FInterfaceDemo {

    public static void main(String[] args) {

        functionDemo();

        Predicate predicate = (value) -> value != null;
        System.out.println(predicate.test(null));

        Supplier<Integer> supplier = () -> new Integer((int) (Math.random() * 1000D));
        System.out.println(supplier.get());

        Consumer<Integer> consumer = (value) -> System.out.println(value);
        consumer.accept(101);

        UnaryOperator<Person> unaryOperator =
                (person) -> { person.name = "chadm"; return person; };
        Person me = new Person();
        System.out.println(unaryOperator.apply(me));

        BinaryOperator<Integer> binaryOperator = (x1, x2) -> x1 + x2;
        System.out.println(binaryOperator.apply(100,1));
    }

    /**
     * 当需要一个Function实例的时候，子类和lambda表达式都可以，效果是一样的
     */
    static void functionDemo() {
        Function<Integer,Integer> add = new AddOne();
        System.out.println(add.apply(8));

        Function<Integer,Integer> addOne = (value) -> value+1;
        System.out.println(addOne.apply(8));
    }

    static class AddOne implements Function<Integer,Integer> {

        /**
         * Applies this function to the given argument.
         *
         * @param aInt the function argument
         * @return the function result
         */
        @Override
        public Integer apply(Integer aInt) {
            return aInt + 1;
        }
    }

    static class Person {
        public String name;
        public int age;
    }

}
