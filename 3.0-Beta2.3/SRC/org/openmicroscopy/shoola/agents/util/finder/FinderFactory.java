/*
 * org.openmicroscopy.shoola.agents.util.finder.FinderFactory 
 *
 *------------------------------------------------------------------------------
 *  Copyright (C) 2006-2007 University of Dundee. All rights reserved.
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
package org.openmicroscopy.shoola.agents.util.finder;


//Java imports
import javax.swing.JFrame;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.env.config.Registry;

/** 
 * Factory to create {@link Finder}.
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
public class FinderFactory
{
	
	/** The sole instance. */
    private static final FinderFactory  singleton = new FinderFactory();
    
	/**
	 * Creates or recycles an advanced search.
	 * 
	 * @param ctx	Reference to the registry. Mustn't be <code>null</code>.
	 * @return See above.
	 */
	public static AdvancedFinder getAdvancedFinder(Registry ctx)
	{
		if (singleton.registry == null) singleton.registry = ctx;
		return (AdvancedFinder) singleton.createFinder();
	}
	
	/**
	 * Creates or recycles an advanced search.
	 * 
	 * @param ctx	Reference to the registry. Mustn't be <code>null</code>.
	 * @return See above.
	 */
	public static QuickFinder getQuickFinder(Registry ctx)
	{
		if (singleton.registry == null) singleton.registry = ctx;
		return (QuickFinder) singleton.createQuickFinder();
	}
	 /**
     * Helper method. 
     * 
     * @return A reference to the {@link Registry}.
     */
    public static Registry getRegistry() { return singleton.registry; }
    
    /**
	 * Returns the task bar frame.
	 * 
	 * @return See above.
	 */
	public static JFrame getRefFrame()
	{
		return singleton.registry.getTaskBar().getFrame();
	}
	
	 /** Reference to the registry. */
    private Registry	registry;
    
    /** The tracked component. */
    //private Finder		finder;
    
    /** Creates a new instance. */
	private FinderFactory()
	{
		//finder = null;
	}
	
	/**
	 * Creates the finder.
	 * 
	 * @return See above.
	 */
	private Finder createFinder()
	{
		//if (advancedFinder != null) {
			//advancedFinder.setFocusOnSearch();
			//advancedFinder.set
		//	return advancedFinder;
		//}
		//finder = new AdvancedFinder();
		return new AdvancedFinder();
	}
	
	/**
	 * Creates the finder.
	 * 
	 * @return See above.
	 */
	private Finder createQuickFinder()
	{
		//if (advancedFinder != null) {
			//advancedFinder.setFocusOnSearch();
			//advancedFinder.set
		//	return advancedFinder;
		//}
		//finder = new 
		return new QuickFinder();
	}
	
}
