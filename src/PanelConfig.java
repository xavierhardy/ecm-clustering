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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Symbole.*;

public class PanelConfig extends JPanel {
	private static final long serialVersionUID = -6101538151168973957L;
	public Vector<Classe> classes;
	private int classeEnCours = 0;
	private ECM ecm;
	private int nbClasses;
	
	private SpringLayoutExt sl = new SpringLayoutExt(this);
	private Symbole[] listSymboles = {new SymboleCarre(), new SymboleCarreC(), new SymboleCarreP(), new SymboleCroix(), new SymboleDiamant(), new SymboleDiamantC(), new SymboleDiamantP(), new SymbolePlus(), new SymboleRond(), new SymboleRondC(), new SymboleRondP(), new SymboleTriangle(), new SymboleTriangleP(),new SymboleParallelogrammeD(),new SymboleParallelogrammeDC(),new SymboleParallelogrammeDP(),new SymboleParallelogrammeG(),new SymboleParallelogrammeGC(),new SymboleParallelogrammeGP(), new SymboleVide()};
	
	private JLabel labelTaille = new JLabel("Taille des symboles:");
	public JTextField textFieldTaille = new JTextField("4", 3);
	
	public JButton buttonAuto = new JButton("Configuration automatique des classes compos�es");
	public ColorCanvas canvasCouleur = new ColorCanvas();
	public HueCanvas canvasHue = new HueCanvas();

	private JLabel labelClasses = new JLabel("Classes:");
	private JComboBox<Classe> comboClasses;
	
	private JLabel labelSymbole = new JLabel("Symbole:");
	public JComboBox<Symbole> comboSymbole;
	public SymbolCanvas canvasSymbole = new SymbolCanvas();
	
	private JCheckBox boxEnveloppe = new JCheckBox("Enveloppe convexe");

	private JCheckBox boxCouleur = new JCheckBox("Couleur");
	private JTextField panneauCouleur = new JTextField();

	private JLabel labelR = new JLabel("R");
	public JFormattedTextField textFieldR = new JFormattedTextField("255");
	
	private JLabel labelV = new JLabel("V");
	public JFormattedTextField textFieldV = new JFormattedTextField("255");
	
	private JLabel labelB = new JLabel("B");
	public JFormattedTextField textFieldB = new JFormattedTextField("255");
	
	public float[] hsb = new float[3];
	
	
	public PanelConfig(Vector<Classe> classes, int tailleSymbole, ECM ecm, int nbClasses){
		this.ecm = ecm;
		this.nbClasses = nbClasses;
		this.setLayout(sl);

		this.classes = new Vector<Classe>();
		for(int i = 0; i < classes.size(); i++){
			this.classes.add(classes.elementAt(i).clone());
		}
		
		comboClasses = new JComboBox<Classe>(this.classes);
		
		comboSymbole = new JComboBox<Symbole>(listSymboles);
				
		this.add(labelTaille);
		sl.mettreAGauche(labelTaille);
		sl.mettreEnHaut(labelTaille);
		
		this.add(textFieldTaille);
		sl.mettreADroiteDe(textFieldTaille,labelTaille);
		sl.mettreEnHaut(textFieldTaille);
		textFieldTaille.addFocusListener(new NumberFieldFocusListener());
		textFieldTaille.setText(Integer.toString(tailleSymbole));
		
		this.add(buttonAuto);
		sl.mettreAGauche(buttonAuto);
		sl.mettreEnDessousDe(buttonAuto,textFieldTaille);
		buttonAuto.addActionListener(new ButtonAutoListener());
		
		this.add(canvasCouleur);
		sl.mettreADroiteDe(canvasCouleur,buttonAuto);
		sl.mettreEnHaut(canvasCouleur);
		canvasCouleur.setSize(170, 170);
		
		this.add(canvasHue);
		sl.mettreADroiteDe(canvasHue,canvasCouleur);
		sl.mettreEnHaut(canvasHue);
		canvasHue.setSize(24, 170);
		
		this.add(labelClasses);
		sl.mettreAGauche(labelClasses);
		sl.mettreEnDessousDe(labelClasses,buttonAuto);
		
		this.add(comboClasses);
		sl.mettreADroiteDe(comboClasses,labelClasses);
		sl.mettreEnDessousDe(comboClasses,buttonAuto);
		comboClasses.addActionListener(new ClassChangeListener());
		
		this.add(labelSymbole);
		sl.mettreAGauche(labelSymbole);
		sl.mettreEnDessousDe(labelSymbole,comboClasses);
		
		this.add(comboSymbole);
		sl.mettreADroiteDe(comboSymbole,labelSymbole);
		sl.mettreEnDessousDe(comboSymbole,comboClasses);
		comboSymbole.addActionListener(new SymbolChangeListener());
		
		this.add(canvasSymbole);
		canvasSymbole.setSize(26, 26);
		sl.mettreADroiteDe(canvasSymbole,comboSymbole);
		sl.mettreEnDessousDe(canvasSymbole,comboClasses);
		
		this.add(boxEnveloppe);
		sl.mettreAGauche(boxEnveloppe);
		sl.mettreEnDessousDe(boxEnveloppe,comboSymbole);
		
		this.add(boxCouleur);
		sl.mettreAGauche(boxCouleur);
		sl.mettreEnDessousDe(boxCouleur,boxEnveloppe);
		
		this.add(labelR);
		sl.mettreADroiteDe(labelR,boxCouleur);
		sl.mettreEnDessousDe(labelR,boxEnveloppe);
		
		this.add(textFieldR);
		sl.mettreADroiteDe(textFieldR,labelR);
		sl.mettreEnDessousDe(textFieldR,boxEnveloppe);
		textFieldR.setColumns(3);
		textFieldR.addFocusListener(new ColorChangeListener());

		this.add(labelV);
		sl.mettreADroiteDe(labelV,textFieldR);
		sl.mettreEnDessousDe(labelV,boxEnveloppe);
		
		this.add(textFieldV);
		sl.mettreADroiteDe(textFieldV,labelV);
		sl.mettreEnDessousDe(textFieldV,boxEnveloppe);
		textFieldV.setColumns(3);
		textFieldV.addFocusListener(new ColorChangeListener());
		
		this.add(labelB);
		sl.mettreADroiteDe(labelB,textFieldV);
		sl.mettreEnDessousDe(labelB,boxEnveloppe);
		
		this.add(textFieldB);
		sl.mettreADroiteDe(textFieldB,labelB);
		sl.mettreEnDessousDe(textFieldB,boxEnveloppe);
		textFieldB.setColumns(3);
		textFieldB.addFocusListener(new ColorChangeListener());
		
		this.add(panneauCouleur);
		sl.mettreADroiteDe(panneauCouleur,textFieldB);
		sl.mettreEnDessousDe(panneauCouleur,boxEnveloppe);
		panneauCouleur.setColumns(3);
		panneauCouleur.setEnabled(false);
        this.setVisible(true);
		miseAJourClasse();
		canvasHue.repaint();
		canvasCouleur.repaint();
	}
	
	public void miseAJour(){
		panneauCouleur.setBackground(new Color(Integer.parseInt(textFieldR.getText()),Integer.parseInt(textFieldV.getText()), Integer.parseInt(textFieldB.getText())));
	}
	
	public void sauverClasse(){
		Classe ancienneClasse = classes.elementAt(classeEnCours);
		ancienneClasse.envDessinee = boxEnveloppe.isSelected();
		ancienneClasse.estColoriee = boxCouleur.isSelected();
		int r = Integer.parseInt(textFieldR.getText());
		int g = Integer.parseInt(textFieldV.getText());
		int b = Integer.parseInt(textFieldB.getText());
		ancienneClasse.couleur = new Color(r,g,b);
		ancienneClasse.symbole = (Symbole) comboSymbole.getSelectedItem();
		classeEnCours = comboClasses.getSelectedIndex();
	}
	
	public void miseAJourClasse(){
		Classe classe = classes.elementAt(classeEnCours);
		boxEnveloppe.setSelected(classe.envDessinee);
		boxCouleur.setSelected(classe.estColoriee);
		comboSymbole.setSelectedItem(classe.symbole);
		textFieldR.setText(Integer.toString(classe.couleur.getRed()));
		textFieldV.setText(Integer.toString(classe.couleur.getGreen()));
		textFieldB.setText(Integer.toString(classe.couleur.getBlue()));
		miseAJour();
		canvasSymbole.repaint();
		hsb = Color.RGBtoHSB(classe.couleur.getRed(),classe.couleur.getGreen(),classe.couleur.getBlue(),null);
		canvasCouleur.repaint();
	}
	
	public float[] getHSB(){
		int r = Integer.parseInt(textFieldR.getText());
		int g = Integer.parseInt(textFieldV.getText());
		int b = Integer.parseInt(textFieldB.getText());
		return Color.RGBtoHSB(r, g, b, null);
	}
	
/*	public AbstractRandom getDistrib(){
		return listDistrib[comboDistrib.getSelectedIndex()];
	}
	
	public FormeAbstraite getForme(){
		return listFormes[comboFormes.getSelectedIndex()];
	}*/
	
	class ButtonAutoListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			Outils.colorie(classes, ecm, nbClasses);
			miseAJourClasse();
		}
	}
	
	class SymbolChangeListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			canvasSymbole.repaint();
		}
	}	
	
	class ClassChangeListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			sauverClasse();
			miseAJourClasse();
		}
	}	
	
	class ColorChangeListener implements FocusListener{
		public void focusGained(FocusEvent e) {
		}

		public void focusLost(FocusEvent e) {
			int newValue = Integer.parseInt(((JTextField)e.getSource()).getText());
			if(newValue > 255) ((JTextField)e.getSource()).setText("255");
			else if (newValue < 0) ((JTextField)e.getSource()).setText("0");
			miseAJour();
		}
	}
	
	class SymbolCanvas extends Canvas{
		private static final long serialVersionUID = 2503253675587856695L;

		public void paint(Graphics g){
			int size = canvasSymbole.getHeight();
			g.setColor(Color.white);
			g.fillRect(1, 1, size-1, size-1);
			g.setColor(Color.black);
			g.drawRect(0, 0, size-1, size-1);
			listSymboles[comboSymbole.getSelectedIndex()].dessine(g, size/2, size/2, size-6);
		}
	}
	
	class ColorCanvas extends Canvas implements MouseMotionListener{
		private static final long serialVersionUID = -231531604223290769L;
		private int x;
		private int y;
		private int taille = 2; //On dessine des carres pour accelerer l'affichage
		private BufferedImage buffer;
		
		public ColorCanvas(){
			this.addMouseMotionListener(this);
		}
		
	    public void update(Graphics g) {
	    	paint(g);
	    }
		
		public void paint(Graphics g){
			if(buffer == null) buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics gBuffer = buffer.getGraphics();
			for(int i = 0; i < getWidth()/taille; i++){
				for(int j = 0; j < getHeight()/taille; j++){
					gBuffer.setColor(Color.getHSBColor(hsb[0],(float)(i*taille)/getWidth(), (float)(j*taille)/getHeight()));
					gBuffer.fillRect(i*taille, j*taille, (i+1)*taille, (j+1)*taille);
				}
			}
			gBuffer.setColor(Color.black);
			gBuffer.drawRect(0, 0, getWidth()-1, getHeight()-1);
			g.drawImage(buffer, 0, 0, null);
		}

		public void mouseDragged(MouseEvent e) {

			if(e.getX() >= 0 && e.getX() < getWidth() && e.getY() >= 0 && e.getY() < getHeight()){
				x = e.getX();
				y = e.getY();
			}
			
			float[] hsb2 = getHSB();
			hsb2[1] = (float)(x)/getHeight();
			hsb2[2] = (float)(y)/getHeight();
			classes.elementAt(classeEnCours).couleur = new Color(Color.HSBtoRGB(hsb2[0],hsb2[1],hsb2[2]));
			miseAJourClasse();
			hsb = hsb2;
		}

		public void mouseMoved(MouseEvent e) {
		}
	}
	
	class HueCanvas extends Canvas implements MouseMotionListener{
		private static final long serialVersionUID = -194748212992096976L;
		private int y;
		
		public HueCanvas(){
			this.addMouseMotionListener(this);
		}
		
		public void paint(Graphics g){
			for(int i = 0; i < getHeight(); i++){
				g.setColor(Color.getHSBColor((float)(i)/getHeight(), 1, 1));
				g.drawLine(0, i, getWidth(), i);
			}
			g.setColor(Color.black);
			g.drawRect(0, 0, getWidth()-1, getHeight()-1);
		}

		public void mouseDragged(MouseEvent e) {
			if(e.getX() >= 0 && e.getX() < getWidth() && e.getY() >= 0 && e.getY() < getHeight()) y = e.getY();
			float[] hsb2 = getHSB();
			hsb2[0] = (float)(y)/getHeight();
			classes.elementAt(classeEnCours).couleur = new Color(Color.HSBtoRGB(hsb2[0],hsb2[1],hsb2[2]));
			miseAJourClasse();
			hsb = hsb2;
		}

		public void mouseMoved(MouseEvent e) {
		}
	}
}