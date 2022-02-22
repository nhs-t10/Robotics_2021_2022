var UCONV_VERSION = 1;

var updateData = require("./update");
var cache = require("../cache");

var unitData = loadData();

module.exports = {
    getUnitForAbbreviation: getUnitForAbbreviation,
    getUnitForKey: getUnitForKey
}

function getUnitForKey(k) {
    return unitData.data[k];
}

function getUnitForAbbreviation(abb) {
    var key = unitData.index[abb];
    
    if(!key) return undefined;
    else if(key.length > 1) return key;
    else return unitData.data[key[0]];
}

function loadData() {
    var data = {v: NaN, index: {}, data: {}}; 
    try {    data = require("./units.json");    } catch(e) {}

    if(data.v != UCONV_VERSION) updateData(UCONV_VERSION);

    return data;
}