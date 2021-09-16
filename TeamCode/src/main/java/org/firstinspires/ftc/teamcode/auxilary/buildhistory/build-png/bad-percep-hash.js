var OUTPUT_LENGTH_BYTES = 3600;
var BYTES_PER_BUCKET = 1;
var UINT_MAX = 65536;

var crc32 = require("./png-file/crc");

module.exports = {
    hash: hashBuffer,
    combineHashes: combineHashes
}

function combineHashes(hashes) {
    hashes = hashes.filter(x=>x!=null);
    
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
    //convert the buffer into an array of uints for more bucket capacity
    var uints = [];
    for(var i = 0; i < Math.floor(buffer.length / 2); i++) {
        uints.push(buffer.readUInt16LE(i*2));
    }
    
    if(buffer.length % 2 != 0) {
        uints.push(buffer[buffer.length - 1]);
    }
    
    //get delta of each uint from its left neighbor
    //gives some sensitivity to order, but not too much
    var deltas = [];
    var smallestDelta = 0;
    var largestDelta = UINT_MAX;
    for(var i = 0; i < uints.length; i++) {
        var rawDelta = (uints[i] - (uints[i - 1] || 0));
        var unsignedDelta = Math.floor((rawDelta / 2) + (UINT_MAX / 2));
        
        smallestDelta = Math.min(smallestDelta, unsignedDelta);
        largestDelta = Math.max(largestDelta, unsignedDelta);
        
        deltas.push(unsignedDelta);
    }
    
    var deltaRange = largestDelta - smallestDelta;
    
    var maxBucketValue = 0;
    var buckets = [];
    for(var i = 0; i < OUTPUT_LENGTH_BYTES / BYTES_PER_BUCKET; i++) buckets.push(0);
    
    for(var i = 0; i < deltas.length; i++) {
        var rangeAdjustedDelta = deltas[i] - smallestDelta;

        
        //put it through a crc32 algorithm so that pixels are evenly spread out
        var bucketIndexBuffer = Buffer.from(rangeAdjustedDelta.toString(16), "hex");
        var bucketIndexPositionHash = crc32(bucketIndexBuffer);
        var bucketIndexPosition = (bucketIndexPositionHash[0] * 0xff + bucketIndexPositionHash[1] ) / UINT_MAX;
        
        var bucketIndex = Math.floor(bucketIndexPosition * buckets.length);
        buckets[bucketIndex] += deltas[i];
        maxBucketValue = Math.max(maxBucketValue, buckets[bucketIndex]);
    }
    
    var normalizedByteBuckets = [];
    for(var i = 0; i < buckets.length; i++) {
        var normalized = Math.log10(buckets[i]) / Math.log10(maxBucketValue);
        var byte = Math.floor(Math.sqrt(normalized) * Math.pow(0xff, BYTES_PER_BUCKET));
        normalizedByteBuckets.push(byte);
    }
    
    return Buffer.from(normalizedByteBuckets);
}

function bufferAvg(buf) {
    var tot = 0;
    for(var i = 0; i < buf.length; i++) tot += buf[i];
    return tot / buf.length;
}