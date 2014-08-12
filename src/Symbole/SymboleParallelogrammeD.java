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

package Symbole;
import java.awt.Color;
import java.awt.Graphics;


public class SymboleParallelogrammeD extends Symbole{
	public SymboleParallelogrammeD(){
		super();
		index = 9;
	}
	
	public SymboleParallelogrammeD(Color couleur){
		super(couleur);
		index = 9;
	}
	
	public void dessine(Graphics g, int x, int y, int taille){
		int d = taille/4;
		g.setColor(couleur);
		int t = taille/2;
		int[] xListe = {x-t-1,x-t+d,x+t+1,x+t-d};
		int[] yListe = {y+t-d,y-t+d,y-t+d,y+t-d};

		g.drawPolygon(xListe, yListe, 4);
	}

	public String toString() {
		return "Parallelogramme droite";
	}
}
