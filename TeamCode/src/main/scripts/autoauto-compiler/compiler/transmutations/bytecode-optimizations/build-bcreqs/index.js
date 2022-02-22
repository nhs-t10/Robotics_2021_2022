var codeIndexedSpec = require("../bc");
var fs = require("fs");

require("../..").registerTransmutation({
    id: "build-bcreqs",
    requires: ["syntax-tree-to-bytecode"],
    type: "information",
    run: function (context) {
        var bytecode = (
            context.inputs["bc-condense-constants"] ||
            context.inputs["syntax-tree-to-bytecode"].blocks);

        //fs.writeFileSync(__dirname + "/raw.txt", fmtBlocks(bytecode));

        var keys = Object.keys(bytecode);
        keys.forEach(k => makeCausallyLinkedCodesHierarchal(bytecode[k]));

        //fs.writeFileSync(__dirname + "/formatted.txt", fmtBlocks(bytecode))

        context.output = bytecode;
        context.status = "pass";
    }
});

function fmtBlocks(blocks) {
    return Object.entries(blocks)
        .map(x => x[0] + ":\n" + fmtBlock(x[1]))
        .join("\n");
}
function fmtBlock(b, indent) {
    indent |= 0;

    var r = [];
    for (var i = 0; i < b.length; i++) {
        var instr = b[i];
        var c = codeIndexedSpec[instr.code];
        
        var ans = "";

        if (instr.deps && instr.deps.length) ans += "(" + fmtBlock(instr.deps, indent + 1) + "\n)";

        if (c) {
            ans += c.mnemom + " " + fmtArity(getArity(b, i));
        } else {
            ans += "const " + JSON.stringify(instr.__value);
        }
        r.push(ans);
    }

    return r
        .map(x => "  ".repeat(indent) + x)
        .join("\n");
}

function fmtArity(a) {
    return a.pop + "," + a.push;
}

function makeCausallyLinkedCodesHierarchal(block) {
    for(var i = 0; i < block.length; i++) {
        var ar = getArity(block, i);
        block[i].deps = consumeNStackValues(block, i - 1, ar.pop).tree;
    }
    return block;
}

function resolveRelativeArity(ar, block, index) {
    if (typeof ar === "number") return ar;

    var tArity = 0;

    for (var j = 0; j < ar.length; j++) {
        if (typeof ar[j] === "number") tArity += ar[j];
        else tArity += findOpcodeLiteralValue(getAritySigil(block, index)) + 1;
    }
    return tArity;
}

function getAritySigil(block, index) {
    var bInd = block[index - 1];
    if (bInd) return bInd;

    var code = block[index];

    return code.deps[code.deps.length - 1];
}

function getArity(block, index) {
    var instr = block[index];
    var opcode = instr.code;
    var opcodeFamily = instr.code >>> 24;

    if (opcodeFamily != 0) {
        return { pop: 0, push: 1 };
    } else if (codeIndexedSpec[opcode]) {
        var pushInContext = resolveRelativeArity(codeIndexedSpec[opcode].push, block, index)
        var popInContext = resolveRelativeArity(codeIndexedSpec[opcode].pop, block, index);

        return { pop: popInContext, push: pushInContext };
    }
}

function findOpcodeLiteralValue(instr) {
    var c = instr.code;
    var f = c >>> 24;
    var k = c & 0xFFFFFF;

    if (f == 0x0E) return k;
    else return instr.__value;
}

function consumeNStackValues(bytecodes, startIndex, num) {
    var toGo = num;
    var r = [];
    var len = 0;

    for (var i = startIndex; toGo > 0;) {
        r.splice(0, 0, bytecodes[i]);

        var arity = getArity(bytecodes, i);
        var deps = consumeNStackValues(bytecodes, i - 1, arity.pop);
        bytecodes[i].deps = deps.tree;

        toGo -= arity.push;
        i -= deps.codeLength + 1;
        len += deps.codeLength;
    }

    return {
        tree: r,
        codeLength: len + r.length
    };
}