var qCache = new Map();

module.exports = {
    getOneOfType: function(ast, type) {
        return cachedGetAllOfType(ast, type, 1)[0];
    }, 
    getAllOfType: function(ast, type) {
        return cachedGetAllOfType(ast, type, Infinity);
    }
}

function cachedGetAllOfType(ast, type, limit) {
    var astCache = qCache.get(ast);
    if(!astCache) astCache = {};
    if(!astCache[type]) astCache[type] = {};

    if(astCache[type][limit] || astCache[type][Infinity]) {
        //Infinity items means that it's a match for this one, too
        if(limit != Infinity) astCache[type][limit] = astCache[type][limit] || astCache[type][Infinity]
    }
    else {
        astCache[type][limit] = recursiveDescentGrabAllOfType(ast, type, limit);
    }

    qCache.set(ast, astCache);

    return astCache[type][limit];
}

function recursiveDescentGrabAllOfType(ast, type, limit) {
    var children = Object.values(ast).filter(x=>typeof x === "object").flat().filter(x => x && typeof x.type === "string");

    var childrenOfType = children.filter(x=>x.type == type);

    limit -= childrenOfType.length;

    for(var i = 0; i < children.length && limit > 0; i++) {
        var childSearch = recursiveDescentGrabAllOfType(children[i], type, limit);
        limit -= childSearch.length;
        childrenOfType = childrenOfType.concat(childSearch);
    }
    return childrenOfType;
}