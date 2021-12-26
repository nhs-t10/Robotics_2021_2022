const makeClassBuffer = require("./make-class-buffer");

var testFile = `#i: 
log(3), next;
goto b

#b: 
log(2), after 2s next;
goto a

#a:
log(1), when(g == 2) next, let g = 2;
goto fin

#fin:
log(0)`;


var ast = require("../aa-parser").parse(testFile);

console.log(makeClassBuffer(ast));