module.exports = {
    addUx: addUx,
    addU4: addU4,
    addU2: addU2,
    addU1: addU1
}

function addU1(b, u) {
    addUx(b, u, 1);
}
function addU2(b, u) {
    addUx(b, u, 2);
}
function addU4(b, u) {
    addUx(b, u, 4);
}

/**
     * 
     * @param {*[]} b 
     * @param {number} u 
     */
 function addUx(b, u, l) {
    //get the given number as bytes.
    var uBytes = [];
    while(u != 0) {
        uBytes.push(u & 0xFF);
        u >>> 8;
    }
    //make it big-endian (big digits first) instead of little-endian, for java compliance.
    uBytes.reverse();

    //pad with 0s to the given length
    while(uBytes.length < l) uBytes.push(0);

    //push each byte
    for(var i = 0; i < l; i++) {
        b.push(uBytes[i]);
    }
}