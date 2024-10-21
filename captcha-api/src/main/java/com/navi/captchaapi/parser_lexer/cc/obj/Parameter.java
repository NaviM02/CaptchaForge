package com.navi.captchaapi.parser_lexer.cc.obj;
import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor
public class Parameter {
    public static final int HREF = 1;
    public static final int BACKGROUND = 2;
    public static final int COLOR = 3;
    public static final int FONT_SIZE = 4;
    public static final int FONT_FAMILY = 5;
    public static final int TEXT_ALIGN = 6;
    public static final int TYPE = 7;
    public static final int ID = 8;
    public static final int NAME = 9;
    public static final int COLS = 10;
    public static final int ROWS = 11;
    public static final int CLASS = 12;
    public static final int SRC = 13;
    public static final int WIDTH = 14;
    public static final int HEIGHT = 15;
    public static final int ALT = 16;
    public static final int ONCLICK = 17;
    public static final String[] PARAMETERS = {"href", "background", "color", "font_size",
        "font_family", "text_align", "type", "id", "name", "cols", "rows", "class",
        "src", "width", "height", "alt", "onclick"};

    private Location loc;
    private int type;
    private String value;
    public Parameter(){

    }
    public Parameter(int line, int col, int type, String value) {
        this.loc = new Location(line, col);
        this.type = type;
        this.value = value;

    }
    public String getParam(){
        System.out.println(PARAMETERS[type-1] + " = \"" + value + "\"");
        return PARAMETERS[type-1] + " = \"" + value + "\"";
    }

    public void defaultParam(){
        switch (type) {
            case COLOR -> {

            }
        }
    }
}
