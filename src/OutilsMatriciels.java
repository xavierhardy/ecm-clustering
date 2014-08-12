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

public class OutilsMatriciels {
	
	public OutilsMatriciels(){}

	public static int nbNonNulsColonne(int[][] m, int col, int nbLignes){
		int result = 0;
		for(int i = 0; i < nbLignes; i++){
			if(m[i][col] != 0) result++;
		}
		return result;
	}
	
	public static void copierVecteur(double[][] m1, double[][] m2, int i1, int i2) {
		for(int j = 0; j < m1[0].length; j++){
			m2[i2][j] = m1[i1][j];
		}
	}

	public static double[][] produitMat(double[][] m1, double[][] m2){
		int n1 = m1.length;
		int l1 = m1[0].length;
		int n2 = m2.length;
		int l2 = m2[0].length;
		double[][] res = new double[n1][l2];
		if (!(l1==n2)){
			System.out.println("Tailles non concordantes");
		} else {
			for (int i=0;i<n1; i++){
				for (int j=0;j<l2;j++){
					res[i][j]=0;
					for (int l=0;l<l1;l++){
						res[i][j]+= m1[i][l]*m2[l][j];
					}
				}
			}
		}
		return res;
	}

	public static double[][] produitMat(double[][] m1, int[][] m2){
		int n1 = m1.length;
		int l1 = m1[0].length;
		int n2 = m2.length;
		int l2 = m2[0].length;
		double[][] res = new double[n1][l2];
		if (!(l1==n2)){
			System.out.println("Tailles non concordantes");
		} else {
			for (int i=0;i<n1; i++){
				for (int j=0;j<l2;j++){
					res[i][j]=0;
					for (int l=0;l<l1;l++){
						res[i][j]+= m1[i][l]*m2[l][j];
					}
				}
			}
		}
		return res;
	}
	public static double[][] inverse(double[][]m){
		int n =m.length;
		boolean bool = false;
		double[][] A = new double[n][n];
		double[][] C = new double[n][n];
		for (int i=0; i<n;i++){
			for (int j=0;j<n;j++){
				C[i][j]=0;
				A[i][j]=m[i][j];
			}
			C[i][i]=1;
		}
		
		for (int k=0;k<n;k++){
			for (int i=k;i<n && bool==false;i++){
				double aik = A[i][k];
				if (!(aik == 0)){
					bool=true;
					echangeLigne(A,i,k);
					echangeLigne(C,i,k);
					multiplieLigne(A,k,1.0/aik);
					multiplieLigne(C,k,1.0/aik);			
				}
			}
			if (bool == false){
				System.out.println("Matrice non inversible");
				return null;
			} else {
				bool = false; //pour le prochain tour de boucle
				for (int i=0;i<n;i++){
					if (i==k){
					} else {
						double aik = A[i][k];
						ligneMoinsLigneMultipliee(A,i,k,aik);
						ligneMoinsLigneMultipliee(C,i,k,aik);
					}
				}
			}
		}
		return C;
	}

	public static void echangeLigne(double[][] m, int i, int j){
		double[] tmp = m[i];
		m[i] = m[j];
		m[j] = tmp;
	}

	public static void multiplieLigne(double[][] m, int i, double d){
		int n = m.length;
		for (int j=0;j<n;j++){
			m[i][j]=d*m[i][j];
		}
	}

	public static void ligneMoinsLigneMultipliee(double[][] m, int i, int k, double d){
		int n = m.length;
		for (int j=0;j<n;j++){
			m[i][j] = m[i][j]-d*m[k][j];
		}
	}

	public static int exposant(int i){
		if (i == 0){
			return 1;
		} else {
			int j = exposant(i/2);
			if (i%2 == 0){
				return j*j;
			}
			else {
				return 2*j*j;
			}
		}
	}

	public static double distanceMat(double[][] m1, double[][] m2){
		double res2 = 0;
		for (int i= 0; i<m1.length; i++){
			for (int j = 0; j<m1[i].length;j++){
				res2 += (m1[i][j]-m2[i][j])*(m1[i][j]-m2[i][j]);
			}
		}
		return Math.sqrt(res2);
	}

	public static void imprime(double[][] m){
		int n = m.length;
		int l = m[0].length;
		for (int i = 0;i<n;i++){
			for (int j=0;j<l;j++){
				System.out.print(" "+m[i][j]);
			}
			System.out.println();
		}
	}

	public static void imprime(int[][] m){
		int n = m.length;
		int l = m[0].length;
		for (int i = 0;i<n;i++){
			for (int j=0;j<l;j++){
				System.out.print(" "+m[i][j]);
			}
			System.out.println();
		}
	}
	
	public static void imprime(int[] v){
		int n = v.length;
		for (int i = 0;i<n;i++){
			System.out.print(" "+v[i]);
		}
	}
	
	public static void imprime(double[] v){
		int n = v.length;
		for (int i = 0;i<n;i++){
			System.out.print(" "+v[i]);
		}
	}

	public static double distanceVec(double[] v1, double[] v2){
		double res2 = 0;
		for (int i= 0; i<v1.length; i++){
			res2 += (v1[i]-v2[i])*(v1[i]-v2[i]);
		}
		return Math.sqrt(res2);	
	}

	public static double[] sommeVec(double[] v1, double[] v2){
		int n = v1.length;
		int m = v2.length;
		double[] res = new double[n];
		if (!(n == m)) {
			System.out.println("Tailles non conformes");
		} else {
			for (int i=0; i<n; i++){
				res[i] = v1[i]+v2[i];
			}
		}
		return res;
	}

	public static double[] produitDoubleVec(double d, double[] v){
		double[] res = new double[v.length];
		for (int i=0; i<v.length; i++){
			res[i] = d*v[i];
		}
		return res;
	}
	
	public static int appartient(int x, int[] v){ //-1 ou position dans le vecteur
		int i = 0;
		while( i < v.length && v[i] != x) i++;
		if(i == v.length) return -1;
		else return i;
	}
}
