var PATHFINDING_GRID_SIZE = 100;

module.exports = function(startPointToEndPoints) {
    var sortedTargets = startPointToEndPoints.sort((a,b) => distance(a.points[0], a.points[1]) - distance(b.points[0], b.points[1]));

    var lines = [];

    sortedTargets.forEach(x=>swapCoordsToPathfindNoOverlap(x, lines));

    return sortedTargets;
}

function swapCoordsToPathfindNoOverlap(requiredLineRecord, pastLines) {
    var start = requiredLineRecord.points[0];
    var end = requiredLineRecord.points[1];

    var yDelta = end[1] - start[1];
    var xDelta = end[0] - start[0];
    var slope = yDelta / xDelta;

    var mid = [];

    if(Math.abs(slope) > 1) {
        mid = [end[0], start[1] + Math.sign(slope) * xDelta];
    } else {
        mid = [start[0] + Math.sign(slope) * yDelta, end[1]];
    }

    requiredLineRecord.points = [start, mid, end];
}

function pathfindBetween(coords, pastLines) {

}

function findLineIntersectionPoint(line1, line2) {
    var start = line.points[0];
    var end = line.points[1];

    var yDelta = end[1] - start[1];
    var xDelta = end[0] - start[0];
    var slope = yDelta / xDelta;

    if(xDelta == 0) {
        return pos[0] == start[0] && (pos[1] >= start[1] && pos[1] <= end[1]);
    } else {
        var isSolution = pos[1] - start[1] == slope * (pos[0] - start[0]);
        return isSolution && (pos[0] >= start[0] && pos[0] <= end[0]);
    }
}

function distance(p1, p2) {
    var dx = p1[0] - p2[0];
    var dy = p1[1] - p2[1];

    return Math.sqrt(dx*dx + dy*dy);
}