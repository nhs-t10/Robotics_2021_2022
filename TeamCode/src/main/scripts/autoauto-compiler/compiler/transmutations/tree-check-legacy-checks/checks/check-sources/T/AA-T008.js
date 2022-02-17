var path = require("path");

var query = require("../query");

var knownKeys = [
    "compilerMode",
    "testIterations",
    "expectedTestOutput",
    "compatflag_afterStartAtState",
    "flag_undefinedUsageCausesErrors"
];

module.exports = {
    summary: "Unknown Front-matter Key",
    run: function(ast) {

        var fmKeyValues = query.getAllOfType(ast, "FrontMatterKeyValue");
        
        for(var i = 0; i < fmKeyValues.length; i++) {
            var key = fmKeyValues[i].key.value;
            
            var isKnownKey = knownKeys.includes(key);
            if(!isKnownKey) {
                return {
                    kind: "WARNING",
                    text: `\`${key}\` isn't a registered front-matter key. Please register it in \`AA-T008.js\`.`,
                    location: fmKeyValues[i].location
                }
            }
        }
    }
}