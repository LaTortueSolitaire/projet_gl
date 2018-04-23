lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

/*Fragment used by others rules but do not produce tokens*/
fragment FLOATHEX: ('0x' | '0X') NUMHEX '.' NUMHEX ('P' | 'p') SIGN NUM ('F' | 'f' | );
fragment LETTER : 'a' .. 'z' | 'A' .. 'Z';
fragment DIGIT :  '0' .. '9';
fragment POSITIVE_DIGIT : '1' .. '9';
fragment NUM: DIGIT+;
fragment SIGN: '+' | '-' | ;
fragment EXP: ('E' | 'e') SIGN NUM;
fragment DEC: NUM '.' NUM;
fragment FLOATDEC: (DEC | DEC EXP) ('F' | 'f' | );
fragment DIGITHEX: '0' .. '9' | 'A' .. 'F' | 'a' .. 'f';
fragment NUMHEX: DIGITHEX+;
fragment STRING_CAR: ~('\n' | '\\' | '"');
fragment FILENAME : (LETTER | DIGIT | '.' | '-' | '_')+;

/* Other reserved words */
ASM: 'asm';
CLASS: 'class';
EXTENDS: 'extends';
INSTANCEOF: 'instanceof';
NEW: 'new';
NULL: 'null';
READINT: 'readInt';
READFLOAT: 'readFloat';
PRINT: 'print';
PRINTLN: 'println';
PRINTLNX: 'printlnx';
PRINTX: 'printx';
PROTECTED: 'protected';
RETURN: 'return';
THIS: 'this';
WHILE: 'while';

/* Logic */
ELSE: 'else';
IF: 'if';
ELSEIF : 'elseif';

/* Boolean */
TRUE: 'true';
FALSE: 'false';

/* Include filename */
INCLUDE : '#include' (' ')* '"' FILENAME '"' { doInclude(getText()); };


/* Labels and integer */
IDENT : (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;
INT: '0' | POSITIVE_DIGIT DIGIT*;

/* Symbols */
LEQ: '<=';
OBRACE: '{';
LT: '<';
GEQ: '>=';
GT: '>';
EQUALS: '=';
NEQ: '!=';
PLUS: '+';
MINUS: '-';
TIMES: '*';
SLASH: '/';
PERCENT: '%';
DOT: '.';
COMMA: ',';
OPARENT: '(';
CPARENT: ')';
CBRACE: '}';
EXCLAM: '!';
EQEQ: '==';
AND: '&&';
OR: '||';
SEMI: ';';



/* Floats */
FLOAT: FLOATDEC | FLOATHEX;

/* String */
STRING : '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING : '"' (STRING_CAR | '\n' | '\\"' | '\\\\')* '"';

/* Commentraires */
COM: ('//' .*? ('\n' | EOF) | '/*' .*? '*/'){skip();};





/* Ignore spaces, tabs, newlines and whitespaces */
SEP :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        ) { skip();}; // avoid producing a token
