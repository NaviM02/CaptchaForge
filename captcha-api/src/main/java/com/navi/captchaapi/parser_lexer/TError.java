package com.navi.captchaapi.parser_lexer;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class TError {
    private String lexeme;
    private int line;
    private int column;
    private String type;
    private String description;

    @Override
    public String toString() {
        if(description.equals("Se esperaba: ")) return "ERROR: " + lexeme + ", línea:" + line + ", columna: " + column + ", tipo: " + type;
        return "ERROR: " + lexeme + ", línea:" + line + ", columna: " + column + ", tipo: " + type + " (" + description + ")";
    }
}
