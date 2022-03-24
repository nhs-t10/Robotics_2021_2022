var fs = require("fs");

require("../..").registerTransmutation({
    id: "combine-basic-blocks",
    requires: ["build-cgraph", "bc-condense-constants"],
    type: "transmutation",
    run: function(context) {
        var bytecode = context.inputs["bc-condense-constants"];
        var cgraph = context.inputs["build-cgraph"];
        
        combineBlocksFrom("ENTRY", bytecode, cgraph);
        
        context.output = {
            bytecode: bytecode,
            cgraph: cgraph
        };
        context.status = "pass";
        
        fs.writeFileSync(__dirname + "/cgraph", JSON.stringify(cgraph, null, 2));
    }
});

function combineBlocksFrom(entryName, bytecode, cgraph, previousBlocks) {
    //if we're going to go into unbounded recursion, exit early
    if(!previousBlocks) previousBlocks = [];
    if(previousBlocks.includes(entryName)) return;
    else previousBlocks.push(entryName);
    
    var to = cgraph[entryName];
    
    to.forEach(x=>combineBlocksFrom(x, bytecode, cgraph, previousBlocks));
    
    if(to.length == 1) {
        var nextBlock = to[0];
        var entriesToNextBlock = Object.keys(cgraph).filter(x=>cgraph[x].includes(nextBlock));
        if(entriesToNextBlock.length == 1 && entriesToNextBlock[0] == entryName) {

            bytecode[entryName].code = bytecode[entryName].code.concat(bytecode[nextBlock].code);
            bytecode[entryName].jumps = bytecode[nextBlock].jumps;
            
            cgraph[entryName] = cgraph[nextBlock];
            delete cgraph[nextBlock];
        }
    }
}