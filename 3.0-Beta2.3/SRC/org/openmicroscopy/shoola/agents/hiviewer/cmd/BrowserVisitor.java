/*
 * org.openmicroscopy.shoola.agents.hiviewer.cmd.SelectedDisplayVisitor
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

package org.openmicroscopy.shoola.agents.hiviewer.cmd;


//Java imports

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.hiviewer.browser.Browser;
import org.openmicroscopy.shoola.agents.hiviewer.browser.ImageDisplayVisitor;
import org.openmicroscopy.shoola.agents.hiviewer.browser.ImageNode;
import org.openmicroscopy.shoola.agents.hiviewer.browser.ImageSet;

/** 
 * SuperClass, that all visitors that need to know about the status of the
 * browser should extend.
 * For example, some visitors may need to know which node has been selected.
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
public class BrowserVisitor
    implements ImageDisplayVisitor
{

    /** A reference to the {@link Browser}. */
    protected Browser   browser;
    
    /**
     * Creates a new instance.
     * 
     * @param browser   Reference to the {@link Browser}.
     *                  Mustn't be <code>null</code>, it shouldn't happen.
     */
    public BrowserVisitor(Browser browser)
    {
        if (browser == null) throw new IllegalArgumentException("No browser.");
        this.browser = browser;
    }
    
    /** 
     * Required by {@link ImageDisplayVisitor} I/F. Sub-classes
     * will implement the method.
     * @see ImageDisplayVisitor#visit(ImageNode)
     */
    public void visit(ImageNode node) {}

    /** 
     * Required by {@link ImageDisplayVisitor} I/F. Sub-classes
     * will implement the method.
     * @see ImageDisplayVisitor#visit(ImageSet)
     */
    public void visit(ImageSet node) {}
    
}
