var fs = require("fs");
var crypto = require("crypto");
var path = require("path");

var savePng = require("./save-png");
var PngFile = require("./png-file/png-file");
var badPerceptualHash = require("./bad-percep-hash");

var cacheFile = path.join(__dirname, "last-build-pixels.json");
if(!fs.existsSync(cacheFile)) fs.writeFileSync(cacheFile, "\"\"");

var pixels = [];

module.exports = function(buildNumber, directory, ignores) {
    pixels = [];
    
    var hash = getDirectoryHash(directory, ignores).toString("hex");
    var oldHash = require(cacheFile);
    
    
    var hashDelta = hexDiff(hash, oldHash);
    
    addHexPixels(hashDelta);
    
    fs.writeFileSync(cacheFile, '"' + hash + '"');
    
    var matrix = pixelsToMatrix(normalizeBrightness(pixels));
    
    if(matrix.length == 0) matrix = [[[0,0,0]]];
    
    var png = new PngFile(matrix, 128);
    
    return savePng(buildNumber, png.toBuffer());
};

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

function normalizeBrightness(pixels) {
    
    //TODO: better normalization
    return pixels;
    
    var greatestBrightness = 0;
    for(var i = 0; i < pixels.length; i++) {
        var brightness = (pixels[i][0] + pixels[i][1] + pixels[i][2]) / 3;
        greatestBrightness = Math.max(greatestBrightness, brightness);
    }
    var normalizedPixels = [];
    
    //don't normalize up to 255, bc that's white. 
    //128 is a pure hue; do that instead
    for(var i = 0; i < pixels.length; i++) {
        var pixel = pixels[i];
        
        var brightness = (pixel[0] + pixel[1] + pixel[2]) / 3;
        var brightnessFactor = (brightness / greatestBrightness) * (brightness < 128 ? 128 : 256);
        
        var normPix = [pixel[0] * brightnessFactor, pixel[1] * brightnessFactor, pixel[2] * brightnessFactor];
        
        normalizedPixels.push(normPix);
    }
    return normalizedPixels;
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