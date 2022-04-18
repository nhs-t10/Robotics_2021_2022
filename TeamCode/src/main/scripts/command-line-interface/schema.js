/**
 * @typedef {object} CommandLineArguments
 * @property {boolean} ascii
 * @property {boolean} color
 * @property {boolean} quiet
 * @property {boolean} no-cache
 * @property {number} threads
 */


module.exports = {
    quiet: {
        value: false,
        short: ["q"]
    },
    ascii: {
        value: false,
        short: ["a"]
    },
    color: {
        value: true,
        short: ["c"]
    },
    "no-cache": {
        value: false,
        short: []
    },
    threads: {
        value: require("os").cpus().length,
        short: []
    }
}