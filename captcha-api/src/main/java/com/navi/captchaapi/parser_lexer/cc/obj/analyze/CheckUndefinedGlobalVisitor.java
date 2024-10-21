package com.navi.captchaapi.parser_lexer.cc.obj.analyze;

import com.navi.captchaapi.parser_lexer.ErrorsLP;
import com.navi.captchaapi.parser_lexer.cc.obj.node.*;

public class CheckUndefinedGlobalVisitor extends Visitor {
    public CheckUndefinedGlobalVisitor(String filename, SymTable ambit) {
        super(filename, ambit);
    }

    public CheckUndefinedGlobalVisitor(String filename) {
        super(filename);
    }

    @Override
    public void visitIdentifier(Identifier node) {
        VariableDeclarator variable = (this.ambit != null) ? this.ambit.getVariable(node.getName(), this.global) : null;
        if (variable == null) {
            ErrorsLP.logError(node.getLoc(), "La variable '" + node.getName() + "' no existe");
        } else if (variable.getInit() == null && !variable.isParam()) {
            ErrorsLP.logError(node.getLoc(), "La variable '" + node.getName() + "' no está inicializada");
        }
    }

    @Override
    public void visitVariableDeclarator(VariableDeclarator node) {

    }
    @Override
    public void visitProgram(Program node) {

    }

    @Override
    public void visitAssignment(Assignment node) {
        VariableDeclarator variable = (this.ambit != null) ? this.ambit.getVariable(node.getId().getName(), this.global) : null;
        if (variable == null) {
            ErrorsLP.logError(node.getLoc(), "La variable '" + node.getId().getName() + "' no existe");
        } else {
            if (variable.getInit() == null && !variable.isParam()) {
                variable.setInit(node.getExpression());
            }
        }
        node.getExpression().accept(this, null);
    }

    @Override
    public void visitCallFunction(CallFunction node) {

    }

    @Override
    public void visitFunctionParam(FunctionParam node) {

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
        if (node.getArgument().getClass().getSimpleName().equals(Identifier.class.getSimpleName())) {
            String name = ((Identifier) node.getArgument()).getName();
            VariableDeclarator variable = (this.ambit != null) ? this.ambit.getVariable(name, this.global) : null;
            if (variable != null) {
                node.setType(variable.getType());
            }
        }
    }
}
