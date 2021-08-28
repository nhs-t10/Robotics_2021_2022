module.exports = function(statements) {
    var used = [];
    for(var i = 0; i < statements; i++) {
        var number = priorityNumber(statements.properties.priority);
        if(used.includes(number)) throw "Re-use of priority `" + statements.properties.priority + "`";
        used.push(number);
    }
    
    return statements.sort(function(a, b) {
        return priorityNumber(a.properties.priority) - priorityNumber(b.properties.priority);
    });
}

function priorityNumber(priority) {
    return ({    
        promise: 5,
        highest: 4,
        higher: 3,
        high: 2,
        normal: 1,
        low: -1,
        lower: -2,
        lowest: -3,
        thought: -4
    })[priority] || 0;
}