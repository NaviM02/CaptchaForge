package com.navi.captchaapi.parser_lexer.cc;

import java.util.HashMap;

public class Validate {
    private static HashMap<String, String> SYMBOLNAMES;

    public static HashMap<String, String> getSymbolNames(){
        if(SYMBOLNAMES == null) {
            SYMBOLNAMES = new HashMap<>();
            SYMBOLNAMES.put("COMMA", ",");
            SYMBOLNAMES.put("COLON", ":");
            SYMBOLNAMES.put("ONE", "1.0");

            SYMBOLNAMES.put("EQUAL", "=");
            SYMBOLNAMES.put("REALIZAR_SOLICITUD", "realizar_solicitud");
            SYMBOLNAMES.put("REALIZAR_SOLICITUDES", "realizar_solicitudes");
            SYMBOLNAMES.put("FIN_SOLICITUD_REALIZADA", "fin_solicitud_realizada");
            SYMBOLNAMES.put("FIN_SOLICITUDES_REALIZADA", "fin_solicitudes_realizada");
            SYMBOLNAMES.put("VERTICAL_BAR", "|");
            SYMBOLNAMES.put("LBRACKET", "[");
            SYMBOLNAMES.put("RBRACKET", "]");
            SYMBOLNAMES.put("LBRACE", "{");
            SYMBOLNAMES.put("RBRACE", "}");
            SYMBOLNAMES.put("LT", "<");
            SYMBOLNAMES.put("GT", ">");
            SYMBOLNAMES.put("EX", "!");
            SYMBOLNAMES.put("Q", "Comillas");
            SYMBOLNAMES.put("STRINGCONTENT", "Cadena válida");
            SYMBOLNAMES.put("IDENTIFIER", "ID válido");
            SYMBOLNAMES.put("QM", "?");
            SYMBOLNAMES.put("DIGIT", "Un número");
        }
        return SYMBOLNAMES;
    }
}
