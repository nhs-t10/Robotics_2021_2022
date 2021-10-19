var fs = require("fs");
var path = require("path");
const astTools = require("./ast-tools");

var parser = require("./parser");

var directory = __dirname.split(path.sep);
var srcDirectory = directory.slice(0, directory.indexOf("src") + 1);

var controlFiles = loadControlsFilesFromFolder(srcDirectory.join(path.sep));

createResultFolder();

for(var i = 0; i < controlFiles.length; i++) {
    var fileContent = fs.readFileSync(controlFiles[i]).toString();
    var basename = path.basename(controlFiles[i], ".c3");
    var parsed = parser(fileContent);
    var source = astTools(parsed, basename + "__controls3");
    
    fs.writeFileSync(getResultFilename(controlFiles[i]), source);
}

function createResultFolder() {
    var folder = path.join(srcDirectory.join(path.sep), "main/java/org/firstinspires/ftc/teamcode/__compiledcontrols");
    if(!fs.existsSync(folder)) fs.mkdirSync(folder);
}

function getResultFilename(filename) {
    var folder = path.join(srcDirectory.join(path.sep), "main/java/org/firstinspires/ftc/teamcode/__compiledcontrols");
    return path.join(folder, path.basename(filename, ".c3") + "__controls3.java")
}

function loadControlsFilesFromFolder(folder) {
    let results = [];

    let folderContents = fs.readdirSync(folder, {
        withFileTypes: true
    });

    for(var i = 0; i < folderContents.length; i++) {
        let subfile = folderContents[i];

        if(subfile.isDirectory()) {
            results = results.concat(loadControlsFilesFromFolder(path.resolve(folder, subfile.name)));
        } else if(subfile.isFile() && subfile.name.endsWith(".c3")) {
            results.push(path.resolve(folder, subfile.name));
        }
    }

    return results;
}