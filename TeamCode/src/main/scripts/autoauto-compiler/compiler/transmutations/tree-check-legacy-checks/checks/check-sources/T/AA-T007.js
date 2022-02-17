var path = require("path");

var query = require("../query");

module.exports = {
    summary: "Empty block",
    run: function(ast) {

        var blocks = query.getAllOfType(ast, "Block");
        
        for(var i = 0; i < blocks.length; i++) {
            var stmt = blocks[i];

            if(stmt.state.statement.length == 0) {
                return {
                    kind: "WARNING",
                    text: `This block only has 1 statement. The curly brackets can be removed!`,
                    location: stmt.location
                }
            }
        }
    }
}