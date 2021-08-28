var fs = require("fs");
var path = require("path");

var aaParser = require("./aa-parser.js");
var astJavaify = require("./ast-tools.js");
var parserTools = require("./parser-tools.js");

var GITIGNORED = ["*__autoauto.java"];

var DEFAULT_SERVOS = ["wobbleArmRight","wobbleArmLeft" , "wobbleGrabRight","wobbleGrabLeft", "shooterStop", "shooterArm"];
var DEFAULT_CRSERVOS = [];

var directory = __dirname.split(path.sep);

var PACKAGE_DECLARATION = "package org.firstinspires.ftc.teamcode.__compiledautoauto;";

var template = fs.readFileSync(path.join(__dirname, "data" + path.sep + "template.notjava")).toString();

var rootDirectory = directory.slice(0, directory.indexOf("TeamCode")).join(path.sep);


//update gitignore with autoauto files
var gitignore = fs.readFileSync(path.join(rootDirectory, ".gitignore")).toString();
var gitignoreLines = gitignore.split(/\r?\n/);

for(var i = 0; i < GITIGNORED.length; i++) {
    if(gitignoreLines.indexOf(GITIGNORED[i]) == -1) gitignoreLines.push(GITIGNORED[i]);
}

gitignore = gitignoreLines.join("\n");
fs.writeFileSync(path.join(rootDirectory, ".gitignore"), gitignore);

var srcDirectory = directory.slice(0, directory.indexOf("src") + 1);
var compiledResultDirectory = path.join(srcDirectory.join(path.sep), "main/java/org/firstinspires/ftc/teamcode/__compiledautoauto");
if(!fs.existsSync(compiledResultDirectory)) fs.mkdirSync(compiledResultDirectory);

var autoautoFiles = loadAutoautoFilesFromFolder(srcDirectory.join(path.sep));

for(var i = 0; i < autoautoFiles.length; i++) {
    var fileSource = fs.readFileSync(autoautoFiles[i]).toString();
    var fileName = autoautoFiles[i].substring(autoautoFiles[i].lastIndexOf(path.sep) + 1);
    var className = fileName.replace(".autoauto", "__autoauto");
    var javaFileName = className + ".java";

    var resultFile = compiledResultDirectory + "/" + javaFileName;

    if(fileSource.trim() == "") {
        console.warn("WARNING: Empty autoauto file " + className)
        fs.writeFileSync(resultFile, "");
        continue;
    }

    var uncommentedFileSource = parserTools.stripComments(fileSource);

    var frontMatter = stripAndParseFrontMatter(uncommentedFileSource);

    console.log("frontmatter : " + JSON.stringify(frontMatter.frontMatter));

    var javaStringFileSource = frontMatter.stripped;

    try {
        var parsedModel = aaParser.parse(fileSource);

        var javaCreationCode = astJavaify(parsedModel);

        fs.writeFileSync(resultFile, processTemplate(template, className, frontMatter.frontMatter, javaStringFileSource, javaCreationCode, autoautoFiles[i]));
    } catch(e) {
        console.error(autoautoFiles[i] + ":" + (e.location ? e.location.start.line + ":" + e.location.start.column + ":" : "") + "\t" + e.toString());
        if(!e.location) console.error(e.stack);
        process.exit(1);
    }

}

function processTemplate(template, className, frontMatter, javaStringFileSource, javaCreationCode, sourceFileName) {
    return template
        .replace("public class template", "public class " + className)
        .replace("/*NSERVO_NAMES*/", buildServoNames(frontMatter.servos))
        .replace("/*NSERVOS*/", buildServos(frontMatter.servos))
        .replace("/*JAVA_CREATION_CODE*/", javaCreationCode)
        .replace("/*CRSERVO_NAMES*/", buildCrServoNames(frontMatter.crServos))
        .replace("/*CRSERVOS*/", buildCrServos(frontMatter.crServos))
        .replace("/*PACKAGE_DECLARATION*/", PACKAGE_DECLARATION)
        .replace("/*TEST_ITERATIONS*/",  (frontMatter.testIterations === undefined ? 3 : frontMatter.testIterations))
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