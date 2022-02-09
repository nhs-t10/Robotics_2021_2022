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
                var msgArr = massageResIntoArrayOfMessages(res, tag, folder, filename);

                msgArr.forEach(x=>sendPlainMessage(x));
                
                if(shouldStopAnalysis(msgArr)) {
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

function shouldStopAnalysis(messageArray) {
    return !!messageArray.find(x=>x.kind == "ERROR" || x.fail == true);
}

function massageResIntoArrayOfMessages(res, tag, folder, filename) {
    if(res.constructor === Array) return res.map(x=>massageResIntoMessage(x, tag, folder, filename));
    else return [massageResIntoMessage(res, tag, folder, filename)];
}

function massageResIntoMessage(res, tag, folder, filename) {
    if(!res.original) res.original = res.text;
    res.text = tag;

    if(res.fail) res.text += " | Skipping File";
    else if(res.titleNote) res.text += " | " + res.titleNote;

    if(res.sources === undefined) {
            res.sources = [{
            file: path.join(folder, filename)
        }];
        
        if(res.location) res.sources[0].location = {
            startLine: res.location.start.line,
            startColumn: res.location.start.column,
            startOffset: res.location.start.offset,
            endLine: res.location.end.line,
            endColumn: res.location.end.line,
            endOffset: res.location.end.offset
        }
        delete res.location;
    }
    return res;
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