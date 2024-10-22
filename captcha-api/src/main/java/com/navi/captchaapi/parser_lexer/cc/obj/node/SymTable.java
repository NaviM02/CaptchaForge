package com.navi.captchaapi.parser_lexer.cc.obj.node;
import lombok.*;
import java.util.HashMap;

@Setter @Getter
public class SymTable {
    private String name;
    private Type returnedType = Type.VOID;
    private HashMap<String, VariableDeclarator> symbolVars = new HashMap<>(); // Variables del ámbito actual
    private HashMap<String, HashMap<String, FunctionDeclaration>> symbolFuncs = new HashMap<>(); // Funciones del ámbito actual
    private SymTable upperAmbit; // Referencia al ámbito superior
    //private double incert = 0.5;

    public SymTable(String name) {
        this.name = name;
        this.upperAmbit = null;
    }

    public void addUpperAmbit(SymTable upperTable) {
        this.upperAmbit = upperTable;
    }

    public VariableDeclarator getVariable(String id, SymTable global) {
        VariableDeclarator variable = symbolVars.get(id);
        if (variable != null) {
            return variable;
        } else if (upperAmbit != null && upperAmbit.getVariable(id, global) != null) {
            return upperAmbit.getVariable(id, global);
        } else if (global != null) {
            return global.getVariable(id, null);
        }
        return null;
    }

    public FunctionDeclaration getFunction(String id, SymTable global) {
        String reg = "\\(.*\\)";
        String generalId = id.replaceAll(reg, "");
        HashMap<String, FunctionDeclaration> funcs = symbolFuncs.get(generalId);
        if (funcs != null && funcs.get(id) != null) {
            return funcs.get(id);
        } else if (upperAmbit != null && upperAmbit.getFunction(id, global) != null) {
            return upperAmbit.getFunction(id, global);
        } else if (global != null) {
            return global.getFunction(id, null);
        }
        return null;
    }

    public boolean addVariable(VariableDeclarator variable) {
        if (isInsertableVar(variable.getId().getName())) {
            if(variable.isGlobal()){
                SymTable aux = upperAmbit;
                var aux2 = symbolVars;
                while (aux != null){
                    aux2 = aux.getSymbolVars();
                    aux = aux.getUpperAmbit();
                }
                aux2.put(variable.getId().getName(), variable);
            }
            else{
                symbolVars.put(variable.getId().getName(), variable);
            }
            return true;
        }
        return false;
    }

    public boolean addFunc(FunctionDeclaration func) {
        if (isInsertableFunc(func.getNameForTable())) {
            symbolFuncs.computeIfAbsent(func.getId(), k -> new HashMap<>()).put(func.getNameForTable(), func);
            return true;
        }
        return false;
    }

    public boolean isInsertableVar(String id) {
        return symbolVars.get(id) == null;
    }

    public boolean isInsertableFunc(String id) {
        String reg = "\\(.+\\)";
        String generalId = id.replaceAll(reg, "");
        HashMap<String, FunctionDeclaration> funcs = symbolFuncs.get(generalId);
        return funcs == null || funcs.get(id) == null;
    }

    public boolean itExistsThisFunctionId(String id) {
        HashMap<String, FunctionDeclaration> funcs = symbolFuncs.get(id);
        if (funcs != null) {
            return true;
        }
        if (upperAmbit == null) {
            return false;
        }
        return upperAmbit.itExistsThisFunctionId(id);
    }


}
