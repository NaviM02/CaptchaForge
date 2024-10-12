package com.navi.captchaapi.parser_lexer.cc;
import java_cup.runtime.*;
import static com.navi.captchaapi.parser_lexer.cc.sym.*;
import com.navi.captchaapi.parser_lexer.*;
%% //separador de area

%public
%class CCLexer
%cup
%line
%column

a = [aA]
b = [bB]
c = [cC]
d = [dD]
e = [eE]
//f = [fF]
g = [gG]
h = [hH]
i = [iI]
//j = [jJ]
k = [kK]
l = [lL]
m = [mM]
n = [nN]
o = [oO]
p = [pP]
//q = [qQ]
r = [rR]
s = [sS]
t = [tT]
u = [uU]
v = [vV]
//w = [wW]
x = [xX]
y = [yY]
//z = [zZ]

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]

/* ___Reserved words___ */

    /* CC labels */
c_cc = {c}_{c}{c}
c_head = {c}_{h}{e}{a}{d}
c_title = {c}_{t}{i}{t}{l}{e}
c_link = {c}_{l}{i}{n}{k}
c_body = {c}_{b}{o}{d}{y}
c_spam = {c}_{s}{p}{a}{m}
c_input = {c}_{i}{n}{p}{u}{t}
c_textarea = {c}_{t}{e}{x}{t}{a}{r}{e}{a}
c_select = {c}_{s}{e}{l}{e}{c}{t}
c_option = {c}_{o}{p}{t}{i}{o}{n}
c_div = {c}_{d}{i}{v}
c_img = {c}_{i}{m}{g}
c_br = {c}_{b}{r}
c_button = {c}_{b}{u}{t}{t}{o}{n}
c_h1 = {c}_{h}1
c_p = {c}_{p}
c_scripting = {c}_{s}{c}{r}{i}{p}{t}{i}{n}{g}

    /* Parameters */
href = (href)
background = (background)
color = (color)
font_size = (font-size)
font_family = (font-family)
text_align = (text-align)
type = (type)
id = (id)
name = (name)
cols = (cols)
rows = (rows)
class = (class)
src = (src)
width = (width)
height = (height)
alt = (alt)
onclick = (onclick)

    /* Data type */
integer = (integer)
decimal = (decimal)
boolean = (boolean)
char = (char)
string = (string)

    /* Functions */
asc = (ASC)
desc = (DESC)
letpar_num = (LETPAR_NUM)
letimpar_num = (LETIMPAR_NUM)
reverse = (REVERSE)
caracter_aleatorio = (CARACTER_ALEATORIO)
num_aleatorio = (NUM_ALEATORIO)
alert_info = (ALERT_INFO)
exit = (EXIT)
redirect = (REDIRECT)
insert = (INSERT)
getElemenById = (getElemenById)
on_load = (ON_LOAD)
    /* Mode */
global = (@global)

    /* Function Block */
init = (INIT)
end = (END)
if = (IF)
then = (THEN)
else = (ELSE)
repeat = (REPEAT)
huntil = (HUNTIL)
while = (WHILE)
thenwhile = (THENWHILE)

    /* Booleans */
true = (true)
false = (False)

/* Operators */
Plus = [+]
Minus = [-]
Times = [*]
Division = [\/]
LParen = [\(]
RParen = [\)]

relatedOperations = "==" | "!=" | "<=" | ">="
or = "||"
and = "&&"
not = "!"


/* Structures */

LBracket = [\[]
RBracket = [\]]
LBrace = [\{]
RBrace = [\}]
LThan = [<]
GThan = [>]
Colon = [:]
Semicolon = [;]
Comma = [,]
VerticalBar = [|]

/* Double String */
Q = [\"]
StringContent = [^\\\n\"]*
String = {Q}{StringContent}{Q}
/* Simple String */
QSingle = \'
StringContentSingle = ([^\'\\])([^\'\\])+
StringSingle = {QSingle}{StringContentSingle}{QSingle}
one_char = {QSingle}[^\'\\]{QSingle}

/* Numbers */
digit = [0-9]+
decinteger = (([1-9]{digit}*) | "0")
number = {decinteger}
double = {decinteger}("."{digit}?{digit}?{digit}?{digit}?)?
equal = "="

/* Comments */
init_multiline_comment =  "<!--"
end_multiline_comment = "-->"
init_inline_comment = "!!"
inline_comment = {init_inline_comment}[^\n]*
multiline_comment = {init_multiline_comment}[^]*{end_multiline_comment}
comment = {inline_comment} | {multiline_comment}

/* Identifier */
letter = [a-zA-Z]
identifier = {letter}({letter}|{digit}|_)*
text = [_`.\|a-zA-Z0-9áéíóú¿?‘’“”¡@][-_.\/`\|a-zA-Z0-9áéíóú¿?‘’“”¡@]*
function_id = "FUNCTION_"{identifier}

%{
    private Symbol symbol(int type){
        return new Symbol(type, yyline+1,yycolumn+1);
    }
    private Symbol symbol(int type, Object value){
        System.out.println(type + " line: " + (yyline+1) + " col: "+(yycolumn+1) + " " + value);
        return new Symbol(type, yyline+1, yycolumn+1, value);
    }
    private void error(){
        ErrorsLP.addError(yytext(), yyline+1, yycolumn+1, "Error Léxico","Cadena no definida");
    }
%}

%%

{c_cc}
{return symbol(C_CC, yytext());          }
{c_head}
{return symbol(C_HEAD, yytext());          }
{c_title}
{return symbol(C_TITLE, yytext());          }
{c_link}
{return symbol(C_LINK, yytext());          }
{c_body}
{return symbol(C_BODY, yytext());          }
{c_spam}
{return symbol(C_SPAM, yytext());          }
{c_input}
{return symbol(C_INPUT, yytext());          }
{c_textarea}
{return symbol(C_TEXTAREA, yytext());          }
{c_select}
{return symbol(C_SELECT, yytext());          }
{c_option}
{return symbol(C_OPTION, yytext());          }
{c_div}
{return symbol(C_DIV, yytext());          }
{c_img}
{return symbol(C_IMG, yytext());          }
{c_br}
{return symbol(C_BR, yytext());          }
{c_button}
{return symbol(C_BUTTON, yytext());          }
{c_h1}
{return symbol(C_H1, yytext());          }
{c_p}
{return symbol(C_P, yytext());          }
{c_scripting}
{return symbol(C_SCRIPTING, yytext());          }

{href}
{return symbol(HREF, yytext());          }
{background}
{return symbol(BACKGROUND, yytext());          }
{color}
{return symbol(COLOR, yytext());          }
{font_size}
{return symbol(FONT_SIZE, yytext());          }
{font_family}
{return symbol(FONT_FAMILY, yytext());          }
{text_align}
{return symbol(TEXT_ALIGN, yytext());          }
{type}
{return symbol(TYPE, yytext());          }
{id}
{return symbol(ID, yytext());          }
{name}
{return symbol(NAME, yytext());          }
{cols}
{return symbol(COLS, yytext());          }
{rows}
{return symbol(ROWS, yytext());          }
{class}
{return symbol(CLASS, yytext());          }
{src}
{return symbol(SRC, yytext());          }
{width}
{return symbol(WIDTH, yytext());          }
{height}
{return symbol(HEIGHT, yytext());          }
{alt}
{return symbol(ALT, yytext());          }
{onclick}
{return symbol(ONCLICK, yytext());          }

{integer}
{return symbol(INTEGER, yytext());          }
{decimal}
{return symbol(DECIMAL, yytext());          }
{boolean}
{return symbol(BOOLEAN, yytext());          }
{char}
{return symbol(CHAR, yytext());          }
{string}
{return symbol(STRING, yytext());          }

{asc}
{return symbol(ASC, yytext());          }
{desc}
{return symbol(DESC, yytext());          }
{letpar_num}
{return symbol(LETPAR_NUM, yytext());          }
{letimpar_num}
{return symbol(LETIMPAR_NUM, yytext());          }
{reverse}
{return symbol(REVERSE, yytext());          }
{caracter_aleatorio}
{return symbol(CARACTER_ALEATORIO, yytext());          }
{num_aleatorio}
{return symbol(NUM_ALEATORIO, yytext());          }
{alert_info}
{return symbol(ALERT_INFO, yytext());          }
{exit}
{return symbol(EXIT, yytext());          }
{redirect}
{return symbol(REDIRECT, yytext());          }
{insert}
{return symbol(INSERT, yytext());          }
{getElemenById}
{return symbol(GET_ELEMENT_BY_ID, yytext());          }
{on_load}
{return symbol(ON_LOAD, yytext()); }
{function_id}
{return symbol(FUNCTION_ID, yytext());}

{global}
{return symbol(GLOBAL, yytext());}

{init}
{return symbol(INIT, yytext());}
{end}
{return symbol(END, yytext());}
{if}
{return symbol(IF, yytext());}
{then}
{return symbol(THEN, yytext());}
{else}
{return symbol(ELSE, yytext());}
{repeat}
{return symbol(REPEAT, yytext());}
{huntil}
{return symbol(HUNTIL, yytext());}
{while}
{return symbol(WHILE, yytext());}
{thenwhile}
{return symbol(THENWHILE, yytext());}
{true}
{return symbol(TRUE, yytext());}
{false}
{return symbol(FALSE, yytext());}

{Plus}
{return symbol(PLUS, yytext());}
{Minus}
{return symbol(MINUS, yytext());}
{Times}
{return symbol(TIMES, yytext());}
{Division}
{return symbol(SLASH, yytext());}
{LParen}
{return symbol(LPAREN, yytext());}
{RParen}
{return symbol(RPAREN, yytext());}
{relatedOperations}
{return symbol(REL_OP, yytext());}
{or}
{return symbol(OR, yytext());}
{and}
{return symbol(AND, yytext());}
{not}
{return symbol(NOT, yytext());}

{LBracket}
{return symbol(LBRACKET, yytext());}
{RBracket}
{return symbol(RBRACKET, yytext());}
{LBrace}
{return symbol(LBRACE, yytext());}
{RBrace}
{return symbol(RBRACE, yytext());}
{LThan}
{return symbol(LT, yytext());}
{GThan}
{return symbol(GT, yytext());}
{Colon}
{return symbol(COLON, yytext());}
{Semicolon}
{return symbol(SEMICOLON, yytext());}
{Comma}
{return symbol(COMMA, yytext());}
{VerticalBar}
{return symbol(VERTICAL_BAR, yytext());}


{String}
{return symbol(STRING_LITERAL, yytext()); }
{StringSingle}
{return symbol(SIMPLE_STRING, yytext()); }
{one_char}
{return symbol(CHAR_LITERAL, yytext()); }

{number}
{return symbol(NUMBER, yytext());}
{double}
{return symbol(NUMBER, yytext());}
{equal}
{return symbol(EQUAL, yytext());}
{comment}
{/*return symbol(COMMENT, yytext());*/}
{identifier}
{return symbol(IDENTIFIER, yytext());}
{text}
{return symbol(TEXT, yytext());}




{WhiteSpace} { /* ignore */ }

[\^°¬\w]+
{ErrorsLP.addError(yytext(), yyline+1, yycolumn+1, "Error Léxico","Cadena no definida");}

[^]                 {error(); }


<<EOF>>             {return symbol(EOF); }