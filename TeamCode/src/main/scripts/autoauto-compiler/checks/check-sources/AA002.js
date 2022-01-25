var path = require("path");

module.exports = {
    summary: "Blank file",
    run: function(ast, folder, filename, originalFileContent) {
        if(originalFileContent.trim() == "") return {
            kind: "WARNING",
            text: "Files shouldn't be blank",
            fail: true
        }
    }
}