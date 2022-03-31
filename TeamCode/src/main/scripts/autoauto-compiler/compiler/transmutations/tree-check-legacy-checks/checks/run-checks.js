var fs = require("fs");
var path = require("path");
var androidStudioLogging = require("../../../../../script-helpers/android-studio-logging");

var checkSubfolders = ["T"];

var checks = checkSubfolders
    .map(x=>loadChecksFromFolder(path.join(__dirname, "check-sources", x)))
    .flat();


module.exports = function(ast, filename) {
    var failed = false;
    for(var i = 0; i < checks.length; i++) {
        var check = checks[i];

        var tag = "[" + check.id + "] " + check.summary;
        
        try {
            var res = check.run(ast);
            if(res) {
                androidStudioLogging.sendTreeLocationMessage(res, filename);
                
                if(shouldStopAnalysis(res)) {
                    failed = true;
                    break;
                }
            }
        } catch(e) {
            if(e instanceof Error) tag += " | failed to execute";
            androidStudioLogging.sendTreeLocationMessage({
                kind: "ERROR",
                text: tag, 
                original: e.toString() + "\n" + (e.stack||"")
            }, filename);
            failed = true;
            break;
        }
    }
    return !failed;
}

function shouldStopAnalysis(messageArray) {
    if(typeof messageArray.find != "function") messageArray = [messageArray];
    
    return !!messageArray.find(x=>x instanceof Error || x.kind == "ERROR" || x.fail == true);
}


function loadChecksFromFolder(folder) {
    if(!fs.existsSync(folder)) return [];

    var checkDir = fs.readdirSync(folder);
    return checkDir.sort().map(x=> {
        var check = require(path.join(folder, x));
        check.id = x.replace(".js", "").toUpperCase();
        return check;
    });
}