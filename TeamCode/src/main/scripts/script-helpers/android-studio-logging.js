//https://github.com/zawn/android-gradle-plugin-src/blob/master/sdk-common/src/main/java/com/android/ide/common/blame/MessageJsonSerializer.java

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
    
    if (logLevel <= l || l === undefined) formatAndSendJsonFormat(msg);
}

function formatAndSendJsonFormat(msg) {
    msg.original = humanReadableFormat(msg);
    console.error("AGPBI: " + JSON.stringify(msg));
}

function formatAndSendHumanyFormat(msg) {
    console.error(humanReadableFormat(msg));
}

function humanReadableFormat(msg) {
    var mForm = "";

    if (msg.sources[0]) {
        mForm += msg.sources[0].file + ":";
        if (msg.sources[0].location) {
            mForm += msg.sources[0].location.startLine + ":";
        }
        mForm += " ";
    }

    mForm += msg.kind.toLowerCase() + ": "

    mForm += msg.text + ":\n" + (msg.original || "");
    mForm += "\n ^\n";

    return mForm;
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
        res = {
            kind: "ERROR",
            text: res.toString(),
            original: res.toString() + "\n" + res.stack,
            location: res.location
        }
    }

    if(res.fail) res.text += " | Skipping File";
    else if(res.titleNote) res.text += " | " + res.titleNote;

    if(res.sources === undefined) {
            res.sources = [{
            file: file
        }];
    }
    if(res.location && !res.sources[0].location) {
        res.sources[0].location = {
            startLine: res.location.start.line,
            startColumn: res.location.start.column,
            startOffset: res.location.start.offset,
            endLine: res.location.end.line,
            endColumn: res.location.end.line,
            endOffset: res.location.end.offset
        };
        delete res.location;
    }
    return res;
}

function rgb216(r, g, b) {
    if (typeof r === "string") {
        r = r.replace("#", "");

        b = parseInt(r.substring(4, 6), 16) / 0xff;
        g = parseInt(r.substring(2, 4), 16) / 0xff;
        r = parseInt(r.substring(0, 2), 16) / 0xff;
    }
    var rf = Math.round(r * 5), gf = Math.round(g * 5), bf = Math.round(b * 5);
    return (rf * 36) + (gf * 6) + (bf) + 16
}