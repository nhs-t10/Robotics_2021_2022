//Like Java's stream classes, this array-reader keeps a reference of where it is, and allows the user to read 1 item at once.

module.exports = function(arr) {
    var index = 0;
    return {
        skip: function(l) {
            index += (l|0);
        },
        read: function() {
            return arr[index++];
        },
        readVarint: function() {
            var vIntBytes = [];
            for(; index < arr.length; index++) {
                vIntBytes.push(arr[index]);
                //if the last bit's set, this is the last item
                if(arr[index] & 0b1) break;
            }
            
            //move the pointer off of the last item
            index++;
            
            var result = 0;
            for(var i = vIntBytes.length - 1; i >= 0; i--) {
                var reverseIndex = vIntBytes.length - i - 1;
                result |= ((vIntBytes[i] >>> 1) << (7 * reverseIndex));
            }
            return result;
        },
        readNextBytes: function(l) {
            var r = [];
            var n = index + l;
            for(; index < n; index++) r.push(arr[index]);
            return r;
        },
        hasNext: function() {
            return index < arr.length;
        }
    }
}