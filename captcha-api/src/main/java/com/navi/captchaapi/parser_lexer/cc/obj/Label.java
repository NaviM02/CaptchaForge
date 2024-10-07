package com.navi.captchaapi.parser_lexer.cc.obj;
import lombok.*;

import java.util.ArrayList;

@Getter @Setter @ToString
public class Label {
    public static final int HTML = 1;
    public static final int HEAD = 2;
    public static final int TITLE = 3;
    public static final int LINK = 4;
    public static final int BODY = 5;
    public static final int SPAM = 6;
    public static final int INPUT = 7;
    public static final int TEXTAREA = 8;
    public static final int SELECTED = 9;
    public static final int OPTION = 10;
    public static final int DIV = 11;
    public static final int IMG = 12;
    public static final int BR = 13;
    public static final int BUTTON = 14;
    public static final int H1 = 15;
    public static final int P = 16;
    public static final int SCRIPT = 17;

    public static final String[] LABELS = {"HTML", "HEAD", "TITLE", "LINK", "BODY", "SPAM", "INPUT",
        "TEXTAREA", "SELECTED", "OPTION", "DIV", "IMG", "BR", "BUTTON", "H1", "P", "SCRIPT"};

    private ArrayList<Label> labels;
    private int type;
    private int line, col;

    public Label(){

    }
    public Label(int type, int line, int col){
        this.type = type;
        this.line = line;
        this.col = col;
        this.labels = new ArrayList<>();
    }
    public Label(int type, int line, int col, ArrayList<Label> labels){
        this.type = type;
        this.line = line;
        this.col = col;
        this.labels = labels;
    }

}
