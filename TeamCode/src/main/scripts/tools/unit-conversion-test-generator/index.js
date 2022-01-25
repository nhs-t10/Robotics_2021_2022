var unitDefRegex = /(\w+) ([A-Z]+) = [^\d]+([\d.e_-]+)/;

var fs = require("fs");

var input = fs.readFileSync(__dirname + "/in.txt").toString();

var inputLines = input.split("\n");

// @type {({unit: string, coef: number})[]}
var coefs = inputLines.map(x=>unitDefRegex.exec(x)).filter(x=>x).map(x=>({type: x[1], unit: x[2], coef: +x[3].replace(/_/g,"")}));

console.log(coefs);

var out = coefs.map(x=> 
    coefs.map(y=> {
        var r = Math.round(Math.random() * 20);
        var rNorm = r * x.coef;
        return `assertEquals(${jLit(rNorm / y.coef)}, ${x.type}.${x.unit}.convertTo(${y.type}.${y.unit}, ${r}), ${r / 100});`
    }).concat([""])
).flat().join("\n")

fs.writeFileSync(__dirname + "/out.txt", out);

function jLit(n) {
    var f = n.toString();
    if(f.includes("e")) return f;
    else if(f.includes(".")) return f;
    else return f + ".0";
}