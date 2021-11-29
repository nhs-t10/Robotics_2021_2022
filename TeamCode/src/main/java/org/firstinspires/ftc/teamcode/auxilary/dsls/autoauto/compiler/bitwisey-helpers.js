module.exports = {
/**
* Encode integers extremely efficiently. Every 7 bits, if the integer is over, insert a 1 bit. Otherwise, insert a 0.
* Encoding an integer is very efficient to process, because it never has to use string conversions or anything. It's all bitwise operators!
* Simple, easy-- that's the power of the home depot :)
* https://en.wikipedia.org/wiki/Variable-length_code
*/
toVarint8: function toVarint8(num) {
    var result = 0;

    var sectionsScanned = 0;
    while(num > 0) {
        var currentSection = 0b1111_111 & num;
        num >>>= 7;

        var isLastChunk = sectionsScanned == 0;
        var encodedChunk = (currentSection << 1) | (isLastChunk ? 1 : 0);
        result |= (encodedChunk << (sectionsScanned * 8));
        sectionsScanned++;
    }
    return result;
},

numToB62: function numToB62(i) {
    i = Math.round(i);
    var r = "";
    if(r < 0) {
        r += "-";
        i *= -1;
    }

    while(i > 0) {
        var m = i % 62;
        if(m < 36) r += m.toString(36);
        else if(m < 61) r += (m - 26).toString(36).toUpperCase();
        else r += "_"

        i /= 62;
        i = Math.floor(i);
    }
    return r || "0";
}
}