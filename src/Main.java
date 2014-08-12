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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.swing.JOptionPane;

import Convexe.Droite;
import Convexe.PointD;
import Forme.FormeAbstraite;
import Forme.FormeCarree;
import Random.AbstractRandom;
import Random.RandomGauss;

public class Main {
	public static void main(String[] args){
		
		/*try {
			test();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*try {
			parseTests("serie1");

			parseTests("serie2");
			parseTests("serie3");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		new Fenetre();
		//System.out.println((new Droite(new PointD(0,0), new PointD(0,1))).eval(new PointD(-0.5,0.5)));
	}
	
	
	public static void parseTests(String dir) throws IOException{
		int i = 0;
		double[][] params = new double[5000][5];

		try {
		while(i<5000){
				//InputStreamReader reader = new InputStreamReader(new FileInputStream(f));
				BufferedReader lecteur = new BufferedReader(new FileReader(new File(dir + "\\test"+i+".csv")));
				lecteur.readLine();
				String[] colonnes = lecteur.readLine().split(";");
				for(int j = 0; j < 5; j++) params[i][j] = Double.parseDouble(colonnes[j]);
				lecteur.close();
			 i++;
		}
		} catch (FileNotFoundException exception) {
			exception.printStackTrace();
			OutputStreamWriter destination = new OutputStreamWriter(new FileOutputStream(new File(dir+".csv")));
			destination.write("n;dim;nbClasses;temps;k\n");
			for(int j = 0; j < i; j++) destination.write(params[j][0] + ";" + params[j][1] + ";" + params[j][2] + ";" + params[j][3] + ";" + params[j][4] + "\n");
			destination.close();
		}
		 catch (IOException exception) {
			exception.printStackTrace();
			
		}

		
			
		
	}
	
	public static void test() throws IOException{
		int numTest = 0;
		int n;
		int nbClassesR = 5;
		int nbClasses = 3;
		int dim;
		double epsilon = 0.001;
		double beta = 2.0, alpha = 1.0, delta = Math.sqrt(20.00);
		PointD[] centres = new PointD[nbClassesR];
		
		double r = 0.5;
		
		FormeAbstraite forme = new FormeCarree();
		forme.setParams(new RandomGauss(), 0.5);
		

		AbstractRandom rndCentre = new RandomGauss();
		
		for(int k = 0; k < 100000; k++){
			for(n = 10; n < 10000; n *= 2){
				for(dim = 1; dim <= 50; dim++){
						double[][] points = new double[n][dim];
			
						double theta = 2*Math.PI / (double)nbClassesR;
						System.out.println();
						System.out.println("theta = " + theta);
						
					
							for (int i = 0; i < nbClassesR; i++){
								double th = i*theta; //Toujours uniforme en theta
								centres[i] = new PointD(r * Math.cos(th), r * Math.sin(th));
							}
						
						
						for (int i = 0; i<n; i++){
							int classe = (int)(Math.random()*nbClassesR);
							PointD next = forme.next(centres[classe].x, centres[classe].y);
							if(dim >= 1) points[i][0] = next.x;
							if(dim >= 2) points[i][1] = next.y;
							if(dim >= 3) {
								for(int j = 2; j < dim; j++) points[i][j] = next.y;
							}
						}
						double dep = System.currentTimeMillis();
						ECM ecm = new ECM(points, nbClasses, alpha, beta, delta, epsilon);
						double time = System.currentTimeMillis() - dep;
						OutputStreamWriter destination = new OutputStreamWriter(new FileOutputStream(new File("test" + numTest + ".csv")));
						destination.write("n;dim;nbClasses;temps;k\n" + n + ";" + dim + ";" + nbClasses + ";" + time + ";"+ k);
						destination.close();
						System.out.println("TEST "+ numTest + " avec k = " + k + "\nn : " + n + "\ndim : " + dim + "\nnbClasses : " + nbClasses + "\nTemps : " + time + "\n********************");
						numTest++;
					
				}
			}
		}
	}
}
