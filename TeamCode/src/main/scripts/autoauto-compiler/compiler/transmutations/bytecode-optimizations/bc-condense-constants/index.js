require("../..").registerTransmutation({
    id: "bc-condense-constants",
    requires: ["syntax-tree-to-bytecode"],
    type: "information",
    run: function(context) {
        var bcBlocks = context.inputs["syntax-tree-to-bytecode"].blocks;
        var constants = context.inputs["syntax-tree-to-bytecode"].constants;
        
        var o = {};
        
        Object.keys(bcBlocks).forEach(x=>o[x] = condenseConstantOps(bcBlocks[x], constants));
        
        context.output = o;
        context.status = "pass";
    }
});

function condenseConstantOps(block, constants) {
    for(var i = 2; i < block.length; i++) {
        if(isDecidableOpWithTwoArgs(block[i].code)) {
            var left = block[i - 2].__value;
            var right = block[i - 1].__value;
            
            var r = doOp(block[i].code, left, right);
            if(isNaN(r)) r = undefined;
            
            block.splice(i - 2, 3, {
                code: constants.getCodeFor(r),
                __value: r,
                location: block[i].location
            });
            i -= 3;
        }
    }
    return block;
}

function isDecidableOpWithTwoArgs(code) {
    return code >= 0x200 && code <= 0x20B;
}

function doOp(code, left, right) {
    switch(code) {
        
    case 0x200: //add
        return left + right;
    case 0x201: //subtr
        return left - right;
    case 0x202: //mul
        return left * right;
    case 0x203: //div
        return left / right;
    case 0x204: //mod
        return left % right;
    case 0x205: //exp
        return Math.pow(left, right);
    case 0x206: //cmp_lt
        return left < right;
    case 0x207: //cmp_lte
        return left <= right;
    case 0x208: //cmp_eq
        return left == right;
    case 0x209: //cmp_neq
        return left != right;
    case 0x20A: //cmp_gte
        return left >= right;
    case 0x20B: //cmp_gt
        return left > right;
    default:
        throw "unknown math op"
    }
}