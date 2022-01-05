autoautoFile = 
  _ f:frontMatter? _ s:labeledStatepath+ _ {
  	return {
      type: "Program", location: location(),
      frontMatter: f,
      statepaths: s
    }
  }

frontMatter =
 DOLLAR_SIGN _ head:frontMatterKeyValue tail:(COMMA _ frontMatterKeyValue)* _ DOLLAR_SIGN {
   return {
     type: "FrontMatter", location: location(),
     values: [head].concat(tail.map(x=>x[2]))
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
  _ head:state tail:(SEMICOLON state)* _ (SEMICOLON _)? {
  	return {
      type: "Statepath", location: location(),
      states: [head].concat(tail.map(x=>x[1]))
    }
  }

state =
  _ head:statement tail:(COMMA statement)* _ {
    return {
      type: "State", location: location(),
      statement: [head].concat(tail.map(x=>x[1]))
    }
  }

statement = singleStatement / multiStatement

multiStatement = _ OPEN_CURLY_BRACKET s:state CLOSE_CURLY_BRACKET _ {
	return { type: "Block", state: s, location: location() }
}

singleStatement =
  _  s:(valueStatement/funcDefStatement/afterStatement/gotoStatement/ifStatement/letStatement/nextStatement/skipStatement)  _

  { return s; }

afterStatement =
 AFTER _ u:value _ s:statement { return { type: "AfterStatement", location: location(), unitValue: u, statement: s } }

valueStatement =
 f:value { return { type: "ValueStatement", location: location(), call: f } }

funcDefStatement = FUNCTION _
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
 l:baseExpression r:(MODULUS baseExpression)? {
   if(!r) return l;
   
    return { type: "OperatorExpression", location: location(), operator: r[0], left: l, right: r[1] }
}

exponent =
 l:modulo r:(EXPONENTIATE modulo)? {
   if(!r) return l;
   
    return { type: "OperatorExpression", location: location(), operator: r[0], left: l, right: r[1] }
}

product =
 l:exponent tail:(o:(MULTIPLY / DIVIDE) r:product { return [o, r]; })* {
   if(!tail.length) return l;
   
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

sum = l:product tail:(o:(PLUS / MINUS) r:product { return [o, r]; })* {
  if(!tail.length) return l;
  
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

arithmeticValue =
 s:sum {
   return s;
 }

atom =
 _  x:(arrayLiteral / stringLiteral / unitValue / NUMERIC_VALUE / functionLiteral / variableReference / valueInParens) _ {
   return x;
 }
 
baseExpression = a:atom _ t:tail* _ {
     if(!t.length) return a;
     
     var value = a;
     for(var i = 0; i < t.length; i++) {
       if(t[i].type == "FunctionCall") value = {type: "FunctionCall",func: value, args: t[i].args, location: t[i].location};
       else if(t[i].type == "TailedValue") value = { type: "TailedValue", head: value, tail: t[i].tail, location: t[i].location };
     }
     
     return value;
}

tail = OPEN_SQUARE_BRACKET a:value CLOSE_SQUARE_BRACKET { return {type: "TailedValue", head: null, tail: a, location: location() } } /
	OPEN_PAREN _ a:argumentList? CLOSE_PAREN { return { type: "FunctionCall", func: null, args: a || {type:"ArgumentList",args:[], location: location()}, location: location() } } /
    DOT a:variableReference { return {type: "TailedValue", head: null, tail: a.variable, location: location() }; }

variableReference =
 i:IDENTIFIER { return { type: "VariableReference", location: location(), variable: i }; }

arrayLiteral =
 OPEN_SQUARE_BRACKET _ a:argumentList? CLOSE_SQUARE_BRACKET {
 return {
   type: "ArrayLiteral", location: location(),
   elems: a || {type:"ArgumentList",args:[], location: location()}
 }
}

functionLiteral = FUNC _ name:IDENTIFIER? _ OPEN_PAREN args:argumentList? _ CLOSE_PAREN _ b:statement
    { return { type: "FunctionLiteral", name: name, args: args || {type:"ArgumentList",args:[], location: location()}, body: b, location: location() }; }

boolean =
 l:arithmeticValue o:comparisonOperator r:arithmeticValue { return { type: "ComparisonOperator", location: location(), left: l, operator: o, right: r }; }
 / TRUE { return { type: "BooleanLiteral", location: location(), value: true }; }
 / FALSE { return { type: "BooleanLiteral", location: location(), value: false }; }
 / r:arithmeticValue { return r; }

comparisonOperator =
 o:(COMPARE_LTE / COMPARE_LT / COMPARE_EQ / COMPARE_NEQ / COMPARE_GTE / COMPARE_GT) { return o; }


stringLiteral =
 DOUBLE_QUOTE q:NON_QUOTE_CHARACTER* DOUBLE_QUOTE { return { type: "StringLiteral", location: location(), str: q.join("") } }

unitValue =
 v:(NUMERIC_VALUE/dynamicValue) u:(IDENTIFIER/dynamicValue) { return {type: "UnitValue", location: location(), value: v, unit: u } }

argumentList =
 head:argument tail:(COMMA argument)* {
 return {
   type: "ArgumentList", location: location(),
   len: tail.length + 1,
   args: [head].concat(tail.map(x=> x[1]))
 }
}

argument = n:value v:(EQUALS value)? {
	if(v) return { type: "TitledArgument", value: v[1], name: n, location: location()  };
    else return n;
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
FUNC = "func"
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
DOT = "."
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
