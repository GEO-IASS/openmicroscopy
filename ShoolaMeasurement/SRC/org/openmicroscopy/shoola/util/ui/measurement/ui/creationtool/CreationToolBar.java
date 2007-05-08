/*
 * measurement.ui.CreationTool 
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
package org.openmicroscopy.shoola.util.ui.measurement.ui.creationtool;

//Java imports
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JToolBar;

//Third-party libraries
import org.jhotdraw.draw.BezierTool;
import org.jhotdraw.draw.ConnectionTool;
import org.jhotdraw.draw.CreationTool;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.EllipseFigure;
import org.jhotdraw.draw.RectangleFigure;
import org.jhotdraw.draw.TextFigure;
import org.jhotdraw.draw.action.ToolBarButtonFactory;
import org.jhotdraw.util.ResourceBundleUtil;

//Application-internal dependencies
import org.openmicroscopy.shoola.util.ui.measurement.model.DrawingEventList;
import org.openmicroscopy.shoola.util.ui.measurement.ui.UIControl;
import org.openmicroscopy.shoola.util.ui.measurement.ui.UIModel;
import org.openmicroscopy.shoola.util.ui.measurement.ui.figures.MeasureBezierFigure;
import org.openmicroscopy.shoola.util.ui.measurement.ui.figures.MeasureBezierTextFigure;
import org.openmicroscopy.shoola.util.ui.measurement.ui.figures.MeasureEllipseFigure;
import org.openmicroscopy.shoola.util.ui.measurement.ui.figures.MeasureEllipseTextFigure;
import org.openmicroscopy.shoola.util.ui.measurement.ui.figures.MeasureLineConnectionFigure;
import org.openmicroscopy.shoola.util.ui.measurement.ui.figures.MeasureLineConnectionTextFigure;
import org.openmicroscopy.shoola.util.ui.measurement.ui.figures.MeasureLineFigure;
import org.openmicroscopy.shoola.util.ui.measurement.ui.figures.MeasureLineTextFigure;
import org.openmicroscopy.shoola.util.ui.measurement.ui.figures.MeasureRectangleFigure;
import org.openmicroscopy.shoola.util.ui.measurement.ui.figures.MeasureRectangleTextFigure;
import org.openmicroscopy.shoola.util.ui.measurement.ui.figures.MeasureTextFigure;
import org.openmicroscopy.shoola.util.ui.measurement.ui.measurementtable.MeasurementTable;
import org.openmicroscopy.shoola.util.ui.measurement.ui.util.ChannelComboBox;
import org.openmicroscopy.shoola.util.ui.measurement.ui.util.UIUtils;
import org.openmicroscopy.shoola.util.ui.roi.model.util.Coord3D;

/** 
 * 
 *
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 	<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author	Donald MacDonald &nbsp;&nbsp;&nbsp;&nbsp;
 * 	<a href="mailto:donald@lifesci.dundee.ac.uk">donald@lifesci.dundee.ac.uk</a>
 * @version 3.0
 * <small>
 * (<b>Internal version:</b> $Revision: $Date: $)
 * </small>
 * @since OME3.0
 */
public class CreationToolBar 
	extends JFrame	
	implements PropertyChangeListener
{
	private		UIControl		control;
	private 	UIModel			model;
	private 	JToolBar		toolBar;
	private 	ButtonGroup 	group;
	private		DrawingEditor	editor;					
	
	private 	JButton			saveBtn;
	private 	MeasurementTable measurementTable;
	
	public CreationToolBar(UIModel model, UIControl control, DrawingEditor editor)
	{
		this.model = model;
		this.control = control;
		this.editor = editor;
		createToolset();
		createUI();
		control.addPropertyChangeListener(this);
	}

	private void createToolset()
	{
		group = new ButtonGroup();
		ResourceBundleUtil labels = 
			ResourceBundleUtil.getLAFBundle("org.jhotdraw.draw.Labels");
		toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.putClientProperty("toolButtonGroup", group);
		ToolBarButtonFactory.addSelectionToolTo(toolBar, editor);
		toolBar.addSeparator(new Dimension(10,20));
		ToolBarButtonFactory.addToolTo(toolBar, editor, 
				new CreationTool(new MeasureRectangleTextFigure()), "createRectangle", 
				labels);
		ToolBarButtonFactory.addToolTo(toolBar, editor, 
				new CreationTool(new MeasureEllipseTextFigure()), "createEllipse", 
				labels);
		ToolBarButtonFactory.addToolTo(toolBar, editor, 
				new CreationTool(new MeasureLineTextFigure()), "createLine", 
				labels);
	    ToolBarButtonFactory.addToolTo(toolBar, editor, 
	    		new ConnectionTool(new MeasureLineConnectionTextFigure()), 
	    		"createLineConnection", labels);
		  ToolBarButtonFactory.addToolTo(toolBar, editor, 
				  new BezierTool(new MeasureBezierTextFigure()), "createScribble", labels);
	      ToolBarButtonFactory.addToolTo(toolBar, editor, 
	    		  new BezierTool(new MeasureBezierTextFigure(true)), "createPolygon", 
	    		  labels);
		ToolBarButtonFactory.addToolTo(toolBar, editor, 
				new CreationTool(new TextFigure()), "createText", 
				labels);
	}

	private void createSaveBtn()
	{
		saveBtn = new JButton("Save ROI");
		saveBtn.setAction(new AbstractAction()
		{

			public void actionPerformed(ActionEvent arg0) 
			{
				measurementTable = new MeasurementTable(model);
				measurementTable.setVisible(true);
			}
		});
	}

	private void createUI()
	{
		this.setTitle("Drawing Toolbar");
		this.setAlwaysOnTop(true);
		this.setResizable(false);
		this.setLayout(new FlowLayout());
		this.setSize(new Dimension(450,65));
		createSaveBtn();
		createToolset();
//		this.getContentPane().add(activeChannel);
		this.getContentPane().add(saveBtn);
		this.getContentPane().add(toolBar);
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent changeEvent) 
	{
//		if(changeEvent.getPropertyName() == DrawingEventList.UIMODEL_ACTIVECHANNELCHANGED)
//		{
//			activeChannel.setSelectedIndex((Integer)changeEvent.getNewValue());
//		}
	}
	
	
}


