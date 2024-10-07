package com.navi.captchaapi.parser_lexer.cc.obj.node;
import lombok.*;

@Getter @Setter
public abstract class Expr extends Node {
    protected Type type;
    protected Object value;

    public Expr(Location loc, Type type) {
        super(loc);
        this.type = type;
        this.value = null;
    }
    public abstract void accept(Visitor visitor, SymTable ambit);

}
