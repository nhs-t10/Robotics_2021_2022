module.exports = function(ast) {
    return ast.statepaths.map(x=>
        x.statepath.states.map((y,i)=>
            y.statement.map((z,j)=>
                ({ statepath: x.label.value, state: i, statement: z, statementIndex: j })
            )
        )
    ).flat(3)
}