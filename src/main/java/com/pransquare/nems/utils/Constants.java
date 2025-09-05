package com.pransquare.nems.utils;

public class Constants {

    public static final String TIMESHEET_PATH = "/home/ems/timesheet";
    public static final String LEAVE_PATH = "/home/ems/leave";
    public static final String APPRISAL_PATH = "/home/ems/appraisal/employeeAppraisal";
    public static final String APPRISAL_LIST_PATH = "/home/ems/appraisal/initiateAppraisal";
    public static final String CANDIDATE_LIST_PATH = "/home/smartHire/candidateList";
    public static final String REQUESTS_PATH = "/home/it/requests";

 // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
