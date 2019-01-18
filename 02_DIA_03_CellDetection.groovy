/*
 * CD45RObis Analysis
 * 02-03) Cell Detection
 *
 * Authors: Micha Eichmann & Stefan Reinhard (University of Bern)
 * Last change: 2017-11-20
 *
 */

// Cell detection
setImageType('BRIGHTFIELD_H_E');
setColorDeconvolutionStains('{"Name" : "H&E Cell Detection", "Stain 1" : "Hematoxylin", "Values 1" : "0.65111 0.70119 0.29049 ", "Stain 2" : "Eosin", "Values 2" : "0.2159 0.8012 0.5581 ", "Background" : " 255 255 255 "}');
selectObjects { p -> p.isAnnotation() && p.getParent().isAnnotation() && p.getName() != null && p.getName().split("_")[1] == "Stroma"  }
runPlugin('qupath.imagej.detect.nuclei.WatershedCellDetection', '{"detectionImageBrightfield": "Hematoxylin OD",  "requestedPixelSizeMicrons": 0.3,  "backgroundRadiusMicrons": 5.0,  "medianRadiusMicrons": 0.0,  "sigmaMicrons": 1.5,  "minAreaMicrons": 10.0,  "maxAreaMicrons": 400.0,  "threshold": 0.1,  "maxBackground": 2.0,  "watershedPostProcess": true,  "cellExpansionMicrons": 1.5,  "includeNuclei": true,  "smoothBoundaries": true,  "makeMeasurements": false}');

// Measure cell features
setImageType('BRIGHTFIELD_OTHER');
setColorDeconvolutionStains('{"Name" : "CD45 Cell Classification", "Stain 1" : "Eosin", "Values 1" : "0.11793 0.79989 0.58844 ", "Stain 2" : "DAB", "Values 2" : "0.26917 0.56824 0.77759 ", "Background" : " 249 244 239 "}');
selectDetections();
runPlugin('qupath.lib.algorithms.IntensityFeaturesPlugin', '{"pixelSizeMicrons": 2.0,  "region": "ROI",  "tileSizeMicrons": 25.0,  "colorOD": false,  "colorStain1": true,  "colorStain2": true,  "colorStain3": true,  "colorRed": true,  "colorGreen": true,  "colorBlue": true,  "colorHue": false,  "colorSaturation": false,  "colorBrightness": false,  "doMean": true,  "doStdDev": true,  "doMinMax": true,  "doMedian": true,  "doHaralick": true,  "haralickDistance": 1,  "haralickBins": 32}');
selectDetections();
runPlugin('qupath.lib.plugins.objects.ShapeFeaturesPlugin', '{"area": true,  "perimeter": true,  "circularity": true,  "useMicrons": true}');
