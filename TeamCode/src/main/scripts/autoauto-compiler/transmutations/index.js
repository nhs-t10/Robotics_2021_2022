var fs = require("fs");
var cache = require("../../cache");

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

loadTransmutations();

function idsToTras(ids) {
    return ids.map(x=>transmutations[x]);
}

function cachedParseSpecExpandAliases(sp) {
    var keys = Object.keys(transmutations);
    
    var t = {
        k: "aa-compiler-transmutation-cache",
        ver: keys,
        v: sp
    };
    
    var cVal = cache.get(t, false);
    
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

function insertDeps(tras, l) {
    if(l === undefined) l = tras.length;
    
    var totalAdded = 0;
    
    for(var i = 0; i < l; i++) {
        var req = transmutations[tras[i]].requires;
        var reqExpanded = cachedParseSpecExpandAliases(req);
        
        reqExpanded.forEach(x=>{
            if(!tras.includes(x)) {
                tras.splice(0,0,x);
                totalAdded++;
            }
        });
    }
    
    if(totalAdded > 0) insertDeps(tras, totalAdded);
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
        else return e.id;
    }).flat(2);
}

function loadTransmutations() {
    var dir = fs.readdirSync(__dirname, { withFileTypes: true });
    for(var i = 0; i < dir.length; i++) {
        if(dir[i].isDirectory()) {
            require("./" + dir[i].name);
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