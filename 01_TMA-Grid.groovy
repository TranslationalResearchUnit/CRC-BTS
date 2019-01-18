/*
 * CD45RObis Analysis
 * 01) TMA Grid
 *
 * Authors: Micha Eichmann & Stefan Reinhard (University of Bern)
 * Last change: 2017-11-20
 *
 */

// 1) DEARRAY TMA
if (!isTMADearrayed()) {
    runPlugin('qupath.imagej.detect.dearray.TMADearrayerPluginIJ', '{"coreDiameterMM": 0.8,  "labelsHorizontal": "A-Z",  "labelsVertical": "1-29",  "labelOrder": "Row first",  "densityThreshold": 0,  "boundsScale": 105}');
    return;
    fireHierarchyUpdate()
    println("TMA dearrayed")
}
else { println("TMA was already dearrayed") }


// UNLOCK ALL TMA CORES
for (def core in getObjects{ p -> p.isTMACore() }){
    core.setLocked(false)
}
fireHierarchyUpdate()
println("Unlocked all TMA cores")


// 2) Rename TMA backwards
// Get the grid & dimensions:
def grid = getCurrentHierarchy().getTMAGrid()
int nrows = grid.getGridHeight()
int ncols = grid.getGridWidth()

// Label columns in letters because they never exceed 26:
abc = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"]
colstring = String.format("%s-A", abc[ncols-1])
// Label rows in numbers
rowstring = String.format("%d-1", nrows)
relabelTMAGrid(colstring, rowstring, true)
fireHierarchyUpdate()
println("TMA relabled. Rows: "+rowstring+", Columns: "+colstring)

// 3) Add leading zeros to TMA core names
for (def core in getObjects{ p -> p.isTMACore() }){
    corename_number = core.getProperties()["displayedName"].split('-')[0]
    corename_letter = core.getProperties()["displayedName"].split('-')[1]
    corename_number = corename_number.toInteger()
    String corename = String.format("%02d-%s", corename_number, corename_letter)
    core.setName(corename)
}
fireHierarchyUpdate()
println("TMA cores renamed: added leading zeros to row index")

// 4) SET ALL TMA CORES WITHOUT A UNIQUE ID TO INVALID AND THE ONES WITH TO VALID
// useful after "File/Import TMA map" was performed
for (def core in getObjects{ p -> p.isTMACore() }){
    uid = core.getProperties()["uniqueID"]
    if (uid == null) { core.setMissing(true)}
    else { core.setMissing(false) }
}
fireHierarchyUpdate()
println("TMA cores WITHOUT a Unique ID set to INVALID")
println("TMA cores WITH a Unique ID set to VALID")


// DO TMA GRID CORRECTIONS
println("")
println("--> DO MANUAL TMA GRID CORRECTIONS NOW")