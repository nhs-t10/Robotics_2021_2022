var fs = require("fs");
var path = require("path");
var crypto = require("crypto");

const androidStudioLogging = require("../../script-helpers/android-studio-logging");

var transmutations = require("./transmutations");
const cache = require("../../cache");
const CACHE_VERSION = require("../config").CACHE_VERSION;

var directory = __dirname.split(path.sep);

var SRC_DIRECTORY = directory.slice(0, directory.indexOf("src") + 1).join(path.sep);

var COMPILED_RESULT_DIRECTORY = path.join(SRC_DIRECTORY, "../gen/org/firstinspires/ftc/teamcode/__compiledautoauto");
createDirectoryIfNotExist(COMPILED_RESULT_DIRECTORY);

var autoautoFileNames = loadAutoautoFilesFromFolder(SRC_DIRECTORY);

var autoautoFileContexts = {};

var codebaseTasks = [];

for(var i = 0; i < autoautoFileNames.length; i++) {
    var file = autoautoFileNames[i];
    var resultFile = getResultFor(file);
    var fileContent = fs.readFileSync(file).toString();
    var frontmatter = loadFrontmatter(fileContent);
    
    var fileContext = {
        sourceBaseFileName: path.basename(file),
        sourceDir: path.dirname(file),
        sourceFullFileName: file,
        sourceRoot: SRC_DIRECTORY,
        resultBaseFileName: path.basename(resultFile),
        resultDir: path.dirname(resultFile),
        resultFullFileName: resultFile,
        resultRoot: COMPILED_RESULT_DIRECTORY,
        fileFrontmatter: frontmatter,
        fileContentText: fileContent,
        lastInput: null,
        inputs: {},
        cacheKey: undefined,
        usedFiles: []
    };

    fileContext.usedFiles.push(fileContext.resultFullFileName);
    
    autoautoFileContexts[file] = fileContext;
    
    
    var tPath = transmutations.expandTasks(frontmatter.compilerMode || "default");
    
    for(var j = 0; j < tPath.length; j++) {
        var mut = tPath[j];
        
        if(mut.type.startsWith("codebase_")) {
            codebaseTasks.push(mut);
            continue;
        }
        
        var mutRan = cachedTryRunTransmutation(mut, fileContext);
        
        if(!mutRan) break;
    }
}

var allFileContexts = Object.values(autoautoFileContexts);
    
for(var j = 0; j < codebaseTasks.length; j++) {
    var mut = codebaseTasks[j];
    
    mut.run(allFileContexts[0], allFileContexts);
}

function cachedRunTransmutation(transmutation, fileContext) {
    if (!fileContext.cacheKey) {
        fileContext.cacheKey = sha(fileContext.sourceFullFileName + " " + transmutation.id + "\0" + fileContext.fileContentText);
    }
    var miss = {};
    var k = `autoauto compiler task ${fileContext.cacheKey} ${transmutation.id}`;
    
    var cacheEntry = cache.get(k, miss);
    if(transmutation.neverCache || cacheEntry == miss || cacheEntry.v != CACHE_VERSION) {
        
        androidStudioLogging.beginOutputCapture();
        runTransmutation(transmutation, fileContext);
        var log = androidStudioLogging.getCapturedOutput();
        
        if(fileContext.status == "pass") {
            try {
                cache.save(k, { v: CACHE_VERSION, c: fileContext.inputs[transmutation.id], log: log });

                fileContext.cacheKey = sha(fileContext.cacheKey + JSON.stringify(fileContext.inputs[transmutation.id]));
            } catch(e) {
                throw "Problem caching result of " + transmutation.id;
            }
        }
        
    } else {
        fileContext.status = "pass";

        if(cacheEntry.log) androidStudioLogging.sendMessages(cacheEntry.log);
        
        fileContext.inputs[transmutation.id] = cacheEntry.c;
        if (cacheEntry.c !== undefined && !transmutation.isDependency) fileContext.lastInput = cacheEntry.c;
    }
}

function sha(s) {
    return crypto.createHash("sha256").update(s + "").digest("hex");
}


function cachedTryRunTransmutation(transmutation, fileContext) {
    try {
        delete fileContext.status;
        
        cachedRunTransmutation(transmutation, fileContext);
        
        if(fileContext.status != "pass") throw {kind: "ERROR", text: `Task ${transmutation.id} didn't report success` };
        
        return true;
    } catch(e) {
        fileContext.status = "fail";

        androidStudioLogging.sendTreeLocationMessage(e, fileContext.sourceFullFileName, "ERROR");
        
        return false;
    }
}

function runTransmutation(transmutation, fileContext) {
    
    var c = {};
    Object.assign(c, fileContext);
    c.usedFiles = null;
    
    transmutation.run(c);
    
    fileContext.status = c.status;
    if(c.usedFiles) fileContext.usedFiles = fileContext.usedFiles.concat(c.usedFiles);
    
    fileContext.inputs[transmutation.id] = c.output;
    if(c.output !== undefined && !transmutation.isDependency) fileContext.lastInput = c.output;
}

function getResultFor(filename) {
    var folder = path.dirname(filename);
    
    var packageFolder = folder
        .replace(SRC_DIRECTORY, "").toLowerCase();
    packageFolder = packageFolder.substring(packageFolder.indexOf("teamcode"));
    
    var javaFileName = jClassIfy(filename) + ".java";
        
    return path.join(COMPILED_RESULT_DIRECTORY, packageFolder, javaFileName);
}

function jClassIfy(str) {
    var p = str.split(/\/|\\/);
    var s = p[p.length - 1].split(".")[0];
    return s.split("-").map(x=>capitalize(x)).join("");
}
function capitalize(str) {
    return str[0].toUpperCase() + str.substring(1);
}

function loadFrontmatter(fCont) {
    
    try {
        var fmI = fCont.indexOf("$");
        if(fmI == -1) return {};
        
        var fmE = fCont.indexOf("$", fmI + 1);
        if(fmE == -1) return {};
        
        var fmPar = (new Function(`return ({${fCont.substring(fmI + 1, fmE)}})`))();
        
        return fmPar;
        
    } catch(e) {
        return {};
    }
}

function createDirectoryIfNotExist(fileName) {
    var dirName = path.dirname(fileName);
    if(!fs.existsSync(dirName)) {
        fs.mkdirSync(dirName, {recursive: true});
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