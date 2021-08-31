module.exports = {
    "reversed": function getSetter(doesStatement) {
        return `values[${doesStatement.directObject.value}] *= -1;`;
    },
    "logged": function getSetter(doesStatement) {
        return `FeatureManager.logger.log(values[${doesStatement.directObject.value}]);`
    }
}