var typeCodes = require("./types");
var bitwiseyTools = require("../../script-helpers/bitwisey-helpers");
var wellKnownConstructors = require("./well-known-constructors");

module.exports = function(obj) {
    var valuePool = [];
    createOrGetIdInValuepool(obj, valuePool);

    return valuePool.map(x=>x.bytes).flat(4);
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
    for(var i = valuePool.length - 1; i >= 0; i--) {
        if(valuePool[i].value === obj) {
            return valuePool[i].id;
        }
    }

    var poolEntry = {value: obj, id: valuePool.length};
    valuePool.push(poolEntry);

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
        case "wellKnownObject": poolEntry.bytes = [ createOrGetIdInValuepool(cstr, valuePool), getEntriesBytes(obj, valuePool) ];
    }

    poolEntry.bytes = [typeCodes[type], poolEntry.bytes.length].concat(poolEntry.bytes);

    return poolEntry.id;
}

function getEntriesBytes(obj, valuePool) {
    var entries = Object.entries(obj);
    var b = [];
    for(var i = 0; i < entries.length; i++) {
        var kB = bitwiseyTools.toVarintBytes(createOrGetIdInValuepool(entries[i][0], valuePool));
        var vB = bitwiseyTools.toVarintBytes(createOrGetIdInValuepool(entries[i][1], valuePool));

        b.push(kB, vB);
    }
    return b;
}