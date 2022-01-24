var structuredSerialise = require("./index");

console.log(structuredSerialise.toBuffer(3));
console.log(structuredSerialise.toBuffer({fef: 2}));
console.log(structuredSerialise.toBuffer([1,2,3]));