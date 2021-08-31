var fs = require("fs");
var path = require("path");

var generateCode = require("./generate-code.js");
var peg = require("./pegjs");
var processTemplate = require("./template-processor.js");

//initiate folders, gitignore rules, etc. that are required
(require("./initializer.js"))();

var template = fs.readFileSync(path.join(__dirname, "templates/default.notjava")).toString();
var teleTemplate = fs.readFileSync(path.join(__dirname, "templates/telemetry.notjava")).toString();

var controlsPegjs = fs.readFileSync(path.join(__dirname, "controls.pegjs")).toString();

var parser = peg.generate(controlsPegjs);

var directory = __dirname.split(path.sep);
var srcDirectory = directory.slice(0, directory.indexOf("src") + 1);

var controlFiles = loadControlsFilesFromFolder(srcDirectory.join(path.sep));

for(var i = 0; i < controlFiles.length; i++) {
    var filename = controlFiles[i];
    
    var fileContent = fs.readFileSync(filename).toString();
    var ast;
    try {
        ast = parser.parse(fileContent);
    } catch(e) {
        console.error(`${filename}:${e.location.start.line}:${e.location.start.column}: Syntax error; could not compile. ${e.message}`);
        process.exitCode = 1;
        throw "";
    }
    
    try {
        var code = generateCode(ast);
    } catch(e) {
        if(typeof e == "object" && e.location) {
            console.error(filename + ":" + e.location.start.line + ": " + e.message);
            process.exitCode = 1;
            throw "";
        }
        else {
            throw e;
        }
    }

    //variation: telemtetry
    if(ast.header.includes("TelemetryManager")) {
        var opmode = processTemplate(teleTemplate, code, filename, "t");
        fs.writeFileSync(getResultFilename(filename, "t"), opmode);
    }
    var opmode = processTemplate(template, code, filename);
    
    fs.writeFileSync(getResultFilename(filename), opmode);
}

function getResultFilename(filename, variation) {
    var folder = path.join(srcDirectory.join(path.sep), "main/java/org/firstinspires/ftc/teamcode/__compiledcontrols");
    return path.join(folder, path.basename(filename, ".controls") +  (variation ? "_" + variation : "") + "__controls.java")
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
        } else if(subfile.isFile() && subfile.name.endsWith(".controls")) {
            results.push(path.resolve(folder, subfile.name));
        }
    }

    return results;
}