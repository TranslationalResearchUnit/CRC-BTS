/*
 * CD45RObis Analysis
 * 02-05) Refine Immune Cell Count
 *
 * Authors: Micha Eichmann & Stefan Reinhard (University of Bern)
 * Last change: 2017-11-20
 *
 */

def cells = getCellObjects()
int n_immuno_noEosin = 0
int n_stroma_noEosin = 0
int n_cells = 0

cells.each {
    if(it.getMeasurementList()[0].getName() != "ROI: 2.00 µm per pixel: Eosin:  Mean") {
        if(it.getPathClass()==getPathClass("Immune cells")) {
            it.setPathClass(getPathClass("Immune cells no Measurement"))
            n_immuno_noEosin = n_immuno_noEosin +1
        }
        if(it.getPathClass()==getPathClass("Stroma")) {
            it.setPathClass(getPathClass("Stroma no Measurement"))
            n_stroma_noEosin = n_stroma_noEosin +1
        }
    }
}

print(n_immuno_noEosin)
print(n_stroma_noEosin)
print(n_cells)