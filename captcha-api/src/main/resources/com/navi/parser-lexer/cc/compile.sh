#! /bin/bash
echo "STARTING JFLEX COMPILING"
java -jar /home/dog/flexycup/jflex-full-1.9.1.jar -d ../../../../../java/com/navi/quizcraftweb/backend/parser_lexer/request RequestLexer.flex

echo "STARTING CUP COMPILING"
java -jar /home/dog/flexycup/java-cup-11b.jar -parser RequestParser RequestParser.cup
mv RequestParser.java ../../../../../java/com/navi/quizcraftweb/backend/parser_lexer/request/RequestParser.java
mv sym.java ../../../../../java/com/navi/quizcraftweb/backend/parser_lexer/request/sym.java