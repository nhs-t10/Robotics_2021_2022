var cIndex = 0;

var colours = ["#9d6b1b","#004fa3","#163b64","#611a2d","#740606","#351431","#100b00","#2a513b","#006638"];

module.exports = function nextColor() {
    cIndex++;
    if(cIndex >= colours.length) cIndex = 0;
    return colours[cIndex];
}