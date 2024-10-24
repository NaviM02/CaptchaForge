package com.navi.captchaapi.parser_lexer.cc.obj;
import lombok.*;

@Getter @Setter @ToString
public class Location {
    public int line, col;
    public Location(int line, int col) {
        this.line = line;
        this.col = col;
    }
}
