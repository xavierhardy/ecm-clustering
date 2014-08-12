/*
    Copyright 2014, Xavier Hardy, ClÃ©ment Pique

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

public class FiltreFichiersSimple extends FileFilter { //Filtre pour un type de fichier avec au max 2 extensions différentes
	private String extension1;
	private String extension2 = "";
	private boolean extensionUnique = true;
	private String descriptionType;
	private String description;
	
	public FiltreFichiersSimple (String extension, String descriptionType, String description){
		super();
		extension1 = extension;
		this.descriptionType = descriptionType;
		this.description = description;
	}
	
	public FiltreFichiersSimple (String extension1, String extension2, String descriptionType, String description){
		super();
		extensionUnique = false;
		this.extension1 = extension1;
		this.extension2 = extension2;
		this.descriptionType = descriptionType;
		this.description = description;
	}
	
	private boolean isValidExtension(String extension){
		return extension != null && (extension.equals(extension1) || (!extensionUnique && extension.equals(extension2)));
	}
	
	public boolean accept(File f) {
	    if (f.isDirectory()) {
	    	return true;
	    }

	    String extension = Outils.getExtension(f);
		if (isValidExtension(extension)) {
		        return true;
		}
		
	    return false;
	}

	public String getTypeDescription(File f) {
	    String extension = Outils.getExtension(f);

	    if (isValidExtension(extension)){
	    	return descriptionType;
	    }
	    return null;
	}

	public String getDescription() {
        return description;
	}
}
