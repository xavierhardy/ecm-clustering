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

import java.awt.Color;

import Convexe.Convexe;
import Symbole.Symbole;


public class Classe {
	public int index;
	public boolean composee = true;
	public Symbole symbole;
	public boolean envDessinee = false;
	public Convexe convexe = new Convexe();
	public Color couleur;
	public boolean estColoriee = false;
	public String nom;
	
	public Classe(int index, Symbole symbole, Color couleur, String nom){
		this.symbole = symbole;
		this.index = index;
		this.couleur = couleur;
		this.nom = nom;
	}
	
	public Classe(int index, Symbole symbole) {
		this.symbole = symbole;
		this.index = index;
	}

	public Classe clone(){
		Classe clone = new Classe(index, symbole, couleur, nom);
		clone.convexe = convexe;
		clone.composee = composee;
		clone.envDessinee = envDessinee;
		clone.estColoriee = estColoriee;
		return clone;
	}
	
	public void initConvexe(){
		convexe.enveloppe();
	}
	
	public String toString(){
		return nom + " (" + index + ")";
	}
}
