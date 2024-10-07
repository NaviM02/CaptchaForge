package com.navi.captchaapi.parser_lexer.cc.obj.node;

import lombok.Getter;

@Getter
public enum Type {
    STRING("string"), INT("integer"), DECIMAL("decimal"), CHAR("char"),
    BOOLEAN("boolean"), VOID("void");

    private final String typeName;
    Type(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
