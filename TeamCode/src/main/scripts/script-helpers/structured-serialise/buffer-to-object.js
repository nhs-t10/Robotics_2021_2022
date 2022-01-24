var arrayReader = require("../../script-helpers/array-reader");
var bitwiseyHelpers = require("../bitwisey-helpers");
var types = require("./types");
const wellKnownConstructors = require("./well-known-constructors");

var typesInverted = Object.fromEntries(Object.entries(types).map(x=>[x[1], x[0]]));

module.exports = function(buf) {
    var pool = {};
    reconstructPool(pool, arrayReader(buf));
    return hydratePool(pool, 0);
}

var byteParsers = {
    "undefined": function(bytes, pool) {
        return undefined;
    },
    "boolean": function(bytes, pool) {
        return !!bytes[0];
    },
    "number": function(bytes, pool) {
        return bitwiseyHelpers.numberFromBytes(bytes);
    },
    "string": function(bytes, pool) {
        return Buffer.from(bytes).toString("utf8");
    },
    "object": function(bytes, pool) {
        var o = {};
        var reader = arrayReader(bytes);
        
        while(reader.hasNext()) {
            var k = hydratePool(pool, reader.readVarint());
            var v = hydratePool(pool, reader.readVarint());
            o[k] = v;
        }
        return o;
    },
    "null": function(bytes, pool) {
        return null
    },
    "wellKnownObject": function(bytes, pool) {
        var reader = arrayReader(bytes);
        
        var constrName = hydratePool(pool, reader.readVarint());
        
        var constrArgId = reader.readVarint();
        var constrArg = constrArgId ? hydratePool(pool, constrArgId) : undefined;
        
        var constr = wellKnownConstructors.byName(constrName);
        var o;
        
        try {
            o = constrArg ? new constr(constrArg) : new constr();
        } catch(e) {
            o = Object.create(constr.prototype);
        }
        
        while(reader.hasNext()) {
            var k = hydratePool(pool, reader.readVarint());
            var v = hydratePool(pool, reader.readVarint());
            o[k] = v;
        }
        return o;
    }
}

function hydratePool(pool, index) {
    if(!pool.hasOwnProperty(index)) return undefined;
       
    var entry = pool[index];
    if(!entry.hydrated) entry.value = byteParsers[entry.type](entry.bytes, pool);
    entry.hydrated = true;
    return entry.value;
}

function reconstructPool(pool, reader) {
    var index = 0;
    
    while(reader.hasNext()) {
        var typeCode = reader.read();
        var length = reader.readVarint();
        
        var contentBytes = reader.readNextBytes(length);
        
        if(typeCode in typesInverted) {
            var typeName = typesInverted[typeCode];
            pool[index] = { typeId: typeCode, type: typeName, bytes: contentBytes, id: index };
        } else {
            pool[index] = { typeId: typeCode, type: undefined, bytes: contentBytes, value: undefined };
        }
        index++;
    }
}