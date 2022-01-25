var structuredSerialise = require("./index");


var t = [];

for(var i = 0; i < 1; i++) {
    t.push(generateRandomObject());
}

function generateRandomObject(d) {
    d = +d || 0;
    if(d > 10) return "";

    var o = {};
    var f = Math.random() * 10;
    for(var i = 0; i < f; i++) {
        var t = Math.random();

        if(t < 0.2) o[i.toString(16)] = null;
        else if(t < 0.4) o[i.toString(16)] = Math.random() * 3000;
        else if(t < 0.6) o[i.toString(16)] = (Math.random() * 3000).toString(16);
        else if(t < 0.8) o[i.toString(16)] = generateRandomObject(d + 1);
        else o[i] = Array.from(generateRandomObject(d + 1));

        o.length = i + 1;
    }
    return o;
}

var timeStart = Date.now();

t.forEach(x=> {
    var p = structuredSerialise.toBuffer(x);
    //var r = structuredSerialise.fromBuffer(p);
});

var structuredSerialiseTime = Date.now() - timeStart;

console.log("===");
console.log("Structured Serialise: ", structuredSerialiseTime);

timeStart = Date.now();

t.forEach(x=> {
    var p = JSON.stringify(x);
    var r = JSON.parse(p);
});

var jsonTime = Date.now() - timeStart;

console.log("JSON: ", jsonTime);
console.log("===");