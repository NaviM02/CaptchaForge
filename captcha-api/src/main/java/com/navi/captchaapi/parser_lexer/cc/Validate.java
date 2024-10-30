package com.navi.captchaapi.parser_lexer.cc;

import java.util.HashMap;

public class Validate {
    private static HashMap<String, String> SYMBOLNAMES;

    public static HashMap<String, String> getSymbolNames(){
        if(SYMBOLNAMES == null) {
            SYMBOLNAMES = new HashMap<>();
            SYMBOLNAMES.put("COMMA", ",");
            SYMBOLNAMES.put("COLON", ":");
            SYMBOLNAMES.put("SEMICOLON", ";");

            SYMBOLNAMES.put("PLUS", "+");
            SYMBOLNAMES.put("MINUS", "-");
            SYMBOLNAMES.put("TIMES", "*");
            SYMBOLNAMES.put("SLASH", "/");
            SYMBOLNAMES.put("REL_OP", "==' o '!=' o '<' o '<=' o '>' o '>='");
            SYMBOLNAMES.put("OR", "||");
            SYMBOLNAMES.put("AND", "&&");
            SYMBOLNAMES.put("NOT", "!");
            SYMBOLNAMES.put("STRING_LITERAL", "Cadena de texto valida");
            SYMBOLNAMES.put("SIMPLE_STRING", "Cadena de texto valida");
            SYMBOLNAMES.put("CHAR_LITERAL", "Caracter valido");
            SYMBOLNAMES.put("NUMBER", "Numero valido");
            SYMBOLNAMES.put("EQUAL", "=");
            SYMBOLNAMES.put("IDENTIFIER", "Identificador de variable");
            SYMBOLNAMES.put("TEXT", "Texto");
            SYMBOLNAMES.put("FUNCTION_ID", "Identificador de funcion");
            SYMBOLNAMES.put("GLOBAL", "@global");

            SYMBOLNAMES.put("INTEGER", "integer");
            SYMBOLNAMES.put("DECIMAL", "decimal");
            SYMBOLNAMES.put("BOOLEAN", "boolean");
            SYMBOLNAMES.put("CHAR", "char");
            SYMBOLNAMES.put("STRING", "string");
            SYMBOLNAMES.put("TRUE", "true");
            SYMBOLNAMES.put("FALSE", "false");

            SYMBOLNAMES.put("VERTICAL_BAR", "|");
            SYMBOLNAMES.put("LBRACKET", "[");
            SYMBOLNAMES.put("RBRACKET", "]");
            SYMBOLNAMES.put("LBRACE", "{");
            SYMBOLNAMES.put("RBRACE", "}");
            SYMBOLNAMES.put("LT", "<");
            SYMBOLNAMES.put("GT", ">");
        }
        return SYMBOLNAMES;
    }
}
