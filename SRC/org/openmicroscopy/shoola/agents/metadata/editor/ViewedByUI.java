/*
 * org.openmicroscopy.shoola.agents.metadata.editor.ViewedByUI 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2008 University of Dundee. All rights reserved.
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
package org.openmicroscopy.shoola.agents.metadata.editor;



//Java imports
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;


//Third-party libraries
import layout.TableLayout;

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.metadata.IconManager;
import org.openmicroscopy.shoola.env.data.util.ViewedByDef;
import org.openmicroscopy.shoola.util.ui.RatingComponent;
import org.openmicroscopy.shoola.util.ui.UIUtilities;
import org.openmicroscopy.shoola.util.ui.border.TitledLineBorder;
import pojos.AnnotationData;
import pojos.ImageData;
import pojos.RatingAnnotationData;

/** 
 * Displays the thumbnails and rating as seen by users.
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * <a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME3.0
 */
class ViewedByUI 
	extends AnnotationUI
	implements ActionListener
{
    
	/** The title associated to this component. */
	private static final String 	TITLE = "Viewed by ";
	
	/** The dimension of the scroll pane. */
	private static final Dimension 	SCROLL_SIZE = new Dimension (100, 150);
	
	/** Indicates to lay out the nodes as a list. */
	private static final int		LIST_VIEW = 0;
	
	/** Indicates to lay out the nodes as a grid. */
	private static final int		GRID_VIEW = 1;
	
	/** The tool bar displayed when the component is expanded. */
	private JToolBar			displayBar;
	
	/** Button indicating to display the nodes in a list. */
	private JToggleButton		listView;
	
	/** Button indicating to display the nodes in a grid. */
	private JToggleButton		gridView;
	
	/** One of the constants defined by this class. */
	private int					layoutIndex;
	
	/** The UI displaying the thumbnail as a grid. */
	private JPanel				gridPane;
	
	/** The UI displaying the thumbnail as a list. */
	private JPanel				listPane;

	/** Flag indicating if the node is expanded or not. */
	private boolean				expanded;
	
	/** Initializes the components composing the display. */
	private void initComponents()
	{
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		layoutIndex = LIST_VIEW;
		IconManager icons = IconManager.getInstance();
		listView = new JToggleButton(icons.getIcon(IconManager.LIST_VIEW));
		listView.setToolTipText("List View");
		listView.setActionCommand(""+LIST_VIEW);
		listView.addActionListener(this);
		gridView = new JToggleButton(icons.getIcon(IconManager.GRID_VIEW));
		gridView.setToolTipText("Grid View");
		gridView.setActionCommand(""+GRID_VIEW);
		gridView.addActionListener(this);
		listView.setSelected(true);
		ButtonGroup group = new ButtonGroup();
		group.add(listView);
		group.add(gridView);
		displayBar = new JToolBar();
		displayBar.setBorder(null);
		displayBar.setFloatable(false);
		displayBar.add(UIUtilities.setTextFont("Display: "));
		displayBar.add(Box.createHorizontalStrut(5));
		displayBar.add(listView);
		displayBar.add(gridView);
		expanded = false;
	}
	
	/** 
	 * Builds an element of the list pane.
	 * 
	 * @param img	The thumbnail to display.
	 * @param id	The id of the user.
	 * @return See above.
	 */
	private JPanel buildListItem(BufferedImage img, long id)
	{
		JPanel p = new JPanel();
		ViewedByDef def = model.getViewedDef(id);
		p.add(new ThumbnailCanvas(model, img, def));
		
		String name = model.formatOwner(def.getExperimenter());
		Collection ratings = def.getRatings();
		int value = 0;
		if (ratings != null && ratings.size() != 0) {
			Iterator i = ratings.iterator();
			while (i.hasNext()) 
				value = ((RatingAnnotationData) i.next()).getRating();
		}
		RatingComponent rate = new RatingComponent(value, 
								RatingComponent.MEDIUM_SIZE, false);
		
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(new JLabel(name));
		content.add(rate);
		
		p.add(content);
		return p;
	}
	
	/** 
	 * Builds an element of the grid pane.
	 * 
	 * @param img	The thumbnail to display.
	 * @param id	The id of the user.
	 * @return See above.
	 */
	private JPanel buildGridItem(BufferedImage img, long id)
	{
		JPanel p = new JPanel();
		int width = img.getWidth();
		int height = img.getHeight();
		double[][] tl = {{width}, //columns
				{height, TableLayout.PREFERRED} }; //rows
		p.setLayout(new TableLayout(tl));
		ViewedByDef def = model.getViewedDef(id);
		p.add(new ThumbnailCanvas(model, img, def), "0, 0");
		
		String name = model.formatOwner(def.getExperimenter());
		Collection ratings = def.getRatings();
		int value = 0;
		if (ratings != null && ratings.size() != 0) {
			Iterator i = ratings.iterator();
			while (i.hasNext()) 
				value = ((RatingAnnotationData) i.next()).getRating();
		}
		RatingComponent rate = new RatingComponent(value, 
								RatingComponent.MEDIUM_SIZE, false);
		
		JPanel content = new JPanel();
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		content.add(new JLabel(name));
		content.add(rate);
		
		p.add(content, "0, 1");
		return p;
	}
	
	/** 
	 * Builds and lays out the nodes as a list. 
	 * 
	 * @return See above.
	 */
	private JPanel layoutList()
	{
		//if (listPane != null) return UIUtilities.buildComponentPanel(listPane);
		listPane = new JPanel();
		listPane.setLayout(new BoxLayout(listPane, BoxLayout.Y_AXIS));
		Map<Long, BufferedImage> thumbnails = model.getThumbnails();
		Iterator i = thumbnails.keySet().iterator();
		Long id;
		while (i.hasNext()) {
			id = (Long) i.next();
			listPane.add(buildListItem(thumbnails.get(id), id));
		}
		return UIUtilities.buildComponentPanel(listPane);
	}
	
	/** 
	 * Builds and lays out the nodes as a grid. 
	 * 
	 * @return See above.
	 */
	private JPanel layoutGrid()
	{
		//if (gridPane != null) return UIUtilities.buildComponentPanel(gridPane);
		gridPane = new JPanel();
		Map<Long, BufferedImage> thumbnails = model.getThumbnails();
		Iterator i = thumbnails.keySet().iterator();
		Long id;
		double[] columns = {TableLayout.PREFERRED, TableLayout.PREFERRED, 
							TableLayout.PREFERRED}; //rows
		TableLayout layout = new TableLayout();
		layout.setColumn(columns);
		gridPane.setLayout(layout);
		for (int j = 0; j < 2*thumbnails.size()-1; j++) {
			if (j%3 == 0) layout.insertRow(j, TableLayout.PREFERRED);
			else layout.insertRow(j, 5);
		}
		int index = 0;
		while (i.hasNext()) {
			id = (Long) i.next();
			gridPane.add(buildGridItem(thumbnails.get(id), id), 
									"0, "+index+", f, c");
			index++;
		}
		return UIUtilities.buildComponentPanel(gridPane);
	}
	
	/** 
	 * Builds the loading component.
	 * 
	 * @return See above.
	 */
	private JPanel buildLoadingPane()
	{
		JPanel p = new JPanel();
		JProgressBar progressBar = new JProgressBar();
	    progressBar.setIndeterminate(true);
	    p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        //p.setBorder(BorderFactory.createEtchedBorder());
        p.add(new JLabel("Loading..."));
        p.add(UIUtilities.buildComponentPanelRight(progressBar));
		return p;
	}
	
	/**
	 * Builds and lays out a component displaying a message.
	 * 
	 * @return See above.
	 */
	private JPanel buildEmptyPane()
	{
		JPanel p = new JPanel();
	    p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        p.add(new JLabel("No views available."));
		return p;
	}
	
	/**
	 * Creates a new instance.
	 * 
	 * @param model Reference to the model. Mustn't be <code>null</code>.
	 */
	ViewedByUI(EditorModel model)
	{
		super(model);
		title = TITLE;
		initComponents();
	}
	
	/**
	 * Sets to <code>true</code> if the node is expanded, 
	 * <code>false</code> otherwise.
	 * 
	 * @param expanded The value to set.
	 */
	void setExpanded(boolean expanded) { this.expanded = expanded; }
	
	/**
	 * Returns <code>true</code> if the node is expanded, 
	 * <code>false</code> otherwise.
	 * 
	 * @return See above.
	 */
	boolean isExpanded() { return expanded; }
	
	/**
	 * Overridden to lay out the nodes depending on the selected layout index.
	 * @see AnnotationUI#buildUI()
	 */
	protected void buildUI()
	{
		title = TITLE+LEFT+model.getViewedByCount()+RIGHT;
		Border border = new TitledLineBorder(title, getBackground());
		setBorder(border);
		getCollapseComponent().setBorder(border);
		removeAll();
		if (model.getRefObject() instanceof ImageData) {
			if (model.isThumbnailsLoaded()) {
				if (model.getThumbnails().size() == 0) {
					add(buildEmptyPane(), Component.CENTER_ALIGNMENT);
				} else {
					add(UIUtilities.buildComponentPanelRight(displayBar), 
										Component.RIGHT_ALIGNMENT);
					JScrollPane pane;
					switch (layoutIndex) {
						case LIST_VIEW:
							pane = new JScrollPane(layoutList()); 
							pane.setPreferredSize(SCROLL_SIZE);
							add(pane, Component.CENTER_ALIGNMENT);
							break;
						case GRID_VIEW:
							pane = new JScrollPane(layoutGrid()); 
							pane.setPreferredSize(SCROLL_SIZE);
							add(pane, Component.CENTER_ALIGNMENT);
					}
				}
			} else {
				if (expanded)
					add(buildLoadingPane(), Component.CENTER_ALIGNMENT);
			}
		}
		revalidate();
		repaint();
	}
	
	/**
	 * Overridden to set the title of the component.
	 * @see AnnotationUI#getComponentTitle()
	 */
	protected String getComponentTitle() { return title; }

	/**
	 * Returns <code>null</code> because data are not editable.
	 * @see AnnotationUI#getAnnotationToRemove()
	 */
	protected List<AnnotationData> getAnnotationToRemove() { return null; }

	/**
	 * Returns <code>null</code> because data are not editable.
	 * @see AnnotationUI#getAnnotationToSave()
	 */
	protected List<AnnotationData> getAnnotationToSave() { return null; }
	
	/**
	 * Returns <code>false</code> because data are not editable.
	 * @see AnnotationUI#hasDataToSave()
	 */
	protected boolean hasDataToSave() { return false; }
	
	/**
	 * Clears the UI.
	 * @see AnnotationUI#clearDisplay()
	 */
	protected void clearDisplay()
	{ 
		removeAll(); 
		revalidate();
	}
	
	/**
	 * Clears the data to save.
	 * @see AnnotationUI#clearData()
	 */
	protected void clearData() {}
	
	/**
	 * Sets the title of the component.
	 * @see AnnotationUI#setComponentTitle()
	 */
	protected void setComponentTitle() {}
	
	/**
	 * Modifies the layout of the thumbnails.
	 * @see ActionListener#actionPerformed(ActionEvent)
	 */
	public void actionPerformed(ActionEvent e)
	{
		int index = Integer.parseInt(e.getActionCommand());
		switch (index) {
			default:
			case LIST_VIEW:
				layoutIndex = LIST_VIEW;
				break;
			case GRID_VIEW:
				layoutIndex = GRID_VIEW;
		}
		buildUI();
	}

}
