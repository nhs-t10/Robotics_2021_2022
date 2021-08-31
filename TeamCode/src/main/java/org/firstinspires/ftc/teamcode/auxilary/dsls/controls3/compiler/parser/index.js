var END_NODE_NAME = "output";

var NORTH = "north";
var SOUTH = "south";
var WEST = "west";
var EAST = "east";
var NORTH_WEST = "north west";
var SOUTH_EAST = "south east";
var SOUTH_WEST = "south west";
var NORTH_EAST = "north east";

var nodeParser = require("./single-node.js");

module.exports = function(source) {
    source = source.replace(/\r\n/g, "\n").replace(/\t/g, "    ");
    
    var grid = makeCharGrid(source);
    var outs = findAll2d(grid);
    
    var outBoundingBoxes = outs.map(x=>findBoundingBox(grid, x));
    
    var paths = outBoundingBoxes.map(x=>goBackUpGraph(grid, x, null));
    
    return paths;
}


function goBackUpGraph(grid, box, parentNode) {
    var entryPoints = findEntryPositionsDirections(grid, box);
    
    var endPoints = entryPoints.map(x=>retracePath(grid, x));
    
    var endBoxes = endPoints.map(x=>findBoundingBox(grid, x.pos));
    
    var node = {};
    var endNodes = endBoxes.map((x,i)=>({
        width: endPoints[i].width,
        direction: relativeDirection(entryPoints[i].direction),
        node: goBackUpGraph(grid, x, node)
    }));
    
    node.in = endNodes;
    node.source = getTextBox(grid, box).trim();

    if(node.source.startsWith("(")) node.source = "math" + node.source;
    
    if(parentNode && node.source.startsWith("outTo(")) throwErrorAt([box.startCol, box.row], "Attempt to use outTo as a non-root node.");
    
    node.ast = nodeParser.parse(node.source);
    
    return node;
}

function findEntryPositionsDirections(grid, box) {
    var entries = [];
    
    //verticals. Needs a null check to prevent "property of undefined" error
    if(box.row > 0) {
        var top = grid[box.row - 1].slice(box.startCol, box.endCol + 1);
        for(var i = 0; i < top.length; i++) {
            if(top[i] == "v") entries.push({
                point: [box.startCol + i, box.row - 1],
                direction: SOUTH
            });
        }
    }
    
    if(grid[box.row][box.startCol - 2] == ">") {
        entries.push({
            point: [box.startCol - 2, box.row],
            direction: EAST
        });
    }
    if(grid[box.row][box.startCol - 1] == ">") {
        entries.push({
            point: [box.startCol - 1, box.row],
            direction: EAST
        });
    }
    
    if(box.row + 1 < grid.length) {
        var bottom = grid[box.row + 1].slice(box.startCol, box.endCol + 1);
        for(var i = 0; i < bottom.length; i++) {
            if(bottom[i] == "^") entries.push({
                point: [box.startCol + i, box.row + 1],
                direction: NORTH
            });
        }
    }
    
    //horizontals! no null check needed :)) thanks javascript
    // we *do* need more checks (to expand the hitbox) though.
    if(grid[box.row][box.endCol + 2] == "<") {
        entries.push({
            point: [box.endCol + 2, box.row],
            direction: WEST
        });
    }
    if(grid[box.row][box.endCol + 1] == "<") {
        entries.push({
            point: [box.endCol + 1, box.row],
            direction: WEST
        });
    }
    
    return entries;
}


function retracePath(grid, heading) {
    /**@type {string} */
    var direction = heading.direction;
    
    var col = heading.point[0];
    var row = heading.point[1];
    
    var char = grid[row][col];
    
    var trackChars = ["|", "-", ">", "<", "v", "^", "+", " ", "/", "\\", "=", "!"];
    
    var narrowTrackChars = ["|", "-"];
    var wideTrackChars = ["=", "!"];
    
    var width = 1;
    while(trackChars.includes(char)) {
        if(direction.includes("north")) row++;
        if(direction.includes("south")) row--;
        if(direction.includes("west")) col++;
        if(direction.includes("east")) col--;
        
        if(wideTrackChars.includes(char)) width = 2;
        else if(narrowTrackChars.includes(char)) width = 1;
        
        if(col < 0 || col >= grid[0].length || row < 0 || row >= grid.length) throwErrorAt([col, row], "Track goes off the edge");
        
        char = grid[row][col];
    }
    
    return {
        pos: [col, row],
        width: width
    }
}

function findBoundingBox(grid, point) {    
    var row = grid[point[1]];
    var char = row[point[0]];
    
    var depth = 0, quote = false, escaped = false;
    if(isUnbalanceableChar(char)) {
        var startCol = point[0];
        //back off so it's squarely on the function
        if(char == "(") startCol--;
        
        //find the *first* letter of all
        while(isUnbalanceableChar(row[startCol])) startCol--;
        startCol++;
        
        var endCol = -1;
        
        for(var i = startCol; i < row.length; i++) {
            if(escaped) escaped = false;
            else if(quote && row[i] == "\\") escaped = true;
            else if(row[i] == ")") depth--;
            else if(row[i] == "(") depth++;
            else if(row[i] == "\"") quote = !quote;
            
            if(depth == 0 && !isUnbalanceableChar(row[i])) {
                endCol = i;
                break;
            }
            if(i + 1 == row.length) {
                endCol = i;
            }
        }
        if(endCol < 0) throwErrorAt(point, "Unbalanced parentheses tracing forwards");
        return {
            startCol: startCol,
            endCol: endCol,
            row: point[1]
        };
    } else if(char == ")") {
        var endCol = point[0];
        var startCol = -1;
        
        for(var i = endCol; i >= 0; i--) {
            if(escaped) escaped = false;
            else if(quote && row[i] == "\\") escaped = true;
            else if(row[i] == ")") depth--;
            else if(row[i] == "(") depth++;
            else if(row[i] == "\"") quote = !quote;
            
            if(depth == 0 && !isUnbalanceableChar(row[i])) {
                startCol = i;
                break;
            }
            if(i == 0) {
                startCol = 0;
            }
        }
        
        if(startCol < 0) throwErrorAt(point, "Unbalanced parentheses tracing backwards");
        return {
            startCol: startCol,
            endCol: endCol,
            row: point[1]
        };
    } else {
        throwErrorAt(point, "Weirdly pointed end. Make sure that verticals are on the end!");
    }
}

function isUnbalanceableChar(char) {
    return isRegexyLetter(char) || char == "(" || char == ".";
}

function makeCharGrid(str) {
    var unrectangled = str.split("\n");
    var maxWidth = 0;
    for(var i = 0; i < unrectangled.length; i++) {
        maxWidth = Math.max(maxWidth, unrectangled[i].length);
    }
    return unrectangled.map(x=>pad(x, maxWidth, " ")).map(x=>x.split(""));;
}

function pad(s, w, c) {
    c = c || " ";
    while(s.length < w) s += c;
    return s;
}

function findAll2d(grid) {
    var outs = [];
    for(var i = 0; i < grid.length; i++) {
        var row = grid[i];
        for(var j = 0; j < row.length; j++) {
            if(row.slice(j, j + END_NODE_NAME.length + 1).join("") == END_NODE_NAME + "(") outs.push([j, i]);
        }
    }
    return outs;
}

function getTextBox(grid, box) {
    var row = grid[box.row];
    return row.slice(box.startCol, box.endCol + 1).join("");
}

function isRegexyLetter(char) {
    return isLetter(char) || isDigit(char);
}

function isLetter(char) {
    if(!char) return false;
    
    var code = char.charCodeAt(0);
    return (code >= 65 && code <= 90) || (code >= 97 && code <= 122);
}

function isDigit(char) {
    if(!char) return false;
    
    var code = char.charCodeAt(0);
    return code >= 48 && code <= 57;
}

function throwErrorAt(point, text) {
    throw text + " " + point[1] + ":" + point[0];
}
function relativeDirection(direction) {
    if(direction == EAST) return "left";
    if(direction == WEST) return "right";
    if(direction == NORTH) return "bottom";
    if(direction == SOUTH) return "top"
}