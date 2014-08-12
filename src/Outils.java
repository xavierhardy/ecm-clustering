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

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.Random;
import java.util.Vector;

import Symbole.SymboleCarre;
import Symbole.SymboleCarreC;
import Symbole.SymboleCarreP;
import Symbole.SymboleCase;
import Symbole.SymboleCroix;
import Symbole.SymboleDiamant;
import Symbole.SymboleDiamantC;
import Symbole.SymboleDiamantP;
import Symbole.SymboleParallelogrammeD;
import Symbole.SymboleParallelogrammeDC;
import Symbole.SymboleParallelogrammeDP;
import Symbole.SymbolePlus;
import Symbole.SymboleRond;
import Symbole.SymboleRondC;
import Symbole.SymboleRondP;
import Symbole.SymboleTriangle;
import Symbole.SymboleTriangleP;
import Symbole.SymboleVide;

import Convexe.PointD;


public class Outils {

    public final static String jpeg = "jpeg";
    public final static String jpg = "jpg";
    public final static String gif = "gif";
    public final static String tiff = "tiff";
    public final static String tif = "tif";
    public final static String png = "png";
    public final static String bmp = "bmp";
    public final static String laby = "laby";
    
    public static Vector<Classe> classesParDefaut = new Vector<Classe>();

    public static String getExtension(File fichier) {
        String extension = null;
        String nom = fichier.getName();
        int i = nom.lastIndexOf('.');

        if (i > 0 &&  i < nom.length() - 1) {
            extension = nom.substring(i+1).toLowerCase();
        }
        return extension;
    }
	
	public static Color melangeCouleurs(Color cAj, Color cBase, double fact) {
		int r = (int)(cBase.getRed() + fact*cAj.getRed());
		int g = (int)(cBase.getGreen() + fact*cAj.getGreen());
		int b = (int)(cBase.getBlue() + fact*cAj.getBlue());
		/*if(r > 255) r = 255;
		if(g > 255) g = 255;
		if(b > 255) b = 255;
		
		System.out.println(r);
		System.out.println(g);
		System.out.println(b);*/
		return new Color(r,g,b);
	}
		
	public static Vector<Classe> nommeEtColorie(Vector<Classe> classes, ECM ecm, int nbClasses) {
		double k = ecm.getk();
		Vector<Color> couleurs = new Vector<Color>();
		int[][] S = ecm.getS();
		double r = 0, g = 0, b = 0;
		String nom = "";
		boolean first = true;
		double n; //Cast pour eviter les successions d'arrondis;
		Classe classe;
		
		for(int i = 0; i < k; i++){
			if(i >= classesParDefaut.size()) classes.add(new Classe(i, new SymboleCroix()));
			n=0;
			r = 0;
			g = 0;
			b = 0;
			couleurs.clear();
			first = true;
			nom = "";
			classe = classes.elementAt(i);
			for(int j = 0; j < nbClasses; j++)
				{
				if(S[j][i] != 0){
					couleurs.add(classes.elementAt((int)Math.pow(2,j)).couleur);
					if(first) first = false;
					else nom += ", ";
					nom += "w"+ (j+1);
					classe.nom = nom;
					n++;
				}
			}
			
			if(n != 0 && n > 1){
				for(int j = 0; j < n; j++){
					r += (double)couleurs.elementAt(j).getRed()/n;
					g += (double)couleurs.elementAt(j).getGreen()/n;
					b += (double)couleurs.elementAt(j).getBlue()/n;
				}
				classe.couleur = new Color((int)r,(int)g,(int)b);
				
			} else{
				classe.composee = true;
				classe.estColoriee = true;
				classe.envDessinee = true;
				if(i >= classesParDefaut.size()) classe.couleur = new Color((int)(255*Math.random()),(int)(255*Math.random()),(int)(255*Math.random()));
			}
		}
		return classes;
	}
	
	public static void colorie(Vector<Classe> classes, ECM ecm, int nbClasses) {
		double k = ecm.getk();
		Vector<Color> couleurs = new Vector<Color>();
		int[][] S = ecm.getS();
		double r = 0, g = 0, b = 0;
		double n; //Cast pour eviter les successions d'arrondis;
		
		for(int i = 0; i < k; i++){
			n=0;
			r = 0;
			g = 0;
			b = 0;
			couleurs.clear();
			for(int j = 0; j < nbClasses; j++)
				{
				if(S[j][i] != 0){
					couleurs.add(classes.elementAt((int)Math.pow(2,j)).couleur);
					n++;
				}
			}
			
			if(n > 1){
				for(int j = 0; j < n; j++){
					r += (double)couleurs.elementAt(j).getRed()/n;
					g += (double)couleurs.elementAt(j).getGreen()/n;
					b += (double)couleurs.elementAt(j).getBlue()/n;
				}
				classes.elementAt(i).couleur = new Color((int)r,(int)g,(int)b);
				
			} 
		}
		
	}
	
	public static String number(String text){
		String result = "";
		for(int i = 0; i < text.length(); i++){
			char c = text.charAt(i);
			if(c >= 48 && c <= 58) result += c;
		}
		return result;
	}
	
	public static String doubleNumber (String text){
		String result = "";
		boolean firstDot = true;
		for(int i = 0; i < text.length(); i++){
			char c = text.charAt(i);
			if(c >= 48 && c <= 58) result += c;
			else if (firstDot && c == '.') {
				result += c;
				firstDot = false;
			}
		}
		if(result.charAt(result.length() - 1) == '.') result += '0';
		return result;
	}
	
	public static void initClassesParDef(){
		classesParDefaut.add(new Classe(0, new SymboleVide(), Color.black,"Vide"));
		classesParDefaut.add(new Classe(1, new SymbolePlus(), Color.red,"w1"));
		classesParDefaut.add(new Classe(2, new SymboleRond(), Color.blue,"w2"));
		classesParDefaut.add(new Classe(3, new SymboleRondC(), new Color(127,0,127),"w1, w2"));
		classesParDefaut.add(new Classe(4, new SymboleCarre(), Color.green,"w3"));
		classesParDefaut.add(new Classe(5, new SymboleCarreC(), new Color(127, 127 , 0),"w1, w3"));
		classesParDefaut.add(new Classe(6, new SymboleCarreP(), new Color(0,127,127),"w2, w3"));
		classesParDefaut.add(new Classe(7, new SymboleTriangle(), new Color(84,84,84),"w1, w2, w3"));
		classesParDefaut.add(new Classe(8, new SymboleDiamant(), new Color(255,255,0),"w4"));
		classesParDefaut.add(new Classe(9, new SymboleDiamantC(), new Color(255,127,0),"w1, w4"));
		classesParDefaut.add(new Classe(10, new SymboleDiamantP(), new Color(127,127,127),"w2, w4"));
		classesParDefaut.add(new Classe(11, new SymboleTriangleP(), new Color(168,84,84),"w1, w2, w4"));
		classesParDefaut.add(new Classe(12, new SymboleRondP(), new Color(127,255,0),"w3, w4"));
		classesParDefaut.add(new Classe(13, new SymboleParallelogrammeD(), new Color(168, 168, 0),"w1, w3, w4"));
		classesParDefaut.add(new Classe(14, new SymboleParallelogrammeDC(), new Color(84,168,84),"w2, w3, w4"));
		classesParDefaut.add(new Classe(15, new SymboleParallelogrammeDP(), new Color(126,126,63),"w1, w2, w3, w4"));

		classesParDefaut.elementAt(1).composee = false;
		classesParDefaut.elementAt(1).envDessinee = true;
		classesParDefaut.elementAt(1).estColoriee = true;
		
		classesParDefaut.elementAt(2).composee = false;
		classesParDefaut.elementAt(2).envDessinee = true;
		classesParDefaut.elementAt(2).estColoriee = true;

		classesParDefaut.elementAt(4).composee = false;
		classesParDefaut.elementAt(4).envDessinee = true;
		classesParDefaut.elementAt(4).estColoriee = true;

		classesParDefaut.elementAt(8).composee = false;
		classesParDefaut.elementAt(8).envDessinee = true;
		classesParDefaut.elementAt(8).estColoriee = true;
	}
}
