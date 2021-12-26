module.exports = function(ast) {
    return ast.statepaths.map(x=>
        x.statepath.states.map((y,i)=>
            y.statement.map(z=>
                ({ statepath: x.label.value, state: i, statement: z })
            )
        )
    ).flat(3)
}