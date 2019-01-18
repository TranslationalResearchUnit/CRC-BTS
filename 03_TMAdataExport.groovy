/*
 * CD45RObis Analysis
 * 03) TMA Data Export
 *
 * Authors: Micha Eichmann & Stefan Reinhard (University of Bern)
 * Last change: 2017-11-20
 *
 */

import qupath.lib.gui.QuPathGUI
def projectPath = QuPathGUI.getInstance().getProject().getBaseDirectory().absolutePath
def exportPath = new File( projectPath, "results" )
if( ! exportPath.exists() ) {
     exportPath.mkdirs()
     print ("export folder created")
}

exportTMAData(projectPath +"/results/", 4.0)