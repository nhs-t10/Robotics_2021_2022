
var initial = require("./initial-assumptions");
var javaFuncs = require("./java-functions");

module.exports = function() {
    var tsMap = {};
    var anonymousInnerTypeCounter = { k: 0 };
    
    restoreFromMappings(tsMap, initial, anonymousInnerTypeCounter);
    restoreFromMappings(tsMap, javaFuncs, anonymousInnerTypeCounter);
    
    return tsMap;
}

function restoreFromMappings(cannonical, historical, a) {
    for(var k in historical) {
        console.log(k);
        upsertInterpretMapping(cannonical, k, historical[k], a);
    }
}

function upsertInterpretMapping(cannonical, mappingTitle, uninterpertedMapping, a) {
    var original = getType(cannonical, mappingTitle, a);
    Object.assign(original, interpertMapping(cannonical, uninterpertedMapping, a));
    return original;
}

function getType(cannonical, typeName, anonymousInnerTypeCounter) {
    if(typeof typeName !== "string") {
        var type = typeName;
        if(anonymousInnerTypeCounter === undefined) throw new Error("no aitc");
        var anonKey = "$$anonymous inner type #" + anonymousInnerTypeCounter.k;
        anonymousInnerTypeCounter.k++;
        return upsertInterpretMapping(cannonical, anonKey, type, anonymousInnerTypeCounter);
    }
    
    if(!cannonical[typeName]) {
        cannonical[typeName] = { type: "?" };
    }
    return cannonical[typeName];
}

function interpertMapping(cannonical, mapping, a) {
    if(typeof mapping === "string") return getType(cannonical, mapping, a);
    
    switch(mapping.type) {
        case "primitive": return Object.assign({}, mapping);
        
        case "alias": return resolveAliasType(cannonical, mapping, a);
        case "union": return resolveUnionType(cannonical, mapping, a);
        case "object": return resolveObjectType(cannonical, mapping, a);
        case "function": return resolveFunctionType(cannonical, mapping, a);
        case "bytecode": return resolveBytecodeType(cannonical, mapping, a);
    }
    console.log(mapping);
    console.log(new Error().stack);
    throw new Error("Unknown type-mapping variant '" + mapping.type + "'");
}

function resolveAliasType(cannonical, aliasMapping, a) {
    return {
        type: "alias",
        typeTo: getType(cannonical, aliasMapping.typeTo, a)
    }
}

function resolveBytecodeType(cannonical, bcMapping, a) {
    return {
        type: "bytecode",
        pop: bcMapping.pop.map(x => getType(cannonical, x, a)),
        push: bcMapping.push.map(x => getType(cannonical, x, a))
    }
}

function resolveUnionType(cannonical, unionMapping, a) {
    return {
        type: "union",
        types: unionMapping.types.map(x => getType(cannonical, x, a))
    };
}

function resolveObjectType(cannonical, mapping, a) {
    var r = { type: "object", properties: {} };
    
    for(var k in mapping.properties) {
        r.properties[k] = getType(cannonical, mapping.properties[k], a);
    }
    
    return r;
}

function resolveFunctionType(cannonical, functionMapping, a) {
    
    return {
        type: "function",
        args: functionMapping.args.map(x => getType(cannonical, x, a)),
        varargs: getType(cannonical, functionMapping.varargs || "undefined", a),
        argnames: (functionMapping.argnames || []).slice(),
        return: getType(cannonical, functionMapping.return || "undefined", a)
    }
}