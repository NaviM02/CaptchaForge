package com.navi.captchaapi.parser_lexer.cc.obj.node;
import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.Visitor;
import lombok.*;

@Getter @Setter
public class Assignment extends Node {
    Identifier id;
    Expr expression;

    public Assignment(Location loc, Identifier id, Expr expression) {
        super(loc, id.getText() + " = " + expression.getText());
        this.id = id;
        this.expression = expression;
    }

    @Override
    public void accept(Visitor visitor, SymTable ambit) {
        if(ambit != null) visitor.setAmbit(ambit);
        this.id.accept(visitor, ambit);
        this.expression.accept(visitor, ambit);
        visitor.visit(this);
    }
}
