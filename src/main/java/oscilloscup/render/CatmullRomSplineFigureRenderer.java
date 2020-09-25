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
 
 
package oscilloscup.render;

public class CatmullRomSplineFigureRenderer extends BSplineFigureRenderer
{
  float b(int i, float t) {
    switch (i) {
    case -2:
      return ((-t+2)*t-1)*t/2;
    case -1:
      return (((3*t-5)*t)*t+2)/2;
    case 0:
      return ((-3*t+4)*t+1)*t/2;
    case 1:
      return ((t-1)*t*t)/2;
    }
    return 0; //we only get here if an invalid i is specified
  }
}
