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
package hero.core.algorithm.metaheuristic.ga;

import java.util.logging.Level;
import hero.core.operator.comparator.SimpleDominance;
import hero.core.operator.crossover.RegionCrossover;
import hero.core.operator.crossover.SinglePointCrossover;
import hero.core.operator.mutation.IntegerFlipMutation;
import hero.core.operator.mutation.SwapMutation;
import hero.core.operator.selection.BinaryTournament;
import hero.core.problem.Solution;
import hero.core.problem.Solutions;
import hero.core.problem.Variable;
import hero.core.problems.cnn.CNN_IDS;
import hero.core.util.logger.HeroLogger;
import java.util.logging.Logger;

public class SimpleGeneticAlgorithm_example {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HeroLogger.setup(Level.FINE);
                Logger logger = Logger.getLogger("");
		// First create the problem
                CNN_IDS problem = new CNN_IDS(25, 23);
		// Second create the algorithm
		//SwapMutation<Variable<Integer>> mutationOp = new SwapMutation<>(0.1);
                IntegerFlipMutation mutationOp = new IntegerFlipMutation<>(problem, 0.1);
		//RegionCrossover<Variable<Integer>> crossoverOp = new RegionCrossover<>(problem);
                SinglePointCrossover<Variable<Integer>> crossoverOp = new SinglePointCrossover<>(problem);
		SimpleDominance<Variable<Integer>> comparator = new SimpleDominance<>();
                BinaryTournament<Variable<Integer>> selectionOp = new BinaryTournament<>(comparator);
		SimpleGeneticAlgorithm<Variable<Integer>> ga = new SimpleGeneticAlgorithm<>(problem, 1000, 2000, true, mutationOp, crossoverOp, selectionOp);
                ga.initialize();

                long begin = System.currentTimeMillis();
                
                Solutions<Variable<Integer>> solutions = ga.execute();
                for(Solution<Variable<Integer>> solution : solutions) {
                    String sols = new String("Solution = ");
                    //logger.info("Solution = ");
                    for (int i = 0; i < problem.getNumberOfVariables(); ++i) {
                        double xi = solution.getVariables().get(i).getValue();
                        sols = sols.concat(xi+",");
                    }
                    logger.info(sols);
                    logger.info("Fitness = " + solution.getObjectives().get(0));
                }

                long end = System.currentTimeMillis();
                logger.info("Time: " + ((end - begin) / 1000.0) + " seconds");

		//for(Solution<Variable<Integer>> solution : solutions) {
		//	System.out.println("Fitness = " + solution.getObjectives().get(0));
		//}
		//System.out.println("solutions.size()="+ solutions.size());
		//System.out.println(solutions.toString());
		//System.out.println("solutions.size()="+ solutions.size());
	}
}
