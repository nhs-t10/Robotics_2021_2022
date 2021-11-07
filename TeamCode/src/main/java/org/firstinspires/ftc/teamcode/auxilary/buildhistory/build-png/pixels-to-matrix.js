
var strategies = [
    pixelsToMatrix,
    pixelsToStripes,
    pixelsToRadialSymmetry,
    pixelsToNotSureWhatThisDoesButItsProbablyGood
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

function pixelsToStripes(pixels) {
    var direction = Math.random() > 0.5 ? 1 : -1;
    
    var width = pixels.length;
    var height = pixels.length;
    
    var result = [];
    
    for(var i = 0; i < height; i++) {
        var row = [];
        for(var j = 0; j < width; j++) {
            row.push(pixels[(i + direction*j) % pixels.length]);
        }
        result.push(row);
    }
    return result;
}

function pixelsToRadialSymmetry(pixels) {
    var width = pixels.length;
    var height = pixels.length;
    
    var result = [];
    
    for(var i = 0; i < height; i++) {
        var row = [];
        for(var j = 0; j < width; j++) {
            row.push(pixels[((i+1) * (j+1)) % pixels.length]);
        }
        result.push(row);
    }
    return result;
}

function pixelsToNotSureWhatThisDoesButItsProbablyGood(pixels) {
    var width = pixels.length;
    var height = pixels.length;
    
    var result = [];
    
    for(var i = 0; i < height; i++) {
        var row = [];
        for(var j = 0; j < width; j++) {
            row.push(pixels[Math.pow(i, j) % pixels.length]);
        }
        result.push(row);
    }
    return result;
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
