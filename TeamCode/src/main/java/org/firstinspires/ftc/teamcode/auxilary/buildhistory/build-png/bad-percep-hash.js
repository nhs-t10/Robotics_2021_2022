module.exports = {
    hash: hashBuffer,
    combineHashes: combineHashes
}

function combineHashes(hashes) {
    hashes = hashes.filter(x=>x!=null);

    if(hashes.length == 0) return hashBuffer(Buffer.from([]));

    var avg = [];
    for(var i = 0; i < hashes[0].length; i++) {
        var cellAverage = 0;
        for(var j = 0; j < hashes.length; j++) cellAverage += hashes[j][i];
        cellAverage = Math.floor(cellAverage/hashes.length);
        avg.push(cellAverage);
    }
    return Buffer.from(avg);
}

/**
 * Compute a (bad) perceptual hash.
 * @param {Buffer} buffer Data to consume.
 * @returns {Buffer} Hash of the data.
 */
function hashBuffer(buffer) {
    var buckets = [];
    
    //fill all the buckets with zeros
    for(var i = 0; i < 0xff; i++) buckets.push(0);
    
    for(var i = 0; i < buffer.length; i++) {
        var item = buffer[i];
        
        //an `if` is faster than using modulo every time
        buckets[item]++;
        if(buckets[item] == 0xff_ff) buckets[item] = 0;
    }
    
    //turn two-byte numbers into two one-bit numbers
    var byteBuckets = [];
    for(var i = 0; i < buckets.length; i += 2) {
        byteBuckets.push(buckets[i] >> 8)
        byteBuckets.push(buckets[i] & 0b1111_1111)
    }
    
    
    return Buffer.from(byteBuckets);
}