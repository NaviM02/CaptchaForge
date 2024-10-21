package com.navi.captchaapi.parser_lexer.cc.obj.node;
import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.Visitor;
import lombok.*;

@Getter @Setter
public class FunctionParam extends Node{
    private Type type;
    private Identifier id;

    public FunctionParam(Location loc, String text, Type type, Identifier id) {
        super(loc, text);
        this.type = type;
        this.id = id;
    }

    @Override
    public void accept(Visitor visitor, SymTable ambit) {
        if (ambit != null) visitor.setAmbit(ambit);
        id.accept(visitor, ambit);
        visitor.visit(this);
    }
}
