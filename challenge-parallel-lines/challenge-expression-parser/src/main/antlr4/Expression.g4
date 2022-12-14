grammar Expression;

expr
	:	value=DIGITS                                               # constant
	|	name=ID derivative=DERIVATIVE?                             # id
	|	OPEN exp=expr CLOSE derivative=DERIVATIVE*                 # group
	|	func=ID derivative=DERIVATIVE? OPEN arg=expr CLOSE         # function
	|	(unaryPlusOp=PLUS | unaryMinusOp=MINUS) exp=expr	       # unary
	|	<assoc=right> base=expr POWER exponent=expr                # exponent
	|	lhs=expr (divideOp=DIVIDE | multiplyOp=MULTIPLY) rhs=expr  # multiplyOrDivide
	|	lhs=expr (plusOp=PLUS | minusOp=MINUS) rhs=expr	           # addOrSubtract
	;

ID             : [a-zA-Z_$]([a-zA-Z0-9_$])* ;
DIGITS         : [0-9]+(.[0-9]+)? ;
NUM            : DIGITS;
DERIVATIVE     : '\''ID?;
PLUS           : '+';
MINUS          : '-';
MULTIPLY       : '*';
DIVIDE         : '/';
POWER          : '^'|'**';
OPEN           : '(';
CLOSE          : ')';
EQUALS         : '=';

WS             : [ \t]+ -> skip;
LF             : ('\r\n'|'\n');
