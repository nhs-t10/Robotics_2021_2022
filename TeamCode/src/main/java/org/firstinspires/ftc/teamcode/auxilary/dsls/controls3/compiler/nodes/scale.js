module.exports = function(ast, inputs, myName, genNonce, javaImports, methodSources, globals, genNonceGlobal, inits) {
    
    javaImports.push("org.firstinspires.ftc.teamcode.auxilary.PaulMath");
    
    if(inputs[0].width == 1) {
        methodSources.push(`public float ${myName}() {
            return ${inputs[0].name}() * ${ast.args.args[0].value};
        }`);
    } else {
    var tempnonce = genNonce();
    var input = genNonce();
     methodSources.push(`public float[] ${myName}() {
          float[] ${input} = ${inputs[0].name}();
          float[] ${tempnonce} = new float[${input}.length];
          for(int i = 0; i < ${tempnonce}.length; i++) ${tempnonce}[i] = ${input}[i] * (float)(${ast.args.args[0].value});
            return ${tempnonce};
        }`);
    }
    
    return myName + "()";
}