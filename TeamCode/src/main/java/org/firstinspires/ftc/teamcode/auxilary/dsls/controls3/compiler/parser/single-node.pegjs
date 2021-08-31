// Slightly Less Simple Arithmetics Grammar
// ==========================
//
// Accepts expressions like "sqrt(2 * (3 + 4))" and makes an AST

EXPRESSION
  = tail:(TERM ("+" / "-"))* head:TERM {
  		if(!tail.length) return head;
        var targetOp = head;
        
        for(var i = tail.length - 1; i >= 0; i--) {
            targetOp = { type: "OperatorExpression", left: tail[i][0], operator: tail[i][1], right: targetOp };
        }
        
        return targetOp;
    }

TERM
  = tail:(FACTOR ("*" / "/"))* head:FACTOR {
      if(!tail.length) return head;
        var targetOp = head;
        
        for(var i = tail.length - 1; i >= 0; i--) {
            targetOp = { type: "OperatorExpression", left: tail[i][0], operator: tail[i][1], right: targetOp };
        }
        
        return targetOp;
    }

FACTOR
  = "(" _ expr:EXPRESSION _ ")" { return expr; }
  / ATOM

ATOM = _ l:(NUMBER / 
	STRING /
    NAME)
    r:(TRAILER / CALL)? _ {
        if(r && r.type == "CallArguments") return { type: "FunctionCall", func: l, args: r };
    	else if(r) return { type: "AccessOperator", left: l, right: r  };
        else return l;
    }
    
CALL = OPEN_PAREN h:(EXPRESSION COMMA)* t:EXPRESSION? CLOSE_PAREN {
    return {
        type: "CallArguments",
        args: t ? h.map(x=>x[0]).concat(t) : [] 
    }
}

TRAILER = DOT a:ATOM { return a; }

STRING = l:(DOUBLE_QUOTE doubleqstringitem* DOUBLE_QUOTE !DOUBLE_QUOTE / SINGLE_QUOTE singleqstringitem* SINGLE_QUOTE) {
      var val = l[0] + l[1].join("") + l[2];
      return {
          type: "StringLiteral",
          value: val
      };
  } / m:MULTILINE_STRING { return {type: "MultilineStringLiteral", value: m }; }

doubleqstringitem
  = l:(doubleqstringchar / escapeseq) {
      return l;
  }

singleqstringitem
  = l:(singleqstringchar / escapeseq) {
      return l;
  }

doubleqstringchar = l:[^\\"] {
    return l;
}

singleqstringchar = l:[^\\'] {
    return l;
}

escapeseq
  = l:("\\" [\\'"abfnrtv]) { 
      if(typeof l.join == "function") return l.join("");
      else return l;
    }

MULTILINE_STRING = DOUBLE_QUOTE DOUBLE_QUOTE DOUBLE_QUOTE 
	com:MULTILINE_STRING_CHARACTER+
	DOUBLE_QUOTE DOUBLE_QUOTE DOUBLE_QUOTE 
    { return com.join(""); }
    
MULTILINE_STRING_BORDER = DOUBLE_QUOTE DOUBLE_QUOTE DOUBLE_QUOTE 

MULTILINE_STRING_CHARACTER = !MULTILINE_STRING_BORDER c:. { return c; }

NAME
  = _ head:[a-zA-Z_] tail:[a-zA-Z0-9_]* _
    &{
    var name = head + tail.join("");
    var reserved = ["v","and","or"];
    if(reserved.indexOf(name) == -1) return true;
    else return false;
    }
    { return {
    	type: "Identifier",
        value: head + tail.join("")
      }; 
    }

NUMBER
  = l:('-'? ('0b' / '0x')? (DOT [0-9]+ / [0-9]+ DOT [0-9]+ / [0-9]+)) {
      function j(arr) {
          if(arr === null) return "";
          if(!arr.join) return arr||"";
          else return arr.map(x=>j(x)).join("");
      }
      return {
          type: "NumericLiteral",
          value: +j(l)
      }
  }
  
DOT = _ "." _ { return "." }
OPEN_PAREN = _ "(" _ { return "(" }
CLOSE_PAREN = _ ")" _ { return ")" }
EQUALS = _ "=" _ { return "=" }
COMMA = _ "," _ { return "," }
DOUBLE_QUOTE = _ '"' _ { return '"'; }
SINGLE_QUOTE = _ "'" _ { return "'"; }

_ "whitespace"
  = [ \t\n\r]*