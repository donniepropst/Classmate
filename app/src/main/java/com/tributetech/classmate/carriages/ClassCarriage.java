package com.tributetech.classmate.carriages;

import com.tributetech.classmate.domain.Class;

/**
 * Created by Donnie Propst on 5/9/2016.
 * Used to carry an object from one activity to another, as opposed to passing by intent
 */
public class ClassCarriage {
    private static ClassCarriage ourInstance = new ClassCarriage();

    private static Class toCarry;

    public static ClassCarriage getInstance() {
        return ourInstance;
    }

    private ClassCarriage() {

    }

    public Class getToCarry() {
        return toCarry;
    }
    public void clear(){
        toCarry = null;
    }

    public void setToCarry(Class toCarry) {
        ClassCarriage.toCarry = toCarry;
    }
}
