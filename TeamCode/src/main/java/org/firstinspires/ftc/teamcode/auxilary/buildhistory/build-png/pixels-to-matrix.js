
var pixelsToStripes = geometryGrapher((x,y,pixels) => x + y);
var pixelsToStripesDown = geometryGrapher((x,y,pixels) => y - x);
var pixelsToRadialSymmetry = geometryGrapher((x,y,pixels) => y * x);
var pixelsToExponential = geometryGrapher((x,y,pixels) => Math.pow(y, x));
var pixelsToBitwisey = geometryGrapher((x,y,pixels) => y | x);
var pixelsToHorizontal = geometryGrapher((x,y,pixels) => x);
var pixelsToVertical = geometryGrapher((x,y,pixels) => y);
var pixelsToCircularIThink = geometryGrapher((x,y,pixels) => Math.sqrt(x*x + y*y));

var strategies = [
    pixelsToMatrix,
    pixelsToStripes,
    pixelsToRadialSymmetry,
    pixelsToExponential,
    pixelsToStripesDown,
    pixelsToBitwisey,
    pixelsToHorizontal,
    pixelsToVertical,
    pixelsToCircularIThink
]

module.exports = randomStrategy;

function randomStrategy(pixels) {
    var matrix = selectRandomStrategy(pixels)(pixels);
    
    //santiy check to make sure it's always a matrix, even when there's no delta
    if(!matrix) return [];
    else return matrix;
}

function selectRandomStrategy() {
    return strategies[Math.floor(Math.random() * strategies.length)];
}

function geometryGrapher(geoCb) {
    return function (pixels) {        
        var width = pixels.length;
        var height = pixels.length;
        
        var result = [];
        
        for(var i = 0; i < height; i++) {
            var row = [];
            for(var j = 0; j < width; j++) {
                var pixIndex = geoCb(j, i, pixels);
                pixIndex = Math.floor(Math.abs(pixIndex) % pixels.length);
                
                row.push(pixels[pixIndex] || pixels[0]);
            }
            result.push(row);
        }
        return result;
    }
}

function pixelsToMatrix(pixels) {
    var width = Math.ceil(Math.sqrt(pixels.length));
    
    var rows = [];
    for(var i = 0; i < pixels.length; i += width) {
        var row = pixels.slice(i, i + width);
        
        //fill shorter rows so the end isn't blank every time
        for(var j = i; row.length < width; j++) {
            row.push(pixels[j % pixels.length]);
        }
        
        rows.push(row);
    }
    return rows;
}
