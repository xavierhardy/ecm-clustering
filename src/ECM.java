/*
    Copyright 2014, Xavier Hardy, Cl√©ment Pique

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

import java.util.Vector;
import java.awt.Color;
import java.io.*;

public class ECM {
	private double[][] ensembleDePoints, V, Vplus,  M, H, B, D, N, K;
	//N est similaire a M, sauf que toutes les intersections sont prises en compte dans le pourcentage
	private int[][] S;
	private int[] c;
	private int nombreDeClasses; //c
	private int k; // 2^c : nombre de sous ensemble de Omega
	private int dim; //p
	private int n; //Nombre de points
	private double epsilon = 0.001;
	private double beta = 2.0, alpha = 1.0, delta = Math.sqrt(20.00);
	
	private void initECM(double[][] points, int classes){
		System.out.println("Initialisation ECM");
		nombreDeClasses = classes;
		n = points.length;
		k = OutilsMatriciels.exposant(nombreDeClasses);
		ensembleDePoints = points;
		dim = ensembleDePoints[0].length;		
		Vplus = new double[nombreDeClasses][dim];
		Vector<Integer> pointsChoisis = new Vector<Integer>();
		Integer dernierPoint = new Integer(0);
		for(int i = 0; i < nombreDeClasses ; i++){
			while(pointsChoisis.contains(dernierPoint)){
				dernierPoint = new Integer((int)(Math.random()*n));
			}
			pointsChoisis.add(dernierPoint);
			Vplus[i]=ensembleDePoints[dernierPoint.intValue()];
		}
	}
	
	private void calculeCS(){
		System.out.println("Calcul de S");
		calculeS();

		System.out.println("Calcul de C");
		calculeC();
	}
	
	public ECM(double[][] points, int classes, double[][] M){
		initECM(points, classes);
		calculeCS();
		System.out.println("Chargement de M...");
		this.M = M;
	}
	
	public ECM(double[][] points, int classes){
		initECM(points, classes);
		calculeCS();

		System.out.println("Calcul de M...");
		loop();
		try{
			exporte(new File("resultat.csv"));
			exporteLaTeX(new File("resultat.tex"));
		} catch(Exception e){System.out.println("exportation");}
	}

	public ECM(double[][] points, int classes, double alpha, double beta, double delta, double epsilon){
		initECM(points, classes);
		this.alpha = alpha;
		this.beta = beta;
		this.delta = delta;
		this.epsilon = epsilon;
		calculeCS();

		System.out.println("Calcul de M...");
		loop();
		try{
			exporte(new File("resultat.csv"));
			exporteLaTeX(new File("resultat.tex"));
		} catch(Exception e){System.out.println("exportation");}
	}
	
	public void calculeS(){
		S = new int[nombreDeClasses][k];

		for (int j=0 ;j<k ;j++){
			int j2 = j;
			for (int i=0; i<nombreDeClasses;i++){
				if (j2%2  != 0) S[i][j] = 1; //S[i][j] est par dÈfaut ‡ 0
				j2 = j2/2;
			}
		}
	}

	//OutilsMatriciels.imprime(B);
	public void calculeC(){
		c = new int[k];
		for (int j=0; j<k;j++){
			for(int i=0; i<nombreDeClasses;i++){
				if(S[i][j] != 0) c[j]++;
			}
		}
	}

	public void loop(){
		int nbetour =0;
		boolean cont = true;
		while (cont){
			V=Vplus;
			nbetour++;
			calculeM();
			calculeH();
			calculeB();
			calculeVplus();
			cont = OutilsMatriciels.distanceMat(V,Vplus)>epsilon;
			
		}
		System.out.println(nbetour);
	}

	public void calculeD(){
		D = new double[n][k];
		for(int i = 0;i<n;i++){
			for(int j=1;j<k;j++){
				double[] vjbarre = new double[dim];
				/*for (int x=0; x<dim;x++){
					vjbarre[x]=0;
				}*/
				for (int l=0; l<nombreDeClasses; l++){
					if(S[l][j] != 0) vjbarre = OutilsMatriciels.sommeVec(OutilsMatriciels.produitDoubleVec(1/(double)c[j],V[l]),vjbarre);
				}
				double distance = OutilsMatriciels.distanceVec(ensembleDePoints[i],vjbarre);
				D[i][j] = distance;
			}
		}
	}

	public  void calculeM(){
		M = new double[n][k];
		calculeD();
		for (int i=0;i<n;i++){
			double denominateur = Math.exp((-2.0)*Math.log(delta)/(beta-1.0));
			M[i][0]=1;
			for (int l=1;l<k;l++){
				denominateur+=Math.exp((-2.0/(beta-1.0))*Math.log(D[i][l])-alpha*Math.log(c[l])/(beta-1.0));
			}
			for (int j=1 ; j<k; j++){ //j = 0 correspondant a l'ensemble vide
				if (D[i][j]!=0)	M[i][j]=Math.exp((-2.0/(beta-1.0))*Math.log(D[i][j])-alpha*Math.log(c[j])/(beta-1.0))/denominateur;
				M[i][0] -= M[i][j];
			}
			
		}
	}

	public void calculeH(){
		H = new double[nombreDeClasses][nombreDeClasses];
		for (int l=0;l<nombreDeClasses;l++){
			for (int q=0;q<nombreDeClasses;q++){
				for (int i=0;i<n;i++){
					for (int j=1;j<k;j++){
						if(S[l][j] != 0 && S[q][j] != 0) H[l][q]+= Math.exp(beta*Math.log(M[i][j])+(alpha-2.0)*Math.log(c[j]));
					}
				}
			}
		}
	}

	public void calculeB(){	
		B = new double[nombreDeClasses][dim];
		for (int l=0;l<nombreDeClasses;l++){
			for (int q=0;q<dim;q++){
				for (int i=0;i<n;i++){
					for (int j=1;j<k;j++){
						if(S[l][j] != 0) B[l][q]+= ensembleDePoints[i][q]*Math.exp(beta*Math.log(M[i][j])+(alpha-1.0)*Math.log(c[j]));
					}
				}
			}
		}
	}

	public void calculeVplus(){
		Vplus = new double[nombreDeClasses][dim];
		Vplus = OutilsMatriciels.produitMat(OutilsMatriciels.inverse(H),B);
	}

	public void exporte(File f) throws IOException{
		OutputStreamWriter destinationFile = new OutputStreamWriter(new FileOutputStream(f));

		for (int i=0;i<M[0].length;i++){
			destinationFile.write(Integer.toString(i));
			if(i != M[0].length -1) destinationFile.write(';');
		}
		destinationFile.write('\n');
		for (int j=0;j<M.length;j++){
			for (int i=0;i<M[0].length;i++){
				destinationFile.write(Double.toString(M[j][i]));
				if(i != M[0].length -1) destinationFile.write(';');
			}
			destinationFile.write('\n');
		}
		destinationFile.close();
	}		
		
	public void exporteLaTeX(File f) throws IOException{
		OutputStreamWriter destination = new OutputStreamWriter(new FileOutputStream(f));
		destination.write("\\begin{table} \\tiny \\begin{tabular}[h]{c");
		for (int i=0;i<M.length;i++){
			destination.write("|c");
		}
		destination.write("}\n ");
		destination.write("& ");
		for (int i=0;i<M.length;i++){
			if(i != M.length -1) {
				destination.write("$m_"+Integer.toString(i+1) + " (A_j)$ & ");
			} else {
				destination.write("$m_"+Integer.toString(i+1) + " (A_j)$ \\\\\n \\hline");
			}
		}
		for (int j=0;j<M[0].length;j++){
			if (j == 0) {
				destination.write("$\\emptyset$ & ");
			}
			else {
				destination.write("$A_{"+Integer.toString(j)+"}$ & ");
			}
			for (int i=0;i<M.length;i++){
				destination.write(Double.toString(M[i][j]));
				if(i != M.length -1){
					destination.write(" & ");
				}
				else {
					destination.write(" \\\\\n");
				}
			}
		}
		destination.write("\\end{tabular} \\caption{Exemple pour $c ="+Integer.toString(nombreDeClasses)+"$ et $n = "+Integer.toString(n)+"$} \\end{table}");
		destination.close();
	}

	
	public double[][] getM(){
		return M;
	}
	
	public int getk(){
		return k;
	}
	
	public double[][] getN(){
		return N;
	}
	
	public double[][] getK(){
		return K;
	}
	
	public int[][] getS(){
		return S;
	}
	
	
	public double[][] calculK(){
		int[] vCard = new int[k];
		double[][] S2 = new double[k][nombreDeClasses];
		for(int i = 1; i < k; i++)
		{
			int card = OutilsMatriciels.nbNonNulsColonne(S,i,nombreDeClasses);
			double fact = 1/(double)(card);
			vCard[i] = card;
			for(int j = 0; j < nombreDeClasses; j++) //Pour tous les sous-ensembles de Omega
				{
					if(S[j][i] != 0) S2[i][j] = fact*S[j][i];
				}
		}
		K = OutilsMatriciels.produitMat(S2, S);
	
		for(int i = 0; i < k; i++){
			/*K[i][i] = 1;
			for (int j = 0; j < i; j++)
			{
				K[j][i] = 0;
			}*/
			for (int j = i + 1; j < k; j++)
			{
				if(vCard[i] >= vCard[j]) K[j][i] = 0;
			}
		}
		return K;
	}
	
	public void calculN(){
		K = calculK();
		OutilsMatriciels.imprime(K);
		N = new double[n][k];
		for(int i = 0; i < n; i++) //Pour l'ensemble des points
		{
			for(int j = 0; j < k; j++) //Pour tous les sous-ensembles de Omega
			{
				N[i][j] = M[i][j];
				for(int l = j + 1; l < k; l++){
					if(K[l][j] != 0)
						N[i][j] += K[l][j] * M[i][l];
				}	
			}
		}
		
	}

}
