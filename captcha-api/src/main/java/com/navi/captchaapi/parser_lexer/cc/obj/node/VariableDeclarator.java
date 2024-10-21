package com.navi.captchaapi.parser_lexer.cc.obj.node;
import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.Visitor;
import lombok.*;

import java.util.List;

@Getter @Setter
public class VariableDeclarator extends Node {
    private Identifier id;
    private Expr init;
    private Type type;
    private Object value;
    private boolean isParam = false;
    private String file = "";

    public VariableDeclarator(Location loc, Identifier id, Expr init) {
        super(loc, "");
        if(init != null) setText(id.getText()+" = "+ init.getText());
        else setText(id.getText());
        this.id = id;
        this.init = init;
        this.type = null;
        this.value = null;
    }

    public void setParam() {
        this.isParam = true;
    }

    @Override
    public void accept(Visitor visitor, SymTable ambit) {
        if(ambit != null) visitor.setAmbit(ambit);
        if(this.init != null) this.init.accept(visitor, ambit);
        this.id.accept(visitor, ambit);
        visitor.visit(this);
    }

}
