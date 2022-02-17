var fs = require("fs");
const path = require("path");
var cache = require("../../../cache");

var transmutations = {};

module.exports = {
    /**
     * 
     * @param {Transmutation} t 
     */
     registerTransmutation: function (t) {
        if (transmutations[t.id]) throw "Re-registered " + t.id;
        transmutations[t.id] = t;
    },
    /**
     * 
     * @param {string} id 
     * @returns {Transmutation}
     */
    get: function(id) {
        if (!transmutations[id]) throw "No such registered " + id;
        return transmutations[id];
    },
    expandTasks: function(s) {
        var tasks = cachedParseSpecExpandAliases(s);
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

function cachedParseSpecExpandAliases(sp) {
    var keys = Object.keys(transmutations);
    
    var t = {
        k: "aa-compiler-transmutation-cache",
        ver: keys,
        v: sp
    };
    
    var cVal = false;//cache.get(t, false);
    
    if(cVal === false) {
        cVal = parseSpecExpandAliases(sp);
        cache.save(t, cVal);
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
        var reqExpanded = cachedParseSpecExpandAliases(req);
        
        var intoIndex = insertInto.indexOf(findDepsIn[i]);
        
        for(var j = 0; j < reqExpanded.length; j++) {
            var x = reqExpanded[j].id;
            if(!insertInto.find(z=>z.id == x)) {
                insertInto.splice(intoIndex,0,{id:x,dep:true});
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
            var pFil = path.join(p, "index.js");
            if(fs.existsSync(pFil)) require(pFil);
            else loadTransmutations(p);
        }
    }
}

/**
 * @typedef {object} Transmutation
 * @property {string[]} requires
 * @property {string} id
 * @property {"output"|"transformation"|"input"|"information"|"check","codebase_postprocess"|"alias"} type
 * @property {TransmutationFunction} run
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
 * @property {object} fileFrontmatter
 */