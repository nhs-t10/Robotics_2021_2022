module.exports = {
    sendPlainMessage: sendPlainMessage,
    sendTreeLocationMessage: sendTreeLocationMessage
}

function sendPlainMessage (msg) {
    console.error("AGPBI: " + JSON.stringify(msg));
}

function sendTreeLocationMessage(res, file) {
    massageResIntoArrayOfMessages(res, "", file).forEach(x=>sendPlainMessage(x));
}


function massageResIntoArrayOfMessages(res, file) {
    if(res.constructor === Array) return res.map(x=>massageResIntoMessage(x, file));
    else return [massageResIntoMessage(res, file)];
}

function massageResIntoMessage(res, file) {
    if(typeof res === "string") res = { text: res };
    
    if(!res.original) res.original = res.text;
    

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