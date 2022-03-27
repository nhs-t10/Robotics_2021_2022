require("../..").registerTransmutation({
    id: "bc-basic-dead-code-elimination",
    requires: ["build-cgraph", "inverted-cgraph", "bc-condense-constants"],
    type: "transformation",
    run: function (context) {
        var bytecode = context.inputs["bc-condense-constants"];
        var cgraph = context.inputs["build-cgraph"];
        var invertedCGraph = context.inputs["inverted-cgraph"];

        Object.keys(bytecode).forEach(x => {
            removeBlockIfDead(x, bytecode, cgraph, invertedCGraph);
        });
        
        context.output = {
            bytecode: bytecode,
            cgraph: cgraph,
            invertedCGraph: invertedCGraph
        };
        context.status = "pass";
    }
});

function removeBlockIfDead(blockLabel, bytecode, cgraph, invertedCgraph) {
    //assume it was deleted by another destination
    if(!invertedCgraph[blockLabel]) return;
    
    if(invertedCgraph[blockLabel].length == 0 && canOnlyBeStaticallyReferenced(blockLabel)) {
        delete invertedCgraph[blockLabel];
        delete bytecode[blockLabel];
        
        removeBlockChildren(blockLabel, bytecode, cgraph, invertedCgraph);
        delete cgraph[blockLabel];
    }
}

function removeBlockChildren(blockLabel, bytecode, cgraph, invertedCGraph) {
    var children = cgraph[blockLabel];
    
    for(var i = 0; i < children.length; i++) {
        var childsParents = invertedCGraph[children[i]];
        childsParents.splice(childsParents.indexOf(blockLabel), 1);
        removeBlockIfDead(children[i], bytecode, cgraph, invertedCGraph);
    }
}

function canOnlyBeStaticallyReferenced(blockLabel) {
    if(blockLabel == "ENTRY" || blockLabel.includes("func_enter")) return false;
    else return true;
}