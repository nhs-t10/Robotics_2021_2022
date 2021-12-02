const visualiseTree = require("./visualise-tree");

var generalNumberToIncrementWhenSomethingNeedsToBeChanged = 0;
var stringDefinitions = {};
var locationSetters = [];
var depthMappedDefinitions = {};
var variables = [];

function initFileState() {
    generalNumberToIncrementWhenSomethingNeedsToBeChanged = 0;
    stringDefinitions = {};
    locationSetters = [];
    depthMappedDefinitions = {};
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
    AutoatuoTailedValue: "Z",
    
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
            
            childDefs.forEach((x,i)=>x.nameNonce = getStringNonce(ast.statepaths[i].label.value, depth));
            
            var programName = genNonce();

            var sortedDefinitions = [];

            function addSortDefs(def) {
                if(def.processed) return;
                def.processed = true;

                def.depends.forEach(y=> {
                    var d = depthMappedDefinitions[y];
                    addSortDefs(d);
                });
                if(def.definition) sortedDefinitions.push(def);
            }
            
            var rootTreeNode = {
                self: nonce,
                depth: depth,
                definition: 0,
                depends: childDefs.map(x=>x.varname)
            };
            //visualiseTree(Object.values(depthMappedDefinitions).concat([rootTreeNode]));

            addSortDefs(rootTreeNode);

             //splitted by semicolons, make each statement into its own depth mapped definition
            var typedDefinitions = sortedDefinitions
            .map(x => ({
                self: x.self,
                depends: x.depends,
                definition: x.definition,
                type: JAVA_TYPE_REGEX.exec(x.definition.trim())[0] //discern and record the type
            }))
            .map((x,i,a) => { //if the type is the same as the previous, join the declarations with commas instead of semicolons
                if(a[i-1] && a[i-1].type == x.type) x.definition = x.definition.replace(x.type, "").replace(/new \w+\[\]/, "");
                if(a[i+1] && a[i+1].type == x.type) x.definition = x.definition.replace(/;$/, ",");
                return x;
            })
            .map(x=>x.definition) //remove type data
            .map(x=>
                (x.includes("\"") ? 
                    x
                    .substring(0, x.indexOf("\""))
                    .replace(/ ?(\W) ?/g, "$1")
                    + x.substring(x.indexOf("\""))
                :
                    x.replace(/ ?(\W) ?/g, "$1")
                ).trim()
            ) //remove extra whitespace, if it's not a string
            .join("") //join into one string
            
                
            var creation = `
            ${typedDefinitions}
            HashMap<String, Statepath> ${nonce} = new HashMap<String, Statepath>();
            ${childDefs.map((x, i) => `${nonce}.put(${x.nameNonce}, ${x.varname});`).join("\n")}
            AutoautoProgram ${programName} = ${STATIC_CONSTRUCTOR_SHORTNAMES.AutoautoProgram}(${nonce}, ${childDefs[0].nameNonce});
            ${locationSetters.join("")}`;

            result = "";
            var creationStatements = (creation).split(",");
            var line = "";
            for(var i = 0; i < creationStatements.length; i++) {
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
            
            var arrayNonce = genNonce();

            addDepthMappedDefinition({
                depth: depth + 1,
                self: arrayNonce,
                depends: childDefs.map(x => x.varname),
                definition: `State[] $${nonce} = new State[] { ${childDefs.map(x => x.varname).join(",")} };`
            });
            addDepthMappedDefinition({
                depth: depth,
                self: nonce,
                depends: [arrayNonce, label.varname],
                definition: `Statepath ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.Statepath}($${nonce}, ${label.varname});`
            });

            result = {
                varname: nonce,
                noLocation: true
            };
            break;
        case "Statepath":
            //covered by LabeledStatepath. Shouldn't be reached, ever.
            throw "ILLEGAL STATE BAD TIME";
            break;
        case "State":
            var childDefs = ast.statement.map(x => process(x));

            addDepthMappedDefinition({
                depth: depth + 1,
                self: "$" + nonce,
                depends: childDefs.map(x=>x.varname),
                definition: `Statement[] $${nonce} = new Statement[] { ${childDefs.map(x => x.varname).join(",")} };`
            });
            addDepthMappedDefinition({
                depth: depth,
                self: nonce,
                depends: ["$" + nonce],
                definition: `State ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.State}($${nonce});`
            });

            result = {
                varname: nonce
            };
            break;
        case "NextStatement":

            addDepthMappedDefinition({
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

            addDepthMappedDefinition({
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
                noLocation: true
            };
            break;
        case "DynamicValue":
            var b = process(ast.value).varname;
            result = {
                varname: b,
                noLocation: true
            };
            break;
        case "FunctionCallStatement":
            var call = process(ast.call);

            addDepthMappedDefinition({
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

            addDepthMappedDefinition({
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

            addDepthMappedDefinition({
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
                varname: getStringNonce(ast.value, depth, nonce),
                noLocation: true,
            };
            break;
        case "ArgumentList":
            var childDefs = ast.args.map(x => process(x));
            addDepthMappedDefinition({
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

            addDepthMappedDefinition({
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

            var operatorName = getStringNonce(ast.operator, depth);


            var right = process(ast.right);

            addDepthMappedDefinition({
                depth: depth,
                self: nonce,
                depends: [left.varname, right.varname, operatorName],
                definition: `ArithmeticValue ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.ArithmeticValue}(${left.varname}, ${operatorName}, ${right.varname});`
            });

            result = {
                varname: nonce
            };
            break;
        case "NumericValue":
            addDepthMappedDefinition({
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
            var strNonce = getStringNonce(ast.str, depth);

            addDepthMappedDefinition({
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

            addDepthMappedDefinition({
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

            addDepthMappedDefinition({
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

            addDepthMappedDefinition({
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

            addDepthMappedDefinition({
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

            addDepthMappedDefinition({
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

            var operatorName = getStringNonce(ast.operator, depth);

            addDepthMappedDefinition({
                depth: depth,
                self: nonce,
                depends: [left.varname, right.varname, operatorName],
                definition: `BooleanOperator ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.BooleanOperator}(${left.varname}, ${right.varname}, ${operatorName});`
            });

            result = {
                varname: nonce
            }
            break;
        case "VariableReference":
            var variable = process(ast.variable);

            addDepthMappedDefinition({
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
            addDepthMappedDefinition({
                depth: depth,
                self: nonce,
                depends: [],
                definition: `AutoautoBooleanValue ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.AutoautoBooleanValue}(${ast.value});`
            });

            result = {
                varname: nonce
            };
            break;
        case "TailedValue":
            var head = process(ast.head).varname;
            var tail = process(ast.tail).varname;
            addDepthMappedDefinition({
                depth: depth,
                self: nonce,
                depends: [head, tail],
                definition: `AutoautoTailedValue ${nonce} = ${STATIC_CONSTRUCTOR_SHORTNAMES.AutoatuoTailedValue}(${head}, ${tail});`
            });
            
            result = {
                varname: nonce
            }
            break;
        default:
            console.error("Unknown type " + ast.type);
            console.error(ast);
    }

    if(!ast.location) {
        console.error("Type "+  ast.type + " doesn't have location");
        return result;
    }
    
    if (typeof result == "object" && !result.noLocation && statepath) {
        locationSetters.push(makeLocationSetter(nonce, getStringNonce(statepath, depth), stateNumber, ast.location.start.line, ast.location.start.column));
    }
    return result;
}

var lastLocationSetterInfo = [];
function makeLocationSetter(nonce, statepathNonce, stateNumber, line, column) {
    var data = [statepathNonce, stateNumber || 0 , line, column];
    var similarityIndex = 0;
    
    //Compressing the method calls for a shorter output-- not working with the optimization sorter.
    //TODO: make this return a normal def ({self: genNonce(), depends: [nonce], ...}) and work that into the optimization system.
    
    // for(var i = 0; i < data.length; i++) {
    //     if(lastLocationSetterInfo[i] == data[i]) similarityIndex++;
    //     else break;
    // }
    var newData = data; //.slice(similarityIndex);

    lastLocationSetterInfo = data;

    return `L(${nonce}${newData.length?",":""}` + newData.join(",") + ");";
}

function getStringNonce(str, depth, nonce) {
    //first of everything, if the str exists already, just return that nonce.
    if(stringDefinitions[str]) return stringDefinitions[str];
    
    if(nonce == null) nonce = genNonce();
    if(depth == null) depth = 0;
    
    stringDefinitions[str] = nonce;
    
    addDepthMappedDefinition({
        depth: depth + 1,
        self: nonce,
        depends: [],
        definition: `String ${nonce} = ${JSON.stringify(str)};`
    });
    return nonce;
}

function addDepthMappedDefinition(def) {
    if(depthMappedDefinitions[def.self]) throw "Already defined!";
    def.depends.forEach(x=> {
        if(!depthMappedDefinitions[x]) {
            throw "No dependency " + x;
        }
    })
    depthMappedDefinitions[def.self] = def;
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
    var usedVariables = ["driver", "limbs", "sense", "imu", "i"]
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