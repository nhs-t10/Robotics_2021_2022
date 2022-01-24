var globalThis = typeof global === "object" ? global : self;


var wkc = Object.entries(globalThis).filter(x=>typeof x[1] === "function" && x[1].prototype != Function );

var constructors = Object.fromEntries(wkc);

module.exports = {
    getName: function(obj) {
        if(obj === undefined) return undefined;
        if(typeof obj.constructor !== "function") return undefined;
        if(!constructors[obj.constructor.name]) return undefined;
        return obj.constructor.name;
    },
    byName: function(name) {
        return constructors[name];
    }
}

