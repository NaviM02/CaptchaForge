package com.navi.captchaapi.parser_lexer.cc.obj;
import lombok.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Getter @Setter @ToString
public class Label {
    public static final int HTML = 1;
    public static final int HEAD = 2;
    public static final int TITLE = 3;
    public static final int LINK = 4;
    public static final int BODY = 5;
    public static final int SPAN = 6;
    public static final int INPUT = 7;
    public static final int TEXTAREA = 8;
    public static final int SELECT = 9;
    public static final int OPTION = 10;
    public static final int DIV = 11;
    public static final int IMG = 12;
    public static final int BR = 13;
    public static final int BUTTON = 14;
    public static final int H1 = 15;
    public static final int P = 16;
    public static final int SCRIPT = 17;

    public static final String[] LABELS = {"HTML", "HEAD", "TITLE", "LINK", "BODY", "SPAN", "INPUT",
        "TEXTAREA", "SELECT", "OPTION", "DIV", "IMG", "BR", "BUTTON", "H1", "P", "SCRIPT"};

    private Location loc;
    private int type;
    private ArrayList<Parameter> parameters;
    private ArrayList<Object> content;

    public Label(){

    }
    public Label(int line, int col, int type){
        this.loc = new Location(line, col);
        this.type = type;
        this.parameters = new ArrayList<>();
        this.content = new ArrayList<>();
    }
    public Label(int line, int col, int type, ArrayList<Object> content){
        this.loc = new Location(line, col);
        this.type = type;
        this.parameters = new ArrayList<>();
        this.content = content;
    }
    public Label(int line, int col, int type, ArrayList<Parameter> parameters, ArrayList<Object> content){
        this.loc = new Location(line, col);
        this.type = type;
        this.parameters = parameters;
        this.content = content;
    }

    public String toHtml(String script){
        boolean done = false;
        parameters.forEach(Parameter::getParam);
        StringBuilder html = new StringBuilder();
        html.append("<").append(LABELS[type-1].toLowerCase()).append(">\n");
        for(Object body: content){
            if(body instanceof String str){
                html.append(str).append(" ");
            }
            else if(body instanceof Label label){
                if(label.getType() == Label.TITLE && !done){
                    html.append(script).append("\n");
                    done = true;
                }
                html.append((label).toHtml(script));
            }
        }
        html.append("</").append(LABELS[type-1].toLowerCase()).append(">\n");
        return html.toString();
    }
}
