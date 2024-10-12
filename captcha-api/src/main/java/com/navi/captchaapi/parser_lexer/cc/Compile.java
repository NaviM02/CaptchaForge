package com.navi.captchaapi.parser_lexer.cc;

import com.navi.captchaapi.parser_lexer.ErrorsLP;

import java.io.Reader;
import java.io.StringReader;

public class Compile {
    private static Reader reader;
    private static CCLexer lexer;
    public static CCParser parser;

    public static void compile(String text){
        reader = new StringReader(text);
        lexer = new CCLexer(reader);
        parser = new CCParser(lexer);

        try {
            ErrorsLP.clearErrors();
            parser.parse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
