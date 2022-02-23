var fakeDom = require("./fake-dom");
var fs = require("fs");

var mapTemplate = fs.readFileSync(__dirname + "/data/Dualshock_4_Layout_2.svg").toString();
var mapTemplateDom = fakeDom.makeDocument(fakeDom.parseHTML(mapTemplate));

module.exports = function () {
    var document = mapTemplateDom.cloneNode();
    var labels = [];


    return {
        addLabel: function (button, lbl) {
            var pos = getButtonPosition(button, document);
            labels.push({ button: getButtonBasicInfo(button), position: pos, description: lbl });
        },
        render: function() {
            addEndPositions(labels);
            labels.forEach(x=>addLabelElem(document, x));
            return document.innerHTML;
        }
    }
}

function getButtonBasicInfo(b) {
    var w = b.split("-");

    return {
        gamepad: w[0] == "b2" ? 2 : 1,
        button: w[1]
    }
}


function addEndPositions(labels) {
    var TOP_THRESHOLD = averageYCoord(labels), LEVEL_SIZE = 200, LAYER_MAX_WIDTH = 1200;
    
    //assign each a basic top/bottom layer first.
    labels.forEach(x=>x.level = x.position[1] > TOP_THRESHOLD ? 1 : -1);

    recalculateLevelIndexes(labels);

    recalculateLevelSizes(labels);

    labels.forEach(x=> x.endPosition = [
        (x.indexInLevel / x.countInLevel) * LAYER_MAX_WIDTH,
        TOP_THRESHOLD + LEVEL_SIZE * x.level 
    ]);
}

function recalculateLevelSizes(labels) {
    execCbForEachLabelLayer(labels, function(levelIndex) {
        
    })
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
        var posLvl = labels.filter(x=>x.level == curLvl);

        if(posLvl.length == 0) checkedPos--;
        else cb(posLvl);

        var negLvl = labels.filter(x=>x.level == -curLvl);

        if(negLvl.length == 0) checkedNeg--;
        else cb(negLvl);

        if(checkedNeg <= 0 && checkedPos <= 0) break;
    }
}

function averageYCoord(labels) {
    var t = 0;
    labels.forEach(x=>t += x.position[1]);
    return t / labels.length;
}

function estimateWidth(label) {
    var d = label.description;
    return Math.max(d.title.length * 5, d.subtitle.length) + 10;
}

function addLabelElem(document, label) {
    var svgElem = document.getElementById("svgRoot");

    var x = label.position[0], y = label.position[1];

    var eX = label.endPosition[0], eY = label.endPosition[1];
    
    var path = document.createElement("path");
    path.setAttribute("d", `M ${x} ${y} L ${eX} ${eY}`);
    path.style.stroke = "#000000";
    path.style.strokeWidth = "3";
    svgElem.appendChild(path);

    var text = document.createElement("text");
    text.setAttribute("x", eX);
    text.setAttribute("y", eY);
    text.textContent = label.description.title;
    svgElem.appendChild(text);
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