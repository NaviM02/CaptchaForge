package com.navi.captchaapi.parser_lexer.cc.obj.node;
import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.Visitor;
import lombok.*;

@Getter @Setter
public class Identifier extends Node {
    private String name;
    public Identifier(Location loc, String name) {
        super(loc, name+".v");
        this.name = name;
    }

    @Override
    public void accept(Visitor visitor, SymTable ambit) {
        if(ambit != null) visitor.setAmbit(ambit);
        visitor.visit(this);
    }
}
