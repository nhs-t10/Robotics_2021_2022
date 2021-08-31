module.exports = function(ast, inputs, myName, genNonce, javaImports, methodSources, globals, genNonceGlobal, inits) {

    if(inputs[0].direction == "top") inputs = inputs.reverse();

    methodSources.push(`public ${inputs[0].width == 1 ? "float" : "float[]"} ${myName}() throws Controls3SkipPathException {
        if(${inputs[1].name}() != 0) return ${inputs[0].name}();
        else throw new Controls3SkipPathException();
    }`);
}