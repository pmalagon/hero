/*
 * Copyright (C) 2010-2016 José Luis Risco Martín <jlrisco@ucm.es>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *  - José Luis Risco Martín
 */
package hero.core.operator.crossover;

import hero.core.problem.Problem;
import hero.core.problem.Solution;
import hero.core.problem.Solutions;
import hero.core.problem.Variable;
import hero.core.util.random.RandomGenerator;

public class RegionCrossover<T extends Variable<?>> extends CrossoverOperator<T> {

    public static final int DEFAULT_SOL_SIZEX = 5;
    public static final int DEFAULT_SOL_SIZEY = 5;
    public static final int DEFAULT_REGION_SIZE = 3;

    protected Problem<T> problem;
    protected int sizeX;
    protected int sizeY;
    protected int regionSize;

    public RegionCrossover(Problem<T> problem, int sizeX, int sizeY, int regionSize) {
        this.problem = problem;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.regionSize = regionSize;
    }  // RegionCrossover

    public RegionCrossover(Problem<T> problem) {
        this(problem, DEFAULT_SOL_SIZEX, DEFAULT_SOL_SIZEY, DEFAULT_REGION_SIZE); 
    }  // RegionCrossover

    /**
     * Creates the new solution. 
     *
     * @param sol1 Chromosome 1
     * @param sol2 Chromosome 2
     *
     */
    private void makeNewSolution(Solution<T> sol1, Solution<T> sol2) {
        int px = RandomGenerator.nextInt(sizeX-regionSize+1);
        int px2 = px + regionSize-1;
        int py = RandomGenerator.nextInt(sizeY-regionSize+1);
        int py2 = py + regionSize-1;

        ArrayList<T> fix1 = new ArrayList<>();
        ArrayList<T> nofix1 = new ArrayList<>();
        ArrayList<T> fix2 = new ArrayList<>();
        ArrayList<T> nofix2 = new ArrayList<>();
        ArrayList<T> common = new ArrayList<>();

        Solution<T> tmp1 = sol1.clone();
        Solution<T> tmp2 = sol2.clone();

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                int index = sizeY*i + j;
                if ((i < px) || (i > px2) || (j < py) || (j > py2)) {
                    nofix1.add(sol1.getVariables().get(index));
                    common.add(sol1.getVariables().get(index));
                    nofix2.add(sol2.getVariables().get(index));
                } else {
                    fix1.add(sol1.getVariables().get(index));
                    fix2.add(sol2.getVariables().get(index));
                }
            }
        }
        common.retainAll(nofix2);
        nofix1.removeAll(common);
        nofix2.removeAll(common);
        Collections.shuffle(nofix1);
        Collections.shuffle(nofix2);
            
        int f1 = 0;
        int f2 = 0;
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                int index = sizeY*i + j;
                if ((i < px) || (i > px2) || (j < py) || (j > py2)) {
                    Variable<Integer> v1 = sol2.getVariables().get(index);
                    if (fix1.contains(v1) && nofix1.size() ) {
                        sol1.getVariables().set(index, nofix1.get(f1));
                        f1 = (f1++)%nofix1.size();
                    } else {
                        sol1.getVariables().set(index, v1);
                    }
                    Variable<Integer> v2 = sol1.getVariables().get(index);
                    if (fix2.contains(v2) && nofix2.size() ) {
                        sol2.getVariables().set(index, nofix2.get(f2));
                        f2 = (f2++)%nofix2.size();
                    } else {
                        sol2.getVariables().set(index, v2);
                    }
                }
            }
        }
    }

    /**
     * Executes the operation
     *
     * @param object An object containing an array of two parents
     * @return An object containing the offSprings
     */
    public Solutions<T> execute(Solution<T> parent1, Solution<T> parent2) {
        Solutions<T> offSpring = new Solutions<T>();

        Solution<T> offSpring0 = parent1.clone();
        Solution<T> offSpring1 = parent2.clone();

        this.makeNewSolution(offSpring0, offSpring1);
        offSpring.add(offSpring0);
        offSpring.add(offSpring1);
        return offSpring;
    } // execute

} // RegionCrossover

