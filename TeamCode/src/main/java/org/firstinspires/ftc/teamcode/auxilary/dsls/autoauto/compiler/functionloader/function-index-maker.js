var path = require("path");
var fs = require("fs");

var directory = __dirname.split(path.sep);
var rootDirectory = directory.slice(0, directory.indexOf("TeamCode")).join(path.sep);
var robotFunctionsDirectory = path.join(rootDirectory, "TeamCode/src/main/java/org/firstinspires/ftc/teamcode/auxilary/dsls/autoauto/runtime/robotfunctions");

if(!fs.existsSync(robotFunctionsDirectory)) fs.mkdirSync(robotFunctionsDirectory);

var indexFile = path.join(robotFunctionsDirectory, ".autoauto-function-index.txt");

var indexFileStream = fs.createWriteStream(indexFile, {flags: "a"});

module.exports = {
    cleanIndex: function() {
        fs.writeFileSync(indexFile, "");
    },
    addFunctionIndexLine: function(line) {
        indexFileStream.write("* " + line + "\n");
    }
}