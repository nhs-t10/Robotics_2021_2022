var path = require("path");

module.exports = {
    summary: "Syntax Error",
    run: function(ast, folder, filename, originalFileContent) {
        if(ast instanceof Error) return {
            kind: "ERROR",
            text: ast.message,
            original: ast + "",
            sources: [{
                file: path.join(folder, filename),
                position: {
                    startLine: ast.location.start.line,
                    startColumn: ast.location.start.column,
                    startOffset: ast.location.start.offset,
                    endLine: ast.location.end.line,
                    endColumn: ast.location.end.line,
                    endOffset: ast.location.end.offset
                }
            }]
        }
    }
}