package com.navi.captchaapi.parser_lexer.cc.obj.node;
import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.StaticVariables;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.SymTableVisitor;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.Visitor;
import com.navi.captchaapi.parser_lexer.cc.obj.node.interfaces.TableHolder;
import lombok.*;
import java.util.List;

@Getter @Setter
public class FunctionDeclaration extends Node implements TableHolder {
    private String id;
    private List<FunctionParam> params;
    private Type type;
    private List<Node> body;
    private SymTable table;
    private String nameForTable;
    private String file = "";

    public FunctionDeclaration(Location loc, String id, List<FunctionParam> params, Type type, List<Node> body) {
        super(loc, "");
        //this.setTextNode(id, params, body);
        this.id = id;
        this.params = params;
        this.type = type;
        this.body = body;
        this.nameForTable = this.getNameForTable();
        this.table = new SymTable("funcion " + id);
        this.table.setReturnedType(this.type);
    }

    public void accept(Visitor visitor, SymTable ambit) {
        visitor.setAmbit(this.table);
        if (visitor instanceof SymTableVisitor) {
            visitor.visit(this);
        } else {
            for (Node child : this.body) {
                child.accept(visitor, this.table);
            }
            for (FunctionParam param : this.params) {
                param.accept(visitor, this.table);
            }
            visitor.visit(this);
        }
    }

    public String getNameForTable() {
        StringBuilder funcTypes = new StringBuilder();
        for (FunctionParam param : this.params) {
            if (funcTypes.isEmpty()) {
                funcTypes.append(param.getType().toString());
            } else {
                funcTypes.append(",").append(param.getType().toString());
            }
        }
        return this.id + "(" + funcTypes + ")";
    }

    public void setTextNode(String id, List<FunctionParam> params, List<Node> body) {
        StringBuilder sb1 = new StringBuilder();
        for (FunctionParam node : params) {
            sb1.append(node.getText()).append(", ");
        }
        StringBuilder sb2 = new StringBuilder();
        for (Node node : body) {
            if(node instanceof IfStmt n) n.setTextNode(n.getTest(), n.getConsequent(), n.getElseIf(), n.getAlternate());
            else if (node instanceof ForStmt n) n.setTextNode(n.getBody(), n.getInit(), n.getTest());
            else if (node instanceof WhileStmt n) n.setTextNode(n.getBody(), n.getTest());

            sb2.append(addTabs(node.getText())).append("\n");
        }
        if(id.equals("ON_LOAD")) setText("function " + id + "(" + sb1 + "){\n"+sb2 +"}\n ON_LOAD();");
        else setText("function " + id + "(" + sb1 + "){\n"+sb2 + "\tshow();\n}");
    }
    public static String addTabs(String text) {
        String[] lines = text.split("\n");
        StringBuilder result = new StringBuilder();

        for (String linea : lines) result.append("\t").append(linea).append("\n");

        return result.toString();
    }
}
