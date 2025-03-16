package pledge.core;

/*
 * Author : Christopher Henard (christopher.henard@uni.lu)
 * Date : 23/02/2013
 * Copyright 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
 * All rights reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author Christopher Henard
 */
public class TSet implements Serializable {

    private Set<Integer> vals;
    static final long serialVersionUID = -6618469844567325812L;

    public TSet(int[] vals) {
        this.vals = new HashSet<Integer>();
        for (Integer i : vals) {
            this.vals.add(i);
        }

    }

    public int getSize() {
        return vals.size();
    }

    public TSet() {
        this.vals = new HashSet<Integer>();
    }

    public void add(Integer i) {
        this.vals.add(i);
    }

    public Set<Integer> getVals() {
        return vals;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TSet other = (TSet) obj;

        return vals.equals(other.vals);
    }

    @Override
    public int hashCode() {
        return vals.hashCode();
    }

    @Override
    public String toString() {
        return vals.toString();
    }
}
