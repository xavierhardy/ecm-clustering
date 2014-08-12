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
import java.util.Vector;

public class DoubletVect<P> {
	Vector<P> v1;
	Vector<P> v2;
	
	public DoubletVect(){
		v1 = new Vector<P>();
		v2 = new Vector<P>();
	}
	
	public DoubletVect(Vector<P> v1, Vector<P> v2){
		this.v1 = v1;
		this.v2 = v2;
	} 
}
