var fs = require("fs");
const path = require("path");

var memoizeParsedAliases = {};

var transmutations = {};

module.exports = {
    /**
     * 
     * @param {Transmutation} t 
     */
     registerTransmutation: function (t) {
        if (transmutations[t.id]) throw new Error("Re-registered " + t.id);
        transmutations[t.id] = t;
    },
    /**
     * 
     * @param {string|string[]} id 
     * @returns {TransmutationFunction}
     */
    get: function(id) {
        if (!transmutations[id]) throw "No such registered " + id;
        return require(transmutations[id].sourceFile);
    },
    /**
     * 
     * @param {string} s 
     * @returns {SerializableTransmutationInstance[]}
     */
    expandTasks: function(s) {
        var tasks = memoizedParseSpecExpandAliases(s);
        insertDeps(tasks);
        
        return idsToTras(tasks);
    }
};

loadTransmutations(__dirname);

function idsToTras(ids) {
    return ids.map(x=>{
        var f = {};
        Object.assign(f, transmutations[x.id]);
        f.isDependency = x.dep;
        return f;
    });
}

function memoizedParseSpecExpandAliases(sp) {
    
    var t = JSON.stringify(sp);
    
    var cVal = memoizeParsedAliases[t];
    
    if(!cVal) {
        cVal = parseSpecExpandAliases(sp);
        memoizeParsedAliases[t] = cVal;
    }
    return cVal;
}

/**
     * @param {string|string[]} s
     * @returns {TransmutationTask[]}
     */
function parseSpecExpandAliases(sp) {
    if(typeof sp === "string") sp = sp.split(/ +/);

    var r = [];
    sp.forEach(x => {
        x = x.trim();

        var optional = x.endsWith("?");
        x = x.replace(/\?/, "");

        var expanded = expandAlias(x);
        
        r = r.concat(expanded);
    });
    
    return r;
}

function insertDeps(insertInto, findDepsIn) {
    if(findDepsIn === undefined) findDepsIn = insertInto;
    
    for(var i = 0; i < findDepsIn.length; i++) {
        var req = transmutations[findDepsIn[i].id].requires;
        var reqExpanded = memoizedParseSpecExpandAliases(req);
        
        var intoIndex = insertInto.findIndex(x=>x.id == findDepsIn[i].id);
        
        for(var j = 0; j < reqExpanded.length; j++) {
            var x = reqExpanded[j].id;
            if(!insertInto.find(z=>z.id == x)) {
                insertInto.splice(intoIndex,0,{id:x,dep:true});
                intoIndex++;
            }
        }
        insertDeps(insertInto, reqExpanded);
    }
}

function expandAlias(spk) {
    spk = spk.trim();
    
    var category = spk.startsWith(":");
    spk = spk.replace(":","");

    var keys = Object.keys(transmutations);
    var rgxp = new RegExp("^" + spk.replace(/\*/g, ".*") + "$");

    var globspanded = keys.filter(x => {
        if(category) return rgxp.test(transmutations[x].type);
        else return rgxp.test(x);
    });
    

    return globspanded.map(x => {
        var e = transmutations[x];
        if (!e) throw "No such transmutation `" + x + "`";
        
        if (e.type == "alias") return e.aliasesTo.split(" ").map(x=>expandAlias(x));
        else return {id: e.id};
    }).flat(2);
}

function loadTransmutations(dirname) {
    var dir = fs.readdirSync(dirname, { withFileTypes: true });
    
    for(var i = 0; i < dir.length; i++) {
        var p = path.join(dirname, dir[i].name);
        
        if(dir[i].isDirectory()) {
            var pIdxFile = path.join(p, "index.transmute-meta.js");
            if(fs.existsSync(pIdxFile)) {
                loadTransmutation(path.join(p, "index.js"), pIdxFile);
            }
            else {
                loadTransmutations(p);
            }
        }
    }
}

function loadTransmutation(sourceFile, metaFile) {
    var meta = require(metaFile);
    meta.sourceFile = sourceFile;
    transmutations[meta.id] = meta;
}

/**
 * @typedef {object} Transmutation
 * @property {string[]} requires
 * @property {string} id
 * @property {"output"|"transformation"|"input"|"information"|"check","codebase_postprocess"|"alias"} type
 * @property {TransmutationFunction} run
 * @property {string[]?} readsFiles
 * @property {string} sourceFile
 */


/**
 * @typedef {object} SerializableTransmutationInstance
 * @property {string[]} requires
 * @property {string} id
 * @property {"output"|"transformation"|"input"|"information"|"check","codebase_postprocess"|"alias"} type
 * @property {string[]?} readsFiles
 * @property {boolean} isDependency
 * @property {string} sourceFile
 */

/**
 * @callback TransmutationFunction
 * @param {TransmutateContext} context
 * @param {TransmutateContext[]?} contexts
 * @returns {TransmutateContext?}
 */

/**
 * @typedef {object} TransmutateContext
 * @property {"pass"|"fail"} status
 * @property {*} output
 * @property {Object.<string, *>} inputs
 * @property {*} lastInput
 *
 * @property {string} fileContentText
 * @property {string} sourceDir
 * @property {string} sourceBaseFileName
 * @property {string} sourceFullFileName
 *
 * @property {string} resultDir
 * @property {string} resultFullFileName
 * @property {string} resultBaseFileName
 *
 * @property {string} resultRoot
 * @property {string} sourceRoot
 * 
 * @property {Object.<string, string>} writtenFiles
 * @property {string[]} readsAllFiles
 *
 * @property {object} fileFrontmatter
 * @property {SerializableTransmutationInstance[]} transmutations
 * 
 * @property {string} cacheKey
 */