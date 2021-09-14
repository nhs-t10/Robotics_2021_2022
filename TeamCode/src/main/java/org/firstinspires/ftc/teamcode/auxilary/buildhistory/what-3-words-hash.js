var adjectives = require("./words/adjectives.json");
var nouns = require("./words/nouns.json");
var familymembers = require("./words/familymembers.json");
var possessivePronouns = require("./words/possessive-pronouns.json");

var simpleNouns = require("./words/simple-nouns.js");

var SIMPLE_NOUNS_LIMIT = 3;

module.exports = {
    complexPhrase: complexPhraseHash,
    simpleNouns: simpleNounHash
}

function simpleNounHash(hash) {
    var hashCharPerWord = Math.ceil(Math.floor(Math.log2(simpleNouns.length)) / 4);
    
    var words = [];
    for(var i = 0; i < hash.length; i+= hashCharPerWord) {
        var code = hash.substring(i, i + hashCharPerWord);
        var numerical = +('0x' + code);
        
        words.push(getSimpleNoun(numerical));
        
        if(words.length >= SIMPLE_NOUNS_LIMIT) break;
    }
    return words.join(" ");
}

function complexPhraseHash(hash) {
    hash = hash + "";

    var grammarConstructData = hash.substring(0, 3);
    var grammarConstructCodes = +('0x' + grammarConstructData);

    var familyMemberIndex = grammarConstructCodes % 8;
    var familyMember = familymembers[familyMemberIndex];

    var NotNotOrNot = (grammarConstructCodes >> 3) % 4;
    var favouriteFamilyMember = (grammarConstructCodes >> 5) % 4;
    var useButOtherAdjective = (grammarConstructCodes >> 7) % 4;
    var really = (grammarConstructCodes >> 9) % 2;
    var butStill = (grammarConstructCodes >> 10) % 4;
    var possessive = (grammarConstructCodes >> 12) % 8;

    hash = hash.substring(3);

    var charsPerWord = Math.floor(hash.length / 3);

    var wordCodes = [
        hash.substring(0, charsPerWord),
        hash.substring(charsPerWord, charsPerWord * 2),
        hash.substring(charsPerWord * 2),
    ];

    var wordCodeNumbers = wordCodes.map(x => +('0x' + x));

    var adjectives = [
        getAdjective(wordCodeNumbers[0]),
        getAdjective(wordCodeNumbers[1])
    ];
    var noun = getNoun(wordCodeNumbers[2]);

    var phrase =
        (["really ", "seriously, ", "", "why is it "])[really]
        + (["not ", "", "as " + adjectives[1] + " as ", "the same as "])[NotNotOrNot]
        + possessivePronouns[possessive] + " "
        + (favouriteFamilyMember == 3 ? "favourite " : "")
        + (useButOtherAdjective ? noun : familyMember)
        + ([" but still pretty " + adjectives[0], ", it's " + adjectives[0], "", ", but still " + adjectives[0]])[butStill];

    console.log(phrase);

    return phrase;
}

function getAdjective(id) {
    return adjectives[id % adjectives.length];
}
function getNoun(id) {
    return nouns[id % nouns.length];
}
function getSimpleNoun(id) {
    return simpleNouns[id % simpleNouns.length];
}