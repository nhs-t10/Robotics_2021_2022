var crypto = require("crypto");

var fs = require("fs");
var path = require("path");

var punycode = require("../script-helpers/punycode");

var cacheDir = path.join(__dirname, ".cache");
if(!fs.existsSync(cacheDir)) fs.mkdirSync(cacheDir);

var CACHE_MAX_BYTES = 100000000; //100 MB

cleanOldCache();

module.exports = {
    save: function(key, value) {
        var encodedKey = sha(key);
        var file = path.join(cacheDir, encodedKey);

        fs.writeFileSync(file, JSON.stringify(value));
    },
    get: function(key, defaultValue) {
        migrateKeys(punycode(key), sha(key));
        
        var encodedKey = sha(key);
        
        var file = path.join(cacheDir, encodedKey);
        if(!fs.existsSync(file)) return defaultValue;

        return JSON.parse(fs.readFileSync(file).toString());
    }
}

function migrateKeys(oldKey, newKey) {
    var oldFile = path.join(cacheDir, oldKey);
    var newFile = path.join(cacheDir, newKey);
    
    if(fs.existsSync(oldFile) && !fs.existsSync(newFile)) fs.renameSync(oldFile, newFile);
}

function sha(k) {
    return crypto.createHash("sha256").update(k + "").digest("hex");
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