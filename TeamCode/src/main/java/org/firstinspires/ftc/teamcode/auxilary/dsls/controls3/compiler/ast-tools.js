var methodSources = [], javaImports = [], methodSources = [], globals = [], inits = [];

//imports that everything needs
javaImports.push("org.firstinspires.ftc.teamcode.auxilary.dsls.controls3.runtime.Controls3SkipPathException");
javaImports.push("com.qualcomm.robotcore.eventloop.opmode.OpMode");
javaImports.push("com.qualcomm.robotcore.eventloop.opmode.TeleOp");

var nameGenerator;
module.exports = function (paths, filename) {
    nameGenerator = makeNonceGenerator();
    var pathSources = [];
    for (var i = 0; i < paths.length; i++) {
        pathSources.push(makeNodeJava(paths[i]));
    }
    return (`package org.firstinspires.ftc.teamcode.__compiledcontrols;
${Array.from(new Set(javaImports)).map(x=>"import " + x + ";").join("\n")}
@TeleOp
public class ${filename} extends OpMode {
${globals.map(x=>"    "+x).join("\n")}
    public void init() {
${inits.map(x=>"        "+x).join("\n")}
    }
    public void loop() {
${pathSources.map(x=>"        "+x).join("\n")};
    }
${methodSources.map(x=>"    " + x.replace(/(^|\n)\s*/g, " ")).join("\n")}
}`);
}

function makeNodeJava(node, name) {
    var type = node.source.split("(")[0] || "math";
    var typeProcessor = require("./nodes/" + type);
    
    if(!name) name = nameGenerator();
    var namedInputs = node.in.map(x=>({ direction: x.direction, name: nameGenerator(), node: x.node }));
    
    var nodeSources = [];
    for(var i = 0; i < namedInputs.length; i++) {
        nodeSources.push(makeNodeJava(namedInputs[i].node, namedInputs[i].name));
    }
    
    return typeProcessor(node.ast, namedInputs, name, makeNonceGenerator(), javaImports, methodSources, globals, nameGenerator, inits);
}

function makeNonceGenerator() {
    var i = 0;
    return function genNonce() {
        var id = i;

        //ensure that it starts with lowercase
        var r = ((id % 26) + 10).toString(36);
        id = Math.floor(id / 26);

        while (id != 0) {
            r += base64Digit(id % 64);
            id = Math.floor(id / 64);
        }

        //screen for banned words. If one's found, regenerate
        var javaKeywords = ["abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "continue", "default", "do", "double", "else", "enum", "extends", "final", "finally", "float", "for", "if", "implements", "import", "instanceof", "int", "interface", "long", "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "try", "void", "volatile", "while", "const", "goto"];
        var usedVariables = Object.keys(globals);
        if (javaKeywords.includes(r) || usedVariables.includes(r)) return genNonce();

        i++;

        return r;
    }
}


function base64Digit(i) {
    if(i == 0) return "$";
    i -= 1;
    
    if(i < 10) return "" + i;
    i -= 10;
    
    if(i == 0) return "_";
    i -= 1;
    
    if(i < 26) return (i + 10).toString(36).toUpperCase();
    i -= 26;
    
    if(i < 26) return (i + 10).toString(36).toLowerCase();
    
    //if it's greater or equal to 64, it'll get here
    throw i + ">= 64";
}