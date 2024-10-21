package com.navi.captchaapi.parser_lexer.cc.obj.node;

import java.util.*;

import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.Visitor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Program extends Node{
    private List<Node> body;
    private SymTable table;
    private boolean correct = true;

    public Program(Location loc, ArrayList<Node> body) {
        super(loc, "");
        this.body = body;
        this.table = new SymTable("global");
    }

    @Override
    public void accept(Visitor visitor, SymTable ambit) {
        visitor.setAmbit(this.table);
        for(Node node : body) {
            node.accept(visitor, this.table);
        }
        visitor.visit(this);
    }
    public String getScript(){
        StringBuilder sb = new StringBuilder();
        for(Node node : body) {
            sb.append(node.getText()).append("\n");
        }
        return sb.toString();
    }
}
