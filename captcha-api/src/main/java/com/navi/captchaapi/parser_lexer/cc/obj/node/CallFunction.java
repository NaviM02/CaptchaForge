package com.navi.captchaapi.parser_lexer.cc.obj.node;
import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.Visitor;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter @Setter
public class CallFunction extends Node {
    private String callee;
    private List<Expr> args;
    private Object returnedValue;

    public CallFunction(Location loc, String callee, List<Expr> args) {
        super(loc, "");
        this.setTextNode(callee, args);
        this.callee = callee;
        this.args = args;
    }

    @Override
    public void accept(Visitor visitor, SymTable ambit) {
        if(ambit != null) visitor.setAmbit(ambit);
        for(Expr expr : args) expr.accept(visitor, ambit);
        visitor.visit(this);
    }
    public String getTableName(){
        String funcTypes = args.stream()
                .map(arg -> arg.getType() == null ? "null" : arg.getType().toString())
                .collect(Collectors.joining(","));
        return callee + "(" + funcTypes + ")";
    }
    private void setTextNode(String callee, List<Expr> args) {
        StringBuilder sb1 = new StringBuilder();
        for (Expr node : args) {
            sb1.append(node.getText()).append(", ");
        }
        if(sb1.isEmpty()) sb1.append(", ");
        setText(callee + "(" + sb1.substring(0, sb1.length() - 2) + ")");
    }
}
