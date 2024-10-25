package com.navi.captchaapi.parser_lexer.cc.obj.node;
import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.Visitor;
import lombok.*;

@Getter @Setter
public class BinaryExpression extends Expr {
    private Expr left;
    private String operator;
    private Expr right;

    public BinaryExpression(Location loc, Type type, Expr left, String operator, Expr right) {
        super(loc, left.getText() + operator + right.getText(), type);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public void accept(Visitor visitor, SymTable ambit) {
        if(ambit != null) visitor.setAmbit(ambit);
        this.left.accept(visitor, ambit);
        this.right.accept(visitor, ambit);
        visitor.visit(this);
    }
}
