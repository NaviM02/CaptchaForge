package com.navi.captchaapi.parser_lexer.cc.obj.node;

import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.Visitor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class Node {
    public Location loc;
    private String text;
    public Node (Location loc, String text) {
        this.loc = loc;
        this.text = text;
    }
    public abstract void accept(Visitor visitor, SymTable symTable);
}
