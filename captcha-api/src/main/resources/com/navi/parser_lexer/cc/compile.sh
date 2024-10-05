#! /bin/bash
echo "STARTING JFLEX COMPILING"
java -jar /home/dog/flexycup/jflex-full-1.9.1.jar -d ../../../../../java/com/navi/captchaapi/parser_lexer/cc CCLexer.flex

echo "STARTING CUP COMPILING"
java -jar /home/dog/flexycup/java-cup-11b.jar -parser CCParser CCParser.cup
mv CCParser.java ../../../../../java/com/navi/captchaapi/parser_lexer/cc/CCParser.java
mv sym.java ../../../../../java/com/navi/captchaapi/parser_lexer/cc/sym.java