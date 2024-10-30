package com.navi.captchaapi.parser_lexer.cc.obj.analyze;

import com.navi.captchaapi.parser_lexer.ErrorsLP;
import com.navi.captchaapi.parser_lexer.cc.obj.node.*;

public class SymTableGlobalVisitor extends Visitor {
    public SymTableGlobalVisitor(String filename, SymTable ambit) {
        super(filename, ambit);
    }

    public SymTableGlobalVisitor(String filename) {
        super(filename);
    }

    @Override
    public void visitProgram(Program node) {
        // Primero agrega todos los métodos globales a la Tabla de Símbolos
        for (Node child : node.getBody()) {
            if (child instanceof FunctionDeclaration func) {
                func.setFile(this.filename);
                if (!node.getTable().addFunc(func)) {
                    ErrorsLP.logError(
                            func.loc,
                            "La función " + func.getNameForTable() + ":" + func.getType().toString() + " ya está definida"
                    );
                }
            } /*else if (child instanceof functionMain) {
                var func = (functionMain) child;
                if (node.main != null) {
                    ErrorsLP.logError(
                            func.loc,
                            "Este archivo ya tiene una función Principal():Void"
                    );
                } else {
                    node.main = func;
                }
            }*/

        }

        // Luego agrega variables y comprueba el uso de variables indefinidas
        for (Object child : node.getBody()) {
            CheckUndefinedGlobalVisitor checkUndefined = new CheckUndefinedGlobalVisitor(
                    this.filename, node.getTable()
            );
            if (child instanceof VariableDeclarator variable) {
                variable.setFile(this.filename);
                if (!node.getTable().addVariable(variable)) {
                    ErrorsLP.logError(
                            variable.getId().loc,
                            "El identificador '" + variable.getId().getName() + "' ya está definido"
                    );
                } else {
                    if (variable.getInit() != null) {
                        variable.getInit().accept(checkUndefined, null);
                    }
                }
            } else if (child instanceof Assignment) {
                checkUndefined.visit((Assignment) child);
                ((Assignment) child).accept(checkUndefined, null);
            }
        }

        var simT = new SymTableVisitor(this.filename);
        simT.setGlobal(node.getTable());
        node.accept(simT, null);
        var expr = new ExpressionsVisitor(this.filename);
        expr.setGlobal(node.getTable());
        node.accept(expr, null);
    }


    @Override
    public void visitFunctionDeclaration(FunctionDeclaration node) {

    }

    @Override
    public void visitIfStmt(IfStmt node) {

    }

    @Override
    public void visitForStmt(ForStmt node) {

    }

    @Override
    public void visitWhileStmt(WhileStmt node) {

    }

    @Override
    public void visitBinaryExpression(BinaryExpression node) {

    }

    @Override
    public void visitUnaryExpression(UnaryExpression node) {

    }

    @Override
    public void visitAssignment(Assignment node) {

    }

    @Override
    public void visitCallFunction(CallFunction node) {

    }

    @Override
    public void visitFunctionParam(FunctionParam node) {

    }


    @Override
    public void visitIdentifier(Identifier node) {

    }

    @Override
    public void visitVariableDeclarator(VariableDeclarator node) {

    }

}
