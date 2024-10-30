package com.navi.captchaapi.parser_lexer.cc.obj.node;
import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.StaticVariables;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.SymTableVisitor;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.Visitor;
import com.navi.captchaapi.parser_lexer.cc.obj.node.interfaces.TableHolder;
import lombok.*;

import java.util.*;
import java.util.function.Consumer;

@Getter @Setter
public class IfStmt extends Node implements TableHolder {
    private Expr test;
    private List<IfStmt> elseIf;
    private List<Node> consequent;
    private List<Node> alternate;
    private SymTable table;
    private List<SymTable> tablesElseIf;
    private SymTable tableAlternate;

    public IfStmt(Location loc, Expr test, List<Node> consequent) {
        super(loc,"");
        this.test = test;
        this.consequent = consequent;
        this.elseIf = new ArrayList<>();
        this.alternate = new ArrayList<>();
        this.table = new SymTable("Si");
        this.tableAlternate = new SymTable("Sino");

    }
    public IfStmt(Location loc, Expr test, List<Node> consequent, List<IfStmt> elseIf, List<Node> alternate) {
        super(loc, "");
        //this.setTextNode(test, consequent, elseIf, alternate);
        this.test = test;
        this.consequent = consequent;
        this.elseIf = elseIf;
        this.alternate = alternate;
        this.table = new SymTable("Si");
        for(int i = 0; i < elseIf.size(); i++) {
            tablesElseIf.add(new SymTable("SinoSi"));
        }
        this.tableAlternate = new SymTable("Sino");
    }

    @Override
    public void accept(Visitor visitor, SymTable ambit) {
        if(ambit != null) visitor.setAmbit(ambit);

        if (visitor.getClass().getSimpleName().equals(SymTableVisitor.class.getSimpleName())) {
            visitor.visit(this);
        } else {
            for (Node child : consequent) {
                child.accept(visitor, table);
            }
            for (Node child : alternate) {
                child.accept(visitor, tableAlternate);
            }
            for (IfStmt child : elseIf) {
                child.accept(visitor, table);
            }
            test.accept(visitor, ambit);
            visitor.visit(this);
        }
    }

    @Override
    public List<Node> getBody() {
        return null;
    }
    public void setTextNode(Expr test, List<Node> consequent, List<IfStmt> elseIf, List<Node> alternate) {
        StringBuilder sb1 = new StringBuilder();
        for (Node node : consequent) {
            sb1.append(addTabs(node.getText()));
        }
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        if(!elseIf.isEmpty()) {
            for (IfStmt node : elseIf) {
                sb2.append("else if(").append(node.getTest().getText()).append("){\n");
                for (Node n : node.getConsequent()) {
                    sb2.append(addTabs(n.getText()));
                }
                sb2.append("}");
            }
        }
        if(!alternate.isEmpty()) {
            sb3.append("else{\n");
            for (Node node : alternate) {
                sb3.append(addTabs(node.getText()));
            }
            sb3.append("}");
        }
        setText("if("+test.getText()+"){\n"+sb1+"}\n"+sb2+"\n"+sb3);
    }
    public static String addTabs(String text) {
        return FunctionDeclaration.addTabs(text);
    }
}
