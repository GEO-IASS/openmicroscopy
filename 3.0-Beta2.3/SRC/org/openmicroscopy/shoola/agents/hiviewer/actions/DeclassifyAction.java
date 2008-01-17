/*
 * org.openmicroscopy.shoola.agents.hiviewer.actions.DeclassifyAction
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

package org.openmicroscopy.shoola.agents.hiviewer.actions;


//Java imports
import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.Action;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.hiviewer.IconManager;
import org.openmicroscopy.shoola.agents.hiviewer.browser.ImageDisplay;
import org.openmicroscopy.shoola.agents.hiviewer.cmd.ClassifyCmd;
import org.openmicroscopy.shoola.agents.hiviewer.view.HiViewer;
import org.openmicroscopy.shoola.util.ui.UIUtilities;
import pojos.ImageData;

/** 
 * Brings up the declassification widget if required.
 * This action is enabled if the hierarchy object is an image.
 * 
 * 
 * @author  Jean-Marie Burel &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:j.burel@dundee.ac.uk">j.burel@dundee.ac.uk</a>
 * @author  <br>Andrea Falconi &nbsp;&nbsp;&nbsp;&nbsp;
 * 				<a href="mailto:a.falconi@dundee.ac.uk">
 * 					a.falconi@dundee.ac.uk</a>
 * @version 2.2
 * <small>
 * (<b>Internal version:</b> $Revision$ $Date$)
 * </small>
 * @since OME2.2
 */
public class DeclassifyAction
    extends HiViewerAction
{

    /** The name of the action. */
    public static final String NAME = "Remove Tag";
    
    /** The description of the action. */
    public static final String DESCRIPTION = "Remove Tag from the selected " +
    											"images.";
    
    /**
     * Sets the action enabled depending on the currently selected display
     * @see HiViewerAction#onDisplayChange(ImageDisplay)
     */
    protected void onDisplayChange(ImageDisplay selectedDisplay)
    {
    	if (selectedDisplay == null) {
            setEnabled(false);
            return;
        }
        if (selectedDisplay.getParentDisplay() == null) {
        	setEnabled(false);
        	return;
        }
        Set nodes = model.getBrowser().getSelectedDisplays();
        if (nodes.size() > 1) {
        	setEnabled(false);
        	return;
        }
        Object ho = selectedDisplay.getHierarchyObject();
        if (!(ho instanceof ImageData)) {
        	setEnabled(false);
        	return;
        }
        ImageData img = (ImageData) ho;
        Long count = img.getClassificationCount();
        setEnabled(count != null && count.longValue() > 0);
    }
    
    /**
     * Creates a new instance.
     * 
     * @param model Reference to the Model. Mustn't be <code>null</code>.
     */
    public DeclassifyAction(HiViewer model)
    {
        super(model);
        putValue(Action.NAME, NAME);
        putValue(Action.SHORT_DESCRIPTION, 
                UIUtilities.formatToolTipText(DESCRIPTION));
        IconManager icons = IconManager.getInstance();
        putValue(Action.SMALL_ICON, icons.getIcon(IconManager.DECATEGORISE));
    }

    /**
     * Creates a {@link ClassifyCmd} command to execute the action.
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
       ClassifyCmd cmd = new ClassifyCmd(model, 
    		   					ClassifyCmd.DECLASSIFICATION_MODE);
       cmd.execute();
    }

}
