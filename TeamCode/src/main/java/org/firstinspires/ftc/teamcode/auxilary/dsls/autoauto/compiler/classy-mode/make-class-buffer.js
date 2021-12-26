const makeStatementList = require("./make-statement-list")

module.exports = function(ast) {
    return makeStatementList(ast);
}