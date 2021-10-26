var generate = require("./generate");
module.exports = function(seed) {
    var seedBuffer = Buffer.from(seed || randomSeed(), "hex");
    return generate(seedBuffer);
}

function randomSeed() {
    return (["","",""]).map(x=>Math.random().toString(16).substring(2)).join("");
}