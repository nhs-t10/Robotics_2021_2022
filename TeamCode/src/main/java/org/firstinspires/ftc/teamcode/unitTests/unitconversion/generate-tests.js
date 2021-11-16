var src = `
public final static RotationUnits DEG = new RotationUnits("Degree", new String[] {"deg", "degs"}, 360);
    public final static RotationUnits RAD = new RotationUnits("Radian", new String[] {"rad", "rads"}, 6.28318530718);
    public final static RotationUnits ROT = new RotationUnits("Full Rotation", new String[] {"rot", "rots"}, 1);
    public final static RotationUnits TURNY = new RotationUnits("Turny", "tn", 4);
    public final static RotationUnits HALF_TURNY = new RotationUnits("Half-Turny", "hlftn", 8);
`;

var unitType = /public final static (\w+)/.exec(src)[1];

var sterilizedSrc = src
    .replace(/public final static \w+ \w+ = new \w+/g, "")
    .replace(/new String\[\] {/g, "[")
    .replace(/}/g, "]")
    .split("\n")
        .map(x=>x.trim())
        .filter(x=>x.length > 0)
        .map(x=>x.replace(/^\(/, "["))
        .map(x=>x.replace(/\);$/, "]"))
    .join(",\n");

var unitArr = JSON.parse("[" + sterilizedSrc + "]");

unitArr.forEach(x=>{
    if(typeof x[1] === "string") x[1] = [x[1]];
});

console.log(unitArr);

console.log(unitArr.map(x=>
    unitArr.filter(y=> x[0] != y[0]).map(y=>{
        return test(x,y, 200) + "\n" +
        test(x,y, 10000) + "\n" +
        test(x,y, 3000) + "\n";
    })
).flat().join("\n"));

function test(x,y) {
    var amount = Math.round(Math.random() * 200) / 10;
    var otherdAmount = (amount * y[1]) / x[1];
    var tolerance = amount / 1000;
    var unit1 = `${unitType}.forAbbreviation(${JSON.stringify(x[0].toLowerCase())})`;
    var unit2 = `${unitType}.forAbbreviation(${JSON.stringify(y[0].toLowerCase())})`;
    
    return `assertEquals(${jNumLit(amount)}, ${unitType}.convertBetween(${unit1}, ${unit2}, ${jNumLit(otherdAmount)}), ${tolerance});`
}

function jNumLit(n) {
    var nstr = n.toString();

    if(nstr.indexOf("e") > -1) return nstr;

    if(n % 1) return nstr;
    else return nstr + ".0";
}