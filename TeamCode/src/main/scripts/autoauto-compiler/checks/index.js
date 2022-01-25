var checks = [];

var fs = require("fs");
var path = require("path");

var checkDirs = fs.readdirSync(__dirname + "/check-sources");
checkDirs.sort().forEach(x=> {
    var check = require("./check-sources/" + x);
    check.id = x.replace(".js", "").toUpperCase();
    checks.push(check)
});

module.exports = function(ast, folder, filename, fileContent) {
    var failed = false;
    for(var i = 0; i < checks.length; i++) {
        var check = checks[i];

        var tag = "[" + check.id + "] " + check.summary;
        
        try {
            var res = check.run(ast, folder, filename, fileContent);
            if(res) {
                if(!res.original) res.original = res.text;
                res.text = tag

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

function sendPlainMessage(msg) {
    console.error("AGPBI: " + JSON.stringify(msg));
}