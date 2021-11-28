autoautoFile = 
  _ f:frontMatter? _ s:labeledStatepath+ _ {
  	return {
      type: "Program", location: location(),
      frontMatter: f,
      statepaths: s
    }
  }

frontMatter =
 DOLLAR_SIGN _ heads:(frontMatterKeyValue COMMA _)* tail:frontMatterKeyValue _ DOLLAR_SIGN {
   return {
     type: "FrontMatter", location: location(),
     values: heads.map(x=>x[0]).concat([tail])
   }
 }

frontMatterKeyValue =
 k:IDENTIFIER COLON _ v:value _ {
   return {
     type: "FrontMatterKeyValue", location: location(),
     key: k,
     value: v
   }
 }

commentOpportunity = _

labeledStatepath =
 _ l:STATEPATH_LABEL_ID COLON _ s:statepath _  {
   return { type: "LabeledStatepath", location: location(), statepath: s, label: l }
 }


statepath =
  _ heads:(state SEMICOLON)* tail:state SEMICOLON? _ {
  	return {
      type: "Statepath", location: location(),
      states: heads.map(x=>x[0]).concat([tail])
    }
  }

state =
  _ heads:(statement COMMA)* tail:statement COMMA? _ {
    return {
      type: "State", location: location(),
      statement: heads.map(x=>x[0]).concat([tail])
    }
  }

statement = singleStatement / multiStatement

multiStatement = _ OPEN_CURLY_BRACKET s:state CLOSE_CURLY_BRACKET _ {
	return { type: "Block", state: s, location: location() }
}

singleStatement =
  _  s:(functionCallStatement/funcDefStatement/afterStatement/gotoStatement/ifStatement/letStatement/nextStatement/skipStatement)  _

  { return s; }

afterStatement =
 AFTER _ u:value _ s:statement { return { type: "AfterStatement", location: location(), unitValue: u, statement: s } }

functionCallStatement =
 f:value { return { type: "FunctionCallStatement", location: location(), call: f } }

funcDefStatement = (FUNCTION / FUNC) _
	name:(IDENTIFIER/dynamicValue) _ OPEN_PAREN args:argumentList? _ CLOSE_PAREN _ b:statement
    { return { type: "FunctionDefStatement", name: name, args: args || {type:"ArgumentList",args:[], location: location()}, body: b, location: location() }; }

gotoStatement =
 GOTO _ p:(IDENTIFIER/dynamicValue) { return { type: "GotoStatement", location: location(), path: p } }

ifStatement =
 (IF/WHEN) _ OPEN_PAREN t:value CLOSE_PAREN s:statement { return { type: "IfStatement", location: location(), conditional: t, statement: s } }

letStatement =
 LET _ v:variableReference _ EQUALS _ val:value { return { type: "LetStatement", location: location(), variable: v, value: val } }

nextStatement =
 NEXT { return { type: "NextStatement", location: location() }   }

skipStatement =
 SKIP s:NUMERIC_VALUE  { return { type: "SkipStatement", location: location(), skip: s }   }

value =
 _  b:boolean _ { return b }

valueInParens =
 _ OPEN_PAREN v:value CLOSE_PAREN _ { return v; }

modulo =
 l:baseExpression o:MODULUS r:baseExpression {
    return { type: "OperatorExpression", location: location(), operator: o, left: l, right: r }
}
/ b:baseExpression  { return b; }

exponent =
 l:modulo o:EXPONENTIATE r:modulo {
    return { type: "OperatorExpression", location: location(), operator: o, left: l, right: r }
}
/ m:modulo { return m; }

product =
 l:exponent tail:(o:(MULTIPLY / DIVIDE) r:product { return [o, r]; })+ {
  var r = { type: "OperatorExpression", location: location(), left: l };
  var tar = r;
  for(var i = 0; i < tail.length - 1; i++) {
    tar.operator = tail[i][0];
    var newTar = { type: "OperatorExpression", location: location(), left: tail[i][1] };
    tar.right = newTar;
    tar = newTar;
  }
  tar.operator = tail[tail.length - 1][0];
  tar.right = tail[tail.length - 1][1];
  return r;
}
/ p:exponent { return p; }

sum = l:product tail:(o:(PLUS / MINUS) r:product { return [o, r]; })+ {
  var r = { type: "OperatorExpression", location: location(), left: l };
  var tar = r;
  for(var i = 0; i < tail.length - 1; i++) {
    tar.operator = tail[i][0];
    var newTar = { type: "OperatorExpression", location: location(), left: tail[i][1] };
    tar.right = newTar;
    tar = newTar;
  }
  tar.operator = tail[tail.length - 1][0];
  tar.right = tail[tail.length - 1][1];
  return r;
}
/ p:product { return p; }
arithmeticValue =
 s:sum {
   return s;
 }

baseExpression =
 _  x:(arrayLiteral / stringLiteral / unitValue / NUMERIC_VALUE / functionCall / variableReference / valueInParens) _ {
   return x;
 }

variableReference =
 i:IDENTIFIER { return { type: "VariableReference", location: location(), variable: i }; }

arrayLiteral =
 OPEN_SQUARE_BRACKET a:argumentList? CLOSE_SQUARE_BRACKET {
 return {
   type: "ArrayLiteral", location: location(),
   elems: a || {type:"ArgumentList",args:[], location: location()}
 }
}

boolean =
 l:arithmeticValue o:comparisonOperator r:arithmeticValue { return { type: "ComparisonOperator", location: location(), left: l, operator: o, right: r }; }
 / TRUE { return { type: "BooleanLiteral", location: location(), value: true }; }
 / FALSE { return { type: "BooleanLiteral", location: location(), value: false }; }
 / r:arithmeticValue { return r; }

comparisonOperator =
 o:(COMPARE_LTE / COMPARE_LT / COMPARE_EQ / COMPARE_NEQ / COMPARE_GTE / COMPARE_GT) { return o; }

functionCall =
 f:IDENTIFIER _ OPEN_PAREN a:argumentList? CLOSE_PAREN {
 return {
   type: "FunctionCall", location: location(),
   func: f, args: a || {type:"ArgumentList",args:[], location: location()}
 }
}

stringLiteral =
 DOUBLE_QUOTE q:NON_QUOTE_CHARACTER* DOUBLE_QUOTE { return { type: "StringLiteral", location: location(), str: q.join("") } }

unitValue =
 v:(NUMERIC_VALUE/dynamicValue) u:(IDENTIFIER/dynamicValue) { return {type: "UnitValue", location: location(), value: v, unit: u } }

argumentList =
 heads:(argument COMMA)* tail:argument {
 return {
   type: "ArgumentList", location: location(),
   len: heads.length + 1,
   args: heads.map(x=> x[0]).concat([tail])
 }
}

argument = n:(value EQUALS)? v:value {
	if(n) return { type: "TitledArgument", value: v, name: n[0]  };
    else return v;
}

dynamicValue = OPEN_SQUARE_BRACKET v:value CLOSE_SQUARE_BRACKET { return { type: "DynamicValue", value: v, location: location() } }

_ = [ \t\n\r]* comment? [ \t\n\r]*

comment = ("//" [^\n]* "\n")
  / ("/*" commentText* "*/")

commentText = !"*/" .

NON_QUOTE_CHARACTER = [^"]
DOUBLE_QUOTE = '"'

HASHTAG = "#"

  EOL = '\r'? '\n'
  COLON =
    ":"
SEMICOLON =
    ";"
COMMA =
    ","
AFTER =
    "after"
FUNC = "func "
FUNCTION = "function"
GOTO =
    "goto"
IF =
    "if"
WHEN = "when"
OPEN_PAREN =
    "("
CLOSE_PAREN =
    ")"
LET =
    "let"
EQUALS =
    "="
NEXT =
    "next"
SKIP =
    "skip"
COMPARE_LTE =
    "<="
COMPARE_EQ =
    "=="
COMPARE_NEQ =
    "!="
COMPARE_GTE =
    ">="
COMPARE_GT =
    ">"
COMPARE_LT =
    "<"
TRUE =
    "true"
FALSE =
    "false"
PLUS =
    "+"
MINUS =
    "-"
DIVIDE =
    "/"
MULTIPLY =
    "*"
MODULUS =
    "%"
EXPONENTIATE =
    "^"
DOLLAR_SIGN =
    "$"
OPEN_SQUARE_BRACKET =
    "["
CLOSE_SQUARE_BRACKET =
    "]"
OPEN_CURLY_BRACKET =
    "{"
CLOSE_CURLY_BRACKET =
    "}"
IDENTIFIER =
    l:(LETTER / DIGIT)+
    &{
    var name = l.join("");
    var reserved = ["if", "goto", "skip", "let", "next", "function", "func", "when", "after"];
    if(reserved.indexOf(name) == -1) return true;
    else return false;
    }
    { return { type: "Identifier", location: location(), value: l.join("") } }
DIGIT = [0-9]
LETTER = [A-Za-z_]
STATEPATH_LABEL_ID = HASHTAG i:IDENTIFIER { return i; }
NUMERIC_VALUE = m:MINUS? v:(
    h:DIGIT* '.' t:DIGIT+ { return h.join("") + "." + t.join("") }
    / 
    d:DIGIT+ { return d.join(""); }) 'f'?
{
    return { type: "NumericValue", location: location(), v: parseFloat((m||"") + v ) }
}  
NUMERIC_VALUE_WITH_UNIT = n:NUMERIC_VALUE u:IDENTIFIER {
    return { type: "UnitValue", location: location(), value: n, unit: u }
}
