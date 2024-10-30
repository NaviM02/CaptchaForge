package com.navi.captchaapi.parser_lexer.cc.obj;

import java.util.*;

public class StaticVariables {
    public static ArrayList<String> variables = new ArrayList<>();
    public static String getVariables(){
        StringBuilder staticVars = new StringBuilder();
        for(String var : variables){
            staticVars.append(var).append(";\n");
        }
        return staticVars.toString();
    }
}
