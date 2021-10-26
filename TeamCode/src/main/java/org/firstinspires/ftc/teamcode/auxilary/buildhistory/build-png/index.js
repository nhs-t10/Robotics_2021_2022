var fs = require("fs");
var crypto = require("crypto");
var path = require("path");

var savePng = require("./save-png");
var PngFile = require("./png-file/png-file");
var badPerceptualHash = require("./bad-percep-hash");

var cacheFile = path.join(__dirname, "last-build-pixels.json");
if(!fs.existsSync(cacheFile)) fs.writeFileSync(cacheFile, JSON.stringify({c:"",p:"buildimgs/0.png"}));


var pixels = [];

module.exports = function(buildNumber, directory, ignores) {
    pixels = [];
    
    var hash = getDirectoryHash(directory, ignores).toString("hex");
    var oldHash = require(cacheFile);
    //translate between old cache and new cache
    if(typeof oldHash === "string") {
        oldHash = {
            c: oldHash,
            p: "buildimgs/0.png"
        };
    }
    
    
    var hashDelta = normalizeHex(hexDiff(hash, oldHash.c));
    
    addHexPixels(hashDelta);

    
    var matrix = pixelsToMatrix(pixels);
    
    if(matrix.length == 0) return oldHash.p;
    
    var png = new PngFile(matrix, 128);
    
    var nonzeroBuild = savePng(buildNumber, png.toBuffer());
    
    fs.writeFileSync(cacheFile, JSON.stringify({
        c: hash,
        p: nonzeroBuild
    }));
    
    return nonzeroBuild;
};

function normalizeHex(hex) {
    var digits = hex.split("").map(x=>+('0x'+x));
    
    //make sure there's an even amount of digits so it'll evenly fit into colours
    if(digits.length % 2) digits.push(0);
    
    //find the maximum colour amount
    var max = 0;
    for(var i = 0; i < digits.length - 1; i++) {
        var digitlen = digits[i]*16 + digits[i + 1];
        max = Math.max(max, digitlen);
    }
    
    var result = "";
    
    for(var i = 0; i < digits.length - 1; i++) {
        var digitlen = digits[i]*16 + digits[i + 1];
        result += Math.round((digitlen / max) * 255).toString(16);
    }
    
    return result;
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

function pixelsToMatrix(pixels) {
    var width = Math.ceil(Math.sqrt(pixels.length));
    
    var rows = [];
    for(var i = 0; i < pixels.length; i += width) {
        rows.push(pixels.slice(i, i + width));
    }
    return rows;
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

function addHexPixels(str) {
    pixels = pixels.concat(getHexPixels(str));
}

function getHexPixels(str) {
    var pixels = [];
    
    str = str + "";
    for(var i = 0; i < str.length; i+=6) {
        pixels.push([
            parseInt(str.substring(i + 0, i + 2), 16) || 0,
            parseInt(str.substring(i + 2, i + 4), 16) || 0,
            parseInt(str.substring(i + 4, i + 6), 16) || 0
        ]);
    }
    return pixels;
}

function addStrAsPixels(str) {
    str = str + "";
    for(var i = 0; i < str.length; i+=3) {
        pixels.push([
            (str[i + 0]||"").charCodeAt(0) || 0,
            (str[i + 1]||"").charCodeAt(0) || 0,
            (str[i + 2]||"").charCodeAt(0) || 0
        ]);
    }
}