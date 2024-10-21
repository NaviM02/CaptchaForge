package com.navi.captchaapi.parser_lexer.cc.obj.analyze;
import com.navi.captchaapi.parser_lexer.cc.obj.node.Node;
import com.navi.captchaapi.parser_lexer.cc.obj.node.*;
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
        switch (nodeName) {
            case "Program":
                visitProgram((Program) node);
                break;
            case "ImportDeclaration":
                //visitImportDeclaration((ImportDeclaration) node);
                break;
            case "Incerteza":
                //visitIncerteza((Incerteza) node);
                break;
            case "Identifier":
                visitIdentifier((Identifier) node);
                break;
            case "VariableDeclarator":
                visitVariableDeclarator((VariableDeclarator) node);
                break;
            case "BinaryExpression":
                visitBinaryExpression((BinaryExpression) node);
                break;
            case "UnaryExpression":
                visitUnaryExpression((UnaryExpression) node);
                break;
            case "Assignment":
                visitAssignment((Assignment) node);
                break;
            case "CallFunction":
                visitCallFunction((CallFunction) node);
                break;
            case "FunctionParam":
                visitFunctionParam((FunctionParam) node);
                break;
            case "ReturnStmt":
                //visitReturnStmt((ReturnStmt) node);
                break;
            case "ContinueStmt":
                //visitContinueStmt((ContinueStmt) node);
                break;
            case "BreakStmt":
                //visitBreakStmt((BreakStmt) node);
                break;
            case "FunctionDeclaration":
                visitFunctionDeclaration((FunctionDeclaration) node);
                break;
            case "FunctionMain":
                //visitFunctionMain((FunctionMain) node);
                break;
            case "IfStmt":
                visitIfStmt((IfStmt) node);
                break;
            case "ForStmt":
                visitForStmt((ForStmt) node);
                break;
            case "WhileStmt":
                visitWhileStmt((WhileStmt) node);
                break;
            case "Mostrar":
                //visitMostrar((Mostrar) node);
                break;
            case "DibujarAST":
                //visitDibujarAST((DibujarAST) node);
                break;
            case "DibujarEXP":
                //visitDibujarEXP((DibujarEXP) node);
                break;
            case "DibujarTS":
                //visitDibujarTS((DibujarTS) node);
                break;
            default:
                throw new IllegalArgumentException("Unknown node type: " + nodeName);
        }
    }
    // Métodos abstractos para cada tipo de nodo que serán implementados por subclases
    //public abstract void visitBreakStmt(BreakStmt node);
    public abstract void visitFunctionDeclaration(FunctionDeclaration node);
    //public abstract void visitFunctionMain(FunctionMain node);
    public abstract void visitIfStmt(IfStmt node);
    public abstract void visitForStmt(ForStmt node);
    public abstract void visitWhileStmt(WhileStmt node);
    //public abstract void visitMostrar(Mostrar node);
    //public abstract void visitDibujarAST(DibujarAST node);
    //public abstract void visitDibujarEXP(DibujarEXP node);
    //public abstract void visitDibujarTS(DibujarTS node);
    public abstract void visitBinaryExpression(BinaryExpression node);
    public abstract void visitUnaryExpression(UnaryExpression node);
    public abstract void visitAssignment(Assignment node);
    public abstract void visitCallFunction(CallFunction node);
    public abstract void visitFunctionParam(FunctionParam node);
    //public abstract void visitReturnStmt(ReturnStmt node);
    //public abstract void visitContinueStmt(ContinueStmt node);
    public abstract void visitIdentifier(Identifier node);
    public abstract void visitVariableDeclarator(VariableDeclarator node);
    public abstract void visitProgram(Program node);
    //public abstract void visitImportDeclaration(ImportDeclaration node);
    //public abstract void visitIncerteza(Incerteza node);
}
