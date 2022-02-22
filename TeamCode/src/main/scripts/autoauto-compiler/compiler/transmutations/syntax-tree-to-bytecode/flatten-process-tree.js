var util = require("util");

var cPool = require("./constant-pool");

var treeBlockToBytecodeBlock = require("./ast-to-bytecode");
const bytecodeSpec = require("./bytecode-spec");

module.exports = function(ast) {
    var constantPool = cPool();
    var treeBlocks = programToTreeBlocks(ast);
    
    var bytecodeBlocks = treeBlocks.map(x=>treeBlockToBytecodeBlock(x, constantPool));
    
    var flattedBlocks = bytecodeBlocks.map(x=>x.subblocks).flat(Infinity);
    
    var blockRecords = Object.fromEntries(flattedBlocks.map(x=>[x.label, x.bcode.flat()]));
    
    if(blockRecords["ENTRY"]) {
        throw "entry block defined!"
    } else {
        blockRecords["ENTRY"] = [
            {code: constantPool.getCodeFor(treeBlocks[0].label), __value: treeBlocks[0].label },
            {code: bytecodeSpec.jmp_l.code}
        ];
    }
    
    return {
        blocks: blockRecords,
        constants: constantPool
    };
}

function programToTreeBlocks(program) {
    var blocks = program.statepaths.map(x=>
        x.statepath.states.map((y,i)=>({
            label: "s/" + x.label + "/" + i,
            treeStatements: y.statement
        }))
    ).flat();
    return blocks;   
}