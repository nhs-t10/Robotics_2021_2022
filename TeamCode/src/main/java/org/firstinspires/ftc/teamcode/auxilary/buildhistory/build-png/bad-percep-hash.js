var OUTPUT_LENGTH_BYTES = 30;

/**
 * Compute a (bad) perceptual hash.
 * @param {Buffer} data Data to consume.
 * @returns {Buffer} Hash of the data.
 */
module.exports = function(buffer) {
    var bytes = [];
    var bytesPer = Math.ceil(buffer.length / OUTPUT_LENGTH_BYTES);
    for(var i = 0; i < OUTPUT_LENGTH_BYTES; i++) {
        bytes.push(Math.cos((i / OUTPUT_LENGTH_BYTES) * Math.PI) - bufferAvg(buffer.slice(i * bytesPer, i * bytesPer + bytesPer)));
    }
    return Buffer.from(bytes);
}

function bufferAvg(buf) {
    var tot = 0;
    for(var i = 0; i < buf.length; i++) tot += buf[i];
    return tot / buf.length;
}