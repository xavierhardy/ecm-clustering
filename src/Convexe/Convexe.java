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

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

public class Convexe {
	private PointD iso;
	private Vector<Arete> ensConv;
	private Vector<PointD> ens;
	
	public Convexe(){
		ens = new Vector<PointD>();
		ensConv = new Vector<Arete>();
		iso = null;
	}
	
	public Convexe(double[][] points){
		ens = new Vector<PointD>();
		ensConv = new Vector<Arete>();
		for(int i = 0; i < points.length; i++){
			ens.add(new PointD(points[i][0], points[i][1]));
		}
		calculBarycentre();
	}
	
	public Convexe(Vector<PointD> points){
		ens = new Vector<PointD>();
		ensConv = new Vector<Arete>();
		for(int i = 0; i < points.size(); i++){
			ens.add(points.elementAt(i));
		}
		calculBarycentre();
	}
	
	public void calculBarycentre(){
		if(ens.size() >= 3)	iso = isoBary3(ens.elementAt(0), ens.elementAt(1), ens.elementAt(2));
		else{
			double max = Double.MAX_VALUE;
			iso = new PointD(max,max);
		}
	}
	
	public PointD isoBary3 (PointD a, PointD b, PointD c){
		return new PointD((a.x + b.x + c.x)/3,(a.y + b.y + c.y)/3);	
	}
	
	public void ajoute(double x, double y){
		ens.add(new PointD(x,y));
	}
	
	private Vector<Arete> triangle(int i, int j, int k)
	{
		 PointD a = ens.elementAt(i);
		 PointD b = ens.elementAt(j);
		 PointD c = ens.elementAt(k);
		 Vector<Arete> v = new Vector<Arete>();
		 v.add(new Arete(i,j,new Droite(a,b,c)));
		 v.add(new Arete(j,k,new Droite(b,c,a)));
		 v.add(new Arete(i,k,new Droite(a,c,b)));
		 return v;
	}
	
	private Vector<Arete> ligne(int i, int j) //Cas degenere d'enveloppe convexe
	{
		 PointD a = ens.elementAt(i);
		 PointD b = ens.elementAt(j);
		 Vector<Arete> v = new Vector<Arete>();
		 v.add(new Arete(i,j,new Droite(a,b,a)));
		 v.add(new Arete(j,i,new Droite(b,a,a)));
		 return v;
	}
	
	private Vector<Arete> point(int i) //Cas degenere d'enveloppe convexe
	{
		 PointD a = ens.elementAt(i);
		 Vector<Arete> v = new Vector<Arete>();
		 v.add(new Arete(i,i,new Droite(a,a,a)));
		 return v;
	}
	
	private boolean aSupprimer(PointD m, Arete a) //On verifie qu'une arete est a supprimer ou non
	{
		return a.d.eval(m) < 0;
	}
	
	private DoubletVect<Arete> separe(PointD p, Vector<Arete> v)
	{
		Vector<Arete> v1 = new Vector<Arete>();
		Vector<Arete> v2 = new Vector<Arete>();
		while(!v.isEmpty()){
			Arete a = v.firstElement();
			if(aSupprimer(p, a)) v1.add(0,v.remove(0));
			else v2.add(0,v.remove(0));
		}
		return new DoubletVect<Arete>(v1,v2);			
	}
	
	private int[] compte(Vector<Arete> v, int nMax){
		int[] result = new int[nMax];
		while(!v.isEmpty()){
			Arete a = v.remove(0);
			result[a.i]++;
			result[a.j]++;
		}
		return result;
	}
	
	private Vector<Arete> nouvellesAretes(int p, int[] tab, int nMax)
	{
		Vector<Arete> result = new Vector<Arete>();
		
		for(int i = 0; i < nMax; i++){
			if(tab[i] == 1) result.add(0, new Arete(p,i, new Droite(ens.elementAt(p), ens.elementAt(i), iso)));
		}
		return result;
	}
	
	private static Vector concat(Vector v1, Vector v2){
		while(!v2.isEmpty()){
			v1.add(v2.remove(0));
		}
		return v1;
	}

	private Vector<Arete> ajoute(int p, Vector<Arete> v){
		DoubletVect<Arete> dVect = separe(ens.elementAt(p), v); //dVect.v1 elements qu'on supprime, dVect.v2 qu'on garde
		int[] tab = compte(dVect.v1, p);
		Vector<Arete> vEnPlus = nouvellesAretes(p, tab, p);
		return concat(dVect.v2, vEnPlus);
	}
	
	private Vector<Arete> enveloppe(int n){
		if(n>3) return ajoute(n-1, enveloppe(n-1));
		else if(n == 3) return triangle(0,1,2);
		else if(n == 2) return ligne(0,1);
		else if(n == 1) return point(0);
		else return new Vector<Arete>();
	}
	
	public void enveloppe(){
		//System.out.println("Taille ensemble: " + ens.size());
		ensConv = enveloppe(ens.size());
	}
	
	public void dessine(Graphics g, double zoom, double xCam, double yCam, int w, int h){
		int x1, y1, x2, y2;
		Iterator<Arete> iter = ensConv.iterator();
		//System.out.println("Dessin convexe");
		
		while(iter.hasNext()){
			Arete next = iter.next();
			x1 = (int)(w/2+(xCam + ens.elementAt(next.i).x)*zoom);
			x2 = (int)(w/2+(xCam + ens.elementAt(next.j).x)*zoom);
			y1 = (int)(h/2+(yCam + ens.elementAt(next.i).y)*zoom);
			y2 = (int)(h/2+(yCam + ens.elementAt(next.j).y)*zoom);
			//System.out.println("Arete: "+ ens.elementAt(next.i).x + " ; " + ens.elementAt(next.i).y + " --> " + ens.elementAt(next.j).x + " ; " + ens.elementAt(next.j).y);
			g.drawLine(x1,y1,x2,y2);
		}
	}
}
