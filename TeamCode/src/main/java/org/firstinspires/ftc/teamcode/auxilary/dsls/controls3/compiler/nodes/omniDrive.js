module.exports = function(ast, inputs, myName, genNonce, javaImports, methodSources, globals, genNonceGlobal, inits) {
    
    if(inputs.length != 3) throw "Not enough inputs to `omniDrive()` -- must be at least 3";
    
    javaImports.push("org.firstinspires.ftc.teamcode.auxilary.PaulMath");
    
    methodSources.push(`public float[] ${myName}() {
        return PaulMath.omniCalc(${inputs[0].name}(),${inputs[1].name}(), ${inputs[2].name}());
    }`);
    
    return myName + "()";
}