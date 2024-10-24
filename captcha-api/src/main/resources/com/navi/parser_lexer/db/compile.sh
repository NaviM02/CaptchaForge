#! /bin/bash
echo "STARTING JFLEX COMPILING"
java -jar /home/dog/flexycup/jflex-full-1.9.1.jar -d ../../../../../java/com/navi/captchaapi/parser_lexer/db DBLexer.flex

echo "STARTING CUP COMPILING"
java -jar /home/dog/flexycup/java-cup-11b.jar -parser DBParser DBParser.cup
mv DBParser.java ../../../../../java/com/navi/captchaapi/parser_lexer/db/DBParser.java
mv sym.java ../../../../../java/com/navi/captchaapi/parser_lexer/db/sym.java