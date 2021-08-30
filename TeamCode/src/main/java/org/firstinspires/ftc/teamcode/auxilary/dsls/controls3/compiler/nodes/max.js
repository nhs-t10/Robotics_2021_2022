module.exports = function(ast, inputs, myName, genNonce, javaImports, methodSources, globals) {
    
    methodSources.push(`public float ${myName}() {
        return ${mathMaxify(binaryLayout(inputs.map(x=>x.name)))};
    }`);
    
    return myName + "()";
}

function mathMaxify(tree) {
    if(tree.length == 1) return tree[0] + "()";
    else return "Math.max(" + tree.map(x=>mathMaxify(x)).join(",") +  ")";
}

function binaryLayout(names) {
    if(names.length == 1) return names[0];
    
    var firstHalf = names.slice(0, names.length / 2);
    var secondHalf = names.slice(names.length / 2);
    
    return [binaryLayout(firstHalf), binaryLayout(secondHalf)];
}