var path = require("path");
var fs = require("fs");

var directory = __dirname.split(path.sep);
var rootDirectory = directory.slice(0, directory.indexOf("TeamCode")).join(path.sep);
var robotFunctionsDirectory = path.join(rootDirectory, "TeamCode/src/main/java/org/firstinspires/ftc/teamcode/auxilary/dsls/autoauto/runtime/robotfunctions");

var parser = require("./javaparser/parser.js");
var astTools = require("./javaparser/ast-tools.js");

var processTemplate = require("./template-render.js");

module.exports = function(javaSource, preexistingNames) {
    try {
        var ast = parser.parse(javaSource);
    } catch(e) {
        var classDeclarationIndex = javaSource.indexOf("public class");
        var classDeclarationIndexEnd = javaSource.indexOf("\n", classDeclarationIndex)
        throw "Error parsing java source code! Make sure that it's correct: " + javaSource.substring(classDeclarationIndex,classDeclarationIndexEnd);
    }

    var primaryType = ast.types.find(x=>x.modifiers.find(y=>y.keyword == "public"));
    if(!primaryType) throw "No public type in manager!";
    
    var primaryTypeName = primaryType.name.identifier;
    var packageName = astTools.astToString(ast.package.name);
    var fullClassName = packageName + "." + primaryTypeName;
    
    
    var methods = findShimableMethods(primaryType);
    var overloads = groupMethodsIntoOverloads(methods);
    
    return [fullClassName, overloads.map(x=>generateRobotFunction(x, fullClassName, preexistingNames))];
}

function generateRobotFunction(overload, definedClass, preexistingNames) {
    var name = overload[0];
    
    var noConflictName = name;
    while(preexistingNames.includes(noConflictName)) noConflictName += definedClass.split(".").slice(-1)[0];
    
    var typemaps = overload[1];
    
    
    var callMethodSource = "";
    
    var processedOverloads = 0;
    
    for(var i = 0; ; i++) {
        var overloadsWithNArgs = typemaps.filter(x=>x.args.length == i);
        //exit if we've done all of them.
        if(overloadsWithNArgs.length == 0) {
            if(processedOverloads >= typemaps.length) {
                break;
            } else {
                continue;
            }
        }
        processedOverloads += overloadsWithNArgs.length;
        
        callMethodSource += `if(args.length == ${i}) {`;
        
        for(var j = 0; j < overloadsWithNArgs.length; j++) {
            var overload = overloadsWithNArgs[j];
            
            //process each arg & add it to the pile of `&&` clauses
            
            if(overload.args.length) {
             callMethodSource += `if(`;
             callMethodSource += overload.args.map((x,k)=>`args[${k}] instanceof ${equivAutoautoClass(x)}`).join("&&");
             callMethodSource += ") {";
            }
                if(overload.type != "void") callMethodSource += `return new ${equivAutoautoClass(overload.type)}(`;
                callMethodSource += `manager.${name}(`;
                    callMethodSource += overload.args.map((x,k)=>`${caster(x)}((${equivAutoautoClass(x)})args[${k}]).${rawValueGetter(x)}`).join(",")
                if(overload.type != "void") callMethodSource += `));`;
                else callMethodSource += "); return new AutoautoUndefined();";
            if(overload.args.length) callMethodSource += "}";
        }

        callMethodSource += `}`;
    }
    callMethodSource += `throw new org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNoNativeMethodOverloadException("No ${noConflictName} with ${overload.args.length} args");`;

    noConflictName = replaceNumbers(noConflictName);

    var classname = noConflictName.charAt(0).toUpperCase() + noConflictName.substring(1) + "Function";
        
    var template = processTemplate(callMethodSource, definedClass, classname);
    
//    fs.writeFileSync(path.join(robotFunctionsDirectory, classname + ".java"), template);



    var ourPath = path.join(robotFunctionsDirectory, classname + ".java");

     fs.mkdirSync(path.dirname(ourPath), { recursive: true});

     fs.writeFileSync(ourPath, template);





    return [noConflictName, classname];
}

function replaceNumbers(str) {
    return str.replace(/(\d)(\d)/g, "$1ty$2")
        .replace(/0/g, "Zero")
        .replace(/1/g, "One")
        .replace(/2/g, "Two")
        .replace(/3/g, "Three")
        .replace(/4/g, "Four")
        .replace(/5/g, "Five")
        .replace(/6/g, "Six")
        .replace(/7/g, "Seven")
        .replace(/8/g, "Eight")
        .replace(/9/g, "Nine");
}

function caster(type) {
    return  ({
        "byte": "(byte)",
        "short": "(short)",
        "int": "(int)",
        "long": "(long)",
        "float": "",
        "double": "(double)",
        "boolean": "",
        "char": "",
        "String": ""
    })[type] || "";
}

function rawValueGetter(type) {
    return  ({
        "byte": "getFloat()",
        "short": "getFloat()",
        "int": "getFloat()",
        "long": "getFloat()",
        "float": "getFloat()",
        "double": "getFloat()",
        "boolean": "getBoolean()",
        "char": "getString().charAt(0)",
        "String": "getString()"
    })[type] || "getString()";
}

function equivAutoautoClass(type) {
    return ({
        "void": "AutoautoUndefined",
        "byte": "AutoautoNumericValue",
        "short": "AutoautoNumericValue",
        "int": "AutoautoNumericValue",
        "long": "AutoautoNumericValue",
        "float": "AutoautoNumericValue",
        "double": "AutoautoNumericValue",
        "boolean": "AutoautoBooleanValue",
        "char": "AutoautoString",
        "String": "AutoautoString"
    })[type] || "AutoautoUndefined";
}

function findShimableMethods(type) {
    var bodyStatements = type.bodyDeclarations.statements;
    var methods = bodyStatements.filter(x=>x.type == "MethodDeclaration");
    var publicMethods = methods.filter(x=>x.modifiers.find(y=>y.keyword == "public"));

    
    var shimableMethods = publicMethods.filter(x=>!x.constructor && doesMethodHaveShimableArgs(x) && isTypeShimable(x.returnType2));
    
    return shimableMethods;
}

function doesMethodHaveShimableArgs(method) {
    var shimableArgs = method.parameters.filter(x=>!x.varargs && isTypeShimable(x.typeType));
    return shimableArgs.length == method.parameters.length;
}

function isTypeShimable(returnType) {
    if(returnType.type == "PrimitiveType") return true;
    else if(returnType.type == "SimpleType") return isTypeShimable(returnType.name);
    else if(returnType.type == "SimpleName" &&  returnType.identifier == "String") return true;
    else return false;
} 

function getTypeCode(type) {
    return type.primitiveTypeCode || type.name.identifier;
}

function getBasicTypeInfo(method) {
    return {
        type: getTypeCode(method.returnType2),
        args: method.parameters.map(x=>getTypeCode(x.typeType))
    };
}

function groupMethodsIntoOverloads(methods) {
    var nameSet = Array.from(new Set(methods.map(x=>x.name.identifier)));
    
    var entries = [];
    for(var i = 0; i < nameSet.length; i++) {
        var name = nameSet[i];
        var overloads = methods.filter(x=>x.name.identifier == name);
        var overloadTypes = overloads.map(x=>getBasicTypeInfo(x));
        entries.push([name, overloadTypes]);
    }
    return entries;
}