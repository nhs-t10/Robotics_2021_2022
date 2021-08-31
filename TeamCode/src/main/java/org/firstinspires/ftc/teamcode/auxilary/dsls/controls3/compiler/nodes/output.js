module.exports = function(ast, inputs, myName, genNonce, javaImports, methodSources, globals, genNonceGlobal, inits, globalInitHelper) {
    
    var loadedArgs = ast.args.args
        .map(x=>loadArg(x, genNonceGlobal, javaImports, globalInitHelper));
    
    var resultName = genNonce();
    
    var setterBody = "";

    if(loadedArgs.length == 0) setterBody = inputs.map(x=>x.name+"();").join("");
    else if(inputs[0].width == 1) setterBody = loadedArgs.map(x=>`${x.outputName}.${x.setterMethod}(${inputs[0].name}());`).join("\n")
    else if(inputs[0].width == 2) setterBody = `float[] ${resultName} = ${inputs[0].name}();` +
        loadedArgs.map((x,i)=>`${x.outputName}.${x.setterMethod}(${resultName}[${i}]);`).join("\n");

    if(inputs[0].node.source.startsWith("if(")) methodSources.push(`public void ${myName}() {
        try {
            ${setterBody}
        } catch (Controls3SkipPathException ignored) { }
    }`);
    else methodSources.push(`public void ${myName}() {
        ${setterBody}
    }`);
    
    return myName + "()";
}

function loadArg(arg, genNonceGlobal, javaImports, globalInitHelper) {
    var outputType = "motor";
    var id;
    if(arg.type == "Identifier" || arg.type == "NumericLiteral") {
        id = arg.value;
    } else if(arg.type == "AccessOperator") {
        outputType = pluralToSingular(arg.left.value);
        id = arg.right.value;
    }
    
    var setterMethod = getSetterMethod(outputType);

    var outputClass = outputTypeToClass(outputType);
    var outputName = globalInitHelper(outputClass, `hardwareMap.get(${outputClass}.class, "${id}")`);
    
    javaImports.push("com.qualcomm.robotcore.hardware." + outputClass);

    return {
        outputType: outputType,
        setterMethod: setterMethod,
        id: id,
        outputName: outputName
    }
}

function getSetterMethod(type) {
    return ({
        "motor": "setPower",
        "crservo": "setPower",
        "servo": "setPosition",
    })[type];
}

function outputTypeToClass(type) {
    if(type == "motor") return "DcMotor";
    else if(type == "crservo") return "CRServo";
    else if(type == "servo") return "Servo";
}

function pluralToSingular(type) {
    return type.toLowerCase().replace(/s$/, "");
}