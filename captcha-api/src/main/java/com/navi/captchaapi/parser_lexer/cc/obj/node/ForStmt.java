package com.navi.captchaapi.parser_lexer.cc.obj.node;
import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.SymTableVisitor;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.Visitor;
import com.navi.captchaapi.parser_lexer.cc.obj.node.interfaces.TableHolder;
import lombok.*;

import java.util.List;

@Getter @Setter
public class ForStmt extends Node implements TableHolder {
    private List<Node> body;
    private VariableDeclarator init;
    private Assignment assignment;

    private Expr test;
    private String update;
    private SymTable table;

    public ForStmt(Location loc, List<Node> body, VariableDeclarator init, Expr test, String update) {
        super(loc, "");
        this.setTextNode(body, init, test);
        this.body = body;
        this.init = init;
        this.init.setType(Type.INT);  // Asignamos el tipo de la variable
        this.test = test;
        this.update = update;
        this.table = new SymTable("Para");  // Inicializamos la tabla de símbolos
    }
    public ForStmt(Location loc, String text, List<Node> body, Assignment assignment, Expr test, String update) {
        super(loc, text);
        this.body = body;
        this.assignment = assignment;
        this.init.setType(Type.INT);  // Asignamos el tipo de la variable
        this.test = test;
        this.update = update;
        this.table = new SymTable("Para");  // Inicializamos la tabla de símbolos
    }
    @Override
    public void accept(Visitor visitor, SymTable ambit) {
        if (ambit != null) {
            visitor.setAmbit(ambit);
        }
        if (visitor.getClass().getName().equals(SymTableVisitor.class.getName())) {
            visitor.visit(this);
        } else {
            for (Node child : this.body) {
                child.accept(visitor, this.table);
            }
            this.init.accept(visitor, ambit);
            //this.assignment.accept(visitor, ambit);
            this.test.accept(visitor, this.table);
            visitor.visit(this);
        }
    }
    private void setTextNode(List<Node> body, VariableDeclarator init, Expr test) {
        init.setText("let " + init.getText());
        StringBuilder sb = new StringBuilder();

        for(Node n: body){
            sb.append(n.getText()).append('\n');
        }
        this.setText("for("+ init.getText() + ";" + init.getId().getText() + " < " + test.getText()+";"+ init.getId().getText() +"++){\n"+ sb + "}");
    }
}
