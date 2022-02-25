var fakeDom = require("./fake-dom");
var fs = require("fs");
var nextColor = require("./colors");

var mapTemplate = fs.readFileSync(__dirname + "/data/Dualshock_4_Layout_2.svg").toString();
var mapTemplateDom = fakeDom.makeDocument(fakeDom.parseHTML(mapTemplate));

var TOP_THRESHOLD = 150, LEVEL_SIZE = 50, LAYER_MAX_WIDTH = 1200, MIDDLE_Y_MARGIN = 200, MAX_SUBTITLE_WIDTH = 40;

module.exports = function () {
    var document = mapTemplateDom.cloneNode();
    var labels = [];


    return {
        addLabel: function (button, lbl) {
            var pos = getButtonPosition(button, document);
            labels.push({ button: getButtonBasicInfo(button), position: pos, description: neatifyDescription(lbl) });
        },
        render: function() {
            condenseLabels(labels);
            addEndPositions(labels, document);
            addLabelElements(labels, document);
            return document.innerHTML;
        }
    }
}

function neatifyDescription(description) {
    return {
        title: camelToSentence(description.title),
        subtitle: description.subtitle
    }
}

function camelToSentence(str) {
    var w = [];
    var acc = str[0];
    for(var i = 1; i < str.length; i++) {
        if(isUpper(str[i]) && !isUpper(str[i - 1])) {
            w.push(acc);
            acc = str[i];
        } else {
            acc += str[i];
        }
    }
    w.push(acc);
    return w.map(x=>titlecase(x)).join(" ");
}

function titlecase(s) {
    return s[0].toUpperCase() + s.substring(1).toLowerCase();
}

function isUpper(c) {
    return c.toUpperCase() == c;
}

function condenseLabels(labels) {
    for(var i = 0; i < labels.length; i++) {
        var lbl = labels[i];
        //find labels with the same title and an x-coord within 400px.
        var coterminalLabels = labels.filter(x=>
            x.description.title == lbl.description.title 
            && Math.abs(x.position[0] - lbl.position[0]) < 1200
            && x.position[1] > TOP_THRESHOLD == lbl.position[1] > TOP_THRESHOLD
        );

        if(coterminalLabels.length > 1) {
            lbl.positions = coterminalLabels.map(x=>x.position);
            lbl.description.subtitle = coterminalLabels.map(x=>x.description.subtitle).join("\n");
            coterminalLabels.forEach(x=> {
                if(x != lbl) labels.splice(labels.indexOf(x), 1);
            });
        } else {
            lbl.positions = [lbl.position];
        }
        
    }
}

function addLabelElements(labels, document) {
    var svgElem = document.getElementsByTagName("svg")[0];

    var lineRoot = document.createElement("g");
    svgElem.appendChild(lineRoot);

    var labelRoot = document.createElement("g");
    svgElem.appendChild(labelRoot);

    labels.forEach(x=>addLabelElem(document, lineRoot, labelRoot, x));
}

function getButtonBasicInfo(b) {
    var w = b.split("-");

    return {
        gamepad: w[0] == "b2" ? 2 : 1,
        button: w[1]
    }
}


function addEndPositions(labels, document) {
    
    //assign each a basic top/bottom layer first.
    labels.forEach(x=>x.level = x.position[1] >= TOP_THRESHOLD ? 1 : -1);
    
    //estimate width & save it
    labels.forEach(x=>{
        x.estWidth = estimateWidth(x);
        x.estHeight = estimateHeight(x);
    });

    recalculateLevelIndexes(labels);

    recalculateLevelSizes(labels, LAYER_MAX_WIDTH);
    

    labels.forEach(x=> x.endPosition = [
        (x.indexInLevel / x.countInLevel) * LAYER_MAX_WIDTH,
        TOP_THRESHOLD + Math.sign(x.level) * MIDDLE_Y_MARGIN + calculateDistanceToLevel(labels, x.level)
    ]);
    
    adjustYBox(document, labels, TOP_THRESHOLD, LEVEL_SIZE, MIDDLE_Y_MARGIN);
}

function calculateDistanceToLevel(labels, lvl) {
    var total = 0;

    var lvlSign = Math.sign(lvl);    
    var lvlMag = Math.abs(lvl);

    //when building "down" (actually up bc svg coords), count the current level.
    var lvlTarg = lvlSign == -1 ? lvlMag : lvlMag - 1;

    for(var i = 1; i <= lvlTarg; i++) {
        var l = lvlSign * i;
        var lbl = labels.filter(x=>x.level == l);
        var levelHeight = lbl.reduce((a,x)=>Math.max(x.estHeight, a), 0);
        
        total += levelHeight + 10;
    }

    return total * lvlSign;
}

function adjustYBox(document, labels, middle, levelSize, marginSize) {
    var svgElem = document.getElementsByTagName("svg")[0]
    
    var width = +svgElem.getAttribute("width");
    var height = +svgElem.getAttribute("height");
    
    var viewBox = [0,0,width,height];
    
    var maxLevel = Math.max(...labels.map(x=>x.level));
    var minLevel = Math.min(...labels.map(x=>x.level));

    var minY = middle - marginSize * 2 + (minLevel - 1) * levelSize;
    var maxY = middle + marginSize * 2 + (maxLevel + 1) * levelSize;
    
    viewBox[1] = minY;
    viewBox[3] = (maxY - minY);
    
    viewBox[0] += -100;
    viewBox[2] += 100;
    
    svgElem.setAttribute("height", viewBox[3]);
    svgElem.setAttribute("viewBox", viewBox.join(" "));
}

function recalculateLevelSizes(labels, max) {
    var labelsNeedCalc = true;
    while(labelsNeedCalc) {
        
        execCbForEachLabelLayer(labels, function(levelIndex) {
            
            var level = labels.filter(x=>x.level == levelIndex)
                .sort((a,b)=>a.indexInLevel - b.indexInLevel);
                
            for(var i = 1; i < level.length; i++) {
                if(labelOverlaps(level, i, max)) level[i].level += Math.sign(levelIndex);
            }

            labelsNeedCalc = false;
        });
    }
}

function labelOverlaps(labels, index, levelMaxWidth) {
    var level = labels[index].level;
    var referenceElementX = calculateX(labels[index], levelMaxWidth);

    for(var i = index - 1; i >= 0; i--) {
        if(labels[i].level == level) {
            var thisX = calculateX(labels[i], levelMaxWidth)
            var availableSpace = referenceElementX - thisX;

            if(labels[index].estWidth >= availableSpace - 100) return true;
        }
    }
    return false;
}

function calculateX(label, levelWidth) {
    return (label.indexInLevel / label.countInLevel) * levelWidth;
}

function recalculateLevelIndexes(labels) {
    execCbForEachLabelLayer(labels, function(levelIndex) {
        var level = labels.filter(x=>x.level == levelIndex)
            .sort((a,b)=>a.position[0] - b.position[0]);

        level.forEach((x,i)=>{
            x.indexInLevel = i;
            x.countInLevel = level.length;
        });
    });
}

function execCbForEachLabelLayer(labels, cb) {
    var checkedPos = 2, checkedNeg = 2;
    for(var curLvl = 0; true; curLvl++) {
        var posLvl = labels.findIndex(x=>x.level == curLvl);

        if(posLvl == -1) checkedPos--;
        else cb(curLvl);

        var negLvl = labels.findIndex(x=>x.level == -curLvl);

        if(negLvl == -1) checkedNeg--;
        else cb(-curLvl);

        if(checkedNeg <= 0 && checkedPos <= 0) break;
    }
}

function estimateWidth(label) {
    var d = label.description;
    var ls = getLines(d.subtitle, MAX_SUBTITLE_WIDTH);
    var mls = ls.reduce((a,x)=>Math.max(x.length, a), 0);

    return Math.max(d.title.length * 18, mls * 14) + 10;
}
function estimateHeight(label) {
    var d = label.description;
    
    var lCount = getLines(d.subtitle, MAX_SUBTITLE_WIDTH).length;

    return (40) + lCount * 18 + 10;
}

function getLines(text, maxLineLength) {
    var explicitLines = text.split("\n");
    return explicitLines
        .map(x=>getLinesCollapseWhitespace(x, maxLineLength))
        .flat();
}

function getLinesCollapseWhitespace(text, maxLineLength) {

    var words = text.split(/\b/);

    var lines = [], line = "";
    for(var i = 0; i < words.length; i++) {
        if(words[i] == "") {
            lines.push(line + "\n");
            line = "";
        } else {
            if(line.length + words[i].length > maxLineLength) {
                lines.push(line);
                line = words[i];
            } else {
                line += words[i];
            }
        }
    }
    lines.push(line);

    return lines;
}

function addLabelElem(document, lineRoot, labelRoot, label) {
    var eX = label.endPosition[0], eY = label.endPosition[1];

    var color = nextColor();
    
    var path = document.createElement("path");
    path.setAttribute("d", 
        label.positions
        .map(p=>lineBetween(p[0], p[1], eX + label.estWidth / 2, eY))
        .join(" ")
    );
    path.style.stroke = color;
    path.style.fill = "none";
    path.style.strokeWidth = "2";
    lineRoot.appendChild(path);

    var labelElem = createLabelElem(label, eX, eY, color, document);
    labelRoot.appendChild(labelElem);

    
}

function createLabelElem(label, x, y, color, document) {
    var g = document.createElement("g");

    addPerspBoxLines(g, x, y, label.estWidth, label.estHeight, label.level, color, document);

    var hText = document.createElement("text");
    hText.style.fontWeight = "900";
    hText.style.fontSize = "18px";
    hText.style.dominantBaseline = "hanging";

    hText.setAttribute("x", x + 5);
    hText.setAttribute("y", y + 5);
    hText.textContent = label.description.title;
    g.appendChild(hText);

    var dText = document.createElement("text");
    var dLines = getLines(label.description.subtitle, MAX_SUBTITLE_WIDTH);
    dLines.forEach(ln=> {
        var t = document.createElement("tspan");
        t.textContent = ln;
        t.setAttribute("x", x + 5);
        t.setAttribute("dy", 18);
        dText.appendChild(t);
    });
    dText.setAttribute("x", x + 5);
    dText.setAttribute("y", y + 25);

    g.appendChild(dText);

    return g;
}

function addPerspBoxLines(parent, x, y, w, h, lvl, color, document) {
    //background box
    parent.appendChild(makeBoxOfColor(x, y, w, h, "#fff", document));

    var lineTop = pathWithD(parent, `M${x} ${y} L ${x + w} ${y}`, color, document);
    var lineBottom = pathWithD(parent, `M${x} ${y + h} L ${x + w} ${y + h}`, color, document);
    var lineLeft = pathWithD(parent, `M${x} ${y} L ${x} ${y + h}`, color, document);
    var lineRight = pathWithD(parent, `M${x + w} ${y} L ${x + w} ${y + h}`, color, document);

    var xStart = x, xEnd = x + w;

    lineLeft.style.strokeWidth = (1 + 2 * Math.round((xStart - LAYER_MAX_WIDTH/2) / (LAYER_MAX_WIDTH/2)));
    lineRight.style.strokeWidth = 4 - (1 + 2 * Math.round((xEnd - LAYER_MAX_WIDTH/2) / (LAYER_MAX_WIDTH/2)));


    if(lvl < 0) {
        lineBottom.style.strokeWidth = 3;
    } else {
        lineTop.style.strokeWidth = 3;
    }

}

function makeBoxOfColor(x, y, w, h, color, document) {
    var bgBox = document.createElement("rect");
    bgBox.setAttribute("x", x);
    bgBox.setAttribute("y", y);
    bgBox.setAttribute("height", h);
    bgBox.setAttribute("width", w);
    bgBox.style.fill = color;
    return bgBox;
}

function pathWithD(parent, d, color, document) {
    if(typeof parent === "string") {
        d = parent;
        parent = undefined;
    }

    var p = document.createElement("path");
    p.setAttribute("d", d);
    p.style.strokeWidth = 1;
    p.style.stroke = color;
    p.style.strokeLinejoin = "round";

    if(parent) parent.appendChild(p);
    return p;
}

function lineBetween(x, y, eX, eY) {
    var deltaXCoef =  0.5;
    
    
    
    return `M ${x} ${y} L ${eX} ${y + (eY - y) * deltaXCoef} L ${eX} ${eY}`
}

function getButtonPosition(button, document) {
    var elm = getButtonElemTryagain(button, document);
    if (!elm) throw "Unknown button " + button;
    var pathSpec = elm.getAttribute("d");

    var coords = pathSpec.matchAll(/(-?\d*(?:.\d+)?),(-?\d*(?:.\d+)?) [A-Z]/g);

    coords = Array.from(coords).map(x => [+x[1], +x[2]]);

    var tx = 0, ty = 0;
    coords.forEach(x=>{
        tx += x[0];
        ty += x[1]
    });

    return [tx / coords.length, ty / coords.length];
}

function getButtonElemTryagain(b, document) {
    var possible = [
        b,
        b.replace(/[yx]$/, ""),
        b.replace(/button$/, "")
    ]
    for(var i = 0; i < possible.length; i++) {
        var f = document.getElementById(possible[i]);
        if(f) return f
    }
}
