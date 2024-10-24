package com.navi.captchaapi.parser_lexer.db;
import java_cup.runtime.*;
import static com.navi.captchaapi.parser_lexer.db.sym.*;
import com.navi.captchaapi.parser_lexer.*;
%% //separador de area

%public
%class DBLexer
%cup
%line
%column

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]

/* ___Reserved words___ */
db_captchas = (db\.captchas)
    /* parameters */
id = (\"ID\")
name = (\"NOMBRE\")
html = (\"HTML\")
usado = (\"USADO\")
aciertos = (\"ACIERTOS\")
fallos = (\"FALLOS\")
ultimo_uso = (\"ULTIMO_USO\")
/* Structures */

LBrace = [\{]
RBrace = [\}]
LParen = [\(]
RParen = [\)]
Colon = [:]
Comma = [,]

/* Strings */

Q = [\"]
StringContent = [^\"]+
String = {Q}{StringContent}{Q}

/* Others */
Integer = [0-9]+


%{
    private Symbol symbol(int type){
        return new Symbol(type, yyline+1,yycolumn+1);
    }
    private Symbol symbol(int type, Object value){
        //System.out.println(type + " line: " + (yyline+1) + " col: "+(yycolumn+1) + " " + value);
        return new Symbol(type, yyline+1, yycolumn+1, value);
    }
    private void error(){
        ErrorsLP.addError(yytext(), yyline+1, yycolumn+1, "Error Léxico","Cadena no definida");
    }
%}

%%
{db_captchas}
{return symbol(DB_CAPTCHAS, yytext());          }
{id}
{return symbol(ID, yytext());          }
{name}
{return symbol(NOMBRE, yytext());          }
{html}
{return symbol(HTML, yytext());          }
{usado}
{return symbol(USADO, yytext());          }
{aciertos}
{return symbol(ACIERTOS, yytext());          }
{fallos}
{return symbol(FALLOS, yytext());          }
{ultimo_uso}
{return symbol(ULTIMO_USO, yytext());         }

{LBrace}
{return symbol(LBRACE, yytext());}
{RBrace}
{return symbol(RBRACE, yytext());}
{LParen}
{return symbol(LPAREN, yytext());}
{RParen}
{return symbol(RPAREN, yytext());}
{Colon}
{return symbol(COLON, yytext());}
{Comma}
{return symbol(COMMA, yytext());}

{Integer}
{return symbol(DIGIT, Integer.parseInt(yytext()));}
{String}
{return symbol(STRING, yytext());}


{WhiteSpace} { /* ignore */ }

[\^°¬¡¿\w]+
{ErrorsLP.addError(yytext(), yyline+1, yycolumn+1, "Error Léxico","Cadena no definida");}

[^]                 {error(); }


<<EOF>>             {return symbol(EOF); }