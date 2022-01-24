const arrayReader = require("../array-reader");
const bitwiseyHelpers = require("../bitwisey-helpers");
var structuredSerialise = require("./index");


var t = [
    3,
    "and I'm not sure why?",
    {fef: 3},
    [1,2,3],
    {fef: 3, foof: 2},
    new Date(1998)
];

t.forEach(x=> {
    console.log("===");
    console.log("Testing", x);
    var p = structuredSerialise.toBuffer(x);
    console.log("Buffer", p);
    var r = structuredSerialise.fromBuffer(p);
    console.log("Re-Deserialised", r);
})