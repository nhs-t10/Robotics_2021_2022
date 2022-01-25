var typeCodes = require("./types");
var bitwiseyTools = require("../../script-helpers/bitwisey-helpers");
var wellKnownConstructors = require("./well-known-constructors");

var timeSpentSearchingPool = 0, timeSpentOnEntries = 0;

module.exports = function(obj) {
    var valuePool = {
        pool: [],
        invertedPoolMap: new Map()
    }

    createOrGetIdInValuepool(obj, valuePool);

    console.log("tsse", timeSpentOnEntries);

    return valuePool.pool.map(x=>x.bytes).flat(6);
}

function createOrGetIdInValuepool(obj, valuePool) {
    var type = obj === null ? "null" : typeof obj;
    var cstr;
    if(type === "object") {
        cstr = wellKnownConstructors.getName(obj);
        if(cstr) type = "wellKnownObject";
    }

    if(!typeCodes[type]) throw "Could not serialise value of type " + type;

    //by searching from the back, we get more-recent values first
    if(valuePool.invertedPoolMap.has(obj)) return valuePool.invertedPoolMap.get(obj);

    var poolEntry = {value: obj, id: valuePool.pool.length, bytes: []};
    valuePool.pool.push(poolEntry);

    switch(type) {
        case "undefined": poolEntry.bytes = [];
        break;
        case "boolean": poolEntry.bytes = [+obj];
        break;
        case "number": poolEntry.bytes = bitwiseyTools.numberToVarBytes(obj);
        break;
        case "string": poolEntry.bytes = Array.from(Buffer.from(obj, "utf8"));
        break;
        case "object": poolEntry.bytes = getEntriesBytes(obj, valuePool);
        break;
        case "null": poolEntry.bytes = [];
        break;
        case "wellKnownObject":
            //wellknownobjects record their constructor so they can be re-constructed later
            poolEntry.bytes = getWellKnownInfo(cstr, obj, valuePool).concat(getEntriesBytes(obj, valuePool));
    }

    poolEntry.bytes.splice(0, 0, typeCodes[type], bitwiseyTools.toVarintBytes(poolEntry.bytes.length));

    return poolEntry.id;
}

function getEntriesBytes(obj, valuePool) {

    var propNames = Object.getOwnPropertyNames(obj);
    var b = [];
    var tsE = Date.now();
    for(var i = 0; i < propNames.length; i++) {
        var kB = bitwiseyTools.toVarintBytes(createOrGetIdInValuepool(propNames[i], valuePool));
        var vB = bitwiseyTools.toVarintBytes(createOrGetIdInValuepool(obj[propNames[i]], valuePool));

        b.push(kB, vB);
    }

    timeSpentOnEntries += (Date.now() - tsE);

    return b.flat();
}

function getWellKnownInfo(constructorName, obj, valuePool) {
    var constructorPoolId = createOrGetIdInValuepool(constructorName, valuePool);
    var constructorPoolBytes = bitwiseyTools.toVarintBytes(constructorPoolId);
    
    var paramPoolId = 0;
    if(typeof obj.valueOf === "function" && !obj.hasOwnProperty("valueOf")) {
        var paramVal = obj.valueOf();
        if(paramVal != obj) paramPoolId = createOrGetIdInValuepool(paramVal, valuePool);
    }

    var paramPoolBytes = bitwiseyTools.toVarintBytes(paramPoolId);
    
    return constructorPoolBytes.concat(paramPoolBytes);
}