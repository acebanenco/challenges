grammar Rules;

import Expression;

rules:
    LF* ( subst (LF+|EOF) )*
;

subst:
    pattern=expr EQUALS replacement=expr
;

