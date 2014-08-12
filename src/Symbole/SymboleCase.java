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


public class SymboleCase extends Symbole{
	public SymboleCase(){
		super();
		index = 4;
	}
	
	public SymboleCase(Color couleur){
		super(couleur);
		index = 4;
	}
	
	public void dessine(Graphics g, int x, int y, int taille){
		g.setColor(couleur);
		int t = taille/2;
		g.fillRect(x - t, y - t, taille, taille);	
	}
	
	public void dessine(Graphics g, int x, int y, int taille, Color couleur){
		int t = taille/2;
		//g.setColor(this.couleur);
		//g.drawRect(x - t - 1, y - t - 1, taille + 1, taille + 1);
		
		g.setColor(couleur);
		g.fillRect(x - t, y - t, taille, taille);	
	}

	public String toString() {
		return "Case coloree";
	}
}
