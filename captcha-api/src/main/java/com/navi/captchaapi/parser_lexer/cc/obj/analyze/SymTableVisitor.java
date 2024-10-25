package com.navi.captchaapi.parser_lexer.cc.obj.analyze;

import com.navi.captchaapi.parser_lexer.ErrorsLP;
import com.navi.captchaapi.parser_lexer.cc.obj.node.*;
import com.navi.captchaapi.parser_lexer.cc.obj.node.interfaces.TableHolder;

public class SymTableVisitor extends Visitor{
    public SymTableVisitor(String filename, SymTable ambit) {
        super(filename, ambit);
    }

    public SymTableVisitor(String filename) {
        super(filename);
    }

    public void visitBlock(TableHolder node) {
        CheckUndefinedGlobalVisitor checkUndefined = new CheckUndefinedGlobalVisitor(this.filename, node.getTable());
        checkUndefined.setGlobal(this.global);
        if (node instanceof ForStmt) {
            ((ForStmt) node).getTest().accept(checkUndefined, null);
        }

        for (Node child : node.getBody()) {
            addUpperAmbit(child, node);

            if (child instanceof VariableDeclarator) {
                VariableDeclarator variable = (VariableDeclarator) child;
                if (!node.getTable().addVariable(variable)) {
                    ErrorsLP.logError(variable.getId().getLoc(),
                            "El identificador '" + variable.getId().getName() + "' ya está definido");
                } else {
                    if (variable.getInit() != null) {
                        variable.getInit().accept(checkUndefined, null);
                    }
                }
            }

            testChildNode(child, checkUndefined);
        }
    }


    @Override
    public void visitFunctionDeclaration(FunctionDeclaration node) {
        for (FunctionParam param : node.getParams()) {
            VariableDeclarator variable = new VariableDeclarator(param.getLoc(), param.getId(), null);
            variable.setParam();
            variable.setType(param.getType());
            node.getTable().addVariable(variable);
        }
        visitBlock(node);
    }


    @Override
    public void visitWhileStmt(WhileStmt node) {
        visitBlock(node);
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
    @Override
    public void visitProgram(Program node) {

    }

    @Override
    public void visitForStmt(ForStmt node) {
        node.getTable().addVariable(node.getInit());
        visitBlock(node);
    }

    @Override
    public void visitIfStmt(IfStmt node) {
        for (Node child : node.getConsequent()) {
            CheckUndefinedGlobalVisitor checkUndefined = new CheckUndefinedGlobalVisitor(this.filename, node.getTable());
            checkUndefined.setGlobal(this.global);
            addUpperAmbit(child, node);
            if (child instanceof VariableDeclarator variable) {
                if (!node.getTable().addVariable(variable)) {
                    ErrorsLP.logError(variable.getId().getLoc(),
                            "El identificador '" + variable.getId().getName() + "' ya está definido");
                } else {
                    if (variable.getInit() != null) {
                        variable.getInit().accept(checkUndefined, null);
                    }
                }
            }

            testChildNode(child, checkUndefined);
        }

        for (Node child : node.getAlternate()) {
            CheckUndefinedGlobalVisitor checkUndefined = new CheckUndefinedGlobalVisitor(this.filename, node.getTableAlternate());
            checkUndefined.setGlobal(this.global);
            addUpperAmbit(child, node);

            if (child instanceof VariableDeclarator variable) {
                if (!node.getTableAlternate().addVariable(variable)) {
                    ErrorsLP.logError(variable.getId().getLoc(),
                            "El identificador '" + variable.getId().getName() + "' ya está definido");
                } else {
                    if (variable.getInit() != null) {
                        variable.getInit().accept(checkUndefined, null);
                    }
                }
            }

            testChildNode(child, checkUndefined);
        }
    }

    public void addUpperAmbit(Node child, TableHolder father) {
        if (child instanceof IfStmt ifStmt) {
            ifStmt.getTable().addUpperAmbit(father.getTable());
            ifStmt.getTable().setReturnedType(father.getTable().getReturnedType());
            for(IfStmt elif: ifStmt.getElseIf()){
                elif.getTable().addUpperAmbit(father.getTable());
                elif.getTable().setReturnedType(father.getTable().getReturnedType());
            }
            ifStmt.getTableAlternate().addUpperAmbit(father.getTable());
            ifStmt.getTableAlternate().setReturnedType(father.getTable().getReturnedType());
            ifStmt.accept(this, null);
        } else if (child instanceof ForStmt forStmt) {
            forStmt.getTable().addUpperAmbit(father.getTable());
            forStmt.getTable().setReturnedType(father.getTable().getReturnedType());
            forStmt.accept(this, null);
        } else if (child instanceof WhileStmt whileStmt) {
            whileStmt.getTable().addUpperAmbit(father.getTable());
            whileStmt.getTable().setReturnedType(father.getTable().getReturnedType());
            whileStmt.accept(this, null);
        }
    }

    public void testChildNode(Node child, CheckUndefinedGlobalVisitor checkUndefined) {
        if (child instanceof Assignment) {
            checkUndefined.visit((Assignment) child);
        } else if (child instanceof ForStmt) {
            if (((ForStmt) child).getInit().getInit() != null) {
                ((ForStmt) child).getInit().getInit().accept(checkUndefined, null);
            }
        } else if (child instanceof WhileStmt) {
            ((WhileStmt) child).getTest().accept(checkUndefined, null);
        } else if (child instanceof IfStmt) {
            ((IfStmt) child).getTest().accept(checkUndefined, null);
        } /*else if (child instanceof ReturnStmt) {
            if (((ReturnStmt) child).getArgument() != null) {
                ((ReturnStmt) child).getArgument().accept(checkUndefined, null);
            }
        }*/
        else if (child instanceof CallFunction) {
            ((CallFunction) child).accept(checkUndefined, null);
        }
    }
}
