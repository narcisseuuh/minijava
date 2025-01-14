package phase.b_syntax;
%% 
%include Jflex.include
%include JflexCup.include

/// Macros
WS         = [ \t\f] | \R
EOLComment = "//" .*
C89Comment = "/*" [^*]* ("*" ([^*/] [^*]*)?)* "*/"
Ignore     = {WS} | {EOLComment} | {C89Comment}
Integer    = 0 | [1-9] [0-9]*
Boolean    = "true" | "false"
Ident      = [:jletter:] [:jletterdigit:]*

%%
//// Mots Clés
"class"   { return TOKEN(CLASS);   }
"main"    { return TOKEN(MAIN);    }
"out"     { return TOKEN(OUT);     }
"println" { return TOKEN(PRINTLN); }
"public"  { return TOKEN(PUBLIC);  }
"static"  { return TOKEN(STATIC);  }
"String"  { return TOKEN(STRING);  }
"System"  { return TOKEN(SYSTEM);  }
"void"    { return TOKEN(VOID);    }

//// Operateurs

//// Ponctuations 
"."       { return TOKEN(DOT);     }
";"       { return TOKEN(SEMI);    }
"["       { return TOKEN(LBRACK);  }
"]"       { return TOKEN(RBRACK);  }
"{"       { return TOKEN(LBRACE);  }
"}"       { return TOKEN(RBRACE);  }
"("       { return TOKEN(LPAREN);  }
")"       { return TOKEN(RPAREN);  }

//// Literals, Identificateurs
{Integer} { return TOKEN(LIT_INT,  Integer.parseInt(yytext()));     }  
{Ident}   { return TOKEN(IDENT,    new String(yytext())) ;          }

//// Ignore, Ramasse Miette
{Ignore}  { }
[^]       { WARN("Invalid char '" + yytext() + "'"); return TOKEN(error); }
