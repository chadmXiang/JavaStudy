/*
 * Copyright (c)  chadm All Rights Reserved.
 * @author: chadm.xiang@hotmail.com
 * @version: 1.0
 */

package com.chadm.functioninterface;

/**
 * @author chadm
 * @date 2020-11-28 20:59
 */
@FunctionalInterface
public interface MyFunctionInterface {

    int print();

    default String getName() {
        return "chadm";
    }

    static boolean notNullAndEmpty(String name) {
        return name != null && !name.equals("");
    }

    public String toString();

    public boolean equals(Object var1);

}
