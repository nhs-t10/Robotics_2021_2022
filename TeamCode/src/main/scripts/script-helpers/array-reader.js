//Like Java's stream classes, this array-reader keeps a reference of where it is, and allows the user to read 1 item at once.

module.exports = function(arr) {
    var index = 0;
    return {
        read: function() {
            return arr[index++];
        }
    }
}