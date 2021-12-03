var src = `
public final static TimeUnit NS = new TimeUnit("Nanosecond", "ns", 1e-6, "Nanoseconds");
    public final static TimeUnit MS = new TimeUnit("Millisecond", "ms", 1, "Milliseconds");
    public final static TimeUnit S = new TimeUnit("Second", "s", 1000, "Seconds");
    public final static TimeUnit MIN = new TimeUnit("Minute", new String[] {"min", "mn"}, 1000 * 60, "Minutes");
    public final static TimeUnit HR = new TimeUnit("Hour", new String[] {"h", "hr"}, 1000 * 60 * 60, "Hours");
    public final static TimeUnit D = new TimeUnit("Day", "d", 1000 * 60 * 60 * 24, "Days");
    public final static TimeUnit YR = new TimeUnit("Year", new String[] {"y", "yr"}, 1000.0 * 60 * 60 * 24 * 365, "Years");
    public final static TimeUnit JIF = new TimeUnit("Jiffy", new String[] {"jif", "jiff", "jiffy", "jiffies", "jiffie"}, 10, "Jiffies");
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

var unitArr = eval("[" + sterilizedSrc + "]");

unitArr.forEach(x=>{
    if(typeof x[1] === "string") x[1] = [x[1]];
});

console.log(unitArr.map(x=>
    unitArr.filter(y=> x[0] != y[0]).map(y=>{
        return test(x,y, 200) + "\n" +
        test(x,y, 10000) + "\n" +
        test(x,y, 3000) + "\n";
    })
).flat().join("\n"));

function test(x,y) {
    var amount = Math.round(Math.random() * 200) / 10;
    var otherdAmount = (amount * y[2]) / x[2];
    var tolerance = amount / 1000;
    var unit1 = `${unitType}.forAbbreviation(${JSON.stringify(randomFrom(x[1]).toLowerCase())})`;
    var unit2 = `${unitType}.forAbbreviation(${JSON.stringify(randomFrom(y[1]).toLowerCase())})`;
    
    return `assertEquals(${jNumLit(amount)}, ${unitType}.convertBetween(${unit1}, ${unit2}, ${jNumLit(otherdAmount)}), ${tolerance});`
}

function randomFrom(arr) {
    return arr[Math.floor(Math.random() * arr.length)];
}

function jNumLit(n) {
    var nstr = n.toString();

    if(nstr.indexOf("e") > -1) return nstr;

    if(n % 1) return nstr;
    else return nstr + ".0";
}