var crypto = require("crypto");

var fs = require("fs");
var path = require("path");

var safeFsUtils = require("../script-helpers/safe-fs-utils");

var cacheDir = path.join(__dirname, ".cache");
if(!fs.existsSync(cacheDir)) fs.mkdirSync(cacheDir);

var CACHE_MAX_BYTES = 100000000; //100 MB

cleanOldCache();

module.exports = {
    save: function(key, value) {
        var encodedKey = sha(key);
        
        var file = keyFile(encodedKey);
        
        safeFsUtils.createDirectoryIfNotExist(file);

        fs.writeFileSync(file, JSON.stringify(value) || null);
    },
    get: function(key, defaultValue) {        
        var encodedKey = sha(key);
        
        var newFile = keyFile(encodedKey);
        var file = path.join(cacheDir, encodedKey);
        
        if(fs.existsSync(file)) return JSON.parse(fs.readFileSync(file).toString());
        
        else if(fs.existsSync(newFile)) return JSON.parse(fs.readFileSync(newFile).toString());
        
        else return defaultValue;
    },
    remove: function(key) {
        var encodedKey = sha(key);
        var file = keyFile(encodedKey);
        if(fs.existsSync(file)) fs.unlinkSync(file);
    }
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