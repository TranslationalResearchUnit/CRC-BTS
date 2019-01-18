/*
 * CD45RObis Analysis
 * 02-04) Cell Classification and Adding Measurements to Parent TMA Cores
 *
 * Authors: Micha Eichmann & Stefan Reinhard (University of Bern)
 * Last change: 2017-11-20
 *
 */

// Cell Classification

setImageType('BRIGHTFIELD_OTHER');
setColorDeconvolutionStains('{"Name" : "CD45 Cell Classification", "Stain 1" : "Eosin", "Values 1" : "0.11793 0.79989 0.58844 ", "Stain 2" : "DAB", "Values 2" : "0.26917 0.56824 0.77759 ", "Background" : " 249 244 239 "}');

// set threshold
def threshold_immuno = 0

// get cells and classify each
def cells = getCellObjects()
//def cells = getSelectedObjects()
cells.each {
    eosin = measurement(it, 'ROI: 2.00 µm per pixel: Eosin:  Mean')
    if( eosin >= threshold_immuno ) { it.setPathClass(getPathClass("Immune cells")) }
    else { it.setPathClass(getPathClass("Stroma")) }
}

fireHierarchyUpdate()




// Add annotation measurements to TMA measurements
println("Adding to TMA core measurements...")
def imageData = getCurrentImageData()
def server = imageData.getServer()
def pixelWidthMicrons = server.getPixelWidthMicrons()
def pixelHeightMicrons = server.getPixelHeightMicrons()

// Tissue area
def tissue_rois = getObjects{ p -> p.isAnnotation() && p.getParent().isTMACore() && p.getName() != null && p.getName().split("_")[1] == "Tissue" }
for (roi in tissue_rois) {
    def areaPixels = roi.getROI().getArea()
    def areaMicrons = roi.getROI().getScaledArea(pixelWidthMicrons, pixelHeightMicrons)
    def areaMM = roi.getROI().getScaledArea(pixelWidthMicrons/1000, pixelHeightMicrons/1000)
    roi.getParent().getMeasurementList().putMeasurement('Area_Tissue (pixels)', areaPixels)
    roi.getParent().getMeasurementList().putMeasurement('Area_Tissue (microm2)', areaMicrons)
    roi.getParent().getMeasurementList().putMeasurement('Area_Tissue (mm2)', areaMM)
    roi.getParent().getMeasurementList().closeList()
}
println("...tissue area added")

// Stroma area
def stroma_rois = getObjects{ p -> p.isAnnotation() && p.getParent().isAnnotation() && p.getName() != null && p.getName().split("_")[1] == "Stroma" }
for (roi in stroma_rois) {
    def areaPixels = roi.getROI().getArea()
    def areaMicrons = roi.getROI().getScaledArea(pixelWidthMicrons, pixelHeightMicrons)
    def areaMM = roi.getROI().getScaledArea(pixelWidthMicrons/1000, pixelHeightMicrons/1000)
    roi.getParent().getParent().getMeasurementList().putMeasurement('Area_Stroma (pixels)', areaPixels)
    roi.getParent().getParent().getMeasurementList().putMeasurement('Area_Stroma (microm2)', areaMicrons)
    roi.getParent().getParent().getMeasurementList().putMeasurement('Area_Stroma (mm2)', areaMM)
    roi.getParent().getParent().getMeasurementList().closeList()
}
println("...stroma area added")

// Tumor area
def tumor_rois = getObjects{ p -> p.isAnnotation() && p.getParent().isAnnotation() && p.getName() != null && p.getName().split("_")[1] == "Tumor" }
for (roi in tumor_rois) {
    def areaPixels = roi.getROI().getArea()
    def areaMicrons = roi.getROI().getScaledArea(pixelWidthMicrons, pixelHeightMicrons)
    def areaMM = roi.getROI().getScaledArea(pixelWidthMicrons/1000, pixelHeightMicrons/1000)
    roi.getParent().getParent().getMeasurementList().putMeasurement('Area_Tumor (pixels)', areaPixels)
    roi.getParent().getParent().getMeasurementList().putMeasurement('Area_Tumor (microm2)', areaMicrons)
    roi.getParent().getParent().getMeasurementList().putMeasurement('Area_Tumor (mm2)', areaMM)
    roi.getParent().getParent().getMeasurementList().closeList()
}
println("...tumor area added")

