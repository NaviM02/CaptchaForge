package com.navi.captchaapi.parser_lexer.cc.obj.node;
import lombok.*;

@Getter @Setter
public class Location {
    public int line, col;
    public Location(int line, int col) {
        this.line = line;
        this.col = col;
    }
}
