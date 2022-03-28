//Lets the compiler use the functionloader's cache to figure out where it should call functions from.
//For example, it needs to know that `omniDrive()` is in the `MovementManager`
var fs = require("fs");
var cache = require("../../../../cache");

var functionLoaderCache = cache.get(require("../../../functionloader/config").CACHE_KEY, {});
var cachedManagers = Object.values(functionLoaderCache).filter(x=>typeof x === "object");
var invertedMapByFunctionName = {};
cachedManagers.forEach(x=>
    x.data.methods.forEach(y => 
        invertedMapByFunctionName[y.shimClassFunction.nameToUseInAutoauto] = y.originalSourceClass)
);

var templateText = fs.readFileSync(__dirname + "/../../transmutations/process-template/template.notjava").toString();
var managerVarDeclarations = Array.from(templateText.matchAll(/^\s*(\w+)\s+(\w+);\s*$/gm)).map(x=>[x[1], x[2]]);
var managerVariableNames = Object.fromEntries(managerVarDeclarations);

module.exports = {
    managerName: function(functionName) {
        if(functionName == "log") return "FeatureManager.logger";

        var managerFullClassName = invertedMapByFunctionName[functionName] + "";

        var managerSimpleClassName = managerFullClassName.substring(managerFullClassName.lastIndexOf(".") + 1);

        return managerVariableNames[managerSimpleClassName];
    }
}