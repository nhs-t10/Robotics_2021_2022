var path = require("path");

var query = require("../query");

var knownKeys = [
    "compatflag_afterStartAtState",
    "compilerMode",
    "testIterations",
    "expectedTestOutput"
]

module.exports = {
    summary: "Unknown Front-matter Key",
    run: function(ast, folder, filename, originalFileContent) {

        var fmKeyValues = query.getAllOfType(ast, "FrontMatterKeyValue");
        
        for(var i = 0; i < fmKeyValues.length; i++) {
            var key = fmKeyValues[i].key.value;
            
            var isKnownKey = knownKeys.includes(key) || key.startsWith("x_");
            if(!isKnownKey) {
                return {
                    kind: "WARNING",
                    text: `\`${key}\` isn't a registered front-matter key. Please replace it with \`x_${key}\` or register it in \`AA-T008.js\`.`,
                    location: fmKeyValues[i].location
                }
            }
        }
    }
}