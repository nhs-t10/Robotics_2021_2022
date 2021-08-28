module.exports = {
    "reversed": function getSetter(doesStatement) {
        return `values[${doesStatement.directObject.value}] *= -1;`;
    }
}