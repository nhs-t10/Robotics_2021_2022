const visualiseTree = require("./visualise-tree");

var generalNumberToIncrementWhenSomethingNeedsToBeChanged = 0;
var stringDefinitions = {};
var locationSetters = [];
var depthMappedDefinitions = [];
var variables = [];

function initFileState() {
    generalNumberToIncrementWhenSomethingNeedsToBeChanged = 0;
    stringDefinitions = {};
    locationSetters = [];
    depthMappedDefinitions = [];
    variables = [];
}

var COLUMN_THRESHOLD = 120;
var JAVA_TYPE_REGEX = /^\w+(<(\w+)( *,(\w+))+>)?(\[\])?/;
var STATIC_CONSTRUCTOR_SHORTNAMES = {
    __do_not_use_reserved_for_location: "L",
    AutoautoProgram: "P",
    AutoautoRuntime: "R",
    Statepath: "S",
    "State[]": "S",
    State: "A",
    "Statement[]": "A",
    NextStatement: "N",
    FunctionCallStatement: "F",
    AfterStatement: "W",
    FunctionCall: "M",
    ArithmeticValue: "O",
    AutoautoNumericValue: "C",
    AutoautoString: "U",
    AutoautoTable: "K",
    AutoautoUnitValue: "E",
    GotoStatement: "G",
    LetStatement: "D",
    IfStatement: "I",
    BooleanOperator: "T",
    VariableReference: "H",
    AutoautoBooleanValue: "B",
    FunctionDefStatement: "J",
    TitledArgument: "V",
    
    "AutoautoValue[]" : "%"
};

var PRIMITIVENESS_OF_TYPE = {
     AutoautoUnitValue: 3,
     AutoautoBooleanValue: 2.9,
     AutoautoNumericValue: 2.8,
     AutoautoString: 2.7,
     VariableReference: 2.6,

     AutoautoArray: 2,
     BooleanOperator: 2,
     ArithmeticValue: 2,
     FunctionCall: 2,
     "AutoautoValue[]": 2,

    NextStatement: 1.9,
    LetStatement: 1.8,
    FunctionCallStatement: 1.7,
    GotoStatement: 1.6,
    AfterStatement: 1.5,
    IfStatement: 1.4,

    "Statement[]": 1.3,
    State: 1,

    "State[]": 0.9,
    Statepath: 0,

    AutoautoRuntime: -1
};

module.exports = function astToString(ast, programNonce, statepath, stateNumber, depth) {
    if (programNonce === undefined) programNonce = genNonce();
    if(depth === undefined) depth = 0;

    var nonce = genNonce();

    function process(child, newStateNumber) {
        var r = astToString(child,
            programNonce,
            child.label ? child.label.value : statepath,
            newStateNumber || stateNumber,
            depth + 1
        );

        return r;
    }

    var result = undefined;

    switch (ast.type) {
        case "FrontMatter":
            result = "";
            break;
        case "Program":
            var childDefs = ast.statepaths.map(x => process(x));
            
            var programName = genNonce();

            var sortedDefinitions = [];

            function addSortDefs(def) {
                if(def.processed) return;
                def.processed = true;

                def.depends.forEach(y=> {
                    var d = depthMappedDefinitions.find(z=>z.self==y);
                    console.log(d, y);
                    if(!d) console.log(def);
                    addSortDefs(d);
                });
                if(def.definition) sortedDefinitions.push(def);
            }

            var childDefsDefinitions = childDefs.map(x=>depthMappedDefinitions.filter(y=>y.self == x.varname)).flat();
            childDefsDefinitions.forEach(x=>addSortDefs(x));

             //split by semicolons, make each statement into its own depth mapped definition
            var typedDefinitions = sortedDefinitions
            .map(x => ({
                self: x.self,
                depends: x.depends,
                definition: x.definition.trim(),
                type: JAVA_TYPE_REGEX.exec(x.definition.trim())[0] //discern and record the type
            }))
            .sort((a,b)=> {
                if(a.depends.includes(b.self)) return 1;
                else if(b.depends.includes(a.self)) return -1;
                else return a.type.localeCompare(b.type);
            })
            .map((x,i,a) => { //if the type is the same as the previous, join the declarations with commas instead of semicolons
                if(a[i-1] && a[i-1].type == x.type) x.definition = x.definition.replace(x.type, "").replace(/new \w+\[\]/, "");
                if(a[i+1] && a[i+1].type == x.type) x.definition = x.definition.replace(/;$/, ",");
                return x;
            })
            .map(x=>x.definition) //remove type data
            .join("") //join into one string
            .replace(/ ?(\W) ?/g, "$1") //remove extra whitespace
                
            var creation = `
            ${typedDefinitions}
            HashMap<String, Statepath> ${nonce} = new HashMap<String, Statepath>();
            ${childDefs.map((x, i) => `${nonce}.put(${stringDefinitions[ast.statepaths[i].label.value]}, ${x.varname});`).join("\n")}
            AutoautoProgram ${programName} = ${STATIC_CONSTRUCTOR_SHORTNAMES.AutoautoProgram}(${nonce}, ${stringDefinitions[ast.statepaths[0].label.value]});
            ${locationSetters.join("")}`;

            result = "";
            result += strings + "\n\n";
            var creationStatements = (creation).split(",");
            var line = "";
            for(var i = 0; i < creationStatements.length; i++) {
                creationStatements[i] = creationStatements[i].split(";").map(x=>x.trim()).join(";");

                if(creationStatements[i] != "") line += creationStatements[i];
                if(i + 1 < creationStatements.length) line += ",";

                if(line.length >= COLUMN_THRESHOLD) {
                    result += line + "\n";
                    line = "";
                }
            }
            result += line;

            result += `return ${programName};`

            var simpleProgramJsonSet

            var jsonProgram = JSON.stringify(ast);

            var jsonSettingCode = "";

            for(var i = 0; i < jsonProgram.length; i += 32768) {
                jsonSettingCode += "programJson.append(" + JSON.stringify(jsonProgram.substring(i, i + 32768)) + ");\n";
            }

            var simpleProgram = Object.fromEntries(ast.statepaths.map(x=>[x.label.value, x.statepath.states.length]));
            var simpleProgramJson = JSON.stringify(simpleProgram);

            jsonSettingCode += "simpleProgramJson = " + JSON.stringify(simpleProgramJson) + ";\n";

            result = {
                genCode: result,
                jsonSettingCode: jsonSettingCode
            }

            initFileState();
            break;
        case "LabeledStatepath":
            var childDefs = ast.statepath.states.map((x,i) => process(x, i));

            var label = process(ast.label);

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [label.varname, "$" + nonce],
                definition: `Statepath ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.Statepath}($${nonce}, ${label.varname});`
            });
            depthMappedDefinitions.push({
                depth: depth + 1,
                self: "$" + nonce,
                depends: childDefs.map(x => x.varname),
                definition: `State[] $${nonce} = new State[] { ${childDefs.map(x => x.varname).join(",")} };`
            });

            result = {
                varname: nonce,
                depends: childDefs.map(x => x.varname).concat([label.varname]),
                noLocation: true
            };
            break;
        case "Statepath":
            //covered by LabeledStatepath. Shouldn't be reached, ever.s
            throw "ILLEGAL STATE BAD TIME";
            break;
        case "State":
            var childDefs = ast.statement.map(x => process(x));

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: ["$" + nonce],
                definition: `State ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.State}($${nonce});`
            });
            
            depthMappedDefinitions.push({
                depth: depth + 1,
                self: "$" + nonce,
                depends: childDefs.map(x=>x.varname),
                definition: `Statement[] $${nonce} = new Statement[] { ${childDefs.map(x => x.varname).join(",")} };`
            });

            result = {
                varname: nonce
            };
            break;
        case "NextStatement":

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [],
                definition: `NextStatement ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.NextStatement}();`
            });

            result = {
                varname: nonce
            }
            break;
        case "FunctionDefStatement":
            var name = process(ast.name);
            var args = process(ast.args);
            var body = process(ast.body);

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [name.varname, args.varname, body.varname],
                definition: `FunctionDefStatement ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.FunctionDefStatement}(${name.varname}, ${args.varname}, ${body.varname});`
            });

            result = {
                varname: nonce
            };
            break;
        case "Block":
            var b = process(ast.state).varname;
            result = {
                varname: b,
                depends: [b],
                noLocation: true
            };
            break;
        case "DynamicValue":
            var b = process(ast.value).varname;
            result = {
                varname: b,
                depends: [b],
                noLocation: true
            };
            break;
        case "FunctionCallStatement":
            var call = process(ast.call);

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [call.varname],
                definition: `FunctionCallStatement ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.FunctionCallStatement}(${call.varname});`
            });

            result = {
                varname: nonce
            }
            break;
        case "AfterStatement":
            var unitValue = process(ast.unitValue);
            var statement = process(ast.statement);

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [unitValue.varname, statement.varname],
                definition: `AfterStatement ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.AfterStatement}(${unitValue.varname}, ${statement.varname});`
            });

            result = {
                varname: nonce
            }
            break;
        case "FunctionCall":
            var func = process(ast.func);
            var args = process(ast.args);

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [func.varname, args.varname],
                definition: `FunctionCall ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.FunctionCall}(${func.varname}, ${args.varname});`
            });

            result = {
                varname: nonce
            };
            break;
        case "Identifier":
            result = {
                varname: getStringNonce(ast.value, nonce, depth),
                noLocation: true,
            };
            break;
        case "ArgumentList":
            var childDefs = ast.args.map(x => process(x));
            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: childDefs.map(x => x.varname),
                definition: `AutoautoValue[] ${nonce} = {${childDefs.map(x => x.varname).join(", ")}};`
            });

            result = {
                varname: nonce,
                noLocation: true
            };
            break;
        case "TitledArgument":
            var value = process(ast.value);
            var name = process(ast.name);

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [value.varname, name.varname],
                definition: `TitledArgument ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.TitledArgument}(${name.varname}, ${value.varname});`
            })

            result = {
                varname: nonce
            }
            break;
        case "OperatorExpression":
            var left = process(ast.left);

            var operatorName;
            if(stringDefinitions[ast.operator]) {
                operatorName = stringDefinitions[ast.operator]
            } else {
                operatorName = stringDefinitions[ast.operator] = genNonce();
            }


            var right = process(ast.right);

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [left.varname, right.varname],
                definition: `ArithmeticValue ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.ArithmeticValue}(${left.varname}, ${operatorName}, ${right.varname});`
            });

            result = {
                varname: nonce
            };
            break;
        case "NumericValue":
            depthMappedDefinitions.push({
                depth: depth,
                depends: [],
                self: nonce,
                definition: `AutoautoNumericValue ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.AutoautoNumericValue}(${ast.v});`
            });
            result = {
                varname: nonce
            };
            break;
        case "StringLiteral":
            var strNonce = getStringNonce(ast.str, null, depth);

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [strNonce],
                definition: `AutoautoString ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.AutoautoString}(${strNonce});`
            });

            result = {
                varname: nonce
            }
            break;
        case "ArrayLiteral":
            var args = process(ast.elems);

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [args.varname],
                definition: `AutoautoTable ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.AutoautoTable}(${args.varname});`
            });

            result = {
                varname: nonce
            };
            break;
        case "UnitValue":
            var unit = process(ast.unit);

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [unit.varname],
                definition: `AutoautoUnitValue ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.AutoautoUnitValue}(${ast.value.v}, ${unit.varname});`
            });

            result = {
                varname: nonce
            };
            break;
        case "GotoStatement":
            var path = process(ast.path);

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [path.varname],
                definition: `GotoStatement ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.GotoStatement}(${path.varname});`
            });

            result = {
                varname: nonce
            }
            break;
        case "LetStatement":
            if(ast.variable.type == "VariableReference" && ast.variable.variable.value == "delete") {
                throw ("Attempt to use reserved keyword `delete` as a variable name");
            }
            var variable = process(ast.variable);
            var value = process(ast.value);

            variables.push(variable.varname);

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [variable.varname, value.varname],
                definition: `LetStatement ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.LetStatement}(${variable.varname}, ${value.varname});`
            });

            result = {
                varname: nonce
            };
            break;
        case "IfStatement":
            var conditional = process(ast.conditional);
            var statement = process(ast.statement);

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [conditional.varname, statement.varname],
                definition: `IfStatement ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.IfStatement}(${conditional.varname}, ${statement.varname});`
            });

            result = {
                varname: nonce
            }
            break;
        case "ComparisonOperator":
            var left = process(ast.left);
            var right = process(ast.right);

            var operatorName;
            if(stringDefinitions[ast.operator]) {
                operatorName = stringDefinitions[ast.operator]
            } else {
                operatorName = stringDefinitions[ast.operator] = genNonce();
            }

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [left.varname, right.varname],
                definition: `BooleanOperator ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.BooleanOperator}(${left.varname}, ${right.varname}, ${operatorName});`
            });

            result = {
                varname: nonce
            }
            break;
        case "VariableReference":
            var variable = process(ast.variable);

            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [variable.varname],
                definition: `VariableReference ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.VariableReference}(${variable.varname});`
            });

            result = {
                varname: nonce,
            };
            break;
        case "BooleanLiteral":
            depthMappedDefinitions.push({
                depth: depth,
                self: nonce,
                depends: [],
                definition: `AutoautoBooleanValue ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.AutoautoBooleanValue}(${ast.value});`
            });

            result = {
                varname: nonce
            };
            break;
        default:
            console.error("Unknown type " + ast.type);
            console.error(ast);
    }

    if(!ast.location) {
        console.error("Type "+  ast.type + " doesn't have location");
        return result;
    }
    
    //if the statepath hasn't been added to the strings, add it too!
    var statepathNonce = genNonce();
    if(statepath && !stringDefinitions[statepath]) {
        stringDefinitions[statepath] = statepathNonce;
    } else {
        statepathNonce = stringDefinitions[statepath]
    }
    
    if (typeof result == "object" && result.definitions != "" && !result.noLocation && statepathNonce) {
        locationSetters.push(makeLocationSetter(nonce, statepathNonce, stateNumber, ast.location.start.line, ast.location.start.column));
    }
    return result;
}

var lastLocationSetterInfo = [];
function makeLocationSetter(nonce, statepathNonce, stateNumber, line, column) {
    var data = [statepathNonce, stateNumber || 0 , line, column];
    var similarityIndex = 0;
    for(var i = 0; i < data.length; i++) {
        if(lastLocationSetterInfo[i] == data[i]) similarityIndex++;
        else break;
    }
    var newData = data.slice(similarityIndex);

    lastLocationSetterInfo = data;

    return `sL(${nonce},L(` + newData.join(",") + "));";
}

function getStringNonce(str, nonce, depth) {
    //first of everything, if the str exists already, just return that nonce.
    if(stringDefinitions[str]) return stringDefinitions[str];
    
    if(!nonce) nonce = genNonce();
    
    stringDefinitions[str] = nonce;
    
    depthMappedDefinitions.push({
        depth: depth + 1,
        self: nonce,
        depends: [],
        definition: `String ${nonce} = ${JSON.stringify(str)};`
    });
    
}

function genNonce() {
    generalNumberToIncrementWhenSomethingNeedsToBeChanged++;
    
    var id = generalNumberToIncrementWhenSomethingNeedsToBeChanged;
    
    //ensure that it starts with lowercase
    var r = ((id % 26) + 10).toString(36);
    id = Math.floor(id / 26);
    
    while(id != 0) {
        r += base64Digit(id % 64);
        id = Math.floor(id / 64);
    }
    
    //screen for banned words. If one's found, regenerate
    var javaKeywords = ["abstract","assert","boolean","break","byte","case","catch","char","class","continue","default","do","double","else","enum","extends","final","finally","float","for","if","implements","import","instanceof","int","interface","long","native","new","null","package","private","protected","public","return","short","static","strictfp","super","switch","synchronized","this","throw","throws","transient","try","void","volatile","while","const","goto"];
    var usedVariables = ["driver", "limbs", "sense", "imu"]
    if(javaKeywords.includes(r) || usedVariables.includes(r)) return genNonce();

    return r;
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

function indent(str) {
    var lines = (str + "").split("\n");
    if (lines.length == 1) return str;

    for (var i = 0; i < lines.length; i++) {
        lines[i] = "    " + lines[i];
    }
    return (lines[0] == "" ? "" : "\n") + //add starting blank line ONLY if it doesn't have one already
        lines.join("\n") +
        (lines[lines.length - 1] == "" ? "" : "\n"); //add ending blank line ONLY if it doesn't have one already
}

function dedent(str) {
    return (str || "").split("\n").map(x => x.trim()).join("\n");
}