module.exports = {
    hash: hashBuffer,
    combineHashes: combineHashes
}

/**
 * 
 * @param {Buffer[]} hashes 
 * @returns Buffer
 */
function combineHashes(hashes) {
    hashes = hashes.filter(x=>x!=null);

    if(hashes.length == 0) return hashBuffer(Buffer.from([]));

    var avg = [];
    for(var i = 0; i < hashes[0].length - 1; i+=2) {
        var cellTotal = 0;
        for(var j = 0; j < hashes.length; j++) {
            cellTotal += (hashes[j][i] << 8) | hashes[j][i + 1];
        }

        if(cellTotal > 0xff_ff) cellTotal %= 0xff_ff;

        avg.push(cellTotal);
    }

    return bufferFrom16BitNums(avg);
}

/**
 * Compute a (bad) perceptual hash.
 * @param {Buffer} buffer Data to consume.
 * @returns {Buffer} Hash of the data.
 */
function hashBuffer(buffer) {
    var buckets = [];
    
    //fill all the buckets with zeros
    for(var i = 0; i < 128; i++) buckets.push(0);
    
    for(var i = 0; i < buffer.length; i++) {
        var item = buffer[i];
        
        if(buckets[item] !== undefined) buckets[item]++;
        //an `if` is faster than using modulo every time
        if(buckets[item] == 0xff_ff) buckets[item] = 0;
    }
    
   return bufferFrom16BitNums(buckets);
}

function bufferFrom16BitNums(buckets) {
    var byteBuckets = [];
    
     //turn two-byte numbers into two one-bit numbers
     for(var i = 0; i < buckets.length; i ++) {
        var bucketVal = buckets[i];
        if(bucketVal > 0xff_ff) bucketVal %= 0xff_ff;

        byteBuckets.push(bucketVal >> 8);
        byteBuckets.push(bucketVal & 0b1111_1111);
     }
     
     
     return Buffer.from(byteBuckets);
}