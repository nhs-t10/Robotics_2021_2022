var fs = require("fs");
var builds = require("./allBuilds.json");

builds = builds.sort((a,b)=> new Date(a.time).getTime() - new Date(b.time).getTime());

var commits = Array.from(new Set(builds.map(x=>x.commit)));

var commitBuilds = Object.fromEntries(commits.map(x=>[x, builds.filter(y=>y.commit == x).map(z=>z.buildHash)]));

var commitTimes = Object.fromEntries(commits.map(x=>[x, new Date(builds.filter(y=>y.commit == x).pop().time).getTime() ]));

var svg = "";

var THING_SIZE = 180;
var MARGIN_PERCENT = 3;

var maxX = 0, minX = 0, minY = 0, maxY = 0;

for(var i = 0; i < commits.length; i++) {
    var width = commitBuilds[commits[i]].length - 1;

    if(width == 0) continue;

    var y = i;
    svg += `<path stroke-width="3" stroke="#333" d="M${-(width * THING_SIZE * MARGIN_PERCENT - THING_SIZE * MARGIN_PERCENT)/2} ${y * THING_SIZE * MARGIN_PERCENT} h${width * THING_SIZE * MARGIN_PERCENT}"/>`;
}

for(var i = 0; i < builds.length; i++) {
    if(i + 1 == builds.length || i == 0) continue;

    var build = builds[i];

    var prevCommit = commits[commits.indexOf(build.commit) - 1];
    if(!prevCommit) continue;

    var possibleParents = builds.filter(x=>x.commit == prevCommit && x.cognomen == build.cognomen);
    var parent = randomFrom(possibleParents);

    if(!parent || parent.isParent) continue;

    parent.isParent = true;

    svg += `<path stroke-width="3" stroke="#333" fill="none" d="${curveBetween(getBuildPoint(build), getBuildPoint(parent))}" />`;
}

for(var i = 0; i < builds.length; i++) {
    var build = builds[i];
    

    svg += makeItem(build, getBuildPoint(build));
}

function getBuildPoint(build) {
    var rowWidth = commitBuilds[build.commit].length;
    var buildX = (rowWidth / 2) - commitBuilds[build.commit].indexOf(build.buildHash);
    var rowY = commits.indexOf(build.commit);

    var x = buildX  * THING_SIZE * MARGIN_PERCENT;
    var y = rowY * THING_SIZE * MARGIN_PERCENT;

    maxY = Math.max(maxY, y);
    minY = Math.min(minY, y);
    maxX = Math.max(maxX, y);
    minX = Math.min(minX, y);

    var pointPos = [x, y];

    return pointPos;
}

function makeItem(build, pos) {
    var x = pos[0];
    var y = pos[1];

    var background = `<ellipse fill="#${getColor(build.cognomen)}" rx="${THING_SIZE / 2}" ry="${THING_SIZE  / 2}" cx="${x}" cy="${y}"/>`;

    var text = `<text style="font-family:'JetBrains Mono';font-size:24px" x="${x}" y="${y + THING_SIZE * 0.8}" text-anchor="middle">${build.name}</text>`

    return background + text;
}

function getColor(str) {
    return Buffer.from(str).toString("hex").substring(0, 6);
}

function curveBetween(pos1, pos2) {
    var midX1 = [pos1[0], pos1[1] + (pos2[1] - pos1[1])];
    var midX2 = [pos2[0], pos1[1] + (pos2[1] - pos1[1])];

    return `M${pos1[0]} ${pos1[1]} C${midX1[0]} ${midX1[1]} ${midX2[0]} ${midX2[1]} ${pos2[0]} ${pos2[1]}`;
}

function randomFrom(arr) {
    return arr[Math.floor(Math.random() * arr.length)];
}

fs.writeFileSync(__dirname + "/vis.svg", `<svg xmlns="http://www.w3.org/2000/svg" viewBox="${minX} ${minY} ${maxX} ${maxY}">` + svg + "</svg>");