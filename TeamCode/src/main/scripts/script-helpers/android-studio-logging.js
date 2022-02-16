module.exports = {
    sendPlainMessage: sendPlainMessage,
    sendTreeLocationMessage: sendTreeLocationMessage
}

function sendPlainMessage (msg) {
    console.error("AGPBI: " + JSON.stringify(msg));
}

function sendTreeLocationMessage(res, file) {
    console.log(res);
    var m = {
        sources: [{
            file: file,
        }]
    };
    if(res.location) {
        m.sources[0].location = {
            startLine: res.location.start.line,
            startColumn: res.location.start.column,
            startOffset: res.location.start.offset,
            endLine: res.location.end.line,
            endColumn: res.location.end.line,
            endOffset: res.location.end.offset
        }
    }
    
    Object.assign(m, res);
    
    sendPlainMessage(m);
}