package com.testTask.university.utils;

import org.springframework.stereotype.Component;

@Component
public class Constants {
    public static final String DATE_PATTERN_REGEX = "^\\d{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2]\\d|3[0-1]))|(02-(0[1-9]|[1-2]\\d))|((0[469]|11)-(0[1-9]|[1-2]\\d|30)))$";
}
