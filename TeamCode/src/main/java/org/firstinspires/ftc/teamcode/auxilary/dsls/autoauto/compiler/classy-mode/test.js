const makeClassBuffer = require("./make-class-buffer");
var fs = require("fs");

var testFile = `$
testIterations: 3801,
errorStackTraceHeight: 7
$


#init:
    let startTime = Time.nano(), let primesTarget = 10001, let lastprime = 2, let composites = [], let primes = [2 = true], goto sieve
#setup:
    goto sieve;
#findnextprime:
    let i = lastprime, next;
    let i = i + 1, if(composites[i] == false) next;
    let primes[i] = true, let lastprime = i, goto sieve;
#sieve:
    let i = 2, next;
    let composites[lastprime * i] = true, let i = i + 1, if(i >= 121) next;
    if(lastprime >= 113) goto finished, goto findnextprime;

#finished:
    log(primes), log(Time.nano() - startTime), next;
    let done = true

`;


var ast = require("../aa-parser").parse(testFile);


var buf = makeClassBuffer(ast);

fs.writeFileSync(__dirname + "/test.class", Buffer.from(buf));