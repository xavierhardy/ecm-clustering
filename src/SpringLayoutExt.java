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

import java.awt.Component;

import javax.swing.SpringLayout;

public class SpringLayoutExt extends SpringLayout{
	private Component window;
		
	public SpringLayoutExt(Component window){
		super();
		this.window = window;
	}
	
	public void mettreEnDessousDe(Component c1, Component c2){ //On met c1 sous c2
		putConstraint(SpringLayout.NORTH, c1, 5, SpringLayout.SOUTH, c2);		
	}
	
	public void mettreADroiteDe(Component c1, Component c2){
		putConstraint(SpringLayout.WEST, c1, 5, SpringLayout.EAST, c2);
	}
	
	public void mettreEnHaut(Component c){
		putConstraint(SpringLayout.NORTH, c, 5, SpringLayout.NORTH, window);
	}
	
	public void mettreAGauche(Component c){
		putConstraint(SpringLayout.WEST, c, 5, SpringLayout.WEST, window);
	}
}
