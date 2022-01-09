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
    //get the given number as bytes (little-endian). This makes an array that starts with the least significant byte.
    var uBytes = [];
    while(u != 0) {
        uBytes.push(u & 0xFF);
        u >>>= 8;
    }

    if(uBytes.length > l) throw `number ${u} too big for length ${l}`;

    //pad with 0s to the given length
    while(uBytes.length < l) uBytes.push(0);


    //push each byte, big digits first
    for(var i = l - 1; i >= 0; i--) {
        b.push(uBytes[i]);
    }
}