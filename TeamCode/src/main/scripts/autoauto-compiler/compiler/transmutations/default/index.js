require("..").registerTransmutation({
    requires: [],
    id: "default",
    type: "alias",
    aliasesTo: "get-syntax-tree tree-check* ast-to-java-setup-method process-template write-to-output-file :*postprocess*"
});