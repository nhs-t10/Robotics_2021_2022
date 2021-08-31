module.exports = function(ast, inputs, myName, genNonce, javaImports, methodSources, globals, genNonceGlobal, inits) {
    
    methodSources.push(`public ${inputs[0].width == 2 ? "float[]" : "float"} ${myName}() {
        return ${inputs[0].name}();
    }`);
}