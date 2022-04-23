const fs = require("fs");
const path = require("path");
const crypto = require("crypto");

const androidStudioLogging = require("../../script-helpers/android-studio-logging");

const transmutations = require("./transmutations");
const cache = require("../../cache");
const CACHE_VERSION = require("../config").CACHE_VERSION;
const commandLineInterface = require("../../command-line-interface");
const safeFsUtils = require("../../script-helpers/safe-fs-utils");
const makeWorkersPool = require("./workers-pool");
const autoautoFolderScanner = require("./folder-scanner");

var SRC_DIRECTORY = __dirname.substring(0 , __dirname.indexOf("src") + "src".length + 1);
var COMPILED_RESULT_DIRECTORY = path.join(SRC_DIRECTORY, "../gen/org/firstinspires/ftc/teamcode/__compiledautoauto");


(async function main() {
    await transmutations.loadTaskList();
    compileAllFromSourceDirectory();
})();

async function compileAllFromSourceDirectory() {
    const compilerWorkers = makeWorkersPool();
    const autoautoFileContexts = [], codebaseTasks = {};

    //this callback will call once for each file.
    //this way, we don't have to wait for ALL filenames in order to start compiling.
    //it starts after the first one!
    var aaFiles = autoautoFolderScanner(SRC_DIRECTORY);

    while(true) {
        var next = aaFiles.next();
        if(next.done) break;
        await makeContextAndCompileFile(next.value, codebaseTasks, compilerWorkers, autoautoFileContexts);
    }

    evaluateCodebaseTasks(autoautoFileContexts, codebaseTasks);
    compilerWorkers.close();
}

function makeContextAndCompileFile(filename, codebaseTasks, compilerWorkers, autoautoFileContexts) {  
    var fileContext = makeFileContext(filename);
    var fileCache = `autoauto compiler file cache ${fileContext.sourceFullFileName}`;
    var cacheEntry = cache.get(fileCache, false);

    markCodebaseTasks(fileContext, codebaseTasks);

    return new Promise(function(resolve, reject) {
        if(cacheEntry.subkey == fileContext.cacheKey) {
            androidStudioLogging.sendMessages(cacheEntry.log);
            writeAndCallback(cacheEntry.data, autoautoFileContexts, resolve);
        } else {
            compilerWorkers.giveJob(fileContext, function(run) {
                if(run.success) cache.save(fileCache, {
                    subkey: fileContext.cacheKey,
                    data: run.fileContext,
                    log: run.log
                });
                androidStudioLogging.sendMessages(run.log);
                writeAndCallback(run.fileContext, autoautoFileContexts, resolve);
            });
        }
    });
}

function writeAndCallback(finishedFileContext, autoautoFileContexts, cb) {
    autoautoFileContexts.push(finishedFileContext);
    writeWrittenFiles(finishedFileContext);
    cb(finishedFileContext);
}

function evaluateCodebaseTasks(allFileContexts, codebaseTasks) {
    for(var id in codebaseTasks) {
        var mutFunc = require(codebaseTasks[id].sourceFile);
        mutFunc(allFileContexts[0], allFileContexts);
    }
}

function sha(s) {
    return crypto.createHash("sha256").update(s).digest("hex");
}

function makeFileContext(file) {
    var resultFile = getResultFor(file);
    var fileContent = fs.readFileSync(file).toString();
    var frontmatter = loadFrontmatter(fileContent);

    var tPath = transmutations.expandTasks(frontmatter.compilerMode || "default");
    
    var ctx = {
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
        lastInput: fileContent,
        inputs: {},
        cacheKey: undefined,
        writtenFiles: {},

        transmutations: tPath,
        readsAllFiles: tPath.map(x=>x.readsFiles || []).flat()
    };

    ctx.cacheKey = makeCacheKey(ctx);

    return ctx;
}

function writeWrittenFiles(fileContext) {
    for(var filename in fileContext.writtenFiles) {
        safeFsUtils.safeWriteFile(filename, fileContext.writtenFiles[filename]);
    }
}

function markCodebaseTasks(fileContext, codebaseTasks) {
    for(var i = 0; i < fileContext.transmutations.length; i++) {
        var mut = fileContext.transmutations[i];
        if(mut.type.startsWith("codebase_")) {
            codebaseTasks[mut.id] = mut;
            fileContext.transmutations.splice(i,1);
            i--;
        }
    }
}

/**
 * 
 * @param {import("./transmutations").TransmutateContext} fileContext 
 */
function makeCacheKey(fileContext) {
    
    var readFileShas = fileContext.readsAllFiles.map(x=>sha(safeFsUtils.cachedSafeReadFile(x))).join("\t");
    var transmutationIdList = fileContext.transmutations.map(x=>x.id).join("\t");

    var keyDataToSha = [CACHE_VERSION, readFileShas, fileContext.sourceFullFileName, fileContext.fileContentText, transmutationIdList];

    return sha(keyDataToSha.join("\0"));
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
