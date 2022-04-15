var crypto = require("crypto");

var fs = require("fs");
var path = require("path");

var safeFsUtils = require("../script-helpers/safe-fs-utils");
const structuredSerialise = require("../script-helpers/structured-serialise");

var cacheDir = path.join(__dirname, ".cache");
if(!fs.existsSync(cacheDir)) fs.mkdirSync(cacheDir);

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
        var encodedKey = sha(key);
        
        var newFile = keyFile(encodedKey);
        var file = path.join(cacheDir, encodedKey);
        
        if(fs.existsSync(file)) return deserialiseData(fs.readFileSync(file));
        
        else if(fs.existsSync(newFile)) return deserialiseData(fs.readFileSync(newFile));
        
        else return defaultValue;
    },
    remove: function(key) {
        var encodedKey = sha(key);
        var file = keyFile(encodedKey);
        if(fs.existsSync(file)) fs.unlinkSync(file);
    }
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

function keyFile(encodedKey) {
    return path.join(cacheDir, encodedKey.substring(0,3), encodedKey.substring(3) + ".cached");
}

function migrateKeys(oldKey, newKey) {
    var oldFile = path.join(cacheDir, oldKey);
    var newFile = path.join(cacheDir, newKey);
    
    if(fs.existsSync(oldFile) && !fs.existsSync(newFile)) fs.renameSync(oldFile, newFile);
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