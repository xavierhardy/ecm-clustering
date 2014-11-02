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


import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Forme.FormeAbstraite;
import Forme.FormeCarree;
import Forme.FormeRonde;
import Random.AbstractRandom;
import Random.RandomExp;
import Random.RandomGauss;
import Random.RandomUniform;

public class PanelNouveau extends JPanel {
	private static final long serialVersionUID = -5277427055243813310L;
	private SpringLayoutExt sl = new SpringLayoutExt(this);
	private JLabel labelDistrib = new JLabel("Distribution:");
	private AbstractRandom[] listDistrib = {new RandomUniform(), new RandomGauss(), new RandomExp()};
	
	private JComboBox<String> comboDistrib;
	
	private JLabel labelType = new JLabel("Forme de l'ensemble:");
	private FormeAbstraite[] listFormes = {new FormeCarree(), new FormeRonde()};

	private JComboBox<String> comboFormes;

	private JLabel labelCentres = new JLabel("Centres des classes:");
	private JComboBox<String> comboCentres;
	
	private JLabel labelNbClasses = new JLabel("Nombre de classes:");
	public JTextField textFieldNbClasses = new JTextField("4", 3);
	
	private JLabel labelDiametre = new JLabel("Diametre des classes:");
	public JTextField textFieldDiametre = new JTextField("0.5", 3);
	
	private JLabel labelEloignement = new JLabel("Eloignement par rapport a l'origine:");
	public JTextField textFieldEloignement = new JTextField("0.5", 3);
	
	private JLabel labelNbPoints = new JLabel("Nombre de points:");
	public JTextField textFieldNbPoints = new JTextField("100", 5);
		
	public PanelNouveau(){
		
		this.setLayout(sl);
        
		this.add(labelDistrib);
		sl.mettreEnHaut(labelDistrib);
		sl.mettreAGauche(labelDistrib);

		String[] listNoms = new String[listDistrib.length];
		
		for(int i = 0; i < listNoms.length; i++) listNoms[i] = listDistrib[i].toString();
		comboDistrib = new JComboBox<String>(listNoms);
		
		listNoms = new String[listDistrib.length + 1];
		
		listNoms[0] = "Repartition reguliere (cercle)";
		for(int i = 1; i < listNoms.length; i++) listNoms[i] = listDistrib[i-1].toString();
		comboCentres = new JComboBox<String>(listNoms);
		
		listNoms = new String[listFormes.length];
		for(int i = 0; i < listNoms.length; i++) listNoms[i] = listFormes[i].toString();
		comboFormes = new JComboBox<String>(listNoms);			
		
		this.add(comboDistrib);
		sl.mettreEnHaut(comboDistrib);
		sl.mettreADroiteDe(comboDistrib,labelDistrib);
		
		this.add(labelType);
		sl.mettreAGauche(labelType);
		sl.mettreEnDessousDe(labelType,comboDistrib);
		
		this.add(comboFormes);
		sl.mettreADroiteDe(comboFormes,labelType);
		sl.mettreEnDessousDe(comboFormes,comboDistrib);
				
		this.add(labelCentres);
		sl.mettreAGauche(labelCentres);
		sl.mettreEnDessousDe(labelCentres,comboFormes);
		
		this.add(comboCentres);
		sl.mettreADroiteDe(comboCentres,labelCentres);
		sl.mettreEnDessousDe(comboCentres,comboFormes);
		
		this.add(labelNbClasses);
		sl.mettreAGauche(labelNbClasses);
		sl.mettreEnDessousDe(labelNbClasses,comboCentres);
		
		this.add(textFieldNbClasses);
		sl.mettreADroiteDe(textFieldNbClasses,labelNbClasses);
		sl.mettreEnDessousDe(textFieldNbClasses,comboCentres);
		textFieldNbClasses.addFocusListener(new NumberFieldFocusListener());
		
		this.add(labelDiametre);
		sl.mettreAGauche(labelDiametre);
		sl.mettreEnDessousDe(labelDiametre,textFieldNbClasses);
		
		this.add(textFieldDiametre);
		sl.mettreADroiteDe(textFieldDiametre,labelDiametre);
		sl.mettreEnDessousDe(textFieldDiametre,textFieldNbClasses);
		textFieldDiametre.addFocusListener(new DoubleFieldFocusListener());
		
		this.add(labelEloignement);
		sl.mettreAGauche(labelEloignement);
		sl.mettreEnDessousDe(labelEloignement,textFieldDiametre);
		
		this.add(textFieldEloignement);
		sl.mettreADroiteDe(textFieldEloignement,labelEloignement);
		sl.mettreEnDessousDe(textFieldEloignement,textFieldDiametre);
		textFieldEloignement.addFocusListener(new DoubleFieldFocusListener());
		
		this.add(labelNbPoints);
		sl.mettreAGauche(labelNbPoints);
		sl.mettreEnDessousDe(labelNbPoints,textFieldEloignement);
		
		this.add(textFieldNbPoints);
		sl.mettreADroiteDe(textFieldNbPoints,labelNbPoints);
		sl.mettreEnDessousDe(textFieldNbPoints,textFieldEloignement);
		textFieldNbPoints.addFocusListener(new NumberFieldFocusListener());
		
        this.setVisible(true);
		
	}
	
	public AbstractRandom getDistrib(){
		return listDistrib[comboDistrib.getSelectedIndex()];
	}
	
	public FormeAbstraite getForme(){
		return listFormes[comboFormes.getSelectedIndex()];
	}
	
	public Object[] getCentres(){
		int index = comboCentres.getSelectedIndex();
		Object[] result;
		if(index == 0){
			result = new Object[1];
			result[0] = new Boolean(false);
			return result;
		}
		else {
			result = new Object[2];
			result[0] = new Boolean(true);
			result[1] = listDistrib[index - 1];
			return result;
		}
	}
}