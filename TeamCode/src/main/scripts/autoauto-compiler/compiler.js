var fs = require("fs");
var path = require("path");

var aaParser = require("./aa-parser.js");
var astJavaify = require("./autoauto-to-java.js");
var parserTools = require("../script-helpers/parser-tools");

var crc = require("../script-helpers/crc-string");

var GITIGNORED = ["*__autoauto.java"];

var DEFAULT_SERVOS = [];
var DEFAULT_CRSERVOS = [];

var directory = __dirname.split(path.sep);

var runChecks = require("./checks");
var makeTestFile = require("./make-test");

var templates = {
    "template": fs.readFileSync(path.join(__dirname, "data" + path.sep + "template.notjava")).toString()
}

var rootDirectory = directory.slice(0, directory.indexOf("TeamCode")).join(path.sep);


//update gitignore with autoauto files
if(!fs.existsSync(path.join(rootDirectory, ".gitignore"))) {
    fs.writeFileSync(path.join(rootDirectory, ".gitignore"), "");
}
var gitignore = fs.readFileSync(path.join(rootDirectory, ".gitignore")).toString();
var gitignoreLines = gitignore.split(/\r?\n/);

for(var i = 0; i < GITIGNORED.length; i++) {
    if(gitignoreLines.indexOf(GITIGNORED[i]) == -1) gitignoreLines.push(GITIGNORED[i]);
}

gitignore = gitignoreLines.join("\n");
fs.writeFileSync(path.join(rootDirectory, ".gitignore"), gitignore); //SAFE

var srcDirectory = directory.slice(0, directory.indexOf("src") + 1).join(path.sep);

var compiledResultDirectory = path.join(srcDirectory, "../gen/org/firstinspires/ftc/teamcode/__compiledautoauto");
createDirectoryIfNotExist(compiledResultDirectory);

var TESTS_PACKAGE = "org.firstinspires.ftc.teamcode.unitTests.__testedautoauto";
var testsDirectory = path.join(srcDirectory, "test/java/" + TESTS_PACKAGE.replace(/\./g, path.sep));

var autoautoFiles = loadAutoautoFilesFromFolder(srcDirectory);
var alreadyUsedAutoautoFileNames = {};

var writtenFiles = [];
var requiredTests = [];

for(var i = 0; i < autoautoFiles.length; i++) {
    var fileSource = fs.readFileSync(autoautoFiles[i]).toString();
    var folder = path.dirname(autoautoFiles[i]);
    var shortButUniqueFolder = folder.replace(srcDirectory, "").toLowerCase();
    shortButUniqueFolder = shortButUniqueFolder.substring(shortButUniqueFolder.indexOf("teamcode"));
    var packageFolder = shortButUniqueFolder

    var package = "org.firstinspires.ftc.teamcode.__compiledautoauto." + packageFolder.replace(/\/|\\/g, ".");

    var fileName = autoautoFiles[i].substring(autoautoFiles[i].lastIndexOf(path.sep) + 1);
    if (fileName.includes(".macro")) throw "Macro is a bad idea you idiots!";
    var templateUsed = "template";
    var className = jClassIfy(fileName)
        .replace(".autoauto", "__autoauto");

    var classNameNoConflict = className;
    if(alreadyUsedAutoautoFileNames[className]) classNameNoConflict += "__" + alreadyUsedAutoautoFileNames[className];

    if(!alreadyUsedAutoautoFileNames[className]) alreadyUsedAutoautoFileNames[className] = 0;
    alreadyUsedAutoautoFileNames[className]++;

    var javaFileName = className + ".java";

    var resultFile = path.join(compiledResultDirectory, packageFolder, javaFileName);
    createDirectoryIfNotExist(resultFile);

    var uncommentedFileSource = parserTools.stripComments(fileSource);

    var frontMatter = stripAndParseFrontMatter(uncommentedFileSource);

    var parsedModel;
    try {
        parsedModel = aaParser.parse(fileSource);
    } catch(e) {
        parsedModel = e;
    }

    if(!runChecks(parsedModel, folder, fileName, fileSource, uncommentedFileSource)) continue;
    if(parsedModel instanceof Error) continue;

    var javaCreationCode = astJavaify(parsedModel);

    var jsonSettingCode = javaCreationCode.jsonSettingCode;

    fs.writeFileSync(resultFile, //SAFE
        processTemplate(templates[templateUsed], className, frontMatter, javaCreationCode.genCode, autoautoFiles[i], jsonSettingCode, package, classNameNoConflict)
    );

    requiredTests.push({
        className: className, package: package, frontMatter: frontMatter
    });
    writtenFiles.push(resultFile);

}

//clean leftover java files from deleted autoauto modes.
clearDirectory(compiledResultDirectory, writtenFiles);

makeTestFile(requiredTests, testsDirectory, TESTS_PACKAGE);

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

function processTemplate(template, className, frontMatter, javaCreationCode, sourceFileName, jsonSettingCode, package, classNameNoConflict) {
    return template
        .replace("public class template", "public class " + className)
        .replace("/*NSERVO_NAMES*/", buildServoNames(frontMatter.servos))
        .replace("/*NSERVOS*/", buildServos(frontMatter.servos))
        .replace("/*JAVA_CREATION_CODE*/", javaCreationCode)
        .replace("/*CRSERVO_NAMES*/", buildCrServoNames(frontMatter.crServos))
        .replace("/*CRSERVOS*/", buildCrServos(frontMatter.crServos))
        .replace("/*PACKAGE_DECLARATION*/", "package " + package + ";")
        .replace("/*JSON_SETTING_CODE*/", jsonSettingCode)
        .replace("/*NO_CONFLICT_NAME*/", classNameNoConflict)
        .replace("/*SOURCE_FILE_NAME*/", JSON.stringify(sourceFileName).slice(1, -1))
        .replace("/*ERROR_STACK_TRACE_HEIGHT*/", (+frontMatter.errorStackTraceHeight) || 1)
        .replace("/*COMPAT_MODE_SETTING*/", frontMatter.oldAfterCompat ? getCompatModeSetter() : "");
}

function getCompatModeSetter() {
    return "runtime.rootModule.globalScope.systemSet(org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoSystemVariableNames.COMPAT_MODE, new AutoautoBooleanValue(true));"
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
    if(startDollarSign == -1) return { };

    var endDollarSign = startDollarSign + 1 + parserTools.findUngroupedSubstring(src.substring(startDollarSign + 1), "$");
    if(endDollarSign == -1) throw src;

    var frontMatter = eval("({" + src.substring(startDollarSign + 1, endDollarSign) + "})");

    return frontMatter;
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