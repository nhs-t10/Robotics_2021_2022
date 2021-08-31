var path = require("path");
module.exports = function (template, code, filename, variation) {
    var className = path.basename(filename, ".controls") + (variation ? "_" + variation : "") + "__controls";
    
    return template
        .replace("public class template", "public class " + className)
        .replace(/^[\s\n]*package [\w.]+;/, "package org.firstinspires.ftc.teamcode.__compiledcontrols;")
        .replace("/*VARIABLES*/", indent(code.VARIABLES, 1))
        .replace("/*GENERATED_JAVA*/", indent(code.GENERATED_JAVA, 2))
        .replace("/*TYPE_SETUP_CODE*/", indent(code.TYPE_SETUP_CODE, 2))
        .replace("/*HARDWARE_SETTING*/", indent(code.HARDWARE_SETTING, 2))
        .replace("/*INPUT_SETTING*/", indent(code.INPUT_SETTING, 2))
        .replace("/*BUILTINS*/", indent(code.BUILTINS, 2))
        .replace("/*RESETTING*/", indent(code.RESETTING, 2));
}

function indent(str, w) {
    w = w || 1;
    return str.split("\n").map(x=>("    ").repeat(w) + x).join("\n");
}