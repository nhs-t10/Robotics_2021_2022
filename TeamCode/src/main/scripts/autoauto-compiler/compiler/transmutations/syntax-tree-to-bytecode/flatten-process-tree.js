var util = require("util");

var cPool = require("./constant-pool");

var treeBlockToBytecodeBlock = require("./ast-to-bytecode");

module.exports = function(ast) {
    var constantPool = cPool();
    var treeBlocks = programToTreeBlocks(ast);
    
    var bytecodeBlocks = treeBlocks.map(x=>treeBlockToBytecodeBlock(x, constantPool));
    
    var flattedBlocks = bytecodeBlocks.map(x=>x.subblocks).flat(Infinity);
    
    var blockRecords = Object.fromEntries(flattedBlocks.map(x=>[x.label, x.bcode.flat()]));
    
    return blockRecords;
}

function programToTreeBlocks(program) {
    var blocks = program.statepaths.map(x=>
        x.statepath.states.map((y,i)=>({
            label: "s_" + x.label.replace(/_/g, "-") + "_" + i,
            treeStatements: y.statement
        }))
    ).flat();
    return blocks;   
}