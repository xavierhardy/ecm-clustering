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

package Random;
public class RandomExp extends AbstractRandom {
	double lambda = 1;
	
	public RandomExp(){
		super();
	}
	
	public RandomExp(double lambda){
		super();
		this.lambda = lambda;
	}
	
	public double next(){
		return - (1 / lambda) * Math.log( 1 - rnd.nextDouble() );
	}

	public String toString() {
		return "Distribution exponentielle";
	}
}
