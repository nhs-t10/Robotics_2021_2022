const aaBytecodeMaker = require("./make-aa-bytecode");
const makeStatementList = require("./make-statement-list")

module.exports = function(ast) {
    var statements = makeStatementList(ast);
    var aaBytecode = aaBytecodeMaker(statements);

    return aaBytecode;
}