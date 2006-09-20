/*
 * ome.server.utests.UpdateFilterMockTest
 *
 *------------------------------------------------------------------------------
 *
 *  Copyright (C) 2005 Open Microscopy Environment
 *      Massachusetts Institute of Technology,
 *      National Institutes of Health,
 *      University of Dundee
 *
 *
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *------------------------------------------------------------------------------
 */
package ome.server.utests;

// Java imports
import java.util.Collection;
import java.util.Map;

// Third-party libraries
import org.testng.annotations.*;

// Application-internal dependencies
import ome.model.IObject;
import ome.model.core.Image;
import ome.model.internal.Details;

/**
 * @author Josh Moore &nbsp;&nbsp;&nbsp;&nbsp; <a
 *         href="mailto:josh.moore@gmx.de">josh.moore@gmx.de</a>
 * @version 1.0 <small> (<b>Internal version:</b> $Rev$ $Date$) </small>
 * @since Omero 2.0
 */
public class UpdateFilterMockTest extends AbstractLoginMockTest
{


    // ~ NON GRAPHS (single elements)
    // =========================================================================
    
    @Test
    public void test_filter_null() throws Exception
    {
        filter.filter( null, (Object) null );
        filter.filter( null, (IObject) null );
        filter.filter( null, (Details) null );
        filter.filter( null, (Map) null );
        filter.filter( null, (Collection) null );
    }

    // ~ GRAPHS (multiple levels)
    // =========================================================================

   
}
