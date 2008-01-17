/*
 * org.openmicroscopy.shoola.util.ui.colourpicker.TabbedPaneUI
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006 University of Dundee. All rights reserved.
 *
 *
 * 	This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *  
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *------------------------------------------------------------------------------
 */

package org.openmicroscopy.shoola.util.ui.colourpicker;

//Java imports
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.util.ui.IconManager;
import org.openmicroscopy.shoola.util.ui.UIUtilities;

/** 
 * The TabbedPaneUI controls the allows the switching between the different 
 * views in the colour picker.  
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author	Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * 	<a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $ $Date: $)
 * </small>
 * @since OME2.2
 */

class TabbedPaneUI
	extends JPanel
	implements ChangeListener
{
	
	/** The number of column of the label displaying the alpha value. */
	static final int			TEXTBOX_COLUMN = 2;
	
	/** Used by card layout to select colour wheel panel. */
	private static final String COLOURWHEELPANE = "Colour Wheel Pane"; 	
	
	/** Used by card layout to select RGB Slider panel. */
	private static final String RGBSLIDERPANE = "RGB Slider Pane"; 	
	
	/** Used by card layout to select swatch panel. */
	private static final String SWATCHPANE = "Swatch Pane"; 	
	
	/**
	 * Toolbar contains the buttons to select the HSVWheelUI, RGB Selector
	 * or Colour Swatch.
	 */
	private JToolBar 			toolbar;
	
	/**
	 * Actionbar contains the buttons to accept, cancel or revert to the 
	 * original colour selection.
	 */
	private JPanel 				userActionPanel;
	
	/** Button to choose HSVColourWheelPanel. */
	private JToggleButton 		colourWheelButton;
	
	/** Button to choose RGB Sliders panel. */
	private JToggleButton		RGBSlidersButton;
	
	/** Button to choose colour swatch panel. */
	private JToggleButton		colourSwatchButton;
	
	/** Accept the current colour choice. */
	private JButton				acceptButton;

	/** Revert to the original colour chosen by the user. */
	private JButton				revertButton;
	
	/** Cancel the colour panel. */
	private JButton				cancelButton;
	
	/** ColourWheel panel, containing the HSVPickerUI. */
	private HSVColourWheelUI	colourWheelPane;
	
	/** RGBPanel containing the ColourSlider UI. */
	private RGBSliderUI 		RGBSliderPane;
	
	/** Containing the Swatch UI. */
	private ColourSwatchUI 		swatchPane;
	
	/** Layout manager for the colourwheel, slider and swatch panels. */
	private CardLayout 			tabPaneLayout;
	
	/** 
	 * Container for the layout manager above. Containers colourwheel, slider 
	 * and swatch panels.
	 */
	private JPanel				tabPanel;
	
	/**
	 * Paintpot pane will be displayed at the top of the window, above selected
	 * pane and below toolbar.
	 */
	private PaintPotUI  		paintPotPane;

    /** Model which will be changed when user adjusts sliders/textfield. */
    private RGBControl      	control;
    
    /** The owner of this component. */
    private ColourPicker    	parent;
    
    /** 
     * The toolbar controls which panel is active, the user has the choice
     * of HSV Colour wheel, RGB Sliders and colour swatches.
     * Create the toolbar and its buttons, add actions to the buttons and load
     * the button icons, attach the buttons to the tool bar. 
     */
    private void createToolbar()
    {
    	toolbar = new JToolBar();
    	IconManager icons = IconManager.getInstance();
        colourWheelButton = new JToggleButton(
        icons.getIcon(IconManager.COLOUR_WHEEL));
        UIUtilities.unifiedButtonLookAndFeel(colourWheelButton);
        colourWheelButton.setBorderPainted(true);
        colourWheelButton.setToolTipText("Show HSV Colour Wheel");
        
        AbstractAction action = new AbstractAction("HSV Wheel Colour Button") {
            public void actionPerformed(ActionEvent evt) 
            {
                clearToggleButtons();
                pickWheelPane();
            }
        };
        
        colourWheelButton.addActionListener(action);
        RGBSlidersButton = new JToggleButton(
        icons.getIcon(IconManager.COLOUR_SLIDER));
        UIUtilities.unifiedButtonLookAndFeel(RGBSlidersButton);
        RGBSlidersButton.setBorderPainted(true);
        RGBSlidersButton.setToolTipText("Show RGB Colour Sliders");
        
        action = new AbstractAction("RGB Slider Button") 
        {
            public void actionPerformed(ActionEvent evt) 
            {
            	clearToggleButtons();
                pickRGBSliderPane();
            }
        };
        RGBSlidersButton.addActionListener(action);
        colourSwatchButton = new JToggleButton(
        icons.getIcon(IconManager.COLOUR_SWATCH));
        colourSwatchButton.setToolTipText("Show Colour List");
        UIUtilities.unifiedButtonLookAndFeel(colourSwatchButton);
        colourSwatchButton.setBorderPainted(true);
        
        action = new AbstractAction("Colour Swatch Button")
        {
            public void actionPerformed(ActionEvent evt) 
            {
                clearToggleButtons();
                pickSwatchPane();
            }
        };
        colourSwatchButton.addActionListener(action);
        toolbar.setFloatable(false);
        toolbar.setRollover(true);
        toolbar.add(colourWheelButton);
        toolbar.add(RGBSlidersButton);
        toolbar.add(colourSwatchButton);
    }
    
    /** 
     * The action bar controls if the user wishes to as accept current colour
     * choice, close the colour picker window or revert to the original colour
     * selected when the colour picker was loaded. 
     * 
     * Creates action bar, its buttons and load icons. Attach action events
     * to buttons and buttons to bar. 
     */
    private void createActionbar()
    {
        userActionPanel = new JPanel();
        userActionPanel.setLayout(new FlowLayout());
        
        acceptButton = new JButton("Accept");
        acceptButton.setToolTipText("Accept Current Colour");
        //UIUtilities.unifiedButtonLookAndFeel(acceptButton);
        //acceptButton.setBorderPainted(true);
        //acceptButton.setFocusPainted(true);
        AbstractAction action = new AbstractAction("Accept Button Action") 
        {
            public void actionPerformed(ActionEvent evt) { parent.accept(); }
        };
        parent.getRootPane().setDefaultButton(acceptButton);
        acceptButton.addActionListener(action);
        
        revertButton = new JButton("Revert");
        revertButton.setToolTipText("Revert to Original Colour");
        //UIUtilities.unifiedButtonLookAndFeel(revertButton);
        //revertButton.setBorderPainted(true);
        action = new AbstractAction("Revert Button Action") 
        {
            public void actionPerformed(ActionEvent evt)  { revertAction(); }
        };
        revertButton.addActionListener(action);
        
        cancelButton = new JButton("Cancel");
        cancelButton.setToolTipText("Cancel Selection and Close Colour Picker");
        //UIUtilities.unifiedButtonLookAndFeel(cancelButton);
        //cancelButton.setBorderPainted(true);
        action = new AbstractAction("Cancel Button Action") 
        {
            public void actionPerformed(ActionEvent evt) { parent.cancel(); }
        };
        cancelButton.addActionListener(action);
        
        userActionPanel.add(acceptButton);
        userActionPanel.add(revertButton);
        userActionPanel.add(cancelButton);
    }

    /**
     * Creates PaintPotUI, RGB slider panel and HSVWheel panel + Colour
     * Swatch panel.
     */
    private void createPanels()
    {
        colourWheelPane = new HSVColourWheelUI(control);
        paintPotPane = new PaintPotUI(control.getColour(), control);
        RGBSliderPane = new RGBSliderUI(control);
        swatchPane = new ColourSwatchUI(control);
    }
    
    /** Creates all the UI elements and display the HSVWheel as active. */
    private void createUI()
    {
        createToolbar();
        createActionbar();
        createPanels();
                
        JPanel container = new JPanel();
        
        container.setLayout(new BorderLayout());
        container.add(toolbar, BorderLayout.WEST);
          
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(container);
        paintPotPane.setPreferredSize(new Dimension(260,24));
        add(Box.createVerticalStrut(5));
        add(paintPotPane);
        add(Box.createVerticalStrut(5));
        tabPanel = new JPanel();
        tabPaneLayout = new CardLayout();
        tabPanel.setLayout(tabPaneLayout);
        tabPanel.add(colourWheelPane,COLOURWHEELPANE);
        tabPanel.add(RGBSliderPane,RGBSLIDERPANE);
        tabPanel.add(swatchPane,SWATCHPANE);
        add(tabPanel);
        add(userActionPanel);
        pickSwatchPane();
    }
    
    /** Clear all buttons. */
    private void clearToggleButtons()
    {
        colourWheelButton.setSelected(false);
        RGBSlidersButton.setSelected(false);
        colourSwatchButton.setSelected(false);
    }
    
    /** Sets Wheelbutton as picked and make it visible. */
    private void pickWheelPane()
    {   
        colourWheelButton.setSelected(true);
        colourWheelPane.setActive(true);

        tabPaneLayout.show(tabPanel,COLOURWHEELPANE);
        RGBSliderPane.setActive(false);
        swatchPane.setActive(false);
        colourWheelPane.findPuck();
        colourWheelPane.refresh();
        colourWheelPane.repaint();
    }
    
    /** Sets swatch as picked and makes it visible. */
    private void pickSwatchPane()
    {   
        tabPaneLayout.show(tabPanel,SWATCHPANE);
        colourSwatchButton.setSelected(true);
        swatchPane.setActive(true);
        RGBSliderPane.setActive(false);
        colourWheelPane.setActive(false);
        this.doLayout();
        swatchPane.refresh();
    }
    
    /** Sets RGBSlider as picked and makes it visible. */
    private void pickRGBSliderPane()
    {
        tabPaneLayout.show(tabPanel,RGBSLIDERPANE);
        RGBSlidersButton.setSelected(true);
        RGBSliderPane.setActive(true);
        colourWheelPane.setActive(false);
        swatchPane.setActive(false);

        this.doLayout();
        RGBSliderPane.refresh();
    }
    
	/**
	 * Instantiates the tabbed pane, create the UI and set the control to c.
	 * 
     * @param parent The parent of this component. Mustn't be <code>null</code>.
	 * @param c      Reference to the control. Mustn't be <code>null</code>.
	 */
	TabbedPaneUI(ColourPicker parent, RGBControl c)
	{
        if (parent == null)
            throw new NullPointerException("No parent.");
        if (c == null)
            throw new NullPointerException("No control.");
        this.parent = parent;
		control = c;
		createUI();
		control.addListener(this);
	}
	
	/** 
	 * User has clicked revert button. Revert current colour to the original 
	 * colour choice passed to Colourpicker.
	 */
	void revertAction() { control.revert(); }

	/** 
	 * Listens to ChangeEvent. 
	 * @see ChangeListener#stateChanged(ChangeEvent)
	 */
	public void stateChanged(ChangeEvent arg0) 
	{
		if (RGBSliderPane != null)
			if (RGBSliderPane.isVisible()) RGBSliderPane.refresh();
		if (colourWheelPane != null)
			if (colourWheelPane.isVisible()) colourWheelPane.refresh();
		if (swatchPane != null)
			if (swatchPane.isVisible()) swatchPane.refresh();
	}

}
