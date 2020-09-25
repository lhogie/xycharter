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
 
 
package oscilloscup.multiscup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InMethodProperty<E> extends Property<E>
{
	private final Method method;

	public InMethodProperty(Method method)
	{
		this.method = method;
		setName(methodName2propertyName(method.getName()));
	}

	@Override
	public Object getRawValue(E target)
	{
		try
		{
			return method.invoke(target);
		}
		catch (IllegalArgumentException | IllegalAccessException
				| InvocationTargetException e)
		{
			throw new IllegalStateException(e);
		}
	}

	public static <E> List<InMethodProperty<E>> findInMethodPropertiesIn(Class<E> c)
	{
		List<InMethodProperty<E>> r = new ArrayList<>();

		for (Method m : c.getDeclaredMethods())
		{
			if (m.getParameterCount() == 0
					&& (Property.acceptedTypes.contains(m.getReturnType())))
			{
				m.setAccessible(true);
				r.add(new InMethodProperty<>(m));
			}
		}

		return r;
	}

	private static String methodName2propertyName(String m)
	{
		if (m.startsWith("get"))
		{
			m = m.substring(3);
		}

		if (Character.isUpperCase(m.charAt(0)))
		{
			m = Character.toLowerCase(m.charAt(0)) + m.substring(1);
		}

		return m;
	}
}
