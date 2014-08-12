/*
    Copyright 2014, Xavier Hardy, ClÃ©ment Pique

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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Convexe.Convexe;
import Convexe.PointD;
import Forme.FormeAbstraite;
import Random.AbstractRandom;
import Symbole.*;

import java.awt.Graphics;
import java.util.Iterator;
import java.util.Vector;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Fenetre extends JFrame{
	private Fenetre fenetre = this;
	private File fichierEnCours = new File("temp.csv");
	
	private JPanel container = new JPanel();
	private JMenuBar menu = new JMenuBar();
	
	//Menus principaux dans la barre de menu
	private JMenu menuFichier = new JMenu("Fichier");
	private JMenu menuClassification = new JMenu("Classification");
	private JMenu menuAffichage = new JMenu("Affichage");
	
	//Menu dans l'onglet Fichier
	private JMenuItem itemNouveau = new JMenuItem("Nouveau");
	private JMenuItem itemOuvrir = new JMenuItem("Ouvrir");
	private JMenuItem itemEnregistrer = new JMenuItem("Enregistrer");
	private JMenuItem itemEnregistrerSous = new JMenuItem("Enregistrer sous...");
	private JMenuItem itemImporter = new JMenuItem("Importer");
	private JMenu menuExporter = new JMenu("Exporter");
	private JMenuItem itemQuitter = new JMenuItem("Quitter");
	
	//Sous-menus de l'onglet Fichier
	private JMenuItem itemImage = new JMenuItem("Image");
	private JMenuItem itemTableau = new JMenuItem("Tableau");
	
	//Menus dans l'onglet Affichage
	private JMenu menuStyle = new JMenu("Style de points");
	private JMenu menuConvexe = new JMenu("Enveloppes convexes");
	private JMenuItem itemConfigAff = new JMenuItem("Configurer");
	
	//Sous-menus de l'onglet Affichage
	private JMenu menuCouleurs = new JMenu("Couleurs");
	private JCheckBoxMenuItem itemCouleur = new JCheckBoxMenuItem("Activees");
	private JMenuItem itemCoulLarge = new JCheckBoxMenuItem("Larges");
	private JMenuItem itemDegradees = new JCheckBoxMenuItem("Degradees");
	
	private JMenuItem itemNumero = new JCheckBoxMenuItem("Numero");
	private JMenuItem itemSymbole = new JCheckBoxMenuItem("Symbole");
	
	private JCheckBoxMenuItem[] itemsEnv = {new JCheckBoxMenuItem("Aucune"), new JCheckBoxMenuItem("Restreintes"), new JCheckBoxMenuItem("Larges")};
	private JMenuItem itemCouleurEnv = new JCheckBoxMenuItem("Couleur");

	//Sous-menus de l'onglet Classification

	private JMenuItem itemECM = new JMenuItem("ECM");
	
	public Vector<Classe> vectClasses = new Vector<Classe>();
	
	double[][] points= new double[0][0];
	private int n = 0; //Nombre de points
	public int nbClasses = 4;
	private int dim = 2;
	
	//private double[] cam = new double[3];
	//private double[] orientation = new double[3];
	
	private Panneau pan = new Panneau();
	private ECM ecm = null;
	
	//Panneau d'information (nord)
	private JLabel labelInfo = new JLabel("Taper un nombre et cliquer sur Générer");

	//Enveloppe
	private int enveloppe = 0; //0 aucune, 1 restreinte, 2 large
	
	public int symbolSize = 4;
		
	public void activerECM() {
		itemECM.setEnabled(true);
		itemEnregistrer.setEnabled(true);
		itemEnregistrerSous.setEnabled(true);
		menuExporter.setEnabled(true);
		menuAffichage.setEnabled(false);
	}
	
	public void activerMenu() {
		menuAffichage.setEnabled(true);
		vectClasses = (Vector<Classe>) Outils.classesParDefaut.clone();
		vectClasses = Outils.nommeEtColorie(vectClasses, ecm, nbClasses);
	}
	
	public void sauver(File f) {
		try {
			OutputStreamWriter destination = new OutputStreamWriter(new FileOutputStream(f));
			
			destination.write(Integer.toString(n));
			destination.write(';');
			destination.write(Integer.toString(dim));
			destination.write(';');
			destination.write(Integer.toString(nbClasses));
			destination.write('\n');
			
			int k = OutilsMatriciels.exposant(nbClasses);
			double[][] M = ecm.getM();

			for(int i = 0; i < n; i++){
				for(int j = 0; j < dim; j++){
					destination.write(Double.toString(points[i][j]));
					if(j != dim - 1) destination.write(';');
					else destination.write('\n');
				}
			}
			destination.write('\n');
			
			for(int i = 0; i < n; i++){
				for(int j = 0; j < k; j++){
					destination.write(Double.toString(M[i][j]));
					if(j != k - 1) destination.write(';');
					else destination.write('\n');
				}
			}
			destination.close();
			
		} catch (FileNotFoundException exception) {
			JOptionPane.showMessageDialog(fenetre,	"Fichier introuvable.","Erreur",JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
		}
		 catch (IOException exception) {
			JOptionPane.showMessageDialog(fenetre,	"Impossible d'ouvrir ce fichier. Ce fichier est problablement utilise par une autre application.","Erreur",JOptionPane.ERROR_MESSAGE);
			exception.printStackTrace();
		}
	}
	
	public void exporterImage(File f) throws IOException {
		ImageIO.write(pan.getImage(), Outils.getExtension(f), f);
	}
	
	public Fenetre(){
		Outils.initClassesParDef();
		this.setTitle("Algorithme ECM");
		this.setSize(700,700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowStateListener(new StateListener());
		
		container.setLayout(new BorderLayout());
		
		JPanel north = new JPanel();
		
		north.add(labelInfo);
		
		container.add(pan,BorderLayout.CENTER);
		container.add(north,BorderLayout.NORTH);
		
		//Menu Fichier
		menuExporter.add(itemImage);
		menuExporter.addSeparator();
		menuExporter.add(itemTableau);
		
		menuFichier.add(itemNouveau);
		menuFichier.add(itemOuvrir);
		menuFichier.addSeparator();
		menuFichier.add(itemEnregistrer);
		menuFichier.add(itemEnregistrerSous);
		menuFichier.addSeparator();
		menuFichier.add(itemImporter);
		menuFichier.add(menuExporter);
		menuFichier.addSeparator();
		menuFichier.add(itemQuitter);
		
		menu.add(menuFichier);
		
		//Menu Affichage
		menuStyle.add(menuCouleurs);
		menuStyle.add(itemNumero);
		menuStyle.add(itemSymbole);
		
		menuCouleurs.add(itemCouleur);
		itemCouleur.addActionListener(new itemCouleurListener());
		menuCouleurs.addSeparator();
		menuCouleurs.add(itemDegradees);	
		menuCouleurs.add(itemCoulLarge);		
		
		for(int i = 0; i < 3; i++){
			menuConvexe.add(itemsEnv[i]);
			itemsEnv[i].addActionListener(new itemEnvListener(i));
		}
		itemsEnv[0].setSelected(true);
		menuConvexe.addSeparator();
		menuConvexe.add(itemCouleurEnv);
		
		menuAffichage.add(menuStyle);
		menuAffichage.add(menuConvexe);
		menuAffichage.addSeparator();
		menuAffichage.add(itemConfigAff);
		
		itemConfigAff.addActionListener(new ItemConfigListener());
		
		itemECM.setEnabled(false);
		itemECM.addActionListener(new ItemECMListener());
		
		menuAffichage.setEnabled(false);
		menu.add(menuAffichage);
		menu.add(menuClassification);
		menuClassification.add(itemECM);
		
		this.setJMenuBar(menu);
		
		itemNouveau.addActionListener(new itemNouveauListener());
		itemOuvrir.addActionListener(new itemOuvrirListener());
		itemEnregistrer.addActionListener(new itemEnregistrerListener());
		itemEnregistrer.setEnabled(false);
		itemEnregistrerSous.addActionListener(new itemEnregistrerSousListener());
		itemEnregistrerSous.setEnabled(false);
		menuExporter.setEnabled(false);
		itemImporter.addActionListener(new itemImporterListener());
		itemQuitter.addActionListener(new itemQuitterListener());
		
		itemImage.addActionListener(new itemExporterImageListener());
		itemTableau.addActionListener(new itemExporterTableauListener());
		
		//ok.addActionListener(new BoutonOkListener());
		itemNumero.addActionListener(new itemNumeroListener());
		itemSymbole.addActionListener(new itemSymboleListener());

		itemCouleurEnv.addActionListener(new itemActionListener());
		itemDegradees.addActionListener(new itemActionListener());
		itemCoulLarge.addActionListener(new itemActionListener());
		
		menuConvexe.add(itemCouleurEnv);
		
		itemSymbole.setSelected(true);
		
		this.setContentPane(container);
		this.setVisible(true);
	}

	class ItemECMListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			new FenetreECM();
			setEnabled(false);
		}
	}

	class ItemConfigListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			new FenetreConfig();
			setEnabled(false);
		}
	}	
	
	class StateListener implements WindowStateListener{
		public void windowStateChanged(WindowEvent e) {
			pan.initBuffer();
			pan.repaint();
		}
	}	
	
	
	
	class itemNouveauListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			new FenetreNouveau();
			setEnabled(false);
		}
	}
	
	class itemOuvrirListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JFileChooser selecteur = new JFileChooser();
	        selecteur.addChoosableFileFilter(new FiltreFichiersSimple("csv","Fichier CSV, Points et resultats","Fichier CSV, Points et resultats (*.csv)"));
	        selecteur.setAcceptAllFileFilterUsed(false);
			int reponse = selecteur.showOpenDialog(fenetre);
			if(reponse == selecteur.APPROVE_OPTION) {
				File f = selecteur.getSelectedFile();
        		charger(f);
        	}
		}
		
		private void charger(File f) {
			try {
				//InputStreamReader reader = new InputStreamReader(new FileInputStream(f));
				BufferedReader lecteur = new BufferedReader(new FileReader(f));
				
				String[] colonnes = lecteur.readLine().split(";");
				
				//Pour simplifier le travail du programme, on ajoute une première ligne qui contient le nombre de points, le nombre de dimensions, et de classes
				n = Integer.parseInt(colonnes[0]);
				dim = Integer.parseInt(colonnes[1]);
				nbClasses = Integer.parseInt(colonnes[2]);
				
				System.out.println(n + " ; " + dim);
				points = new double[n][dim];
				
				int k = OutilsMatriciels.exposant(nbClasses);
				double[][] M = new double[n][k];
				
				for(int i = 0; i < n; i++){
					colonnes = lecteur.readLine().split(";");
					for(int j = 0; j < dim; j++){
						points[i][j] = Double.parseDouble(colonnes[j]);
					}
				}
				OutilsMatriciels.imprime(points);
				System.out.println();
				lecteur.readLine();
				
				for(int i = 0; i < n; i++){
					colonnes = lecteur.readLine().split(";");
					for(int j = 0; j < k; j++){
						M[i][j] = Double.parseDouble(colonnes[j]);
					}
				}
				OutilsMatriciels.imprime(M);
				lecteur.close();
				ecm = new ECM(points, nbClasses, M);
				activerECM();
				activerMenu();
				pan.init();
				
			} catch (FileNotFoundException exception) {
				JOptionPane.showMessageDialog(fenetre,	"Fichier introuvable.","Erreur",JOptionPane.ERROR_MESSAGE);
				exception.printStackTrace();
			}
			 catch (IOException exception) {
				JOptionPane.showMessageDialog(fenetre,	"Impossible d'ouvrir ce fichier. Ce fichier est problablement utilise par une autre application.","Erreur",JOptionPane.ERROR_MESSAGE);
				exception.printStackTrace();
			}
		}
	}
	
	class itemEnregistrerListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			sauver(fichierEnCours);
		}
	}
	
	class itemEnregistrerSousListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JFileChooser selecteur = new JFileChooser();
	        selecteur.addChoosableFileFilter(new FiltreFichiersSimple("csv","Fichier CSV, Points et resultats","Fichier CSV, Points et resultats (*.csv)"));
	        selecteur.setAcceptAllFileFilterUsed(false);
			int reponse = selecteur.showSaveDialog(fenetre);
			if(reponse == selecteur.APPROVE_OPTION) {
				File f = selecteur.getSelectedFile();
        		sauver(f);
        	}
		}
	}
	
	class itemImporterListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JFileChooser selecteur = new JFileChooser();
	        selecteur.addChoosableFileFilter(new FiltreFichiersSimple("csv","Fichier CSV, Ensemble de points","Fichier CSV, Ensemble de points (*.csv)"));
	        selecteur.setAcceptAllFileFilterUsed(false);
			int reponse = selecteur.showOpenDialog(fenetre);
			if(reponse == selecteur.APPROVE_OPTION) {
				File f = selecteur.getSelectedFile();
        		importer(f);
        	}
		}

		private void importer(File f) {
			try {
				//InputStreamReader reader = new InputStreamReader(new FileInputStream(f));
				BufferedReader lecteur = new BufferedReader(new FileReader(f));
				
				String[] colonnes = lecteur.readLine().split(";");
				
				//Pour simplifier le travail du programme, on ajoute une première ligne qui contient le nombre de points et le nombre de dimensions
				n = Integer.parseInt(colonnes[0]);
				dim = Integer.parseInt(colonnes[1]);
				
				System.out.println(n + " ; " + dim);
				points = new double[n][dim];
				
				for(int i = 0; i < n; i++){
					colonnes = lecteur.readLine().split(";");
					for(int j = 0; j < dim; j++){
						points[i][j] = Double.parseDouble(colonnes[j]);
					}
				}
				OutilsMatriciels.imprime(points);
				pan.init();
				activerECM();
			} catch (FileNotFoundException exception) {
				JOptionPane.showMessageDialog(fenetre,	"Fichier introuvable.","Erreur",JOptionPane.ERROR_MESSAGE);
				exception.printStackTrace();
			}
			 catch (IOException exception) {
				JOptionPane.showMessageDialog(fenetre,	"Impossible d'ouvrir ce fichier. Ce fichier est problablement utilise par une autre application.","Erreur",JOptionPane.ERROR_MESSAGE);
				exception.printStackTrace();
			}
		}
	}
	
	class itemExporterTableauListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JFileChooser selecteur = new JFileChooser();
	        selecteur.addChoosableFileFilter(new FiltreFichiersSimple("csv","Fichier CSV, Resultats","Fichier CSV, Resultats (*.csv)"));
	        selecteur.addChoosableFileFilter(new FiltreFichiersSimple("csv","Fichier LaTeX","Fichier LaTeX (*.tex)"));
	        selecteur.setAcceptAllFileFilterUsed(false);
	         
			int reponse = selecteur.showSaveDialog(fenetre);
			if(reponse == selecteur.APPROVE_OPTION) {
				File f = selecteur.getSelectedFile();
				try {
					if(Outils.getExtension(f).equals("csv")) ecm.exporte(f);
					else ecm.exporteLaTeX(f);
				} catch (IOException exception) {
					JOptionPane.showMessageDialog(fenetre,	"Impossible d'exporter dans ce fichier. Le fichier est problablement utilise par une autre application.","Erreur",JOptionPane.ERROR_MESSAGE);
					exception.printStackTrace();
				}
        	}
		}
	}
	
	class itemExporterImageListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JFileChooser selecteur = new JFileChooser();
	        //selecteur.addChoosableFileFilter(new FiltreFichiersSimple("bmp","Image BMP","Image BMP (*.bmp)"));
	        selecteur.addChoosableFileFilter(new FiltreFichiersSimple("gif","Image GIF","Image GIF (*.gif)"));
	        //selecteur.addChoosableFileFilter(new FiltreFichiersSimple("jpeg","jpg","Image JPEG","Image JPEG (*.jpg, *.jpeg)"));
	        selecteur.addChoosableFileFilter(new FiltreFichiersSimple("png","Image PNG","Image PNG (*.png)"));
	        selecteur.addChoosableFileFilter(new FiltreFichiersSimple("tiff","tif","Image TIFF","Image TIFF (*.tiff, *.tif)"));
	        selecteur.addChoosableFileFilter(new FiltreFichiersImages());
	        selecteur.setAcceptAllFileFilterUsed(false);
	         
			int reponse = selecteur.showSaveDialog(fenetre);
			
			if(reponse == selecteur.APPROVE_OPTION) {
				File f = selecteur.getSelectedFile();
				try {
					exporterImage(f);
				} catch (IOException exception) {
					JOptionPane.showMessageDialog(fenetre,	"Impossible d'ecrire dans ce fichier. Le fichier est problablement utilise par une autre application.","Erreur",JOptionPane.ERROR_MESSAGE);
					exception.printStackTrace();
				}
        	}
		}
	}
	
	class itemQuitterListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	class itemActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			pan.repaint();
		}
	}
	
	class itemCouleurListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(itemCouleur.isSelected()) {
				itemSymbole.setSelected(false);
				itemNumero.setSelected(false);
				
				pan.repaint();
			}
			else {
				itemSymbole.setSelected(true);
				itemNumero.setSelected(false);
				
				pan.repaint();
			}
		}
	}
	
	class itemEnvListener implements ActionListener{
		private int i;
		public itemEnvListener(int i){
			super();
			this.i = i;
		}
		
		public void actionPerformed(ActionEvent e) {
			if(itemsEnv[i].isSelected()) {
				for(int j = 0; j < 3; j++) itemsEnv[j].setSelected(false);
				itemsEnv[i].setSelected(true);
				enveloppe = i;
				if(i>0) pan.initConvexe();
				pan.repaint();
			}
			else itemsEnv[i].setSelected(true);
		}
	}
	
	class itemNumeroListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(itemNumero.isSelected()) {
				itemSymbole.setSelected(false);
				itemCouleur.setSelected(false);
				pan.repaint();
			}
			else itemNumero.setSelected(true);
		}
	}
	
	class itemSymboleListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(itemSymbole.isSelected()) {
				itemNumero.setSelected(false);
				itemCouleur.setSelected(false);
				pan.repaint();
			}
			else itemSymbole.setSelected(true);
		}
	}
	
	class FenetreNouveau extends JFrame{
		public FenetreNouveau fenNouveau = this;
		private JPanel panPrincipal = new JPanel();
		private JPanel panBoutons = new JPanel();
		private JButton boutonOK = new JButton("OK");
		private JButton boutonAnnuler = new JButton("Annuler");
		private PanelNouveau panNouveau = new PanelNouveau();
		
		public FenetreNouveau(){
			this.setTitle("Nouveau");
		    this.setSize(335, 255);
			this.setVisible(true);
		    this.setLocationRelativeTo(null);
		    this.setResizable(false); 
		   	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		   	panPrincipal.setLayout(new BorderLayout());
	        panPrincipal.add(panNouveau, BorderLayout.CENTER);
	      
	        panPrincipal.add(panBoutons, BorderLayout.SOUTH);
	        panBoutons.setLayout(new FlowLayout());
	         
	        panBoutons.add(boutonOK);
	        boutonOK.addActionListener(new BoutonOkListener());

	        panBoutons.add(boutonAnnuler);
	        boutonAnnuler.addActionListener(new BoutonAnnulerListener());
	         
	        this.setContentPane(panPrincipal);
	        this.setVisible(true);
		}
		
		class BoutonAnnulerListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				fenetre.setEnabled(true);
				fenNouveau.setVisible(false);
			}
		}
		
		class BoutonOkListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				
				n = Integer.parseInt(panNouveau.textFieldNbPoints.getText());
				int nbClassesR = Integer.parseInt(panNouveau.textFieldNbClasses.getText());
				PointD[] centres = new PointD[nbClassesR];
				
				double r =  Double.parseDouble(panNouveau.textFieldEloignement.getText());
				
				Object[] optionsCentres = panNouveau.getCentres();
				FormeAbstraite forme = panNouveau.getForme();
				forme.setParams(panNouveau.getDistrib(), Double.parseDouble(panNouveau.textFieldDiametre.getText()));
				

				AbstractRandom rndCentre = null;
				
				boolean centresAleatoires = ((Boolean)optionsCentres[0]).booleanValue();
				
				if(centresAleatoires) rndCentre = (AbstractRandom) optionsCentres[1];
				
				fenetre.setEnabled(true);
				fenNouveau.setVisible(false);
				points = new double[n][dim];

				double theta = 2*Math.PI / (double)nbClassesR;
				System.out.println();
				System.out.println("theta = " + theta);
				
				if(centresAleatoires){
					for (int i = 0; i < nbClassesR; i++){ 
						double ra = rndCentre.next() * r;
						double th = Math.random()*Math.PI*2; //Toujours uniforme en theta
						centres[i] = new PointD(ra * Math.cos(th), ra * Math.sin(th));
					}
				}
				else {
					for (int i = 0; i < nbClassesR; i++){
						double th = i*theta; //Toujours uniforme en theta
						centres[i] = new PointD(r * Math.cos(th), r * Math.sin(th));
					}
				}
				
				for (int i = 0; i<n; i++){
					int classe = (int)(Math.random()*nbClassesR);
					PointD next = forme.next(centres[classe].x, centres[classe].y);
					points[i][0] = next.x;
					points[i][1] = next.y;
				}
				
				ecm = null;//new ECM(points, nbClasses);
				vectClasses.clear();

				vectClasses.add(Outils.classesParDefaut.elementAt(0));
				pan.init();
				pan.initConvexe();
				
			}
		}
	}
	
	class FenetreECM extends JFrame{
		public FenetreECM fenECM = this;
		private JPanel panPrincipal = new JPanel();
		private JPanel panBoutons = new JPanel();
		private JButton boutonOK = new JButton("OK");
		private JButton boutonAnnuler = new JButton("Annuler");
		private PanelECM panECM = new PanelECM();
		
		public FenetreECM(){
			this.setTitle("ECM");
		    this.setSize(200, 190);
			this.setVisible(true);
		    this.setLocationRelativeTo(null);
		    this.setResizable(false); 
		   	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		   	panPrincipal.setLayout(new BorderLayout());
	        panPrincipal.add(panECM, BorderLayout.CENTER);
	      
	        panPrincipal.add(panBoutons, BorderLayout.SOUTH);
	        panBoutons.setLayout(new FlowLayout());
	         
	        panBoutons.add(boutonOK);
	        boutonOK.addActionListener(new BoutonOkListener());

	        panBoutons.add(boutonAnnuler);
	        boutonAnnuler.addActionListener(new BoutonAnnulerListener());
	         
	        this.setContentPane(panPrincipal);
	        this.setVisible(true);
		}
		
		class BoutonAnnulerListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				fenetre.setEnabled(true);
				fenECM.setVisible(false);
			}
		}
		
		class BoutonOkListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				nbClasses = Integer.parseInt(panECM.textFieldNbClasses.getText());
				double alpha = Double.parseDouble(panECM.textFieldAlpha.getText());
				double beta = Double.parseDouble(panECM.textFieldBeta.getText());
				double delta = Double.parseDouble(panECM.textFieldDelta.getText());
				double epsilon = Double.parseDouble(panECM.textFieldEpsilon.getText());
				
								
				ecm = new ECM(points, nbClasses, alpha, beta, delta, epsilon);
				pan.init();
				pan.initConvexe();
				fenetre.setEnabled(true);
				fenECM.setVisible(false);
			}
		}
	}
	
	class FenetreConfig extends JFrame{
		public FenetreConfig fenConfig = this;
		private JPanel panPrincipal = new JPanel();
		private JPanel panBoutons = new JPanel();
		private JButton boutonOK = new JButton("OK");
		private JButton boutonAnnuler = new JButton("Annuler");
		private PanelConfig panConfig = new PanelConfig(vectClasses, symbolSize, ecm, nbClasses);
		
		public FenetreConfig(){
			this.setTitle("Configuration de l'affichage");
		    this.setSize(548, 242);
			this.setVisible(true);
		    this.setLocationRelativeTo(null);
		    this.setResizable(false); 
		   	this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		   	panPrincipal.setLayout(new BorderLayout());
	        panPrincipal.add(panConfig, BorderLayout.CENTER);
	      
	        panPrincipal.add(panBoutons, BorderLayout.SOUTH);
	        panBoutons.setLayout(new FlowLayout());
	         
	        panBoutons.add(boutonOK);
	        boutonOK.addActionListener(new BoutonOkListener());

	        panBoutons.add(boutonAnnuler);
	        boutonAnnuler.addActionListener(new BoutonAnnulerListener());
	         
	        this.setContentPane(panPrincipal);
	        this.setVisible(true);
		}
		
		class BoutonAnnulerListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				fenetre.setEnabled(true);
				fenConfig.setVisible(false);
			}
		}
		
		class BoutonOkListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				panConfig.sauverClasse();
				vectClasses = new Vector<Classe>();
				symbolSize = Integer.parseInt(panConfig.textFieldTaille.getText());
				Iterator<Classe> iter = panConfig.classes.iterator();
				while(iter.hasNext()){
					Classe next = iter.next();
					vectClasses.add(next.clone());
				}
				fenetre.setEnabled(true);
				pan.initConvexe();
				pan.repaint();
				fenConfig.setVisible(false);
			}
		}
	}
	
	

	class Panneau extends Canvas implements MouseMotionListener, MouseListener, MouseWheelListener{		
		private boolean dessine = false;
		
		//Centre du canvas
		private double xCam = 0;
		private double yCam = 0;
		
		private double zoom = 1000;
		
		//Vitesse du scrolling, et du zoom
		private double speed = 0.01;
		private double zoomSpeed = 10;
		
		//Coordonnees necessaires pour calculer le mouvement de la souris
		private int xCursor = 0;
		private int yCursor = 0;
		
		private double xMin = 0;
		private double yMin = 0;
		
		private double xMax = 0;
		private double yMax = 0;
		
		//3D
		/*private double translateSpd = 0.1;
		private double translateYSpd = 100;
		private double rotationSpd = 0.1;
		
		//Angle que fait la camera avec les differents axes
		double tx = 0,ty = 0,tz = 0;
		
		//Position de la camera
		double cx = 0,cy = 0,cz = 1;
		
		//Position de l'oeil relative a la surface de projection, determine le champ de perception
		double ex = 0,ey = 0,ez = 10;
		*/
		
		private int[] classes = new int[points.length];
		
		private double[][] M;
		private double[][] N;
		
		private SymboleCase symboleCase = new SymboleCase();
		
		//Dessins symboles
		private Vector<Symbole> symbolesClasses = new Vector<Symbole>();
		
		//Controle Souris
		private int boutonSouris = 0; //DROITE = 3, GAUCHE = 1
		
		//Double buffering (affiche sans que l'image clignote, permet aussi de recuperer l'image pour l'enregistrer)
		private BufferedImage buffer;
		
		public Panneau(){
		}
		
		public void initBuffer() {
			buffer.flush();
			buffer = null;
		}

		public BufferedImage getImage() {
			return buffer;
		}

		public void initConvexe(){
			Iterator<Classe> iter = vectClasses.iterator();
			while(iter.hasNext()){
				Classe next = iter.next();
				next.convexe = new Convexe();
			}
			
			if(enveloppe == 1)
			{
				for(int i = 0; i < points.length; i++){
					Classe classe = vectClasses.elementAt(classes[i]);
					if(classe.envDessinee) {
						classe.convexe.ajoute(points[i][0],points[i][1]);
					}
				}
			}
			else
			{
				for(int i = 0; i < points.length; i++){
					Classe classe = vectClasses.elementAt(classes[i]);
					iter = vectClasses.iterator();
					while(iter.hasNext()){
						Classe next = iter.next();
						if(next.index == classes[i] || (next.index < classes[i] && ecm.getK()[next.index][classes[i]] != 0)) next.convexe.ajoute(points[i][0],points[i][1]);
					}
					/*for(int j = classes[i]; j < ecm.getk()){
						if(classe.envDessinee || ecm.getK()[j][classes[i]] != 0) classe.convexe.ajoute(points[i][0],points[i][1]);
					}*/
					//if(classe.envDessinee || classes[i] > classeConv) classe.convexe.ajoute(points[i][0],points[i][1]);
					/*
					for(int j = 0; j < n; j++){
						
						int classeConv = listConv.elementAt(j).intValue();
						if(classes[i] == classeConv || ( classes[i] > classeConv && ecm.getK()[listConv.elementAt(j).intValue()][classes[i]] != 0)) conv.elementAt(j).ajoute(points[i][0],points[i][1]);
					}
					*/
				}
			}
			
			iter = vectClasses.iterator();
			while(iter.hasNext()){
				Classe next = iter.next();
				if(next.envDessinee){
					next.convexe.calculBarycentre();
					next.convexe.enveloppe();
				}
			}			
		}
		
		public void init(){
			int k;
			fenetre.activerECM();
			
			if(ecm == null) {
				k = 4;
				M = new double[n][k];
				N = new double[n][k];
			}
			else
			{
				fenetre.activerMenu();
				System.out.println("Calcul de N...");
				ecm.calculN();
				M = ecm.getM();
				N = ecm.getN();
				k = ecm.getk();
			}
			
			classes = new int[points.length];
			
			double max = 0;
			int jMax = 0;
			for (int i = 0; i < points.length; i++)
			{
				for (int j = 0; j < k; j++)
				{
					if(M[i][j] > max) {
						max = M[i][j];
						jMax = j;
					}
				}
				classes[i] = jMax;
				max = 0;
				jMax = 0;
			}
			
			//System.out.println("Calcul enveloppes convexes des classes selectionnees");
			//initConvexe();
			
			addMouseWheelListener(this);
			addMouseMotionListener(this);
			addMouseListener(this);
			
			symbolesClasses.add(new SymboleVide());
			symbolesClasses.add(new SymbolePlus());
			symbolesClasses.add(new SymboleRond());
			symbolesClasses.add(new SymboleTriangle());
			symbolesClasses.add(new SymboleCroix());
			symbolesClasses.add(new SymboleCarre());
			symbolesClasses.add(new SymboleDiamant());
			symbolesClasses.add(new SymboleTriangleP());
			symbolesClasses.add(new SymboleCarreP());
			symbolesClasses.add(new SymboleDiamantP());
			symbolesClasses.add(new SymboleRondC());
			symbolesClasses.add(new SymboleCarreC());
			symbolesClasses.add(new SymboleDiamantC());
			symbolesClasses.add(new SymboleRondP());
			dessine = true;

			centre();
			repaint();
		}
		
	    public void update(Graphics g) {
	    	paint(g);
	    }
		
		public void centre(){
			for (int i=0;i<points.length;i++){
				if(points[i][0] < xMin) xMin = points[i][0];
				if(points[i][1] < yMin) yMin = points[i][1];
				if(points[i][0] > xMax) xMax = points[i][0];
				if(points[i][1] > yMax) yMax = points[i][1];
			}

			
			zoom = Math.min(getWidth()/(xMax-xMin),getHeight()/(yMax-yMin));
			xCam = (xMax+xMin)/2;
			yCam = (yMax+yMin)/2;
			
			System.out.println("Min: " + xMin + ";" + yMin);
			System.out.println("Max: " + xMax + ";" + yMax);
			System.out.println("\nCenter: " + xCam + " ; " + yCam + " ; zoom: " + zoom);
		}
		
		public void paint(Graphics g){
			if(buffer == null) buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics gBuffer = buffer.getGraphics();
			gBuffer.setColor(Color.white);	
			gBuffer.fillRect(0,0,getWidth(),getHeight());
			gBuffer.setColor(Color.black);

			//double costx = Math.cos(tx), costy = Math.cos(ty), costz = Math.cos(tz);
			//double sintx = Math.sin(tx), sinty = Math.sin(ty), sintz = Math.sin(tz);
			for (int i=0;i<points.length;i++){
				int x = (int)(getWidth()/2 +  (xCam + points[i][0])*zoom);//(int)((xCam+points[i][0])*zoom);
				int y = (int)(getHeight()/2 + (yCam + points[i][1])*zoom);//(int)((yCam+points[i][1])*zoom);
				
				//3D_projection

				/*double ax = points[i][0];
				double ay = points[i][1];
				double az = points[i][2];
				
				double dx = costy * ( sintz * (ay-cy)  + costz * (ax-cx) ) - sinty * (az-cz);
				double dy = sintx * ( costy * (az-cz)  + sinty * (sintz * (ay-cy) + costz * (ax-cx))) + costx * ( costz * (ay-cy) - sintz * (ax-cx) );
				double dz = costx * ( costy * (az-cz)  + sinty * (sintz * (ay-cy) - costz * (ax-cx))) - sintx * ( costz * (ay-cy) - sintz * (ax-cx) );
				
				int x, y;
				if(dz < 0) {
					x = Integer.MAX_VALUE;
					y = Integer.MAX_VALUE;	
				}
				else {
					x = (int)(getWidth()/2  + (dx-ex)*(ez/dz));
					y = (int)(getHeight()/2 + (dy-ey)*(ez/dz));	
				}
				*/
				
				//System.out.println(x + " ; " + y);
				if(itemNumero.isSelected()){
					gBuffer.drawString(Integer.toString(classes[i]), x, y);
				}
				else {
					if(itemCouleur.isSelected()){
						Color couleur = Color.black;
						if(itemDegradees.isSelected()){
							if(itemCoulLarge.isSelected()) {
								Iterator<Classe> iter = vectClasses.iterator();
								while(iter.hasNext()){
									Classe next = iter.next();
									if(next.estColoriee) couleur = Outils.melangeCouleurs(next.couleur,couleur,N[i][next.index]);
								}	
							}
							else {
								Iterator<Classe> iter = vectClasses.iterator();
								while(iter.hasNext()){
									Classe next = iter.next();
									if(next.estColoriee) couleur = Outils.melangeCouleurs(next.couleur,couleur,M[i][next.index]);
								}	
							}
						}
						else couleur = vectClasses.elementAt(classes[i]).couleur;
						symboleCase.dessine(gBuffer, x, y, symbolSize, couleur);
					}
					else vectClasses.elementAt(classes[i])
					.symbole.dessine(gBuffer, x, y, symbolSize);
				}
			}
			if(dessine && enveloppe > 0){
				Iterator<Classe> iter = vectClasses.iterator();
				while(iter.hasNext()){
					Classe next = iter.next();
					if(itemCouleurEnv.isSelected()) gBuffer.setColor(next.couleur);
					else gBuffer.setColor(Color.black);
					next.convexe.dessine(gBuffer, zoom, xCam, yCam, getWidth(), getHeight());
				}				
			}
			g.drawImage(buffer, 0, 0, null);
		}

		public void mouseDragged(MouseEvent e) {
			//System.out.println("Mouse dragged: " + (e.getX()-xCursor) + " ; " + (e.getY()-yCursor));
			xCam += (e.getX()-xCursor)*speed;
			yCam += (e.getY()-yCursor)*speed;
			/*if(boutonSouris == e.BUTTON1) {
				cx += (e.getX()-xCursor)*translateSpd;
				cz += (e.getY()-yCursor)*translateSpd;
			}
			else {
				ty += (e.getX()-xCursor)*rotationSpd;
				tx += (e.getY()-yCursor)*rotationSpd;
			}*/

	        xCursor = e.getX();
	        yCursor = e.getY();
			repaint();
		}
		
		public void mouseMoved(MouseEvent e) {
			int x = Integer.MAX_VALUE;
			int y = Integer.MAX_VALUE;
			//tx += (e.getX()-xCursor)*speed;
			//tz += (e.getY()-yCursor)*speed;
	        xCursor = e.getX();
	        yCursor = e.getY();
			int i = -1;
			boolean result = false;
			
			while(!result && i < points.length - 1){
				//System.out.println( Math.pow(xCursor-x,2)+Math.pow(yCursor-y,2) + ">" + Math.pow(symbolSize,2));
				//System.out.println(xCursor + " " + x + ";" + yCursor + " " + y);
				i++;
				x = (int)(getWidth()/2+(xCam+points[i][0])*zoom);
				y = (int)(getHeight()/2+(yCam+points[i][1])*zoom);
				result = (Math.pow(xCursor-x,2) + Math.pow(yCursor-y,2)) <= Math.pow(symbolSize,2);
			}
			
			if(result){
				labelInfo.setText("( " + points[i][0] + " ; " + points[i][1] + " )  Classe: " + vectClasses.elementAt(classes[i]).nom + " (" + (int)(M[i][classes[i]]*100)+ "%)");
			}
			else {
				labelInfo.setText("Passez la souris sur un point pour plus de détails");
			}
			
			
			/*if(i < points.length) selected = --i;
			else selected = -1;
			repaint();
			*/
			
			
		}

		public void mouseClicked(MouseEvent e) {
			xCursor = e.getX();
	        yCursor = e.getY();
	        boutonSouris = e.getButton();
	        //System.out.println("Mouse clicked: " + xCursor + " ; " + yCursor);
		}

		public void mouseEntered(MouseEvent e) {			
		}

		public void mouseExited(MouseEvent e) {			
		}

		public void mouseReleased(MouseEvent e) {			
		}

		public void mousePressed(MouseEvent e) {
	        xCursor = e.getX();
	        yCursor = e.getY();
	        boutonSouris = e.getButton();
	        if(boutonSouris == 3){
	        	centre();
	        	repaint();
	        }
	        //System.out.println("Mouse pressed: " + xCursor + " ; " + yCursor);
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
			double nouvZoom = zoom + e.getWheelRotation() * zoomSpeed;
			if(nouvZoom > 0) zoom = nouvZoom;
			//cy += e.getWheelRotation() * translateYSpd;
			//System.out.println("Zoom " + (e.getWheelRotation() * zoomSpeed) + " -> " + zoom);
			repaint();
		}
	}
}	
