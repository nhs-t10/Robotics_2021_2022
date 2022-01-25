var structuredSerialise = require("./index");


var t = [];

for(var i = 0; i < 1; i++) {
    t.push(generateRandomObject(1));
}

console.log(t);

function generateRandomObject(d) {
    if(d < 0) return "";

    var o = {};
    var f = Math.random() * 10;
    for(var i = 0; i < f; i++) {
        var t = Math.random();

        if(t < 0.2) o[i.toString(16)] = null;
        else if(t < 0.4) o[i.toString(16)] = Math.random() * 3000;
        else if(t < 0.6) o[i.toString(16)] = (Math.random() * 3000).toString(16);
        else if(t < 0.8) o[i.toString(16)] = generateRandomObject(d - 1);
        else o[i] = Array.from(generateRandomObject(d - 1));

        if(t > 0.8) o.length = i + 1;
    }
    return o;
}

var timeStart = Date.now();

var solvedSerial = t.map(x=> {
    var p = structuredSerialise.toBuffer(x);
    //return structuredSerialise.fromBuffer(p);
});

var structuredSerialiseTime = Date.now() - timeStart;

console.log(solvedSerial);

var solvedSerialCorrectness = solvedSerial.map((x,i)=>+(JSON.stringify(x)==JSON.stringify(t[i]))).reduce((a,b)=>a+b) / t.length;

console.log("===");
console.log("Structured Serialise: ", structuredSerialiseTime);
console.log("Structured Serialise Correctness: ", solvedSerialCorrectness);

timeStart = Date.now();

t.forEach(x=> {
    var p = JSON.stringify(x);
    var r = JSON.parse(p);
});

var jsonTime = Date.now() - timeStart;

console.log("JSON: ", jsonTime);
console.log("===");