var fs = require("fs");
var path = require("path");

module.exports = {
    createDirectoryIfNotExist: function(fileName) {
        var dirName = path.dirname(fileName);
        if(!fs.existsSync(dirName)) {
            fs.mkdirSync(dirName, {recursive: true});
        }
    },
    cleanDirectory: function (dir, dontDeleteFiles) {
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
}