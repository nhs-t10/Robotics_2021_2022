var magic = require("./magic");
var version = require("./version");
var arrayReader = require("../../script-helpers/array-reader");
var bitwiseyHelpers = require("../bitwisey-helpers");
var types = require("./types");
const wellKnownConstructors = require("./well-known-constructors");

var typesInverted = Object.fromEntries(Object.entries(types).map(x=>[x[1], x[0]]));

module.exports = bufferToObject


function bufferToObject(buf) {
    var reader = arrayReader(buf);

    reader.skip(magic.length);
    
    var bVersion = reader.read();
    if(version != bVersion) throw new Error("Version mismatch in structured-serialise!");
    
    var pool = [];
    
    reconstructPool(pool, reader);
    
    hydratePool(pool);
    return pool[0].value;
}

var byteParsers = {
    "undefined": function(entry, pool, thisId) {
        entry.value = undefined;
        entry.hydrated = true;
    },
    "boolean": function(entry, pool, thisId) {
        entry.value = !!entry.bytes[0];
        entry.hydrated = true;
    },
    "number": function(entry, pool, thisId) {
        entry.value = bitwiseyHelpers.numberFromBytes(entry.bytes);
        entry.hydrated = true;
    },
    "string": function(entry, pool, thisId) {
        entry.value = Buffer.from(entry.bytes).toString("utf8");
        entry.hydrated = true;
    },
    "null": function(entry) {
        entry.value = null;
        entry.hydrated = true;
    },
    "object": function(entry, pool) {
        var o = entry.value || {};
        var reader = arrayReader(entry.bytes);
        
        entry.value = o;
        
        while(reader.hasNext()) {
            var k = pool[reader.readVarint()];
            if(!k.hydrated) return;
            
            var v = pool[reader.readVarint()];   
            if(!v.hasOwnProperty("value")) return;

            o[k.value] = v.value;
        }
        entry.hydrated = true;
    },
    "wellKnownObject": function(entry, pool) {
        var reader = arrayReader(entry.bytes);
        
        var constrNameEntry = pool[reader.readVarint()];
        if(!constrNameEntry.hydrated) return;
        var constrName = constrNameEntry.value;
        
        var constrArg = undefined;
        var constrArgId = reader.readVarint();
        if(constrArgId && constrArgId != entry.id) {
            var constrArgEntry = pool[constrArgId];
            if(!constrArgEntry.hydrated) return;
            constrArg = constrArgEntry.value;
        }
        
        var constr = wellKnownConstructors.byName(constrName);
        var o = entry.value;
        
        if(!o) {
            try { o = constrArg ? new constr(constrArg) : new constr(); }
            catch(e) { o = Object.create(constr.prototype); }
        }
        
        entry.value = o;
        
        while(reader.hasNext()) {
            var k = pool[reader.readVarint()];
            if(!k.hydrated) return;
            
            var v = pool[reader.readVarint()];   
            if(!v.hasOwnProperty("value")) return;

            o[k.value] = v.value;
        }
        entry.hydrated = true;
    }
}

function hydratePool(pool) {
    while(true) {
        var allHy = true;
        for(var i = pool.length - 1; i >= 0; i--) {
            var entry = pool[i];
            if(!entry.hydrated) {
                byteParsers[entry.type](entry, pool, i);
                if(!entry.hydrated) allHy = false;
            }
        }
        if(allHy) break;
    }
}

function reconstructPool(pool, reader) {
    var index = 0;
    
    while(reader.hasNext()) {
        var typeCode = reader.read();
        var length = reader.readVarint();
        
        var contentBytes = reader.readNextBytes(length);
        
        if(typeCode in typesInverted) {
            var typeName = typesInverted[typeCode];
            pool.push({ typeId: typeCode, type: typeName, bytes: contentBytes, id: index });
        } else {
            pool.push({ typeId: typeCode, type: undefined, bytes: contentBytes, value: undefined, hydrated: true, id: index });
        }
        index++;
    }
}