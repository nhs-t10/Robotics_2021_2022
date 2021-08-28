var sortByTrustworthiness = require("./trustworthiness-sorter.js");
var statementToJava = require("./statement-to-java.js");

var identifiers = {}, lastSubject = "", idCount = 0;

module.exports = function(ast) {
    lastSubject = "";
    identifiers = {};
    idCount = 0;
    
    addBuiltins(ast);
    
    loadAllIdentifiers(ast);
    
    var identifierEntries = Object.entries(identifiers);
    
    findAndAssignTypes(identifierEntries);
    
    return {
        GENERATED_JAVA: buildLoopCode(identifierEntries),
        VARIABLES: buildVariableInits(identifierEntries),
        TYPE_SETUP_CODE: makeTypeSetupCode(identifierEntries),
        HARDWARE_SETTING: makeHardwareSetting(identifierEntries),
        INPUT_SETTING: makeInputSetting(identifierEntries),
        RESETTING: makeResetting(identifierEntries),
        BUILTINS: `values[__timeSinceStartMs] = (float)(System.currentTimeMillis() - startTimeMs);`
    };
}

function makeResetting(identifierEntries) {
    return identifierEntries.filter(x=>x[1].typeType != "vector" && isNaN(x[0])).map(x=>`values[${x[0]}] = 0f;`).join("\n");
}

function makeInputSetting(identifierEntries) {
    var inputTypes = {"leftStickX":"float","leftStickY":"float","leftStickButton":"boolean","rightStickX":"float","rightStickY":"float","rightStickButton":"boolean","leftTrigger":"float","rightTrigger":"float","leftBumper":"boolean","rightBumper":"boolean","aButton":"boolean","bButton":"boolean","xButton":"boolean","yButton":"boolean","dpadLeft":"boolean","dpadRight":"boolean","dpadUp":"boolean","dpadDown":"boolean","gamepad2LeftStickX":"float","gamepad2LeftStickY":"float","gamepad2LeftStickButton":"boolean","gamepad2RightStickX":"float","gamepad2RightStickY":"float","gamepad2RightStickButton":"boolean","gamepad2LeftTrigger":"float","gamepad2RightTrigger":"float","gamepad2LeftBumper":"boolean","gamepad2RightBumper":"boolean","gamepad2AButton":"boolean","gamepad2BButton":"boolean","gamepad2XButton":"boolean","gamepad2YButton":"boolean","gamepad2DpadLeft":"boolean","gamepad2DpadRight":"boolean","gamepad2DpadUp":"boolean","gamepad2DpadDown":"boolean"};
    var inputNames = {"leftStickX":"gamepad1.left_stick_x","leftStickY":"gamepad1.left_stick_y","rightStickX":"gamepad1.right_stick_x","rightStickY":"gamepad1.right_stick_y","leftTrigger":"gamepad1.left_trigger","rightTrigger":"gamepad1.right_trigger","leftBumper":"gamepad1.left_bumper","rightBumper":"gamepad1.right_bumper","aButton":"gamepad1.a","bButton":"gamepad1.b","xButton":"gamepad1.x","yButton":"gamepad1.y","dpadLeft":"gamepad1.dpad_left","dpadRight":"gamepad1.dpad_right","dpadUp":"gamepad1.dpad_up","dpadDown":"gamepad1.dpad_down","gamepad2LeftStickX":"gamepad2.left_stick_x","gamepad2LeftStickY":"gamepad2.left_stick_y","leftStickButton":"gamepad2.left_stick_button","gamepad2RightStickX":"gamepad2.right_stick_x","gamepad2RightStickY":"gamepad2.right_stick_y","rightStickButton":"gamepad2.right_stick_button","gamepad2LeftTrigger":"gamepad2.left_trigger","gamepad2RightTrigger":"gamepad2.right_trigger","gamepad2LeftBumper":"gamepad2.left_bumper","gamepad2RightBumper":"gamepad2.right_bumper","gamepad2AButton":"gamepad2.a","gamepad2BButton":"gamepad2.b","gamepad2XButton":"gamepad2.x","gamepad2YButton":"gamepad2.y","gamepad2DpadLeft":"gamepad2.dpad_left","gamepad2DpadRight":"gamepad2.dpad_right","gamepad2DpadUp":"gamepad2.dpad_up","gamepad2DpadDown":"gamepad2.dpad_down"}
    var settingStatements = [];
    for(var i = 0; i < identifierEntries.length; i++) {
        var name = identifierEntries[i][0];
        if(inputTypes[name]) {
            //make a type-specific setter that will work out to a float either way
            var setter = "";
            if(inputTypes[name] == "float") setter = inputNames[name];
            else setter = `${inputNames[name]} ? 1f : 0f`;
            
            //set the value
            settingStatements.push(`values[${name}] = ${setter};\n`)
        }
    }
    return settingStatements.join("");
}

function findAndAssignTypes(identifierEntries) {
    var foundUnassigned = true;
    var typeTypes = {};
    var vectorLengths = {};
    while(foundUnassigned) {
        foundUnassigned = false;
        for(var i = 0; i < identifierEntries.length; i++) {
            var writers = identifierEntries[i][1].writers;
            var name = identifierEntries[i][0];
            
            //default is a scalar. there are NO built-in vectors.
            if(writers.length == 0) typeTypes[name] = "scalar";
            for(var j = 0; j < writers.length; j++) {
                if(writers[j].type == "ifStatement") {
                    processWriter(writers[j].statement);
                    processWriter(writers[j].otherwise);
                } else {
                    processWriter(writers[j]);
                }
            }
            
            var processWriter = function (writer) {
                if(writer.indirectObject.type == "vector") {
                    typeTypes[name] = "vector";
                    vectorLengths[name] = Math.max(writer.indirectObject.values.length, vectorLengths[name]||0);
                } else if(writer.indirectObject.type == "number") {
                    typeTypes[name] = "scalar";
                } else if(writer.indirectObject.type == "identifier" && typeTypes[writer.indirectObject.value]) {
                    typeTypes[name] = typeTypes[writer.indirectObject.value];
                } else {
                    foundUnassigned = true;
                }
            }
        }
    }
    for(var i = 0; i < identifierEntries.length; i++) {
        identifierEntries[i][1].typeType = typeTypes[identifierEntries[i][0]]
        
        if(vectorLengths[identifierEntries[i][0]]) identifierEntries[i][1].vectorLength = vectorLengths[identifierEntries[i][0]];
    }
}

function buildLoopCode(identifierEntries) {
    var result = "";
    for(var i = 0; i < identifierEntries.length; i++) {
        var identifierJava = "";
        
        var record = identifierEntries[i][1];
        
        var sortedWriters = sortByTrustworthiness(record.writers);
        
        for(var j = 0; j < sortedWriters.length; j++) {
            identifierJava += statementToJava(sortedWriters[j]) + "\n";
        }
        result += identifierJava + "\n";
    }
    return result.replace(/\n{2,}/g, "\n\n");
}

function buildVariableInits(identifierEntries) {
    var vectors = [];
    var vectorLengths = [];
    var scalars = [];
    
    for(var i = 0; i < identifierEntries.length; i++) {
        if(!isNaN(identifierEntries[i][0])) continue;
        
        if(identifierEntries[i][1].typeType == "vector") {
            vectors.push(identifierEntries[i][0]);
            vectorLengths.push(identifierEntries[i][1].vectorLength);
        }
        else if(identifierEntries[i][1].typeType == "scalar") {
            scalars.push(identifierEntries[i][0]);
        }
    }
    
    return `HardwareDevice[] hwdevices = new HardwareDevice[${scalars.length}];
final int ${scalars.map((x,i)=>`${x} = ${i}`).join(",")};
final int[] ${vectors.map((x,i)=>`${x} = new int[${vectorLengths[i]}]`).join(",")};
float[] values = new float[${scalars.length}];
String[] keys = new String[] {${scalars.map(x=>JSON.stringify(x)).join(",")}};`;
}

function makeTypeSetupCode(identifierEntries) {
    return `for(int i = 0; i < hwdevices.length; i++) {
        hwdevices[i] = hardwareMap.tryGet(DcMotor.class, keys[i]);
        if(hwdevices[i] == null) hwdevices[i] = hardwareMap.tryGet(Servo.class, keys[i]);
    }`;
}

function makeHardwareSetting(identifierEntries) {
    return `for(int i = 0; i < hwdevices.length; i++) {
   if(hwdevices[i] instanceof DcMotor) ((DcMotor) hwdevices[i]).setPower(values[i]);
   else if(hwdevices[i] instanceof Servo) ((Servo) hwdevices[i]).setPosition(values[i]);
}`;

} 

function loadAllIdentifiers(ast) {
    if(ast.type == "program") ast.statements.forEach(x=>loadAllIdentifiers(x));
    else if(ast.type == "doesStatement") loadIdentifiersFromDoesStatement(ast);
    else if(ast.type == "ifStatement") loadIdentifiersFromIfStatement(ast);
    else console.warn("WARNING: Attempt to load AST of unknown type " + ast.type);
}

function loadIdentifiersFromDoesStatement(doesStatement, conditionalUpon, conditionalAgainst) {
    //resolve pronouns like 'it' and 'itself' to their real values
    resolvePronouns(doesStatement);
    //load the properties, like the scale and the calculation
    loadProperties(doesStatement);
    
    //default the indirect object to the subject. This transforms something like `leftTrigger does flywheel` to `leftTrigger sets flywheel to leftTrigger`
    if(doesStatement.indirectObject == undefined) doesStatement.indirectObject = doesStatement.subject;
    
    //if the verb is 'is', transform its data to 'sets'. This'll make it much easier later
    if(doesStatement.verb == "is") {
        doesStatement.verb = "sets";
        doesStatement.indirectObject = doesStatement.directObject;
        doesStatement.directObject = doesStatement.subject;
        doesStatement.subject = {type: "identifier", value: "god"};
    }
    
    //the direct object always has a writer relationship
    registerWriter(doesStatement.directObject, conditionalAgainst || conditionalUpon || doesStatement);
    
    //if indirect object and subject are the same, only register one as a reader
    if(doesStatement.indirectObject.value != doesStatement.subject.value) {
        registerReader(doesStatement.indirectObject, doesStatement);
        registerReader(doesStatement.subject, doesStatement);
    } else {
        registerReader(doesStatement.indirectObject, doesStatement, false);
        registerReader(doesStatement.subject, doesStatement);
    }
    
}

function loadIdentifiersFromIfStatement(ifStatement) {
    resolvePronouns(ifStatement);
    loadProperties(ifStatement);
    
    registerReader(ifStatement.condition.left, ifStatement);
    if(ifStatement.condition.threshold) registerReader(ifStatement.condition.threshold, ifStatement);
    registerReader(ifStatement.condition.right, ifStatement);
    
    loadIdentifiersFromDoesStatement(ifStatement.statement, ifStatement);
    if(ifStatement.otherwise) loadIdentifiersFromDoesStatement(ifStatement.otherwise, null, ifStatement);
    
}

function loadProperties(statement) {
    if(statement.properties) {
        var props = Object.values(statement.properties);
        props.forEach(x=>registerReader(x, statement));
    }
}

function resolvePronouns(statement) {
    if(statement.type == "ifStatement") {
        if(statement.condition.left.value === "it") statement.condition.left.value = lastSubject;
        
        if(statement.condition.operator == "~#<") {
            if(statement.condition.threshold.value === "it") throw "Bad Pronoun Error -- 'it' isn't allowed here";
            if(statement.condition.threshold.value === "itself") statement.condition.threshold.value = statement.condition.left.value;
        }
        
        if(statement.condition.right.value === "it") throw "Bad Pronoun Error -- 'it' isn't allowed here";
        if(statement.condition.right.value === "itself") statement.condition.right.value = statement.condition.left.value;
        
        lastSubject = statement.condition.left.value;
    } else if (statement.type == "doesStatement") {
        if(statement.subject.value == "it") statement.subject.value = lastSubject;
        
        if(statement.directObject.value == "it") statement.directObject.value = lastSubject;
        if(statement.directObject.value == "itself") statement.directObject.value = statement.subject.value;
        
        if(statement.indirectObject) {
            if(statement.indirectObject.value == "it") statement.indirectObject.value = lastSubject;
            if(statement.indirectObject.value == "itself") statement.indirectObject.value = statement.subject.value;
        }
        
        lastSubject = statement.subject.value;
    }
} 

function pad(v, l) {
    v = v.toString();
    while(v.length < l) v = " " + v;
    return v;
}

function registerReader(variable, statement, dryRun) {    
    if(variable.type == "vector") {
        variable.values.forEach(x=>registerReader(x, statement));
    } else {
        var variableName = variable.value;
        if(identifiers[variableName] === undefined) {
            identifiers[variableName] = {
                id: idCount++,
                writers: [],
                readers: []
            };
        }
        if(!dryRun) identifiers[variableName].readers.push(statement);
        variable.__variableId = identifiers[variableName].id;
    }
}

function registerWriter(variable, statement) {
    verifyAllowedWriter(variable, statement);
    if(typeof variable === "undefined") return false;
    
    var variableName = variable.value;
    
    if(identifiers[variableName] === undefined) {
        identifiers[variableName] = {
            id: idCount++,
            writers: [],
            readers: []
        };
    }
    identifiers[variableName].writers.push(statement);
    variable.__variableId = identifiers[variableName].id;
}

function verifyAllowedWriter(variable, statement) {
    //if there's no location, then it's a system set. that's always allowed.
    if(!statement.location) return true;
    
    function aFitAbout(str) {
        return {
            message: "error: " + str,
            location: statement.location
        }
    }
    if(variable.type != "identifier") throw aFitAbout("Attempt to write a to constant or vector literal");
    
    var variableName = variable.value;
    
    if(variableName == "god") {
        var indirectObject = statement.indirectObject && statement.indirectObject.value;
        
        if(indirectObject == "dead") throw aFitAbout("Attempt to kill god. god is immortal. you can't kill god.");
        if(indirectObject == "nothing") throw aFitAbout("Attempt to tell people that god is fake. Shhhhhhhh, don't tELl anyone!");
        if(indirectObject == "real") throw aFitAbout("Attempt to tell people that god is real. Shhhhhhhh, don't tELl anyone!");
        if(indirectObject == 0) throw aFitAbout("zero to hero from disney's hercules is one of the less-good songs tbh");
        if(indirectObject == "god") throw aFitAbout("Found a wandering religious philosopher. get out of my code, religious philosopher!");
        
        throw aFitAbout("Please don't bother god.");
    }
    if(variableName == "real" || variableName == "reality") {
        throw aFitAbout("Stop trying to change reality");
    }
    if(variableName == "dozen") {
        if(indirectObject == 13) throw aFitAbout("What are you, a baker? No. Stoooppp.");
        if(indirectObject == "hundred") throw aFitAbout("well now that's just wrong");
        throw aFitAbout("Attempt to redefine 'dozen'");
    }
    if(variableName == "time") {
        throw aFitAbout("Attempt to interfere with the timestream.");
    }
    if(variableName == "hundred") {
        throw aFitAbout("Attempt to redefine 'hundred'");
    }
    if(variableName == "dead") {
        throw aFitAbout("necromancy alert!");
    }
    if(variableName == "nothing") {
        var indirectObject = statement.indirectObject && statement.indirectObject.value;
        
        if(indirectObject == "real") throw aFitAbout("Attempt to be nihilist.");
        if(indirectObject == "god") throw aFitAbout("Attempt to be athiest.");
        if(indirectObject == "nothing") throw aFitAbout("wh- i mean- i guess?? but still, don't do that");
        
        throw aFitAbout("Attempt to make nothing something");
    }
    
}

function addBuiltins(program) {
    var builtins = {
        real: 1,
        dead: 0,
        nothing: 0,
        zero: 0,
        zilch: 0,
        hundred: 100,
        dozen: 12,
        god: 1,
        pressed: 1,
        time: "__timeSinceStartMs"
    }
    var builtinsEntries = Object.entries(builtins);
    
    for(var i = 0; i < builtinsEntries.length; i++) {
        program.statements.splice(0, 0, {
            "type": "doesStatement",
            "subject": {
               "type": "number",
               "value": 1
            },
            "directObject": {
               "type": "identifier",
               "value": builtinsEntries[i][0]
            },
            "verb": "set",
            "indirectObject": {
               "type": typeof builtinsEntries[i][1] == "number" ? "number" : "identifier",
               "value": builtinsEntries[i][1]
            },
            "properties": {
               "priority": "promise"
            }
         });   
    }
}