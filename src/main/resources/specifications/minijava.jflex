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
"return"  { return TOKEN(RETURN);  }
"int"     { return TOKEN(INT);     }
"boolean" { return TOKEN(BOOL);    }
"if"      { return TOKEN(IF);      }
"else"    { return TOKEN(ELSE);    }
"while"   { return TOKEN(WHILE);   }
"new"     { return TOKEN(NEW);     }
"extends" { return TOKEN(EXTENDS); }
"length"  { return TOKEN(LENGTH);  }

//// Operateurs
"&&"      { return TOKEN(AND);     }
"="       { return TOKEN(ASSIGN);  }
"<"       { return TOKEN(LESS);    }
"-"       { return TOKEN(MINUS);   }
"!"       { return TOKEN(NOT);     }
"+"       { return TOKEN(PLUS);    }
"*"       { return TOKEN(TIMES);   }

//// Ponctuations 
"."       { return TOKEN(DOT);     }
";"       { return TOKEN(SEMI);    }
"["       { return TOKEN(LBRACK);  }
"]"       { return TOKEN(RBRACK);  }
"{"       { return TOKEN(LBRACE);  }
"}"       { return TOKEN(RBRACE);  }
"("       { return TOKEN(LPAREN);  }
")"       { return TOKEN(RPAREN);  }
","       { return TOKEN(COL);     }
"!"       { return TOKEN(NOT);     }

//// Literals, Identificateurs
{Integer} { return TOKEN(LIT_INT,  Integer.parseInt(yytext()));     }
{Boolean} { return TOKEN(LIT_BOOL, Boolean.parseBoolean(yytext())); }
{Ident}   { return TOKEN(IDENT,    new String(yytext())) ;          }

//// Ignore, Ramasse Miette
{Ignore}  { }
[^]       { WARN("Invalid char '" + yytext() + "'"); return TOKEN(error); }
