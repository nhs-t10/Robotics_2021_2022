var strategies = [
    pixelsToMatrix,
    geometryGrapher((x,y,pixels) => x + y),
    geometryGrapher((x,y,pixels) => y - x),
    geometryGrapher((x,y,pixels) => Math.max(y * x, y, x)),
    geometryGrapher((x,y,pixels) => x),
    geometryGrapher((x,y,pixels) => y),
    geometryGrapher((x,y,pixels) => Math.sqrt(x*x + y*y)),
    geometryGrapher((x,y,pixels) => pixels.length * (Math.sin(x) + Math.sin(y))),
    geometryGrapher((x,y,pixels) => (Math.atan2(x - pixels.length/2, y - pixels.length/2) / (Math.PI)) * pixels.length ),
];

var largeStrategies = [
    geometryGrapher((x,y,pixels) => y | x),
    geometryGrapher((x,y,pixels) => y & x),
    geometryGrapher((x,y,pixels) => y ^ x),
    geometryGrapher((x,y,pixels) => y / x),
    geometryGrapher((x,y,pixels) => x / y),
    geometryGrapher((x,y,pixels) => y % x),
    geometryGrapher((x,y,pixels) => x % y),
    geometryGrapher((x,y,pixels) => (Math.atan2(x + pixels.length/2, y + pixels.length/2) / (Math.PI)) * pixels.length ),
]

module.exports = randomStrategy;

function randomStrategy(pixels, strategySeed) {
    
    var matrix = selectRandomStrategy(strategySeed, pixels.length)(pixels);
    
    //santiy check to make sure it's always a matrix, even when there's no delta
    if(!matrix) return [];
    else return matrix;
}

function selectRandomStrategy(seed, pixels) {
    var allowedStrategies = largeStrategies;
    
    if(pixels.length <= 25) allowedStrategies = strategies.concat(largeStrategies);

    if(seed === undefined) seed = Math.random() * allowedStrategies.length;
    var seedIndex = Math.floor(seed % allowedStrategies.length)

    return allowedStrategies[seedIndex];
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
