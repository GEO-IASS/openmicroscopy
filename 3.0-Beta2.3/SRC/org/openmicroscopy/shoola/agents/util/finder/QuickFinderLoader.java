/*
 * org.openmicroscopy.shoola.agents.util.finder.QuickFinderLoader 
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

//Third-party libraries

//Application-internal dependencies
import org.openmicroscopy.shoola.agents.events.hiviewer.Browse;
import org.openmicroscopy.shoola.env.data.util.SearchResult;
import org.openmicroscopy.shoola.env.data.views.CallHandle;
import org.openmicroscopy.shoola.env.event.EventBus;
import org.openmicroscopy.shoola.env.ui.UserNotifier;
import pojos.ExperimenterData;

/** 
 * Searches for tags, images, etc.
 * This class calls <code>searchFor</code> method in the
 * <code>DataHandlerView</code>.
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
public class QuickFinderLoader
	extends FinderLoader
{

	/** The scope of the search.  */
	private Class			type;
	
	/** Collection of terms to search for. */
	private List			values;
	
	/** The separator used between the terms. */
	private String 			separator;
	
	/** Handle to the async call so that we can cancel it. */
    private CallHandle  	handle;

    /**
     * Creates a new instance.
     * 
     * @param viewer 	The viewer this data loader is for.
     *               	Mustn't be <code>null</code>.
     * @param index		One of the constants defined by this class. 
     * @param values	Collection of terms to search for.
     * @param type		The type of data to search, One of the constants 
     * 					defined by this class.
     * @param separator 
     */
    public QuickFinderLoader(QuickFinder viewer, List values, int type, 
    		String separator)
    {
    	super(viewer);
    	this.type = checkType(type);
    	if (values == null || values.size() == 0) 
    		throw new IllegalArgumentException("No terms to search for.");
    	this.values = values;
    	this.separator = separator;
    }
    
    /**
     * Searches for values.
     * @see FinderLoader#load()
     */
    public void load()
    {
    	List<Class> scope = new ArrayList<Class>(1);
    	scope.add(type);
    	List<ExperimenterData> users = new ArrayList<ExperimenterData>(1);
    	users.add(getUserDetails());
    	
    	handle = dhView.advancedSearchFor(scope, values, users, null, null, 
    									separator, false, this);
    }

    /**
     * Cancels the ongoing data retrieval.
     * @see FinderLoader#cancel()
     */
    public void cancel() { handle.cancel(); }
    
    /** 
     * Feeds the result back to the viewer. 
     * @see FinderLoader#handleResult(Object)
     */
    public void handleResult(Object result)
    {
    	if (viewer.getState() == Finder.DISCARDED) return;  //Async cancel.
        EventBus bus = registry.getEventBus();
        SearchResult r = (SearchResult) result;
        if (r == null) {
        	UserNotifier un = registry.getUserNotifier();
        	un.notifyInfo("Search", "No results matching your criteria.");
        	viewer.setStatus("", false);
        	return;
        }
        Set<Long> set = r.getNodeIDs();
        if (set == null || set.size() == 0) {
        	UserNotifier un = registry.getUserNotifier();
        	un.notifyInfo("Search", "No results matching your criteria.");
        	return;
        }
        Browse event = new Browse(set, Browse.IMAGES, getUserDetails(), null); 
        Iterator i = values.iterator();
        String s = " for \"";
        while (i.hasNext()) {
			s += (String) i.next();
			s += " ";
		}
        s = s.substring(0, s.length()-1);
        s += "\" in ";
        s += convertType(type);
        
        event.setSearchContext(s);
		bus.post(event); 
		viewer.dispose();
    }

}
