program = h:header? a:assertation? s:statementblock {
    return {
      location: location(),
      type: "program",
      header: h,
      assertation: a,
      statements: s
    }
}

statementblock = head:statement tail:(NEWLINE? statement)* {
    return [head].concat(tail.map(x=>x[1]));
}

header = FIRST_OFF COMMA? ANYTHING* ___ {
    return text().trim();
}

assertation = HERES_WHAT_WILL_HAPPEN COLON? ___ {
    return text().trim();
}

statement "a statement" = _ s:(if_statement / claused_statement / does_statement) t:trailers? c:commaClause? (SEMICOLON / PERIOD)? ___ {
  s.properties = s.properties||{};
  
  //don't override claused statement
  if(c && c.value && !s.properties[c.type]) s.properties[c.type] = c.value;
  
  (t||[]).forEach(x=>{
      if(s.properties[x.type] === undefined) s.properties[x.type] = x.value
    });
  return s;
}

claused_statement = c:(promiseCommaClause / thoughtCommaClause) (__ THAT)? __ s:statement {
  s.properties = s.properties||{};
  s.properties[c.type] = c.value;
  return s;
}

commaClause = COMMA __ c:(promiseCommaClause / thoughtCommaClause) {
    return c;
}

thoughtCommaClause = I_THINK {
    return {
        location: location(),
        type: "priority",
        value: "thought"
    }
}

promiseCommaClause = I_PROMISE {
    return {
      location: location(),
    	type: "priority",
      value: "promise"
    }
}

if_statement = (IF / WHEN) a:comparison COMMA _ THEN? s:statement e:else_clause? {
  return {
    location: location(),
    type: "ifStatement",
    condition: a,
    statement: s,
    otherwise: e
  };
}

else_clause = ((OR/BUT) __ )?  (WHEN_NOT/IF_NOT / ELSE / OTHERWISE) COMMA? s:statement {
    return s;
}

comparison = a:non_vector o:comparer b:non_vector {
  return {
    location: location(),
    type: "comparison",
    left: a,
    right: b,
    comparer: o
  }; 
}

comparer = IS _ r:((REALLY / ACTUALLY / TRULY) _)* c:comparisonAdjective? {
    return {
      location: location(),
      type: "comparisonOperation",
      really: r.length,
      threshold: (c&&c.threshold) || null,
      operator: (c&&c.op) || c || "=="
    }
}

comparisonAdjective "comparison adjective" = LESS_THAN {return "<"}
  / MORE_THAN {return ">"} 
  / (THE_SAME_AS / EQUAL_TO) {return "=="}
  / ROUGHLY (__ (EQUAL_TO / THE_SAME_AS))? { return "~=" }
  / WITHIN h:non_vector OF { return {op: "~#<", threshold: h}; }

does_statement = a:value v:verb b:value i:(TO value)? {
  return {
    location: location(),
    type: "doesStatement",
    subject: a,
    directObject: b,
    verb: v,
    indirectObject: i && i[1]
  };
}

verb "a verb" = v:(IS / DOES / HATES / HATE / MIMICS / SETS / SET / ARE / CONTROLS / CONTROL) {
    v = v.toLowerCase();
    if(v == "hates") return "hate";
    if(v == "are" || v == "was") return "is";
    if(v == "control") v == "controls";
    if(v == "sets") v = "set"
    
    return v;
}

trailers = 
WITH __ a:trailer __ AND __ b:trailer {
    return [a, b];
}
/
WITH __ h:trailer a:(COMMA __ trailer)+ COMMA? __ AND __ t:trailer { 
  return [h].concat(a.map(x=>x[2])).concat(t); 
}
/
WITH __ t:trailer {
    return [t];
}


trailer =  calculation_trailer / otherwise_trailer / scale_trailer / priority_trailer

calculation_trailer = c:identifier _ CALCULATION {
    return {
        location: location(),
        type: "calculation",
        value: c
    }
}

priority_trailer = (THE/A) __ p:priority __ PRIORITY {
  return {
    location: location(),
    type: "priority",
    value: p
  };
}
priority = p:(HIGHEST / HIGHER / HIGH / LOWEST / LOWER / LOW / NORMAL) {
  return p;
}

otherwise_trailer = A_DEFAULT_OF __ v:value {
  return {
    location: location(),
    type: "otherwise",
    value: v
  };
}

scale_trailer = A_SCALE_OF __ n:value {
  return {
    location: location(),
    type: "scale",
    value: n
  };
}

type = identifier

identifier "an identifier" = _ ((THE/AN/A) __)? l:LETTER t:(DIGIT / LETTER)* _ &{
    var banned = ["and", "the", "an", "a", "to", "but"];
    if(banned.indexOf([l].concat(t).join("")) != -1) return false;
    else return true;
  }
{ return {
    location: location(),
    type: "identifier",
    value: [l].concat(t).join("")
  }
}

value "a value" = _ v:(vector / non_vector) _ {
    return v;
}

non_vector "a scalar value" = _ v:(percentage / number / identifier) _ {
    return v;
}

vector "a vector" = h:non_vector b:(COMMA __ (AND __ non_vector / non_vector))+ {
    return {
        location: location(),
        type: "vector",
        values: [h].concat(b.map(x=>x[2].constructor == Array ? x[2][2] : x[2]))
    };
}

percentage = v:number PERCENT_SIGN {
    return {
        location: location(),
        type: "percentage",
        value: v
    }
}

number "a number" = MINUS? digitsequence (PERIOD digitsequence)? {
  return {
    location: location(),
    type: "number",
    value: parseFloat(text().replace(/,/g, ""), 10)
  };
}

digitsequence = [0-9]+ (COMMA [0-9]+)*

ANYTHING = [^\n]

__ "whitespace" = [ \t]+

_ "whitespace"
  = [ \t]*
  
___ "whitespace with newline" = [ \t\r\n]*
  
PERIOD = '.'
DOES = 'does'
SETS = "sets" / "SETS"
TO = "to" / "TO"
ARE = "are" / "ARE"
NAMED = "named" / "NAMED"
WITH = "with" / "WITH"
PRIORITY = "priority" / "PRIORITY"
COMMA = ","
HIGHEST = "highest" / "HIGHEST"
HIGHER = "higher" / "HIGHER"
HIGH = "high" / "HIGH"
NORMAL = "normal" / "NORMAL"
LOW = "low" / "LOW"
LOWER = "lower" / "LOWER"
LOWEST = "lowest" / "LOWEST"
A_DEFAULT_OF = "a default of" / "A DEFAULT OF"
A_SCALE_OF = "a scale of" / "A SCALE OF"
MINUS = "-"
LETTER = [a-zA-Z_]
DIGIT = [0-9]
SEMICOLON = ';'
NEWLINE = "\n"
AND = "and" / "AND"
THE = "the" / "THE"
A = "a" / "A"
FIRST_OFF = "first off" / "FIRST OFF"
HERES_WHAT_WILL_HAPPEN = "here's what will happen" / "HERE'S WHAT WILL HAPPEN"
COLON = ":"
IF = "if" / "IF"
WHEN = "when" / "WHEN"
IS = "is" / "IS"
REALLY = "really" / "REALLY"
LESS_THAN = "less than" / "LESS THAN"
MORE_THAN = "more than" / "MORE THAN"
THE_SAME_AS = "the same as" / "THE SAME AS"
ACTUALLY = "actually" / "ACTUALLY"
OR = "or" / "OR"
ELSE = "else" / "ELSE"
OTHERWISE = "otherwise" / "OTHERWISE"
BUT = "but" / "BUT"
TRULY = "truly" / "TRULY"
THEN = "then" / "THEN"
WHEN_NOT = "when not" / "WHEN NOT"
IF_NOT = "if not" / "IF NOT"
I_PROMISE = "i promise" / "I PROMISE"
I_THINK = "i think" / "I THINK"
THAT = "that" / "THAT"
HATES = "hates" / "HATES"
AN = "an" / "AN"
MIMICS = "mimics" / "MIMICS"
CONTROLS = "controls" / "CONTROLS"
CONTROL = "control" / "CONTROL"
CALCULATION = "calculation" / "CALCULATION"
SET = "set" / "SET"
IT = "it" / "IT"
MIGHT = "might" / "MIGHT"
HATE = "hate" / "HATE"
ROUGHLY = "roughly" / "ROUGHLY"
EQUAL_TO = "equal to" / "EQUAL TO"
WITHIN = "within" / "WITHIN"
OF = "of" / "OF"
PERCENT_SIGN = "%"