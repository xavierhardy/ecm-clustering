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

package Forme;

import Convexe.PointD;
import Random.AbstractRandom;

public class FormeRonde extends FormeAbstraite{	
	public FormeRonde(){
		super();
	}
	
	public FormeRonde(AbstractRandom rnd, double x, double y, double r){
		super(rnd,r);
	}
	
	public PointD next(double x, double y){
		double rayon = r * rnd.next();
		double theta = 2 * Math.PI * rnd.next();
		return new PointD(x + rayon * Math.cos(theta), y + rayon * Math.sin(theta));
	}

	public String toString() {
		return "Disque";
	}
}
