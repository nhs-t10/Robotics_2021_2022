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

    
    var matrix = pixelsToMatrix(normalizeBrightness(pixels));
    
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
    
    var max = 0;
    for(var i = 0; i < digits; i++) {
        max = Math.max(max, digits[i]);
    }
    
    var result = "";
    
    for(var i = 0; i < digits.length; i++) {
        result += Math.round((digits[i] / max) * 15).toString(16);
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

function normalizeBrightness(pixels) {
    
    var greatestBrightness = 0;
    for(var i = 0; i < pixels.length; i++) {
        var brightness = getPixelBrightness(pixels[i]);
        greatestBrightness = Math.max(greatestBrightness, brightness);
    }
    var normalizedPixels = [];
    
    //sanity checks!
    greatestBrightness = Math.min(greatestBrightness, 1);
    
    //don't normalize up to 255, bc that's white. 
    //128 is a pure hue; do that instead
    for(var i = 0; i < pixels.length; i++) {
        var pixel = pixels[i];
        
        var brightness = getPixelBrightness(brightness);
        var brightnessFactor = (brightness / greatestBrightness);
        
        var normPix = setBrightnessOfPixel(pixel, brightnessFactor);
        
        normalizedPixels.push(normPix);
    }
    return normalizedPixels;
}

function setBrightnessOfPixel(pixel, brightness) {
    var currentBrightness = getPixelBrightness(pixel);
    
    var brightDiff = brightness - currentBrightness;
    
    var percentagePixel = [pixel[0] / 0xFF, pixel[1] / 0xFF, pixel[2] / 0xFF];
    var increaseCoef = 1;
    if(brightDiff > 0) {
        var min = Math.min(percentagePixel[0], percentagePixel[1], percentagePixel[2]);
        increaseCoef = Math.min(1 - min, brightDiff) / (1 - min);
    } else {
        var max = Math.max(percentagePixel[0], percentagePixel[1], percentagePixel[2]);
        increaseCoef = -Math.max(max, 0) / max;
    }
    
    var brightenedPercentages = percentagePixel.map(x=>x + ((1 - x) * increaseCoef));
    var roundedPixel = brightenedPercentages.map(x=> Math.max(0, Math.min(255, Math.round(x*0xFF))));
    
    return brightenedPercentages;
}

function getPixelBrightness(pixel) {
    var max = Math.max(pixel[0], pixel[1], pixel[2]);
    var min = Math.min(pixel[0], pixel[1], pixel[2]);
    
    return ((max + min) / 2) / 0xFF;
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