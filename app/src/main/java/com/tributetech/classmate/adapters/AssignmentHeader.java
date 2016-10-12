package com.tributetech.classmate.adapters;

import com.tributetech.classmate.utils.CalendarUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Donnie Propst on 5/9/2016.
 */
public class AssignmentHeader {
    private Date date;

    public AssignmentHeader(Date date){
        Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.HOUR, 0);
        this.date = calendar.getTime();

    }
    public String getDateString(){
        return CalendarUtils.getFormattedDate(date);
    }

    @Override
    public boolean equals(Object o) {
      return o instanceof AssignmentHeader && date.equals(((AssignmentHeader)o).date);
    }

    public Date getDate() {
        return date;
    }


}
