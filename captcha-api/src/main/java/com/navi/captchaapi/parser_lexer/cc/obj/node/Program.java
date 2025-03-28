package com.navi.captchaapi.parser_lexer.cc.obj.node;

import java.util.*;

import com.navi.captchaapi.parser_lexer.cc.obj.Location;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.ExpressionsVisitor;
import com.navi.captchaapi.parser_lexer.cc.obj.analyze.SymTableVisitor;
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
        addFunctions();
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
            if(node instanceof FunctionDeclaration fun){
                if(addFun(fun.getId())){
                    fun.setTextNode(fun.getId(), fun.getParams(), fun.getBody());
                    sb.append(fun.getText()).append("\n");
                }
            }
            else sb.append(node.getText()).append("\n");
        }
        sb.append("\nshow();\n");
        return sb.toString();
    }
    public boolean addFun(String id){
        switch (id){
            case "INSERT", "ASC", "DESC", "LETPAR_NUM", "LETIMPAR_NUM", "REVERSE",
                 "CARACTER_ALEATORIO", "NUM_ALEATORIO", "ALERT_INFO", "EXIT",
                 "REDIRECT", "document.getElementById" -> {return false;}
            default -> {return true;}
        }
    }
    public void addFunctions(){
        var loc = new Location(0, 0);
        var paramText = new ArrayList<FunctionParam>();
        paramText.add(new FunctionParam(loc, Type.STRING, new Identifier(loc, "text")));

        addFunction(loc, "INSERT", paramText, Type.VOID, new ArrayList<>());
        addFunction(loc, "ASC", paramText, Type.STRING, new ArrayList<>());
        addFunction(loc, "DESC", paramText, Type.STRING, new ArrayList<>());
        addFunction(loc, "LETPAR_NUM", paramText, Type.STRING, new ArrayList<>());
        addFunction(loc, "LETIMPAR_NUM", paramText, Type.STRING, new ArrayList<>());
        addFunction(loc, "REVERSE", paramText, Type.STRING, new ArrayList<>());
        addFunction(loc, "CARACTER_ALEATORIO", new ArrayList<>(), Type.CHAR, new ArrayList<>());
        addFunction(loc, "NUM_ALEATORIO", new ArrayList<>(), Type.INT, new ArrayList<>());
        addFunction(loc, "ALERT_INFO", paramText, Type.VOID, new ArrayList<>());
        addFunction(loc, "EXIT", new ArrayList<>(), Type.VOID, new ArrayList<>());
        addFunction(loc, "REDIRECT", new ArrayList<>(), Type.VOID, new ArrayList<>());
        addFunction(loc, "document.getElementById", paramText, Type.STRING, new ArrayList<>());
    }

    public void addFunction(Location loc, String id, List<FunctionParam> params, Type type, List<Node> body){
        var functionDeclaration = new FunctionDeclaration(loc, id, params, type, body);
        functionDeclaration.setText("");
        this.body.add(functionDeclaration);
    }
}
