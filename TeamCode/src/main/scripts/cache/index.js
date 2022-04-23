var crypto = require("crypto");

var fs = require("fs");
var path = require("path");
const commandLineInterface = require("../command-line-interface");

var safeFsUtils = require("../script-helpers/safe-fs-utils");
const structuredSerialise = require("../script-helpers/structured-serialise");

var oldCacheDir = path.join(__dirname, ".cache");

var cacheDir = findCacheDirectory();

var CACHE_MAX_BYTES = 100000000; //100 MB

cleanOldCache();

module.exports = {
    save: function(key, value) {
        var encodedKey = sha(key);
        
        var file = keyFile(encodedKey);
        
        safeFsUtils.createDirectoryIfNotExist(file);

        fs.writeFileSync(file, serialiseData(value) || null);
    },
    get: function(key, defaultValue) {
        if(commandLineInterface["no-cache"]) return defaultValue;

        var encodedKey = sha(key);
        
        var possibleFilesInAgeOrder = [
            keyFile(encodedKey),
            keyFile(encodedKey, 3, oldCacheDir),
            keyFile(encodedKey, 0, oldCacheDir)
        ];

        for(var i = 0; i < possibleFilesInAgeOrder.length; i++) {
            var f = possibleFilesInAgeOrder[i];
            if(fs.existsSync(f)) return deserialiseData(fs.readFileSync(f));
        }
        
        return defaultValue;
    },
    remove: function(key) {
        var encodedKey = sha(key);
        var file = keyFile(encodedKey);
        if(fs.existsSync(file)) fs.unlinkSync(file);
    }
}

function findCacheDirectory() {
    var SIGIL = ".autoauto-compiler-cache-directory";

    var gitRoot = safeFsUtils.getGitRootDirectory();
    var folder = "";
    if(gitRoot) {
        var gradleFolder = path.join(gitRoot, ".gradle");
        if(fs.existsSync(gradleFolder)) {
            folder = path.join(gradleFolder, SIGIL)
        } else {
            folder = path.join(gitRoot, SIGIL);
            safeFsUtils.addToGitignore(SIGIL + "/**");
        }
    } else {
        folder = path.join(require("os").homedir(), SIGIL);
    }

    if(!fs.existsSync(folder)) fs.mkdirSync(folder);
    return folder;
}

function serialiseData(data) {
    try {
        return structuredSerialise.toBuffer(data);
    } catch(e) {
        console.error(data);
        throw new Error("Couldn't serialise. " + e.message);
    }
}

function deserialiseData(dataBuffer) {
    if(isStructuredSerialised(dataBuffer)) return structuredSerialise.fromBuffer(dataBuffer);
    else return JSON.parse(dataBuffer.toString());
}

function isStructuredSerialised(dataBuffer) {
    var maybeMagic = dataBuffer.slice(0, structuredSerialise.magic.length);
    if(maybeMagic.join(",") == structuredSerialise.magic.join(",")) return true;
    else return false;
}

function keyFile(encodedKey, n, pfx) {
    if(n === undefined) n = 2;
    if(!pfx) pfx = cacheDir;

    return path.join(pfx, encodedKey.substring(0,n), encodedKey.substring(n) + ".cached");
}

function sha(k) {
    return crypto.createHash("sha256").update(JSON.stringify(k)).digest("hex");
}

function cleanOldCache() {
    var cacheFiles = fs.readdirSync(cacheDir);
    var cacheFileStats = cacheFiles.map(x=>({name: x, stats: fs.statSync(path.join(cacheDir, x)) }) );
    
    var sortedYoungestToOldest = cacheFileStats.sort((a,b)=>b.stats.mtimeMs - a.stats.mtimeMs);
    
    var totalSize = cacheFileStats.reduce((a,b)=>a + b.stats.size, 0);
    
    while(totalSize > CACHE_MAX_BYTES) {
        var toRm = sortedYoungestToOldest.pop();
        totalSize -= toRm.stats.size;
        fs.unlinkSync(path.join(cacheDir, toRm.name));
    }
}