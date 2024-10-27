%{
const { Captcha } = require('../app/model/captcha');
%}

%lex
%%

[\r\n]+                    /* ignore line terminators */
[ \t\f]+                   /* ignore whitespace */

"\"ID\""                      return 'ID';
"\"NOMBRE\""                  return 'NOMBRE';
"\"HTML\""                    return 'HTML';
"\"USADO\""                   return 'USADO';
"\"ACIERTOS\""                return 'ACIERTOS';
"\"FALLOS\""                  return 'FALLOS';
"\"ULTIMO_USO\""              return 'ULTIMO_USO';

"{"                         return 'LBRACE';
"}"                         return 'RBRACE';
"["                         return 'LBRACKET';
"]"                         return 'RBRACKET';
":"                          return 'COLON';
","                          return 'COMMA';

\"[^\"]+\"                 return 'STRING';

[0-9]+                     return 'INTEGER';

<<EOF>>                    return 'EOF';

.                          return 'INVALID';
/lex


%start initialState

%%

initialState 
    : initialState0
    {
        return $1;
    }
    ;

initialState0
    : captchas_otp EOF
    {
        $$ = $1;
    }
    ;

captchas_otp 
    : LBRACKET captchas RBRACKET
    {
        $$ = $2;
    }
    | captchas
    {
        $$ = $1;
    }
    ;

captchas
    : captcha
    {
        $$ = [$1]; // Devuelve un array con el captcha
    }
    | captchas COMMA captcha
    {
        $1.push($3); // Agrega un captcha al array
        $$ = $1; // Devuelve el array actualizado
    }
    ;

captcha
    : LBRACE id name timesUsed successes fails lastUse html RBRACE
    {
        $$ = new Captcha($2, $3, $4, $5, $6, $7, $8); // Devuelve el objeto captcha
    }
    ;

id
    : ID COLON STRING COMMA
    {
        $$ = $3.replace(/\"/g, ""); // Elimina las comillas del string
    }
    ;

name
    : NOMBRE COLON STRING COMMA
    {
        $$ = $3.replace(/\"/g, ""); // Elimina las comillas del string
    }
    ;

timesUsed
    : USADO COLON INTEGER COMMA
    {
        $$ = parseInt($3, 10); // Convierte el string a un número
    }
    ;

successes
    : ACIERTOS COLON INTEGER COMMA
    {
        $$ = parseInt($3, 10); // Convierte el string a un número
    }
    ;

fails
    : FALLOS COLON INTEGER COMMA
    {
        $$ = parseInt($3, 10); // Convierte el string a un número
    }
    ;

lastUse
    : ULTIMO_USO COLON STRING COMMA
    {
        $$ = $3.replace(/\"/g, ""); // Elimina las comillas del string
    }
    ;

html
    : HTML COLON STRING
    {
        $$ = $3.replace(/\"/g, ""); // Elimina las comillas del string
    }
    ;

