var fs = require("fs");

var builds = require("./generate-build-dataset")();

var BUILD_ICON_SIZE = 180;
var BUILD_ICON_MARGIN_PERCENT = 3;


//TODO: make this actually work
process.exit(0);

builds.forEach(x=>x.timeDate = new Date(x.time));

builds = builds.sort((a,b)=> a.timeDate.getTime() - b.timeDate.getTime());

builds.forEach((x,i)=>x.globalBuildNumber = i);

var modernBuilds = builds.filter(x=>isModernBuild(x));

modernBuilds.forEach((x,i)=>x.globalModernBuildNumber = i);

addChildrenAndPartners(modernBuilds);

var greatGrandparents = modernBuilds.filter(x=> !x.relations.parents);

var marriageSvgs = greatGrandparents.map(x=>makePersonAndTheirDescendantsSvg(x));

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
}

function initRelations(build) {
    if(!build.relations) build.relations = {};
    if(!build.relations.children) build.relations.children = [];
}

function fillMarriageChildren(marriages, builds) {
    
    for(var j = 0; j < marriages.length; j++) {
        var marriage = marriages[j];
        
        marriage.children = [];
        var bIndex = builds.indexOf(marriage.parent1);
        if(bIndex == -1) throw "parent not in list";
        
        for(var i = bIndex + 1; i < builds.length; i++) {
            if(isChildOf(builds[i], marriage)) {
                marriage.children.push(builds[i]);
                if(builds[i].isMarried) break;
            }
        }
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
function fillLastMarriages(marriages) {
    marriages.forEach(x=>x.lastMarriages = []);
    
    for(var i = 0; i < marriages.length; i++) {
        var marriage = marriages[i];
        
        for(var j = i + 1; j < marriages.length; j++) {
            if(!marriage.nextFamily1Marriage &&
                (marriages[j].parent1.cognomen == marriage.parent1.cognomen ||
                marriages[j].parent2.cognomen == marriage.parent1.cognomen)) {
                    marriage.nextFamily1Marriage = marriages[j];
                    marriages[j].lastMarriages.push(marriage);
                }

            if(!marriage.nextFamily2Marriage &&
                (marriages[j].parent1.cognomen == marriage.parent2.cognomen ||
                marriages[j].parent2.cognomen == marriage.parent2.cognomen)) {
                    marriage.nextFamily2Marriage = marriages[j];
                    marriages[j].lastMarriages.push(marriage);
                }

            if(marriage.family1NextMarriage && marriage.family2NextMarriage) break;
        }
    }
}
function noteMarriedStatus(marriages) {
    marriages.forEach(x=>{
        x.parent1.isMarried = x.parent2.cognomen;
        x.parent2.isMarried = x.parent1.cognomen;
    })
}

function isChildOf(build, marriage) {
    if(marriage.parent1 == build || marriage.parent2 == build) return false;
    
    return build.cognomen == marriage.parent1.cognomen || build.cognomen == marriage.parent2.cognomen;
}
function isMarried(builds, index) {
    return builds[index].cognomen != builds[index + 1].cognomen;
}

function addChildrenAndPartners(builds) {
    for(var i = 0; i < builds.length; i++) {
        var build = builds[i];
        initRelations(build);
        for(var j = i + 1; j < builds.length; j++) {
            
        }
    }
}