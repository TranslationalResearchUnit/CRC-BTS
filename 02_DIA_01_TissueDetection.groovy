/*
 * CD45RObis Analysis
 * 02-01) Tissue Detection
 *
 * Authors: Micha Eichmann & Stefan Reinhard (University of Bern)
 * Last change: 2017-11-20
 *
 */
 
setImageType('BRIGHTFIELD_H_DAB');
setColorDeconvolutionStains('{"Name" : "TissueStain", "Stain 1" : "Hematoxylin", "Values 1" : "0.86052 0.45971 0.21946 ", "Stain 2" : "DAB", "Values 2" : "0.19171 0.42587 0.88424 ", "Background" : " 249 246 239 "}');
selectTMACores()
runPlugin('qupath.imagej.detect.tissue.SimpleTissueDetection2', '{"threshold": 235,  "requestedPixelSizeMicrons": 2.0,  "minAreaMicrons": 10000.0,  "maxHoleAreaMicrons": 1000.0,  "darkBackground": false,  "smoothImage": true,  "medianCleanup": true,  "dilateBoundaries": false,  "smoothCoordinates": true,  "excludeOnBoundary": false,  "singleAnnotation": true}');

// Check
// Is tissue annotation a child of TMA core?
//selectObjects{ p -> p.isAnnotation() && !p.getParent().isTMACore() }

for (def tissue in getObjects{ p -> p.isAnnotation() && p.getParent().isTMACore() }) {
    tissue.locked = false
    tissue.setName(tissue.getParent().getUniqueID() + "_Tissue")
}
fireHierarchyUpdate()
println("Tissue detected, unlocked, Unique ID added to name")

// --> Do manual air bubble removal and tissue detection refinement now.
println("--> DO MANUAL AIR BUBBLE REMOVAL AND TISSUE DETECTION REFINEMENT NOW")