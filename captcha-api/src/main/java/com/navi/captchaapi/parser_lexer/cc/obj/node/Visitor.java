package com.navi.captchaapi.parser_lexer.cc.obj.node;
import lombok.*;

@Getter @Setter
public abstract class Visitor {
    protected SymTable global;
    protected SymTable ambit;
    protected boolean correct = true;
    protected String filename;

    public Visitor(String filename, SymTable ambit) {
        if (ambit != null) {
            this.ambit = ambit;
        } else {
            this.ambit = new SymTable("base");
        }
        this.global = new SymTable("base");
        this.filename = filename;
    }
    public Visitor(String filename) {
        this(filename, null);
    }


    // Metodo principal para visitar nodos
    public void visit(Node node) {
        String nodeName = node.getClass().getSimpleName();

    }
}
