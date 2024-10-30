package com.navi.captchaapi.parser_lexer.cc.obj.node;
import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.SymTableVisitor;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.Visitor;
import com.navi.captchaapi.parser_lexer.cc.obj.node.interfaces.TableHolder;
import lombok.*;

import java.util.List;

@Getter @Setter
public class WhileStmt extends Node implements TableHolder {
    private List<Node> body;
    private Expr test;
    private SymTable table;

    public WhileStmt(Location loc, List<Node> body, Expr test) {
        super(loc, "");
        this.setTextNode(body, test);
        this.body = body;
        this.test = test;
        this.table = new SymTable("Mientras");  // Inicializamos la tabla de símbolos
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
                child.accept(visitor, ambit);
            }
            this.test.accept(visitor, ambit);
            visitor.visit(this);
        }
    }
    public void setTextNode(List<Node> body, Expr test) {
        StringBuilder sb = new StringBuilder();

        for(Node n: body){
            sb.append(addTabs(n.getText()));
        }
        this.setText("while("+test.getText()+"){\n"+ sb + "}");
    }
    public static String addTabs(String text) {
        return FunctionDeclaration.addTabs(text);
    }
}
