/*
    Copyright 2014, Xavier Hardy, Clément Pique

    This file is part of ecm-classifier.

    ecm-classifier is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ecm-classifier is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ecm-classifier.  If not, see <http://www.gnu.org/licenses/>.
*/




import java.io.File;

import javax.swing.filechooser.FileFilter;

public class FiltreFichiersImages extends FileFilter {

	public boolean accept(File f) {
	    if (f.isDirectory()) {
	    	return true;
	    }

	    String extension = Outils.getExtension(f);
	    if (extension != null) {
		if (extension.equals(Outils.tiff) ||
		    extension.equals(Outils.tif) ||
		    extension.equals(Outils.gif) ||
		    extension.equals(Outils.jpeg) ||
		    extension.equals(Outils.jpg) ||
		    extension.equals(Outils.png)||
		    extension.equals(Outils.bmp)) {
		        return true;
		} else {
		    return false;
		}
	    }
	    return false;
	}

	public String getTypeDescription(File f) {
	    String extension = Outils.getExtension(f);
	    String type = null;

	    if (extension != null) {
	        if (extension.equals(Outils.jpeg) ||
	            extension.equals(Outils.jpg)) {
	            type = "Image JPEG";
	        } else if (extension.equals(Outils.gif)){
	            type = "Image GIF";
	        } else if (extension.equals(Outils.tiff) ||
	                   extension.equals(Outils.tif)) {
	            type = "Image TIFF";
	        } else if (extension.equals(Outils.png)){
	            type = "Image PNG";
	        } else if (extension.equals(Outils.bmp)){
	            type = "Image BMP";}
	    }
	    return type;
	}

	public String getDescription() {
        return "Images (*.jpeg, *.jpg, *.gif, *.tif, *.tiff, *.png, *.bmp)";
	}
}
