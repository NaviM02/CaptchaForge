package com.navi.captchaapi.parser_lexer.cc.obj.node;
import lombok.*;
import java.util.HashMap;

@Setter @Getter
public class SymTable {
    private String name;
    private Type returnedType = Type.VOID;


    private SymTable upperAmbit;
    private double incert = 0.5;

    public SymTable(String name) {
        this.name = name;
        this.upperAmbit = null;
    }

    public void addUpperAmbit(SymTable upperTable) {
        this.upperAmbit = upperTable;
    }





}
