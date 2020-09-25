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
 
 
package oscilloscup.multiscup.demo.mortgage;

public class BankSimulation implements Changeable
{
	final static double prixDuBien = 223000;
	double enBanque = 230000;
	final double garantie;
	final double fraisDeDossier;

	double resteARembourser;
	final double apport;

	final double mensualite;
	double tauxEpargne = 2.5 / 100d;
	double revenusMensuels = 3000 + 840 + 129;
	double chargesMensuelles = 2200;

	double gainEpargneTotal = 0;
	final String name;

	public BankSimulation(String name, double apport, double mensualite,
			double fraisDeDossier, double garantie)
	{
		this.name = name;
		this.apport = apport;
		this.mensualite = mensualite;
		this.fraisDeDossier = fraisDeDossier;
		this.garantie = garantie;

		resteARembourser = prixDuBien - apport;
		enBanque -= apport;
		enBanque -= fraisDeDossier;
		enBanque -= garantie;
	}

	@Override
	public void change(double time)
	{
		if (time == 84)
		{
			enBanque -= resteARembourser;
			resteARembourser = 0;
		}
		else
		{
			enBanque += revenusMensuels;
			enBanque -= chargesMensuelles;

			double cetteMensualite = Math.min(mensualite, resteARembourser);
		//	enBanque -= cetteMensualite;

			resteARembourser -= cetteMensualite;

			double gainEpargneCeMois = enBanque * (tauxEpargne / 12d);
			gainEpargneTotal += gainEpargneCeMois;
			enBanque += gainEpargneCeMois;
		}
	}
}
