var fs = require("fs");
var path = require("path");

var checkSubfolders = ["P", "T"];

var checks = checkSubfolders
    .map(x=>loadChecksFromFolder(path.join(__dirname, "check-sources", x)))
    .flat();


module.exports = function(ast, folder, filename, fileContent) {
    var failed = false;
    for(var i = 0; i < checks.length; i++) {
        var check = checks[i];

        var tag = "[" + check.id + "] " + check.summary;
        
        try {
            var res = check.run(ast, folder, filename, fileContent);
            if(res) {
                if(!res.original) res.original = res.text;
                res.text = tag;
                if(res.fail) res.text += " | Skipping File";

                if(res.sources === undefined) res.sources = [{
                    file: path.join(folder, filename)
                }]

                sendPlainMessage(res);
                if(res.kind == "ERROR" || res.fail) {
                    failed = true;
                    break;
                }
            }
        } catch(e) {
            if(e instanceof Error) tag += " | failed to execute";
            sendPlainMessage({
                kind: "ERROR",
                text: tag, 
                original: e.toString() + "\n" + (e.stack||""),
                sources: [{
                    file: path.join(folder, filename)
                }]
            });
            failed = true;
            break;
        }
    }
    return !failed;
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

function sendPlainMessage(msg) {
    console.error("AGPBI: " + JSON.stringify(msg));
}