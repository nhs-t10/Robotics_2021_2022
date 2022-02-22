module.exports = {
    sendPlainMessage: sendPlainMessage,
    sendTreeLocationMessage: sendTreeLocationMessage,
    
    warning: sendWarn
}

var logLevel = 0;
if(process.argv.includes("-q")) logLevel = 2;

function sendWarn(msgStr) {
    sendPlainMessage({
        kind: "WARNING",
        text: msgStr
    });
}

function sendPlainMessage (msg) {
    var l = ["INFO","WARNING","ERROR"].indexOf(msg.kind);
    
    if(logLevel <= l) console.error("AGPBI: " + JSON.stringify(msg));
}

function sendTreeLocationMessage(res, file, defaultKind) {
    massageResIntoArrayOfMessages(res, file, defaultKind).forEach(x=>sendPlainMessage(x));
}


function massageResIntoArrayOfMessages(res, file, defaultKind) {
    if(res.constructor === Array) return res.map(x=>massageResIntoMessage(x, file, defaultKind));
    else return [massageResIntoMessage(res, file, defaultKind)];
}

function massageResIntoMessage(res, file, defaultKind) {
    if(typeof res === "string") res = { text: res };
    
    if(!res.kind) res.kind = defaultKind;
    
    if(!res.original) res.original = res.text;
    
    if(res instanceof Error) {
        console.log("err");
        res.kind = "ERROR";
        res.text = res.toString();
        res.original = res.stack;
    }

    if(res.fail) res.text += " | Skipping File";
    else if(res.titleNote) res.text += " | " + res.titleNote;

    if(res.sources === undefined) {
            res.sources = [{
            file: file
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