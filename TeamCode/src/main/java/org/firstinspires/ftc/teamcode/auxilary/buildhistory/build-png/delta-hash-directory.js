var fs = require("fs");
var path = require("path");

var badPerceptualHash = require("./bad-percep-hash");

var cacheFile = path.join(__dirname, "last-build-pixels.json");
if(!fs.existsSync(cacheFile)) fs.writeFileSync(cacheFile, JSON.stringify({c:"",p:"buildimgs/0.png"}));


module.exports = function(directory, ignores) {
    var hash = getDirectoryHash(directory, ignores).toString("hex");
    var oldHash = require(cacheFile);
    //translate between old cache and new cache
    if(typeof oldHash === "string") {
        oldHash = {
            c: oldHash,
            p: "buildimgs/0.png"
        };
    }
    
    return {
        hash: hash,
        diff: hexDiff(hash, oldHash.c),
        oldHash: oldHash
    }
}

function getDirectoryHash(directory, ignores) {
    ignores = ignores || [];
    
    directory = directory + "";
    if(!fs.existsSync(directory)) return null;
    
    var dir = fs.readdirSync(directory).sort();
    
    var hashes = [];
    
    for(var i = 0; i < dir.length; i++) {
        if(ignores.includes(dir[i])) continue;
        
        hashes.push(getFileHash(path.join(directory, dir[i]), ignores));
    }
    
    return badPerceptualHash.combineHashes(hashes);
}

function getFileHash(file, ignores) {
    file = file + "";
    if(!fs.existsSync(file)) return null;
    if(fs.statSync(file).isDirectory()) return getDirectoryHash(file, ignores);
    
    var fileContent = fs.readFileSync(file);
    var hash = badPerceptualHash.hash(fileContent);
    
    return hash;
}


function hexDiff(a, b) {
    var res = "";
    
    for(var i = 0; i < a.length; i++) {
        var counterpoint = b[i] || 0;
        var counterNumber = +('0x' + counterpoint);
        
        var digit = +('0x' + a[i]);
        
        var delta = digit - counterNumber;
        
        if(delta != 0) res += Math.abs(delta).toString(16);
    }
    return res;
}