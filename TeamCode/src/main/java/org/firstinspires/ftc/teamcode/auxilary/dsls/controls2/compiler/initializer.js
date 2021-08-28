var fs = require("fs");
var path = require("path");

var GITIGNORED = ["*__controls.java"];

var directory = __dirname.split(path.sep);

var rootDirectory = directory.slice(0, directory.indexOf("TeamCode")).join(path.sep);
var srcDirectory = directory.slice(0, directory.indexOf("src") + 1);

module.exports = function () {
    //update gitignore with autoauto files
    var gitignore = fs.readFileSync(path.join(rootDirectory, ".gitignore")).toString();
    var gitignoreLines = gitignore.split(/\r?\n/);

    for (var i = 0; i < GITIGNORED.length; i++) {
        if (gitignoreLines.indexOf(GITIGNORED[i]) == -1) gitignoreLines.push(GITIGNORED[i]);
    }

    gitignore = gitignoreLines.join("\n");
    fs.writeFileSync(path.join(rootDirectory, ".gitignore"), gitignore);
    
    
    var compiledResultDirectory = path.join(srcDirectory.join(path.sep), "main/java/org/firstinspires/ftc/teamcode/__compiledcontrols");
    if(!fs.existsSync(compiledResultDirectory)) fs.mkdirSync(compiledResultDirectory);
}