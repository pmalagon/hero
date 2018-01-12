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
 *  - Pedro Malagón
 */
package hero.core.problems;

import java.util.logging.Logger;

import java.util.ArrayList;

import hero.core.problem.Problem;
import hero.core.problem.Solution;
import hero.core.problem.Solutions;
import hero.core.problem.Variable;
import hero.core.util.random.RandomGenerator;
import java.util.Collections;

public class CNN_IDS extends Problem<Variable<Integer>> {

    private static String[] ips = {"kylo.lsi.die", "milo.lsi.die", "floyd.lsi.die"};
    private static final int port = 22222;

    private static final Logger logger = Logger.getLogger(CNN_IDS.class.getName());
    protected static int lastid = 0;

    protected int id = 0;
    protected String ip;
    protected Integer numberOfFeatures;
    protected double bestValue = Double.POSITIVE_INFINITY;

    public CNN_IDS(Integer numberOfVariables, Integer numberOfFeatures) {
        super(numberOfVariables, 1);
        this.numberOfFeatures = numberOfFeatures;
        for (int i = 0; i < numberOfVariables; i++) {
            lowerBound[i] = 0;
            upperBound[i] = numberOfFeatures-1;
        }
        this.id = lastid % ips.length();
        this.ip = ips[this.id];
        lastid++;
    }

    public Solutions<Variable<Integer>> newRandomSetOfSolutions(int size) {
        Solutions<Variable<Integer>> solutions = new Solutions<Variable<Integer>>();
        ArrayList<Variable<Integer>> features1 = new ArrayList<Variable<Integer>>();
        ArrayList<Variable<Integer>> features_extra = new ArrayList<Variable<Integer>>();
        
        for (int j = 0; j < numberOfFeatures; ++j) {
            features1.add(new Variable<Integer>(j));
            features_extra.add(new Variable<Integer>(j));
            features_extra.add(new Variable<Integer>(j));
        }
        
        ArrayList<Variable<Integer>> features = new ArrayList<Variable<Integer>>();
        features.addAll(features1);
        int v;
        for (v = numberOfFeatures; v < numberOfVariables; ++v) {
            features.add(features_extra.get(v-numberOfFeatures));
        }

        for (int i = 0; i < size; ++i) {
            for (v = 0; v < numberOfFeatures; ++v) {
                features.set(v, features1.get(v));
            }
            Collections.shuffle(features_extra);
            for (; v < numberOfVariables; ++v) {
                features.set(v, features_extra.get(v-numberOfFeatures));
            }
            Collections.shuffle(features);
            
            Solution<Variable<Integer>> solI = new Solution<Variable<Integer>>(numberOfObjectives);
            for (int j = 0; j < numberOfVariables; ++j) {
                solI.getVariables().add(features.get(j));
            }
            solutions.add(solI);
        }
        return solutions;
    }

    public int getIpsAvailable() {
        return ips.length();
    }

    @Override
    public void evaluate(Solutions<Variable<Integer>> solutions) {
        for (Solution<Variable<Integer>> solution : solutions) {
            evaluate(solution);
        }
    }

    @Override
    public void evaluate(Solution<Variable<Integer>> solution) {
        //Call CNN evaluation function in Python instead of this
        //IP: ip, PORT: port
        double fitness = 0;
        for (int i = 0; i < numberOfVariables; ++i) {
            int xi = solution.getVariables().get(i).getValue();
            fitness += Math.pow((xi-i), 2);
        }
        //Replace until here
        solution.getObjectives().set(0, fitness);
        if (fitness < bestValue) {
            logger.info("Best value found: " + fitness);
            bestValue = fitness;
        }
    }

    public CNN_IDS clone() {
        CNN_IDS clone = new CNN_IDS(this.numberOfVariables, this.numberOfFeatures);
        return clone;
    }
}
