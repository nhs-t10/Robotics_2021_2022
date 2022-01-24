var fs = require("fs");
var path = require("path");

var punycode = require("../script-helpers/punycode");

var structuredSerialise = require("./structured-serialise");

var cacheDir = path.join(__dirname, ".cache");
if(!fs.existsSync(cacheDir)) fs.mkdirSync(cacheDir);

module.exports = {
    save: function(key, value) {
        var encodedKey = punycode(key);
        var file = path.join(cacheDir, encodedKey);

        fs.writeFileSync(file, JSON.stringify(value));
    },
    get: function(key, defaultValue) {
        var encodedKey = punycode(key);
        var file = path.join(cacheDir, encodedKey);
        if(!fs.existsSync(file)) return defaultValue;

        return JSON.stringify(fs.readFileSync(file).toString());
    }
}