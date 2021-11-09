var fs = require("fs");
var path = require("path");

var cacheFile = path.join(__dirname, "last-build-pixels.json");
if(!fs.existsSync(cacheFile)) fs.writeFileSync(cacheFile, JSON.stringify({c:"",p:"buildimgs/0.png"}));

var pngFromHash = require("./create-png-from-hash.js");
var deltaHashDirectory = require("./delta-hash-directory.js");

module.exports = function(buildNumber, directory, ignored) {
    var hexHash = deltaHashDirectory(directory, ignored);
    var nonzeroBuildAddress = pngFromHash(buildNumber, hexHash.diff) || {address: hexHash.oldHash.p, colors: ""};

    fs.writeFileSync(cacheFile, JSON.stringify({
        c: hexHash.hash,
        p: nonzeroBuildAddress.address
    }));   
    
    return {
        imageAddress: nonzeroBuildAddress.address,
        colors: nonzeroBuildAddress.colors,
        perceptualDiff: hexHash.diff
    };
}