package com.navi.captchaapi.parser_lexer.cc.obj.node;
import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.Visitor;
import lombok.*;

@Getter @Setter
public abstract class Expr extends Node {
    protected Type type;
    protected Object value;

    public Expr(Location loc, String text, Type type) {
        super(loc, text);
        this.type = type;
        this.value = null;
    }
    public abstract void accept(Visitor visitor, SymTable ambit);

}
