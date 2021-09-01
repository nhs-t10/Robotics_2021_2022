package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import java.util.ArrayList;
import java.util.HashMap;

public class URIEncoding {
    public static String decode(String str) {
        String result = "";
        for(int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == '%') {
                result += (char)Integer.parseInt(str.substring(i + 1, i + 3), 16);
                i += 2;
            } else {
                result += str.charAt(i);
            }
        }
        return result;
    }
    //TODO: implement
    public static String encode(String str) {
        return str;
    }
}
