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
    var frontmatter = loadFrontmatter(file);
    
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
        lastInput: null,
        inputs: {},
        cacheKey: undefined
    };
    
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
    if (!fileContext.cacheKey) fileContext.cacheKey = sha(fileContext.sourceFullFileName + " " + transmutation.id);
    var miss = {};
    var k = `${CACHE_VERSION} autoauto compiler task ${fileContext.cacheKey} ${transmutation.id}`;
    
    var cacheEntry = cache.get(k, miss);
    if(cacheEntry == miss) {
        runTransmutation(transmutation, fileContext);
        
        if(fileContext.status == "pass") {
            cache.save(k, fileContext.inputs[transmutation.id]);
            fileContext.cacheKey = sha(fileContext.cacheKey + JSON.stringify(fileContext.inputs[transmutation.id]));
        }
        
    } else {
        fileContext.status = "pass";
        
        fileContext.inputs[transmutation.id] = cacheEntry;
        if (cacheEntry != undefined && !transmutation.isDependency) fileContext.lastInput = cacheEntry;
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
    
    transmutation.run(c);
    
    fileContext.status = c.status;
    
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

function loadFrontmatter(filename) {
    var fCont = fs.readFileSync(filename).toString();
    
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