var fs = require("fs");
var path = require("path");

/**
 * @typedef {object} ClassedTree
 * @property {string} class
 * @property {ClassedTree[]} children
 */



 var NODE_DOT_DIAMETER = 10;
 var NODE_DOT_MARGIN = 1;
 var STROKE_WIDTH = 1;
 var VERTICAL_SPACING = 200;
 var EMBEDDED_DOT_DIAMETER = 20;

/**
 * 
 * @param {ClassedTree} tree 
 */
var m = 0;
module.exports = function(tree) {
    var svg = treeToSvg(tree, NODE_DOT_DIAMETER, NODE_DOT_DIAMETER);
    var flattededTree = naiveFlatten(tree, NODE_DOT_DIAMETER, svg.height * 0.9, svg.width);
    var goodFlatten = spiffyFlatten(tree, NODE_DOT_DIAMETER, svg.height * 0.95, svg.width);
    
    var svgFile = `<svg width="${svg.width + NODE_DOT_DIAMETER/2}" height="${svg.height + NODE_DOT_DIAMETER/2}" xmlns="http://www.w3.org/2000/svg">`
    + svg.xml + flattededTree + goodFlatten
    + `</svg>`;
    fs.writeFileSync(path.join(__dirname, "trees", (m++) + "tree.svg"), svgFile);
    
}

function naiveFlatten(tree, x, y, totalWidth) {
    var currentX = x;

    function n(t) {
        var m = `<ellipse cx="${currentX}" cy="${y}" rx="${EMBEDDED_DOT_DIAMETER/2}" ry="${EMBEDDED_DOT_DIAMETER/2}" style="fill:${getClassColour(t.class)}"/>`;
        currentX += EMBEDDED_DOT_DIAMETER + NODE_DOT_MARGIN;
        if(Math.abs(currentX - x) + EMBEDDED_DOT_DIAMETER >= totalWidth) {
            currentX = x;
            y += EMBEDDED_DOT_DIAMETER + NODE_DOT_MARGIN;
        }
        t.children.forEach(z=> {
            m+=n(z);
        });
        return m;
    }
    return n(tree);
}

function spiffyFlatten(tree, x, y, totalWidth) {
    /**
     * @type {ClassedTree[]}
     */
    var aC = [];
    function n(t) {
        aC.push({class: t.class, children:[]});
        t.children.forEach(j=>n(j));
    }
    tree.children.forEach(j=>n(j));
    
    return naiveFlatten({class: tree.class, children: aC.sort((a,b)=>a.class.localeCompare(b.class))}, x, y, totalWidth);
}

/**
 * 
 * @param {ClassedTree} tree 
 */
function treeToSvg(tree, x, y) {
    x = x||0;
    y = y||0;
    
    var xml = "";
    var totalWidth = 0;
    var totalHeight = 0;
    
    totalWidth += NODE_DOT_MARGIN + NODE_DOT_DIAMETER;
    totalHeight += NODE_DOT_MARGIN + NODE_DOT_DIAMETER;
    
    tree.children.forEach((child,i,a)=>{
        var childX = x + totalWidth + NODE_DOT_MARGIN;
        var childY = y + NODE_DOT_MARGIN + VERTICAL_SPACING;
        
        xml += `<path d="M${x} ${y} L${childX} ${childY}" style="stroke:#000;stroke-width:${STROKE_WIDTH}px"/>`
        var processedChild = treeToSvg(child, childX, childY);
        xml += processedChild.xml;
        totalWidth += processedChild.width + NODE_DOT_MARGIN;
        totalHeight += processedChild.height;
    });
    
    xml += `<ellipse cx="${x}" cy="${y}" rx="${NODE_DOT_DIAMETER/2}" ry="${NODE_DOT_DIAMETER/2}" style="fill:${getClassColour(tree.class)}"/>`;
    
    return {
        width: totalWidth,
        xml: xml,
        height: totalHeight
    }
}


var classColours = {};
function getClassColour(cl) {
    if(classColours[cl]) return classColours[cl];
    else return (classColours[cl] = nextColour());
}


var colours = (`034732-008148-c6c013-ef8a17-ef2917-4d243d-561d25-ce8147-99ddc8-f6511d-00072d-001c55-0a2472-0e6ba8-a6e1fa-b55300-ff01fb-02a9ea-b00b31-ceb992-000000-3d2645-832161-da4167-6BF178-f0f600-00e5e8-007c77-d9dbf1-d0cdd7`)
    .split("-").slice(0, 25);
function nextColour() {
    return "#" + colours.pop();    
}