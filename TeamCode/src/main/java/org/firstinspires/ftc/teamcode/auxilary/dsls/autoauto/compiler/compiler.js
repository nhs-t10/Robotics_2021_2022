var fs = require("fs");
var path = require("path");

var aaParser = require("./aa-parser.js");
var astJavaify = require("./ast-tools.js");
var parserTools = require("./parser-tools.js");
var encodeAsJavaPackageName = require("./punycode.js");
var crc = require("./crc-string.js");

var GITIGNORED = ["*__autoauto.java"];

var DEFAULT_SERVOS = [];
var DEFAULT_CRSERVOS = [];

var directory = __dirname.split(path.sep);

var templates = {
    "template": fs.readFileSync(path.join(__dirname, "data" + path.sep + "template.notjava")).toString(),
    "macro": fs.readFileSync(path.join(__dirname, "data" + path.sep + "macro.notjava")).toString()
}

var rootDirectory = directory.slice(0, directory.indexOf("TeamCode")).join(path.sep);


//update gitignore with autoauto files
var gitignore = fs.readFileSync(path.join(rootDirectory, ".gitignore")).toString();
var gitignoreLines = gitignore.split(/\r?\n/);

for(var i = 0; i < GITIGNORED.length; i++) {
    if(gitignoreLines.indexOf(GITIGNORED[i]) == -1) gitignoreLines.push(GITIGNORED[i]);
}

gitignore = gitignoreLines.join("\n");
fs.writeFileSync(path.join(rootDirectory, ".gitignore"), gitignore); //SAFE

var srcDirectory = directory.slice(0, directory.indexOf("src") + 1).join(path.sep);

//Clean up old folders
var oldCompileDirectories = [
    path.join(srcDirectory, "main/java/org/firstinspires/ftc/teamcode/__compiledautoauto"),
    path.join(srcDirectory, "main/java/org/firstinspires/ftc/teamcode/__compiledcontrols")
];
//if the folder exists, delete it!
oldCompileDirectories.forEach(x=> fs.existsSync(x) ? deleteRecursive(x) : false);

var compiledResultDirectory = path.join(srcDirectory, "../gen/org/firstinspires/ftc/teamcode/__compiledautoauto");
createDirectoryIfNotExist(compiledResultDirectory)

var autoautoFiles = loadAutoautoFilesFromFolder(srcDirectory);
var alreadyUsedAutoautoFileNames = {};

var writtenFiles = [];

for(var i = 0; i < autoautoFiles.length; i++) {
    var fileSource = fs.readFileSync(autoautoFiles[i]).toString();
    var shortButUniqueFolder = path.dirname(autoautoFiles[i]).replace(srcDirectory, "").toLowerCase();
    shortButUniqueFolder = shortButUniqueFolder.substring(shortButUniqueFolder.indexOf("teamcode"));
    var package = shortButUniqueFolder

    var packageDeclaration = "package org.firstinspires.ftc.teamcode.__compiledautoauto." + package.replace(/\/|\\/g, ".") + ";";

    var fileName = autoautoFiles[i].substring(autoautoFiles[i].lastIndexOf(path.sep) + 1);
    var templateUsed = fileName.includes(".macro") ? "macro" : "template";
    var className = jClassIfy(fileName)
        .replace(".macro.autoauto", "__macro_autoauto")
        .replace(".autoauto", "__autoauto");

    var classNameNoConflict = className;
    if(alreadyUsedAutoautoFileNames[className] && className.endsWith("__autoauto")) classNameNoConflict += "__" + crc(package);

    if(!alreadyUsedAutoautoFileNames[className]) alreadyUsedAutoautoFileNames[className] = 0;
    alreadyUsedAutoautoFileNames[className]++;

    var javaFileName = className + ".java";

    var resultFile = path.join(compiledResultDirectory, package, javaFileName);
    createDirectoryIfNotExist(resultFile);

    if(fileSource.trim() == "") {
        console.warn("WARNING: Empty autoauto file " + autoautoFiles[i])
        continue;
    }

    var uncommentedFileSource = parserTools.stripComments(fileSource);

    var frontMatter = stripAndParseFrontMatter(uncommentedFileSource);

    var javaStringFileSource = frontMatter.stripped;

    try {
        var parsedModel = aaParser.parse(uncommentedFileSource);

        var javaCreationCode = astJavaify(parsedModel);

        var programModelGeneration = javaCreationCode.genCode;
        var jsonSettingCode = javaCreationCode.jsonSettingCode;

        fs.writeFileSync(resultFile, //SAFE
            processTemplate(templates[templateUsed], className, frontMatter.frontMatter, javaStringFileSource, programModelGeneration, autoautoFiles[i], jsonSettingCode, packageDeclaration, classNameNoConflict)
        );
        writtenFiles.push(resultFile);
    } catch(e) {
        console.error(autoautoFiles[i] + ":" + (e.location ? e.location.start.line + ":" + e.location.start.column + ":" : "") + "\t" + e.toString());
        if(!e.location) console.error(e.stack);
        process.exit(1);
    }

}

//clean leftover java files from deleted autoauto modes.
clearDirectory(compiledResultDirectory, writtenFiles);

//see if more methods have been made
(require("./functionloader"));

function jClassIfy(str) {
    return str.split("-").map(x=>capitalize(x)).join("");
}
function capitalize(str) {
    return str[0].toUpperCase() + str.substring(1);
}

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

function createDirectoryIfNotExist(fileName) {
    var dirName = path.dirname(fileName);
    if(!fs.existsSync(dirName)) {
        fs.mkdirSync(dirName, {recursive: true});
    }
}

function deleteRecursive(folder) {
    clearDirectory(folder, []);
}

function processTemplate(template, className, frontMatter, javaStringFileSource, javaCreationCode, sourceFileName, jsonSettingCode, packageDeclaration, classNameNoConflict) {
    return template
        .replace("public class template", "public class " + className)
        .replace("/*NSERVO_NAMES*/", buildServoNames(frontMatter.servos))
        .replace("/*NSERVOS*/", buildServos(frontMatter.servos))
        .replace("/*JAVA_CREATION_CODE*/", javaCreationCode)
        .replace("/*CRSERVO_NAMES*/", buildCrServoNames(frontMatter.crServos))
        .replace("/*CRSERVOS*/", buildCrServos(frontMatter.crServos))
        .replace("/*PACKAGE_DECLARATION*/", packageDeclaration)
        .replace("/*JSON_SETTING_CODE*/", jsonSettingCode)
        .replace("/*NO_CONFLICT_NAME*/", classNameNoConflict)
        .replace("/*TEST_ITERATIONS*/",  (frontMatter.testIterations === undefined ? 3 : frontMatter.testIterations))
        .replace("/*OUTPUT_ASSERTATION*/", frontMatter.expectedTestOutput == undefined ? "" : `assertThat("Log printed correctly", ((TelemetryManager.LogCatcher)telemetry.log()).getLogHistory(), containsString(${JSON.stringify(frontMatter.expectedTestOutput)}));` )
        .replace("/*SOURCE_FILE_NAME*/", JSON.stringify(sourceFileName).slice(1, -1));
}

function buildServoNames(servos) {
    if(servos === undefined) servos = DEFAULT_SERVOS;
    return servos.map(x=> `"${x}"`).join(", ");
}

function buildCrServoNames(crServos) {
    if(crServos === undefined) crServos = DEFAULT_CRSERVOS;
    return crServos.map(x=> `"${x}"`).join(", ");
}

function buildCrServos(crServos) {
    if(crServos === undefined) crServos = DEFAULT_CRSERVOS;
    return crServos.map(x=> `hardwareMap.get(CRServo.class, "${x}")`).join(", ");
}

function buildServos(servos) {
    if(servos === undefined) servos = DEFAULT_SERVOS;
    return servos.map(x=> `hardwareMap.get(Servo.class, "${x}")`).join(", ");
}

function stripAndParseFrontMatter(src) {
    var startDollarSign = parserTools.findUngroupedSubstring(src, "$");
    if(startDollarSign == -1) return { stripped: src, frontMatter: {} };

    var endDollarSign = startDollarSign + 1 + parserTools.findUngroupedSubstring(src.substring(startDollarSign + 1), "$");
    if(endDollarSign == -1) throw src;

    var frontMatter = eval("({" + src.substring(startDollarSign + 1, endDollarSign) + "})");

    return {
        stripped: src.substring(parserTools.findUngroupedSubstring(src, "#")),
        frontMatter: frontMatter
    }
}

function loadAutoautoFilesFromFolder(folder) {
    let results = [];

    let folderContents = fs.readdirSync(folder, {
        withFileTypes: true
    });

    for(var i = 0; i < folderContents.length; i++) {
        let subfile = folderContents[i];

        if(subfile.isDirectory()) {
            results = results.concat(loadAutoautoFilesFromFolder(path.resolve(folder, subfile.name)));
        } else if(subfile.isFile() && subfile.name.endsWith(".autoauto")) {
            results.push(path.resolve(folder, subfile.name));
        }
    }

    return results;
}