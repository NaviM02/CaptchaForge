package com.navi.captchaapi.parser_lexer.cc.obj.node;

import com.navi.captchaapi.parser_lexer.cc.obj.Location;

public abstract class Node {
    public Location loc;

    public Node (Location loc) {
        this.loc = loc;
    }
    public abstract void accept(Visitor visitor, SymTable symTable);
}
