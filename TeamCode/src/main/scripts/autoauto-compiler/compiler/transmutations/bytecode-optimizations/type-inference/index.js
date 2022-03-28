const fs = require("fs");
const bytecodeTools = require("../bytecode-tools");
const bc = require("../bc");
const bytecodeSpec = require("../bytecode-spec");
const typeSystemCreator = require("./type-system-creator");

require("../..").registerTransmutation({
    id: "type-inference",
    requires: ["single-static"],
    type: "transmutation",
    run: function (context) {
        var bytecode = context.inputs["single-static"];
        var typeSystem = typeSystemCreator();
        
        console.log(typeSystem["Math@0"]);
    }
});

function processBytecodeBlock(block, typeSystem) {
    console.log(block);
}

function addAllLocalVariablesToTypeSystem(codeArray, typeSystem) {
    
} 