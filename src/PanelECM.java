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

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelECM extends JPanel {
	private static final long serialVersionUID = 3428805294675127204L;

	private SpringLayoutExt sl = new SpringLayoutExt(this);
	
	private JLabel labelNbClasses = new JLabel("Nombre de classes:");
	public JTextField textFieldNbClasses = new JTextField("4", 2);

	private JLabel labelAlpha = new JLabel("Alpha:");
	public JTextField textFieldAlpha = new JTextField("1", 3);
	
	private JLabel labelBeta = new JLabel("Beta:");
	public JTextField textFieldBeta = new JTextField("2", 3);
	
	private JLabel labelDelta = new JLabel("Delta:");
	public JTextField textFieldDelta = new JTextField(Double.toString(Math.sqrt(20.00)), 10);	
	
	private JLabel labelEpsilon = new JLabel("Epsilon:");
	public JTextField textFieldEpsilon = new JTextField("0.001", 10);	
	
	public PanelECM(){
		
		this.setLayout(sl);
        
		this.add(labelNbClasses);
		sl.mettreAGauche(labelNbClasses);
		sl.mettreEnHaut(labelNbClasses);
		
		this.add(textFieldNbClasses);
		sl.mettreADroiteDe(textFieldNbClasses,labelNbClasses);
		sl.mettreEnHaut(textFieldNbClasses);
		textFieldNbClasses.addFocusListener(new NumberFieldFocusListener());
		
		this.add(labelAlpha);
		sl.mettreAGauche(labelAlpha);
		sl.mettreEnDessousDe(labelAlpha,textFieldNbClasses);
		
		this.add(textFieldAlpha);
		sl.mettreADroiteDe(textFieldAlpha,labelAlpha);
		sl.mettreEnDessousDe(textFieldAlpha,textFieldNbClasses);
		textFieldAlpha.addFocusListener(new DoubleFieldFocusListener());
		
		this.add(labelBeta);
		sl.mettreAGauche(labelBeta);
		sl.mettreEnDessousDe(labelBeta,textFieldAlpha);
		
		this.add(textFieldBeta);
		sl.mettreADroiteDe(textFieldBeta,labelBeta);
		sl.mettreEnDessousDe(textFieldBeta,textFieldAlpha);
		textFieldBeta.addFocusListener(new DoubleFieldFocusListener());
		
		this.add(labelDelta);
		sl.mettreAGauche(labelDelta);
		sl.mettreEnDessousDe(labelDelta,textFieldBeta);
		
		this.add(textFieldDelta);
		sl.mettreADroiteDe(textFieldDelta,labelDelta);
		sl.mettreEnDessousDe(textFieldDelta,textFieldBeta);
		textFieldDelta.addFocusListener(new DoubleFieldFocusListener());
		
		this.add(labelEpsilon);
		sl.mettreAGauche(labelEpsilon);
		sl.mettreEnDessousDe(labelEpsilon,textFieldDelta);
		
		this.add(textFieldEpsilon);
		sl.mettreADroiteDe(textFieldEpsilon,labelEpsilon);
		sl.mettreEnDessousDe(textFieldEpsilon,textFieldDelta);
		textFieldEpsilon.addFocusListener(new DoubleFieldFocusListener());
		
        this.setVisible(true);
		
	}
}