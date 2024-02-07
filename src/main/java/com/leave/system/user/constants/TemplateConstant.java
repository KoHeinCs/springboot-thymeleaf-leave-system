package com.leave.system.user.constants;

/**
 * @author HeinHtetAung
 * @created 03/Oct/2020 -11:57 PM
 **/
public class TemplateConstant {
    /**  controller name for user**/
    public static final String USER = "/user";
    public static final String APPLY_LEAVE = "/apply-leave";
    public static final String LEAVE_TRANSACTION = "/leave-transaction" ;
    public static final String LEAVE_DETAIL = "/leave-detail";
    /**  template name for user**/
    public static final String USER_APPLY_LEAVE = USER+"/apply-leave";
    public static final String USER_LEAVE_TRANSACTION = USER + "/leave-transaction" ;
    public static final String USER_LEAVE_DETAIL = USER +"/leave-detail";

    /**  controller name for admin**/
    public static final String ADMIN = "/admin";
    public static final String APPROVE = "/approve";
    /**  template name for admin**/
    public static final String ADMIN_USER = ADMIN + "/user";


}
