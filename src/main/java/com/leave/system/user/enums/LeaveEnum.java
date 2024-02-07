package com.leave.system.user.enums;

public enum LeaveEnum {
    ANNUAL("Annual Leave"),
    MEDICAL("Medical Leave"),
    UNPAID("Unpaid Leave");
    private String description;
    private LeaveEnum(String description){
        this.description = description;
    }
    public String getDescription(){
        return this.description;
    }

}
