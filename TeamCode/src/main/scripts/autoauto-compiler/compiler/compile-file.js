

module.exports = function(fileContext) {
    androidStudioLogging.beginOutputCapture();
    var success = compileFile(fileContext);
    var log = androidStudioLogging.getCapturedOutput();

    return {
        success: success,
        fileContext: fileContext,
        log: log
    }
}

function compileFile(fileContext) {
    for(var j = 0; j < fileContext.length; j++) {
        var transmut = tPath[j];
        
        var mutRan = tryRunTransmutation(mut, fileContext);
        
        if(!mutRan) break;
    }
}