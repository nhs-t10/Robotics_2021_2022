
var initial = require("./initial-assumptions");
var javaFuncs = require("./java-functions");

module.exports = function() {
    var tsMap = {};
    restoreFromMappings(tsMap, initial);
    restoreFromMappings(tsMap, javaFuncs);
    
    return tsMap;
}

function restoreFromMappings(cannonical, historical) {
    for(var k in historical) {
        upsertInterpretMapping(cannonical, k, historical[k]);
    }
}

function upsertInterpretMapping(cannonical, mappingTitle, uninterpertedMapping) {
    var or = getType(cannonical, mappingTitle);
    Object.assign(or, interpertMapping(cannonical, uninterpertedMapping, mappingTitle));
}

function getType(cannonical, typeName) {
    if(typeof typeName !== "string") {
        console.error("Non-string type name!", typeName);
        throw new Error("");
    }
    
    if(!cannonical[typeName]) {
        cannonical[typeName] = { type: "?" };
    }
    return cannonical[typeName];
}

function interpertMapping(cannonical, mapping) {
    if(typeof mapping === "string") return getType(cannonical, mapping);
    
    switch(mapping.type) {
        case "primitive": return Object.assign({}, mapping);
        case "alias": return resolveAliasType(cannonical, mapping);
        case "union": return resolveUnionType(cannonical, mapping);
        case "object": return resolveObjectType(cannonical, mapping);
        case "function": return resolveFunctionType(cannonical, mapping);
        case "bytecode": return resolveBytecodeType(cannonical, mapping);
    }
    console.log(mapping);
    console.log(new Error().stack);
    throw new Error("Unknown type-mapping variant '" + mapping.type + "'");
}

function resolveAliasType(cannonical, aliasMapping) {
    return {
        type: "alias",
        typeTo: interpertMapping(cannonical, aliasMapping.typeTo)
    }
}

function resolveBytecodeType(cannonical, bcMapping) {
    return {
        type: "bytecode",
        pop: bcMapping.pop.map(x => interpertMapping(cannonical, x)),
        push: bcMapping.push.map(x => interpertMapping(cannonical, x))
    }
}

function resolveUnionType(cannonical, unionMapping) {
    return {
        type: "union",
        types: unionMapping.types.map(x => interpertMapping(cannonical, x))
    };
}

function resolveObjectType(cannonical, mapping) {
    var r = { type: "object", properties: {} };
    
    for(var k in mapping.properties) {
        r.properties[k] = interpertMapping(cannonical, mapping.properties[k]);
    }
    
    return r;
}

function resolveFunctionType(cannonical, functionMapping) {
    
    return {
        type: "function",
        args: functionMapping.args.map(x => interpertMapping(cannonical, x)),
        varargs: interpertMapping(cannonical, functionMapping.varargs || "undefined"),
        argnames: (functionMapping.argnames || []).slice(),
        return: interpertMapping(cannonical, functionMapping.return || "undefined")
    }
}