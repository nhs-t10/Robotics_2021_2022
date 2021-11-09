var badPercepHash = require("./bad-percep-hash");

var ins = process.argv.slice(2);

var inBuffs = ins.map(x=>Buffer.from(x, "ascii"));

var hashes = inBuffs.map(x=>badPercepHash.hash(x));

hashes.forEach(x=>console.log(x.toString("hex") + "\n"));

console.log("\n\n");

console.log(badPercepHash.combineHashes(hashes).toString("hex"));