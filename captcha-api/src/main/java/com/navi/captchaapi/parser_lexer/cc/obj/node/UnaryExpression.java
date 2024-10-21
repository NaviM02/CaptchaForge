package com.navi.captchaapi.parser_lexer.cc.obj.node;
import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.Visitor;
import lombok.*;

@Getter @Setter
public class UnaryExpression extends Expr {
    private String operator;
    private Object argument;

    public UnaryExpression(Location loc, String text, Type type, String operator, Object argument) {
        super(loc, text, type);
        this.operator = operator;
        this.argument = argument;
    }

    @Override
    public void accept(Visitor visitor, SymTable ambit) {
        if(ambit != null) visitor.setAmbit(ambit);

        if(argument instanceof Identifier) {
            ((Identifier) argument).accept(visitor, ambit);
        }
        else if(argument instanceof CallFunction){
            ((CallFunction) argument).accept(visitor, ambit);
        }
        visitor.visit(this);
    }
}
