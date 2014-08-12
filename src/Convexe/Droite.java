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

package Convexe;

public class Droite {
	public double u;
	public double v;
	public double h;
	
	public Droite(PointD a, PointD b){
		u = a.y - b.y;
		v = b.x - a.x;
		h = a.x * b.y - a.y * b.x;		
	}
	
	public void copie (Droite d){
		u = d.u;
		v = d.v;
		h = d.h;
	}
	
	public Droite(PointD a, PointD b, PointD c){
		Droite d = new Droite(a,b);
		if(d.eval(c) > 0) copie(d);
		else {
			u= -d.u;
			v= -d.v;
			h= -d.h;
		}
	}
	
	public double eval(PointD a){
		return a.x * u + a.y * v + h;
	}
}
