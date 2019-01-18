/*
 * CD45RObis Analysis
 * 02-02) Tumor and Stroma Detection
 *
 * Authors: Micha Eichmann & Stefan Reinhard (University of Bern)
 * Last change: 2017-11-20
 *
 */

// Tumor & Stroma Detection

setImageType('BRIGHTFIELD_H_DAB');
setColorDeconvolutionStains('{"Name" : "TumorStromaStain", "Stain 1" : "Haematoxylin", "Values 1" : "0.11838 0.78231 0.61154 ", "Stain 2" : "DAB", "Values 2" : "0.18668 0.33073 0.92508 ", "Background" : " 249 244 239 "}');

selectObjects{ p -> p.isAnnotation() && p.getParent().isTMACore() && p.getName() != null && p.getName().split("_")[1] == "Tissue" && p.getProperties()["childObjects"] == []}
runPlugin('qupath.opencv.DetectCytokeratinCV', '{"downsampleFactor": 1,  "gaussianSigmaMicrons": 3.0,  "thresholdTissue": 0.01,  "thresholdDAB": 0.2,  "separationDistanceMicrons": 0.0}');
println("Tumor & stroma tissue detected within the tissue annotation.")

for (def tissuetype in getObjects{ p -> p.isAnnotation() && p.getParent().isAnnotation() && p.getParent().getName() != null && p.getParent().getName().split("_")[1] == "Tissue" && p.getPathClass() == getPathClass("Tumor") }){
    tissuetype.setName(tissuetype.getParent().getParent().getUniqueID() + "_Tumor")
    tissuetype.setPathClass(null)
}
for (def tissuetype in getObjects{ p -> p.isAnnotation() && p.getParent().isAnnotation() && p.getParent().getName() != null && p.getParent().getName().split("_")[1] == "Tissue" && p.getPathClass() == getPathClass("Stroma") }){
    tissuetype.setName(tissuetype.getParent().getParent().getUniqueID() + "_Stroma")
    tissuetype.setPathClass(null)
}
fireHierarchyUpdate()
println("Added '_Tumor' & '_Stroma' to annotation names and deleted annotation classes.")

