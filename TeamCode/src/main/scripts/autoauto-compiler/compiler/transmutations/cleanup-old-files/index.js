var fs = require("fs");
var path = require("path");

require("..").registerTransmutation({
    requires: [],
    id: "cleanup-old-files",
    type: "codebase_postprocess",
    run: function(context, contexts) {
        clearDirectory(context.resultRoot, contexts.map(x=>x.usedFiles).flat());
    }
});

function clearDirectory(dir, dontDeleteFiles) {
    var files = fs.readdirSync(dir, { withFileTypes: true });
    var filesLeft = files.length;
    files.forEach(x=> {
        var name = path.join(dir, x.name);
        if(x.isFile()) {
            if(!dontDeleteFiles.includes(name)) {
                fs.unlinkSync(name);
                filesLeft--;
            }
        } else if(x.isDirectory()) {
            if(clearDirectory(name, dontDeleteFiles)) filesLeft--;
        }
    });

    if(filesLeft == 0) fs.rmdirSync(dir);
    return filesLeft == 0;
}