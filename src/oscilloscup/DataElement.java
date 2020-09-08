/* (C) Copyright 2009-2013 CNRS (Centre National de la Recherche Scientifique).

Licensed to the CNRS under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The CNRS licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.

*/

/* Contributors:

Luc Hogie (CNRS, I3S laboratory, University of Nice-Sophia Antipolis) 

*/

package oscilloscup;

/**
 * <p>
 * A <code>DataElement</code> is the central notion of the system: this is what
 * will be graphically rendered.
 * </p>
 * 
 * <p>
 * A <code>DataElement</code> can be a single point or a figure (a list of
 * points and subfigures).
 * </p>
 * 
 * <p>
 * The rendering of this data element is made by its renderers. A
 * <code>DataElement</code> defines a list of renderers that will, sequencely,
 * render it on a graphical pane.
 * </p>
 * 
 * @author Luc Hogie
 */

public abstract class DataElement extends GraphicalElement implements Cloneable {
}
