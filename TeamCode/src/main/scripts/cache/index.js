var crypto = require("crypto");

var fs = require("fs");
var path = require("path");

var punycode = require("../script-helpers/punycode");

var cacheDir = path.join(__dirname, ".cache");
if(!fs.existsSync(cacheDir)) fs.mkdirSync(cacheDir);

module.exports = {
    save: function(key, value) {
        var encodedKey = sha(key);
        var file = path.join(cacheDir, encodedKey);

        fs.writeFileSync(file, JSON.stringify(value));
    },
    get: function(key, defaultValue) {
        migrateKeys(punycode(key), hashKey(key));
        
        var encodedKey = sha(key);
        
        var file = path.join(cacheDir, encodedKey);
        if(!fs.existsSync(file)) return defaultValue;

        return JSON.stringify(fs.readFileSync(file).toString());
    }
}

function migrateKeys(oldKey, newKey) {
    var oldFile = path.join(cacheDir, oldKey);
    var newFile = path.join(cacheDir, newKey);
    
    if(fs.existsSync(oldFile) && !fs.existsSync(newFile)) fs.renameSync(oldFile, newFile);
}

function sha(k) {
    return crypto.createHash("sha256").update(k).digest("hex");
}