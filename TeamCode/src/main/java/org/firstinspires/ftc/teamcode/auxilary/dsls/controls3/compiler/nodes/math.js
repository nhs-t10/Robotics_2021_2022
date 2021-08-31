module.exports = function (ast, inputs, myName, genNonce, javaImports, methodSources, globals, genNonceGlobal, inits) {

    if(inputs[0].width == 2) methodSources.push(`public float[] ${myName}() {
        ${inputs.map((x,i)=>`${x.width==2?"float[]":"float"} in${i} = ${inputs[0].name}();`)}
        return new float[] {${astToString(ast.args)}};
    }`);
    else if(inputs[0].width == 1) methodSources.push(`public float ${myName}() {
        ${inputs.map((x,i)=>`${x.width==2?"float[]":"float"} in${i} = ${inputs[0].name}();`)}
        return ${astToString(ast.args.args[0])};
    }`);

    function astToString(ast) {
        switch (ast.type) {
            case "OperatorExpression":
                return astToString(ast.left) + ast.operator + astToString(ast.right);
            case "FunctionCall":
                return astToString(ast.func) + "(" + astToString(ast.args) + ")";
            case "AccessOperator":
                if(ast.right.type == "NumericLiteral") return astToString(ast.left) + "[" + astToString(ast.right) + "]";
                else return astToString(ast.left) + "." + astToString(ast.right);
            case "CallArguments":
                return ast.args.map(x=>astToString(x)).join(",");
            case "StringLiteral":
                return ast.value;
            case "MultilineStringLiteral":
                return JSON.stringify(ast.value);
            case "Identifier":
                if(ast.value == "in") return "in0";
                else return ast.value;
            case "NumericLiteral":
                return ast.value;

        }
    }
}