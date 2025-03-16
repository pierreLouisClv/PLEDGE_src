package pledge.core;

import java.util.List;
import java.util.Set;
import org.sat4j.core.VecInt;
import org.sat4j.minisat.core.Solver;
import org.sat4j.specs.IVecInt;
import org.sat4j.specs.TimeoutException;

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
/**
 *
 * @author Christopher Henard
 */
public class Util {
    
    public static void nCk(int n, int k, Set<TSet> tsets, List<Integer> featuresList, boolean checkValid, Solver solver) throws TimeoutException {
        int[] a = new int[k];
        nCkH(n, k, 0, a, k, tsets, featuresList, checkValid, solver);
    }
    
    public static void nCkH(int n, int loopno, int ini, int[] a, int k, Set<TSet> tsets, List<Integer> featuresList, boolean checkValid, Solver solver) throws TimeoutException {
        
        if (k == 0) {
            return;
        }
        
        int i;
        loopno--;
        if (loopno < 0) {
            a[k - 1] = ini - 1;
            TSet p = new TSet();
            for (i = 0; i < k; i++) {
                p.add(featuresList.get(a[i]));
            }
            
            if (checkValid) {
                IVecInt prod = new VecInt(p.getSize());
                
                for (Integer in : p.getVals()) {
                    prod.push(in);
                }
                
                if (solver.isSatisfiable(prod) && p.getSize() == k) {
                    tsets.add(p);
                }
            } else {
		if (p.getSize() == k)
                	tsets.add(p);
            }
            return;
            
        }
        for (i = ini; i <= n - loopno - 1; i++) {
            a[k - 1 - loopno] = i;
            nCkH(n, loopno, i + 1, a, k, tsets, featuresList, checkValid, solver);
        }
        
        
    }
}
