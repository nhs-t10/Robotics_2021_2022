const aaBytecodeMaker = require("./make-aa-bytecode");
const makeStatementList = require("./make-statement-list");
const aaBytecodeToJvmBytecode = require("./aa-bytecode-to-jvm-bytecode");

module.exports = function(ast) {
    var statements = makeStatementList(ast);
    var aaBytecode = aaBytecodeMaker(statements);
    var jvmBytecode = aaBytecodeToJvmBytecode(aaBytecode, "class");

    return jvmBytecode;
}