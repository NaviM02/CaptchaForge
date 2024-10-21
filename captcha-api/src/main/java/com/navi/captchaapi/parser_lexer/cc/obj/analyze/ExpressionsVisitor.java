package com.navi.captchaapi.parser_lexer.cc.obj.analyze;
import com.navi.captchaapi.parser_lexer.ErrorsLP;
import com.navi.captchaapi.parser_lexer.cc.obj.node.*;
import lombok.*;

@Getter @Setter
public class ExpressionsVisitor extends Visitor {
    private static final String[][] COMBINATIONS = {
            {Type.BOOLEAN.name(), Type.BOOLEAN.name()},
            {Type.BOOLEAN.name(), Type.DECIMAL.name()},
            {Type.BOOLEAN.name(), Type.STRING.name()},
            {Type.BOOLEAN.name(), Type.INT.name()},
            {Type.BOOLEAN.name(), Type.CHAR.name()},
            {Type.DECIMAL.name(), Type.DECIMAL.name()},
            {Type.DECIMAL.name(), Type.STRING.name()},
            {Type.DECIMAL.name(), Type.INT.name()},
            {Type.DECIMAL.name(), Type.CHAR.name()},
            {Type.STRING.name(), Type.STRING.name()},
            {Type.STRING.name(), Type.INT.name()},
            {Type.STRING.name(), Type.CHAR.name()},
            {Type.INT.name(), Type.INT.name()},
            {Type.INT.name(), Type.CHAR.name()},
            {Type.CHAR.name(), Type.CHAR.name()},
    };

    private static final Type[] ADDITION_CAST = {
            Type.BOOLEAN, /* boolean + boolean */
            Type.DECIMAL, /* boolean + decimal */
            null, /* boolean + string */
            Type.INT, /* boolean + int */
            Type.INT, /* boolean + char */
            Type.DECIMAL, /* decimal + decimal */
            Type.STRING, /* decimal + string */
            Type.DECIMAL, /* decimal + int */
            Type.DECIMAL, /* decimal + char */
            Type.STRING, /* string + string */
            Type.STRING, /* string + int */
            Type.STRING, /* string + char */
            Type.INT, /* int + int */
            Type.INT, /* int + char */
            Type.INT, /* char + char */
    };
    private static final Type[] SUBTRACTION_CAST = {
            null, /* boolean - boolean */
            Type.DECIMAL, /* boolean - decimal */
            null, /* boolean - string */
            Type.INT, /* boolean - int */
            null, /* boolean - char */
            Type.DECIMAL, /* decimal - decimal */
            null, /* decimal - string */
            Type.DECIMAL, /* decimal - int */
            Type.DECIMAL, /* decimal - char */
            null, /* string - string */
            null, /* string - int */
            null, /* string - char */
            Type.INT, /* int - int */
            Type.INT, /* int - char */
            Type.INT, /* char - char */
    };

    private static final Type[] MULTIPLICATION_CAST = {
            Type.BOOLEAN, /* boolean * boolean */
            Type.DECIMAL, /* boolean * decimal */
            null, /* boolean * string */
            Type.INT, /* boolean * int */
            Type.INT, /* boolean * char */
            Type.DECIMAL, /* decimal * decimal */
            null, /* decimal * string */
            Type.DECIMAL, /* decimal * int */
            Type.DECIMAL, /* decimal * char */
            null, /* string * string */
            null, /* string * int */
            null, /* string * char */
            Type.INT, /* int * int */
            Type.INT, /* int * char */
            Type.INT, /* char * char */
    };

    private static final Type[] DIVISION_CAST = {
            null, /* boolean / boolean */
            Type.DECIMAL, /* boolean / decimal */
            null, /* boolean / string */
            Type.DECIMAL, /* boolean / int */
            Type.DECIMAL, /* boolean / char */
            Type.DECIMAL, /* decimal / decimal */
            null, /* decimal / string */
            Type.DECIMAL, /* decimal / int */
            Type.DECIMAL, /* decimal / char */
            null, /* string / string */
            null, /* string / int */
            null, /* string / char */
            Type.DECIMAL, /* int / int */
            Type.DECIMAL, /* int / char */
            Type.DECIMAL, /* char / char */
    };

    private static final Type[] STRING_ERROR_ASSIGN = {};

    private static final Type[] DECIMAL_ERROR_ASSIGN = {Type.STRING};
    private static final Type[] BOOLEAN_ERROR_ASSIGN = {Type.STRING, Type.DECIMAL, Type.INT, Type.CHAR};
    private static final Type[] INT_ERROR_ASSIGN = {Type.STRING};
    private static final Type[] CHAR_ERROR_ASSIGN = {Type.STRING, Type.DECIMAL, Type.BOOLEAN};

    public ExpressionsVisitor(String filename, SymTable ambit) {
        super(filename, ambit);
    }

    public ExpressionsVisitor(String filename) {
        super(filename);
    }

    @Override
    public void visitCallFunction(CallFunction node) {
        if(this.ambit !=null){
            var func = this.ambit.getFunction(node.getTableName(), this.global);
            if(func == null){
                ErrorsLP.logError(node.loc, "La función " + node.getTableName() + " no existe");
            }
        }
    }

    @Override
    public void visitBinaryExpression(BinaryExpression node) {
        var newType = getBinaryType(node.getLeft(), node.getOperator(), node.getRight());
        if(newType == null){
            ErrorsLP.logError(node.loc,  "La operación " + node.getLeft().getType() + " " + node.getOperator() + " " + node.getRight().getType() + " no es posible");
        }
        else{
            node.setType(newType);
        }
    }


    /*@Override
    public void visitBreakStmt(BreakStmt node) {
        boolean inCycle = false;
        SymTable upper = this.ambit;
        while (upper != null) {
            if (upper.getName().equals("Mientras") || upper.getName().equals("Para")) {
                inCycle = true;
                break;
            }
            upper = upper.getUpperAmbit();
        }
        if (!inCycle) {
            ErrorsLP.logError(node.loc, "La instrucción 'Detener' debe de ir dentro de un ciclo");
        }
    }*/

    @Override
    public void visitUnaryExpression(UnaryExpression node) {
        if(node.getArgument() instanceof CallFunction){
            var cf = (CallFunction) node.getArgument();
            if(this.ambit != null){
                var func = this.ambit.getFunction(cf.getTableName(), this.global);
                if(func != null){
                    node.setType(func.getType());
                }
            }
        }
        switch (node.getOperator()){
            case "!" -> {
                if(node.getType() != Type.BOOLEAN){
                    ErrorsLP.logError(node.loc, "La operación " + node.getOperator() + " " + node.getType() + " es inválida");
                }
            }
            case "-" -> {
                var tp = getBinaryType(new UnaryExpression(null, ("-"+node.getText()), Type.INT, "", 0), "-", node);
                if(tp != null){
                    node.setType(tp);
                }
                else{
                    ErrorsLP.logError(node.loc, "La operación " + node.getOperator() + " " + node.getType() + " es inválida");
                }
            }
        }
    }

    @Override
    public void visitAssignment(Assignment node) {
        var variable =  this.ambit.getVariable(node.getId().getName(), this.global);
        if(variable != null){
            Type[] errorType = getErrorTypes(variable.getType());
            if(errorType.length > 0 && node.getExpression().getType() != null && contains(errorType, node.getExpression().getType())){
                ErrorsLP.logError(node.loc, "La asignación " + variable.getType() + " = " + node.getExpression().getType() + " no está permitida");
            }
        }
    }

    @Override
    public void visitVariableDeclarator(VariableDeclarator node) {
        if(node.getInit() != null){
            var errorType = getErrorTypes(node.getType());
            if(errorType.length > 0 && node.getInit().getType() != null && contains(errorType, node.getInit().getType())){
                ErrorsLP.logError(node.loc, "La asignación " + node.getType() + " = " + node.getInit().getType() + " no está permitida");

            }
        }
    }

    @Override
    public void visitProgram(Program node) {

    }

    @Override
    public void visitFunctionDeclaration(FunctionDeclaration node) {}

    @Override
    public void visitIfStmt(IfStmt node) {}

    @Override
    public void visitForStmt(ForStmt node) {

    }

    @Override
    public void visitWhileStmt(WhileStmt node) {

    }

    @Override
    public void visitFunctionParam(FunctionParam node) {

    }


    @Override
    public void visitIdentifier(Identifier node) {

    }


    public Type getBinaryType(Expr left, String op, Expr right) {
        Type[] castingList = null;
        switch (op) {
            case "+" -> castingList = ADDITION_CAST;
            case "-" -> castingList = SUBTRACTION_CAST;
            case "*" -> castingList = MULTIPLICATION_CAST;
            case "/" -> castingList = DIVISION_CAST;
        }
        if (castingList != null) {
            int index = getCombinationIndex(left.getType(), right.getType());
            if (index != -1) {
                Type type = castingList[index];
                return type != null ? type : null;
            }
        } else {
            if (left.getType() == right.getType() ||
                    (left.getType() == Type.INT && right.getType() == Type.DECIMAL) ||
                    (left.getType() == Type.DECIMAL && right.getType() == Type.INT)) {
                return Type.BOOLEAN;
            }
        }
        return null;
    }
    private int getCombinationIndex(Type left, Type right) {
        for (int i = 0; i < COMBINATIONS.length; i++) {
            // Verifica ambas combinaciones: (left, right) y (right, left)
            if ((COMBINATIONS[i][0].equals(left.name()) && COMBINATIONS[i][1].equals(right.name())) ||
                    (COMBINATIONS[i][0].equals(right.name()) && COMBINATIONS[i][1].equals(left.name()))) {
                return i;
            }
        }
        return -1;
    }
    private Type[] getErrorTypes(Type type) {
        return switch (type) {
            case STRING -> STRING_ERROR_ASSIGN;
            case DECIMAL -> DECIMAL_ERROR_ASSIGN;
            case BOOLEAN -> BOOLEAN_ERROR_ASSIGN;
            case INT -> INT_ERROR_ASSIGN;
            case CHAR -> CHAR_ERROR_ASSIGN;
            default -> new Type[0];
        };
    }
    private boolean contains(Type[] array, Type value) {
        for (Type type : array) {
            if (type == value) {
                return true;
            }
        }
        return false;
    }
}
