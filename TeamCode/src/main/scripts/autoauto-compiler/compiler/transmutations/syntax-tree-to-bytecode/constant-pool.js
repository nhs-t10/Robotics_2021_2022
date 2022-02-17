module.exports = function() {
    var pool = [];
    var subId = 0;
    var tempvars = 0;
    return {
        getCodeFor: function(cons) {
            var pid = pool.length;
            pool.push(cons);
            
            return 0x0F000000 | pid;
        },
        subblockLabel: function(label, subcategory) {
            subId++
            return `${label}_${subcategory}_${subId.toString(16)}`;
        },
        tempVar: function() {
            return "@temp" + (tempvars++);
        }
    }
}