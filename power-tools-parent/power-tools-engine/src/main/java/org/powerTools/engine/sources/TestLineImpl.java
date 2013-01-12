/*	Copyright 2012 by Martin Gijsen (www.DeAnalist.nl)
 *
 *	This file is part of the PowerTools engine.
 *
 *	The PowerTools engine is free software: you can redistribute it and/or
 *	modify it under the terms of the GNU Affero General Public License as
 *	published by the Free Software Foundation, either version 3 of the License,
 *	or (at your option) any later version.
 *
 *	The PowerTools engine is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *	GNU Affero General Public License for more details.
 *
 *	You should have received a copy of the GNU Affero General Public License
 *	along with the PowerTools engine. If not, see <http://www.gnu.org/licenses/>.
 */

package org.powerTools.engine.sources;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import org.powerTools.engine.TestLine;


public final class TestLineImpl implements TestLine {
	private int mNrOfParts;
	private String[] mParts;
	private String[] mOriginalParts;


	public TestLineImpl () {
		mNrOfParts = 0;
	}
	

	public void parseQuotedInstruction (String instruction) {
		setParts (getParts (instruction));
	}
	
	private List<String> getParts (String instruction) {
		final String[] partsArray	= instruction.split ("\"");
		final List<String> parts	= new ArrayList<String> ();		

		parts.add (getInstructionName (partsArray));

		for (int partNr = 1; partNr < partsArray.length; partNr += 2) {
			parts.add (partsArray[partNr]);
		}
		
		return parts;
	}
	
	private String getInstructionName (String[] parts) {
		String instructionName = parts[0];
		for (int partNr = 2; partNr < parts.length; partNr += 2) {
			instructionName += " _ " + parts[partNr];
		}
		if (parts.length % 2 == 0) {
			instructionName += " _";
		}
		return instructionName;
	}

	public void setParts (List<String> list) {
		mNrOfParts = list.size ();
		if (mNrOfParts != 0) {
			mParts			= new String[mNrOfParts];
			mOriginalParts	= new String[mNrOfParts];

			int partNr = 0;
			final Iterator<String> iter = list.iterator ();
			while (iter.hasNext ()) {
				setPart (partNr++, iter.next ());
			}
		}
	}
	
	public void createParts (int nrOfParts) {
		mNrOfParts	= nrOfParts;
		mParts		= new String[nrOfParts];
		for (int partNr = 0; partNr < nrOfParts; ++partNr) {
			mParts[partNr] = "";
		}
	}

	public boolean setPart (int partNr, String value) {
		try {
			mParts[partNr] = value;
			return true;
		} catch (IndexOutOfBoundsException ioobe) {
			return false;
		}
	}

	public boolean setEvaluatedPart (int partNr, String value) {
		try {
			mOriginalParts[partNr]	= mParts[partNr];
			mParts[partNr]			= value;
			return true;
		} catch (IndexOutOfBoundsException ioobe) {
			return false;
		}
	}

	@Override
	public int getNrOfParts () {
		return mNrOfParts;
	}
	
	@Override
	public String getPart (int partNr) {
		return (partNr < 0 || partNr >= mNrOfParts) ? "" : mParts[partNr];
	}

	@Override
	public String getOriginalPart (int partNr) {
		return (partNr < 0 || partNr >= mNrOfParts) ? null : mOriginalParts[partNr];
	}

	@Override
	public boolean isEmpty () {
		for (int partNr = 0; partNr < mNrOfParts; ++partNr) {
			if (!mParts[partNr].isEmpty ()) {
				return false;
			}
		}
		return true;
	}
}