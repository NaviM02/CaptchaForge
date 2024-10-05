package com.navi.quizcraftweb.backend.parser_lexer.request;
import java_cup.runtime.*;
import static com.navi.quizcraftweb.backend.parser_lexer.request.sym.*;
import com.navi.quizcraftweb.backend.parser_lexer.*;
%% //separador de area

%public
%class RequestLexer
%cup
%line
%column

a = [aA]
//b = [bB]
c = [cC]
d = [dD]
e = [eE]
f = [fF]
//g = [gG]
//h = [hH]
i = [iI]
//j = [jJ]
//k = [kK]
l = [lL]
//m = [mM]
n = [nN]
o = [oO]
//p = [pP]
//q = [qQ]
r = [rR]
s = [sS]
t = [tT]
u = [uU]
v = [vV]
//w = [wW]
x = [xX]
//y = [yY]
z = [zZ]

LineTerminator = \r|\n|\r\n
WhiteSpace = {LineTerminator} | [ \t\f]

/* ___Reserved words___ */

    /* XMLSON labels */
xson = {x}{s}{o}{n}
equal = [=]
version = {v}{e}{r}{s}{i}{o}{n}
one = (1\.0)
q_mark = [?]
realizar_solicitud = ({r}{e}{a}{l}{i}{z}{a}{r}_{s}{o}{l}{i}{c}{i}{t}{u}{d})
realizar_solicitudes = ({realizar_solicitud}{e}{s})
fin = ({f}{i}{n})
solicitud = ({s}{o}{l}{i}{c}{i}{t}{u}{d})
solicitudes = ({solicitud}{e}{s})
realizada = ({r}{e}{a}{l}{i}{z}{a}{d}{a})
fin_solicitud_realizada = ({fin}_{solicitud}_{realizada})
fin_solicitudes_realizada = ({fin}_{solicitudes}_{realizada})

    /* parameters */
usuario_nuevo = (USUARIO_NUEVO)
datos_usuario = (DATOS_USUARIO)
usuario = (USUARIO)
password = (PASSWORD)
insitucion = (INSTITUCION)
nombre = (NOMBRE)
fecha_creacion = (FECHA_CREACION)

modificar_usuario = (MODIFICAR_USUARIO)
usuario_antiguo = (USUARIO_ANTIGUO)
nuevo_password = (NUEVO_PASSWORD)
fecha_modificacion = (FECHA_MODIFICACION)

eliminar_usuario = (ELIMINAR_USUARIO)

login_usuario = (LOGIN_USUARIO)
ver_trivias = (VER_TRIVIAS)
add_data = (ADD_DATA)

tiempo_total = (TIEMPO_TOTAL)
estado = (ESTADO)
punteo = (PUNTEO)

nueva_trivia = (NUEVA_TRIVIA)
parametros_trivia = (PARAMETROS_TRIVIA)
id_trivia = (ID_TRIVIA)
tiempo_pregunta = (TIEMPO_PREGUNTA)
usuario_creacion = (USUARIO_CREACION)
tema = (TEMA)

eliminar_trivia = (ELIMINAR_TRIVIA)

modificar_trivia = (MODIFICAR_TRIVIA)

agregar_componente = (AGREGAR_COMPONENTE)
parametros_componente = (PARAMETROS_COMPONENTE)

id = (ID)
trivia = (TRIVIA)
clase = (CLASE)
indice = (INDICE)
texto_visible = (TEXTO_VISIBLE)
opciones = (OPCIONES)
filas = (FILAS)
columnas = (COLUMNAS)
respuesta = (RESPUESTA)

campo_texto = (CAMPO_TEXTO)
area_texto = (AREA_TEXTO)
checkbox = (CHECKBOX)
radio = (RADIO)
fichero = (FICHERO)
combo = (COMBO)

eliminar_componente = (ELIMINAR_COMPONENTE)
modificar_componenente = (MODIFICAR_COMPONENTE)

/* Structures */

LBracket = [\[]
RBracket = [\]]
LBrace = [\{]
RBrace = [\}]
LThan = [<]
GThan = [>]
EX = [!]
Colon = [:]
Comma = [,]
VerticalBar = [|]

/* Strings */

Q = [\"]
StringContent = [a-zA-Z0-9]+[-:;$a-zA-Z0-9]*
//String = {Q}{StringContent}{Q}

/* Others */

Identifier = [-_$][-_$a-zA-Z0-9]+
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

{xson}
{return symbol(XSON, yytext());          }
{equal}
{return symbol(EQUAL, yytext());          }
{version}
{return symbol(VERSION, yytext());          }
{one}
{return symbol(ONE, yytext());          }
{q_mark}
{return symbol(QM, yytext());          }
{realizar_solicitud}
{return symbol(REALIZAR_SOLICITUD, yytext());          }
{realizar_solicitudes}
{return symbol(REALIZAR_SOLICITUDES, yytext());          }
{fin_solicitud_realizada}
{return symbol(FIN_SOLICITUD_REALIZADA, yytext());          }
{fin_solicitudes_realizada}
{return symbol(FIN_SOLICITUDES_REALIZADA, yytext());          }

{usuario_nuevo}
{return symbol(USUARIO_NUEVO, yytext());          }
{datos_usuario}
{return symbol(DATOS_USUARIO, yytext());          }
{usuario}
{return symbol(USUARIO, yytext());          }
{password}
{return symbol(PASSWORD, yytext());          }
{insitucion}
{return symbol(INSTITUCION, yytext());          }
{nombre}
{return symbol(NOMBRE, yytext());          }
{fecha_creacion}
{return symbol(FECHA_CREACION, yytext());          }


{modificar_usuario}
{return symbol(MODIFICAR_USUARIO, yytext());          }
{usuario_antiguo}
{return symbol(USUARIO_ANTIGUO, yytext());          }
{nuevo_password}
{return symbol(NUEVO_PASSWORD, yytext());          }
{fecha_modificacion}
{return symbol(FECHA_MODIFICACION, yytext());          }

{eliminar_usuario}
{return symbol(ELIMINAR_USUARIO, yytext());          }
{login_usuario}
{return symbol(LOGIN_USUARIO, yytext());          }
{ver_trivias}
{return symbol(VER_TRIVIAS, yytext());          }
{add_data}
{return symbol(ADD_DATA, yytext());          }
{tiempo_total}
{return symbol(TIEMPO_TOTAL, yytext());          }
{estado}
{return symbol(ESTADO, yytext());          }
{punteo}
{return symbol(PUNTEO, yytext());          }

{nueva_trivia}
{return symbol(NUEVA_TRIVIA, yytext());          }
{parametros_trivia}
{return symbol(PARAMETROS_TRIVIA, yytext());          }
{id_trivia}
{return symbol(ID_TRIVIA, yytext());          }
{tiempo_pregunta}
{return symbol(TIEMPO_PREGUNTA, yytext());          }
{usuario_creacion}
{return symbol(USUARIO_CREACION, yytext());          }
{tema}
{return symbol(TEMA, yytext());          }

{eliminar_trivia}
{return symbol(ELIMINAR_TRIVIA, yytext());}
{modificar_trivia}
{return symbol(MODIFICAR_TRIVIA, yytext());}

{agregar_componente}
{return symbol(AGREGAR_COMPONENTE, yytext());}
{parametros_componente}
{return symbol(PARAMETROS_COMPONENTE, yytext());}
{id}
{return symbol(ID, yytext());}
{trivia}
{return symbol(TRIVIA, yytext());}
{clase}
{return symbol(CLASE, yytext());}
{indice}
{return symbol(INDICE, yytext());}
{texto_visible}
{return symbol(TEXTO_VISIBLE, yytext());}
{opciones}
{return symbol(OPCIONES, yytext());}
{filas}
{return symbol(FILAS, yytext());}
{columnas}
{return symbol(COLUMNAS, yytext());}
{respuesta}
{return symbol(RESPUESTA, yytext());}

{campo_texto}
{return symbol(CAMPO_TEXTO, yytext());}
{area_texto}
{return symbol(AREA_TEXTO, yytext());}
{checkbox}
{return symbol(CHECKBOX, yytext());}
{radio}
{return symbol(RADIO, yytext());}
{fichero}
{return symbol(FICHERO, yytext());}
{combo}
{return symbol(COMBO, yytext());}

{eliminar_componente}
{return symbol(ELIMINAR_COMPONENTE, yytext());}
{modificar_componenente}
{return symbol(MODIFICAR_COMPONENTE, yytext());}

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
{EX}
{return symbol(EX, yytext());}
{Colon}
{return symbol(COLON, yytext());}
{Comma}
{return symbol(COMMA, yytext());}
{VerticalBar}
{return symbol(VERTICAL_BAR, yytext());}


{Q}
{return symbol(Q, yytext());}

{Integer}
{return symbol(DIGIT, yytext());}
{Identifier}
{return symbol(IDENTIFIER, yytext());}
{StringContent}
{return symbol(STRINGCONTENT, yytext());}


{WhiteSpace} { /* ignore */ }

[\^°¬¡¿\w]+
{ErrorsLP.addError(yytext(), yyline+1, yycolumn+1, "Error Léxico","Cadena no definida");}

[^]                 {error(); }


<<EOF>>             {return symbol(EOF); }