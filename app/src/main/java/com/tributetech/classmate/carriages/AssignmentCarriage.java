package com.tributetech.classmate.carriages;

import com.tributetech.classmate.domain.Assignment;

/**
 * Created by Donnie Propst on 5/28/2016.
 */
public class AssignmentCarriage {
    private static AssignmentCarriage ourInstance = new AssignmentCarriage();
    private static Assignment toCarry;


    public static AssignmentCarriage getInstance() {
        return ourInstance;
    }

    public Assignment getToCarry() {
        return toCarry;
    }
    public void clear(){
        toCarry = null;
    }

    private AssignmentCarriage() {
    }
    public void setToCarry(Assignment toCarry){
       AssignmentCarriage.toCarry = toCarry;
    }
}
