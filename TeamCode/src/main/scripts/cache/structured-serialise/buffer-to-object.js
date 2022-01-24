const arrayReader = require("../../script-helpers/array-reader");

module.exports = function(buf) {
    var pool = [];
    var reader = arrayReader(buf);
    reconstructPool(pool, reader);
}

function reconstructPool(pool, reader) {
    while(true) {
        var type = reader.read();
        if(type === undefined) break;

        
    }
}