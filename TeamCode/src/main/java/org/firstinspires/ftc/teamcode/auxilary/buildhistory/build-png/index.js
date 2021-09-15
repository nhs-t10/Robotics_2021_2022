var fs = require("fs");
var crypto = require("crypto");
var path = require("path");

var savePng = require("./save-png");
var PngFile = require("./png-file/png-file");
var badPerceptualHash = require("./bad-percep-hash");

var ENTER_FILE = [0xff, 0x00, 0xff];
var ENTER_DIRECTORY = [0x00, 0xff, 0x00];
var LEAVE_DIRECTORY = [0xff, 0x00, 0x00];
var START_HASH = [0x00, 0x00, 0x00];
var START_ITEM = [0x00, 0x00, 0xff];
var START_DELTA = [0xff, 0xff, 0xff];

if(!fs.existsSync(__dirname + "/last-build-pixels.json")) fs.writeFileSync(__dirname + "/last-build-pixels.json", "[]");

var pixels = [];

module.exports = function(buildNumber, directory, ignores) {
    pixels = [];
    addDirectoryToImage(directory, ignores);
    
    var deltaEncodedPixels = pixelsDiff(pixels, require("./last-build-pixels.json"));
    
    fs.writeFileSync(__dirname + "/last-build-pixels.json", JSON.stringify(pixels));
    
    var matrix = pixelsToMatrix(deltaEncodedPixels);
    
    if(matrix.length == 0) matrix = [[[0,0,0]]];
    
    var png = new PngFile(matrix, matrix[0].length * 4);
    
    return savePng(buildNumber, png.toBuffer());
};

function pixelsDiff(a, b) {
    var delta = [];
    var itemsSinceDifference = 0;
    for(var i = 0; i < a.length; i++) {
        var difference = pixelDiff(a[i], b[i]);
        var isDifferent = difference[0] || difference[1] || difference[2];
        
        if(isDifferent) {
            delta.push([0xff, itemsSinceDifference, 0xf0]);
            delta = delta.concat(getHexPixels(itemsSinceDifference.toString(15)) + "ff");
            delta.push(difference);
            
            itemsSinceDifference = 0;
        }
        itemsSinceDifference++;
    }
    return delta;
}

function pixelDiff(a, b) {
    if(!a) a = [0,0,0];
    if(!b) b = [0,0,0];
    
    return [
        a[0] - b[0],
        a[1] - b[1],
        a[2] - b[2]
    ];
}

function pixelsToMatrix(pixels) {
    var width = Math.ceil(Math.sqrt(pixels.length));
    
    var rows = [];
    for(var i = 0; i < pixels.length; i += width) {
        rows.push(pixels.slice(i, i + width));
    }
    return rows;
}

function addDirectoryToImage(directory, ignores) {
    ignores = ignores || [];
    
    directory = directory + "";
    if(!fs.existsSync(directory)) return "tree -1\u0000";
    
    var dir = fs.readdirSync(directory).sort();
    
    pixels.push(ENTER_DIRECTORY);
    
    for(var i = 0; i < dir.length; i++) {
        if(ignores.includes(dir[i])) continue;
        
        pixels.push(START_ITEM);
        
        addStrAsPixels(dir[i].substring(0, 30));
        
        pixels.push(START_HASH);
        
        addFileToImage(path.join(directory, dir[i]), ignores);
    }
    
    pixels.push(LEAVE_DIRECTORY);
}

function addFileToImage(file, ignores) {
    file = file + "";
    if(!fs.existsSync(file)) return "blob -1\u0000";
    if(fs.statSync(file).isDirectory()) return addDirectoryToImage(file, ignores);
    
    var fileContent = fs.readFileSync(file);
    var hash = badPerceptualHash(fileContent).toString("hex")
    
    pixels.push(ENTER_FILE);
    addHexPixels(fileContent.length.toString(16).substring(0,6));
    
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