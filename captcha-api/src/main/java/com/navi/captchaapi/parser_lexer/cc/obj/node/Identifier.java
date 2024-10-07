package com.navi.captchaapi.parser_lexer.cc.obj.node;
import lombok.*;

@Getter @Setter
public class Identifier extends Node {
    private String name;
    public Identifier(Location loc, String name) {
        super(loc);
        this.name = name;
    }

    @Override
    public void accept(Visitor visitor, SymTable ambit) {
        if(ambit != null) visitor.setAmbit(ambit);
        visitor.visit(this);
    }
}
