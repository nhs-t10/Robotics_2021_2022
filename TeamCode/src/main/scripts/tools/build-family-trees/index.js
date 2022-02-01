var fs = require("fs");
const familyColors = require("./family-colors");

var builds = require("./generate-build-dataset")();

var BUILD_ICON_SIZE = 180;
var BUILD_ICON_MARGIN_PERCENT = 3;


builds.forEach(x=>x.timeDate = new Date(x.time));

builds = builds.sort((a,b)=> a.timeDate.getTime() - b.timeDate.getTime());

builds.forEach((x,i)=>x.globalBuildNumber = i);

var modernBuilds = builds.filter(x=>isModernBuild(x));

modernBuilds.forEach((x,i)=>x.globalModernBuildNumber = i);

addChildrenAndPartners(modernBuilds);

var greatGrandparents = modernBuilds.filter(x=> !x.relations.hasParent && x.relations.children.length);

greatGrandparents = [greatGrandparents[0]];

var marriageSvgs = greatGrandparents.map(x=>makePersonAndTheirDescendantsSvg(x));

console.log(marriageSvgs.length);

var svgContent = marriageSvgs[0];

var maxX = svgContent.width, minX = svgContent.xMin, minY = svgContent.yMin, maxY = svgContent.height;


fs.writeFileSync(__dirname + "/vis.svg", `<svg xmlns="http://www.w3.org/2000/svg" viewBox="${minX} ${minY} ${maxX} ${maxY}">` + svgContent.svg + "</svg>"); //SAFE

function makePersonAndTheirDescendantsSvg(person, x, y, dir) {
    //convert x and y to numbers
    x |= +x;
    y |= +y;

    //make dir 1 or -1
    dir = +(dir > 0) * 2 - 1;

    var width = 0;
    var height = 0;
    var xMin = x;
    var xMax = x;
    var yMin = y;
    
    var svg = "";

    var marginCt = BUILD_ICON_MARGIN_PERCENT * BUILD_ICON_SIZE;

    var p1x = x, 
        p2x = x + marginCt * dir;

    xMin = Math.min(p1x, p2x);
    xMax = Math.max(p1x, p2x);
    
    svg += makeAtomicBuildItem(person, [p1x, y]);
    if(person.relations.partner) {
        svg += makeAtomicBuildItem(person.relations.partner, [p2x, y]);
        width += marginCt;
    }

    width += marginCt;
    height += marginCt;

    svg += `<path d="M${p1x} ${y}H${p2x}" class="marriage-line"/>`;

    var childrenG = "";

    var nextRowY = y + marginCt;

    var maxChildHeight = 0;

    shuffleArray(person.relations.children);

    var childOffsetLeft = 0, childOffsetRight = marginCt;

    for(var i = 0; i < person.relations.children.length; i++) {
        var child = person.relations.children[i];
        var cSvg;
        if(i % 2) {
            cSvg = makePersonAndTheirDescendantsSvg(child, x + childOffsetRight, nextRowY, 1);
            childOffsetRight += cSvg.r1width;
        } else {
            cSvg = makePersonAndTheirDescendantsSvg(child, x - childOffsetLeft, nextRowY, -1);
            childOffsetLeft += cSvg.r1width;
        }

        xMin = Math.min(xMin, cSvg.xMin);
        xMax = Math.max(xMax, cSvg.xMax);

        maxChildHeight = Math.max(maxChildHeight, cSvg.heightWithoutMargin);
        childrenG += cSvg.svg;

    }

    height += maxChildHeight;

    svg += childrenG;

    //give some top margin
    y -= marginCt;
    
    return {
        xMin: xMin,
        xMax: xMax,
        yMin: y,
        r1width: marginCt * (person.relations.partner ? 2 : 1),
        width: xMax - xMin,
        height: height + marginCt,
        heightWithoutMargin: height,
        svg: svg
    };
}

function initRelations(build) {
    if(!build.relations) build.relations = {};
    if(!build.relations.children) build.relations.children = [];
}

function makeAtomicBuildItem(build, pos) {
    var x = pos[0];
    var y = pos[1];

    if(!build) build = {cognomen: "", name: "UNKNOWN"};

    var background = `<ellipse fill="#${familyColors.primary(build)}" rx="${BUILD_ICON_SIZE / 2}" ry="${BUILD_ICON_SIZE  / 2}" cx="${x}" cy="${y}"/>`;

    var text = `<text style="font-family:'JetBrains Mono';font-size:24px" x="${x}" y="${y + BUILD_ICON_SIZE * 0.8}" text-anchor="middle">${build.name}</text>`

    return background + text;
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

function addChildrenAndPartners(builds) {

    builds.forEach(x=>initRelations(x));

    for(var i = 0; i < builds.length; i++) {
        var build = builds[i];
        if(builds[i + 1] && builds[i + 1].cognomen != build.cognomen) {
            build.relations.partner = builds[i + 1];
            builds[i + 1].relations.partner = build;
            build.relations.children = build.relations.partner.relations.children;
        }
    }
    for(var i = 0; i < builds.length; i++) {
        var build = builds[i];
        if(build.relations.partner) {
            var childCognomens = [build.cognomen, build.relations.partner.cognomen];

            for(i += 2; i < builds.length; i++) {
                if(childCognomens.includes(builds[i].cognomen)) {
                    var children = build.relations.children;
                    children.push(builds[i]);
                    builds[i].relations.hasParent = true;
                    if(builds[i].relations.partner) {
                        i--;
                        break;
                    }
                }
            }
        }
    }
}