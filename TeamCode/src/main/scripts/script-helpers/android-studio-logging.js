module.exports = {
    sendPlainMessage: sendPlainMessage,
    sendTreeLocationMessage: sendTreeLocationMessage
}

var logLevel = 0;
if(process.argv.includes("-q")) logLevel = 2;

function sendPlainMessage (msg) {
    var l = ["INFO","WARNING","ERROR"].indexOf(msg.kind);

    if(l == 2) console.log(2);
    
    if(logLevel <= l) console.error("AGPBI: " + JSON.stringify(msg));
}

function sendTreeLocationMessage(res, file) {
    massageResIntoArrayOfMessages(res, file).forEach(x=>sendPlainMessage(x));
}


function massageResIntoArrayOfMessages(res, file) {
    if(res.constructor === Array) return res.map(x=>massageResIntoMessage(x, file));
    else return [massageResIntoMessage(res, file)];
}

function massageResIntoMessage(res, file) {
    if(typeof res === "string") res = { text: res };
    
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