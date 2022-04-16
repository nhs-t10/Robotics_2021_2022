var fs = require("fs");
var path = require("path");


var cachedGitDirectory = getGitRootDirectory();

module.exports = {
    safeWriteFile: function(fileName, content) {
        var dir = path.dirname(fileName);
        this.createDirectoryIfNotExist(dir);

        console.log("writing " + fileName);

        fs.writeFileSync(fileName, content);
    },
    createDirectoryIfNotExist: function(fileName) {
        var dirName = fileName;
        
        //if the final term has a dot, assume it's a filename
        if(path.basename(fileName).includes(".")) dirName = path.dirname(fileName);
        
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
    },
    getGitRootDirectory: function() {
        return cachedGitDirectory;
    },
    addToGitignore: function(path) {
        var gitRoot = cachedGitDirectory;
        if(!gitRoot) return false;

        var gitignore = path.join(gitRoot, ".gitignore");
        //if it doesn't exist, just create it with the required content
        if(!fs.existsSync(gitignore)) {
            fs.writeFileSync(gitignore, path + "\n");
            return true;
        }
        
        var gContent = fs.readFileSync(gitignore).toString();
        var gLines = gContent.split(/\r?\n/);

        //early exit if the gitignore already has the path
        if(gLines.includes(path)) return true;
        else gLines.push(path);

        fs.writeFileSync(gitignore, gLines.join("\n"));
        return true;
    }
}

function getGitRootDirectory() {
    var dir = process.cwd().split(path.sep);
    while(true) {
        var dirPath = dir.join(path.sep);
        if(fs.existsSync(path.join(dirPath, ".git"))) break;
        else dir.pop();

        if(dir.length == 0) return undefined;
    }
    return dir.join(path.sep);
}