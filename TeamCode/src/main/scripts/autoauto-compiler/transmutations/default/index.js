require("..").registerTransmutation({
    requires: [],
    id: "default",
    type: "alias",
    aliasesTo: "source-file-text text-to-syntax-tree ast-to-java-setup-method process-template write-to-output-file :*postprocess*"
});