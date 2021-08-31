module.exports = function(ast, inputs, myName, genNonce, javaImports, methodSources, globals, genNonceGlobal, inits) {

    javaImports.push("org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager");
    javaImports.push("org.firstinspires.ftc.teamcode.managers.FeatureManager");

    inits.push("FeatureManager.setIsOpModeRunning(true);");
    inits.push("telemetry = new TelemetryManager(telemetry);");
    inits.push("((TelemetryManager)telemetry).setGamepads(gamepad1, gamepad2);");
    inits.push("FeatureManager.logger.setBackend(telemetry.log());");

    methodSources.push("public void stop() { FeatureManager.setIsOpModeRunning(false); }");

    var floatyMethods = methodSources.filter(x=>x.substring(x.indexOf("*/") + 3).startsWith("public float"));

    if(floatyMethods.find(x=>x.substring(x.indexOf("*/") + 3).startsWith("public float[]"))) javaImports.push("java.util.Arrays");

    var logCalls = floatyMethods.map(x=>{
        var name = /public \w+(?:\[\])? (\w+)/.exec(x)[1];
        var source = x.substring(x.indexOf("/*") + 2, x.indexOf("*/"));

        var unwrappedTelCall = "";
        var telCallNonce = genNonce();
        if(x.substring(x.indexOf("*/") + 3).startsWith("public float[]")) unwrappedTelCall = `
        float[] ${telCallNonce} = ${name}();
        for(int i = 0; i < ${telCallNonce}.length; i++) telemetry.addData("(" + ${JSON.stringify(source || name)} + ")[" + i + "]", ${telCallNonce}[i]);`
        else unwrappedTelCall = `telemetry.addData(${JSON.stringify(source || name)}, ${name}());`

        if(source.startsWith("if(")) {
            return "try { " + unwrappedTelCall + "} catch(Controls3SkipPathException ignored) {}"
        } else {
            return unwrappedTelCall;
        }
    });


    methodSources.push(`public void ${myName}() { ${logCalls.join("")} telemetry.update(); }`);

}