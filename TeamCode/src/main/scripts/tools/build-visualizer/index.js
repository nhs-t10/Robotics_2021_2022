var fs = require("fs");

var builds = require("./generate-build-dataset")();

var BUILD_ICON_SIZE = 180;
var BUILD_ICON_MARGIN_PERCENT = 3;

builds.forEach(x=>x.timeDate = new Date(x.time));

builds = builds.sort((a,b)=> a.timeDate.getTime() - b.timeDate.getTime());

builds.forEach((x,i)=>x.globalBuildNumber = i);

var modernBuilds = builds.filter(x=>isModernBuild(x));

modernBuilds.forEach((x,i)=>x.globalModernBuildNumber = i);

var marriages = modernBuilds
    .map((x,i,a)=>i>0 && ({parent2: x, parent1: a[i - 1]}))
    .filter(x=>x && x.parent1.cognomen != x.parent2.cognomen);

marriages.forEach(x=>x.time = new Date(Math.min(x.parent1.timeDate.getTime(), x.parent2.timeDate.getTime())));

marriages.forEach(x=>x.lastMarriages = []);

for(var i = 0; i < marriages.length; i++) {
    for(var j = i + 1; j < marriages.length; j++) {
        if(!marriages[i].nextFamily1Marriage &&
            (marriages[j].parent1.cognomen == marriages[i].parent1.cognomen ||
            marriages[j].parent2.cognomen == marriages[i].parent1.cognomen)) {
                marriages[i].nextFamily1Marriage = marriages[j];
                marriages[j].lastMarriages.push(marriages[i]);
            }

        if(!marriages[i].nextFamily2Marriage &&
            (marriages[j].parent1.cognomen == marriages[i].parent2.cognomen ||
            marriages[j].parent2.cognomen == marriages[i].parent2.cognomen)) {
                marriages[i].nextFamily2Marriage = marriages[j];
                marriages[j].lastMarriages.push(marriages[i]);
            }

        if(marriages[i].family1NextMarriage && marriages[i].family2NextMarriage) break;
    }
}

console.log(marriages.length);

var rootMarriages = marriages.filter(x=>x.lastMarriages.length == 0);

rootMarriages.forEach(x=>fillMarriageTree(x, modernBuilds));
rootMarriages.forEach(x=>bringMarriageIntoPeople(x));

console.log(rootMarriages.length);

var marriageSvgs = rootMarriages.map(x=>makePersonAndTheirDescendantsSvg(x.parent1));

var svgContent = marriageSvgs[0];

var maxX = svgContent.width, minX = svgContent.xMin, minY = svgContent.yMin, maxY = svgContent.height;


fs.writeFileSync(__dirname + "/vis.svg", `<svg xmlns="http://www.w3.org/2000/svg" viewBox="${minX} ${minY} ${maxX} ${maxY}">` + svgContent.svg + "</svg>"); //SAFE

function makePersonAndTheirDescendantsSvg(person, x, y) {
    //convert x and y to numbers
    x |= +x;
    y |= +y;

    var width = 0;
    var height = 0;
    var xMin = x;
    var yMin = y;
    
    var svg = "";

    var p1x = x, 
        p2x = x + BUILD_ICON_MARGIN_PERCENT * BUILD_ICON_SIZE;

    xMin = Math.min(p1x, p2x);
    
    svg += makeAtomicBuildItem(person, [p1x, y]);
    if(person.relations.partner) {
        svg += makeAtomicBuildItem(person.relations.partner, [p2x, y]);
        width += BUILD_ICON_MARGIN_PERCENT * BUILD_ICON_SIZE;
    }

    width += BUILD_ICON_MARGIN_PERCENT * BUILD_ICON_SIZE;
    height += BUILD_ICON_MARGIN_PERCENT * BUILD_ICON_SIZE;

    svg += `<path d="M${p1x} ${y}H${p2x}" class="marriage-line"/>`;

    var childrenG = "";

    var nextRowY = y + BUILD_ICON_MARGIN_PERCENT * BUILD_ICON_SIZE;

    var maxChildHeight = 0;

    shuffleArray(person.relations.children);

    for(var i = 0; i < person.relations.children.length; i++) {
        var child = person.relations.children[i];
        var cSvg = makePersonAndTheirDescendantsSvg(child, x + width, nextRowY);

        width += cSvg.width;
        maxChildHeight = Math.max(maxChildHeight, cSvg.height);
        childrenG += cSvg.svg;
    }

    height += maxChildHeight;

    svg += transformSvg(childrenG, -width / 2, 0);
    
    return {
        xMin: xMin - width / 2,
        xMax: xMin + width / 2,
        yMin: y,
        width: width,
        height: height,
        svg: svg
    };
}

function bringMarriageIntoPeople(marriage) {
    initRelations(marriage.parent1);
    initRelations(marriage.parent2);

    marriage.parent1.relations.partner = marriage.parent2;
    marriage.parent2.relations.partner = marriage.parent1;

    marriage.children.forEach(x=>{
        initRelations(x);
        x.relations.parents = [marriage.parent1, marriage.parent2];

        marriage.parent1.relations.children.push(x);
        marriage.parent2.relations.children.push(x);
    });

    marriage.knownToPeople = true;

    if(marriage.nextFamily1Marriage && !marriage.nextFamily1Marriage.knownToPeople) bringMarriageIntoPeople(marriage.nextFamily1Marriage);
    if(marriage.nextFamily2Marriage && !marriage.nextFamily2Marriage.knownToPeople) bringMarriageIntoPeople(marriage.nextFamily2Marriage);
}

function initRelations(build) {
    if(!build.relations) build.relations = {};
    if(!build.relations.children) build.relations.children = [];
}

function fillMarriageTree(marriage, builds) {
    marriage.children = [];
    
    for(var i = marriage.parent1.globalModernBuildNumber + 1; i < builds.length; i++) {
        if(marriage.nextFamily1Marriage &&
            builds[i].timeDate.getTime() > marriage.nextFamily1Marriage.time.getTime()) break;

        if(builds[i].cognomen == marriage.parent1.cognomen) marriage.children.push(builds[i]);
    }

    for(var i = marriage.parent2.globalModernBuildNumber + 1; i < builds.length; i++) {
        if(marriage.nextFamily2Marriage &&
            builds[i].timeDate.getTime() > marriage.nextFamily2Marriage.time.getTime()) break;

        if(builds[i].cognomen == marriage.parent2.cognomen) marriage.children.push(builds[i]);
    }

    if(marriage.nextFamily2Marriage && !marriage.nextFamily2Marriage.children) {
        fillMarriageTree(marriage.nextFamily2Marriage, builds);
    }
    if(marriage.nextFamily1Marriage && !marriage.nextFamily1Marriage.children) {
        fillMarriageTree(marriage.nextFamily1Marriage, builds);
    }
}

function makeAtomicBuildItem(build, pos) {
    var x = pos[0];
    var y = pos[1];

    if(!build) build = {cognomen: "", name: "UNKNOWN"};

    var background = `<ellipse fill="#${getColor(build.cognomen)}" rx="${BUILD_ICON_SIZE / 2}" ry="${BUILD_ICON_SIZE  / 2}" cx="${x}" cy="${y}"/>`;

    var text = `<text style="font-family:'JetBrains Mono';font-size:24px" x="${x}" y="${y + BUILD_ICON_SIZE * 0.8}" text-anchor="middle">${build.name}</text>`

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

function isModernBuild(build) {
    var keys = Object.keys(build);

    return keys.includes("colors") && keys.includes("perceptualHash");
}

function shuffleArray(a) {
    for (var i = a.length - 1; i > 0; i--) {
        var swTo = Math.floor(Math.random() * (i + 1));
        var tmp = a[i];
        a[i] = a[swTo];
        a[swTo] = tmp;
    }
    return a;
}

function transformSvg(svg, x, y) {
    return `<g transform="translate(${x} ${y})">${svg}</g>`
}