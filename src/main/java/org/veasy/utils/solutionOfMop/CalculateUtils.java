package org.veasy.utils.solutionOfMop;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import java.util.List;


public class CalculateUtils {

    public static List<DoubleSolution> calculate() {
        //创建参数对象
        Problem<DoubleSolution> problem;
        Algorithm<List<DoubleSolution>> algorithm;
        CrossoverOperator<DoubleSolution> crossover;
        MutationOperator<DoubleSolution> mutation;
        SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
        //真实帕累托前沿，用于计算指标值

        //实例化参数对象
        problem = new CustomProblem();
        //配置交叉算子
        double crossoverProbability = 0.9;//设置交叉概率
        double crossoverDistributionIndex = 20.0;
        crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);
        // 配置变异算子
        double mutationProbability = 1.0 / problem.getNumberOfVariables();
        double mutationDistributionIndex = 20.0;
        mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);
        // 配置选择算子
        selection = new BinaryTournamentSelection<>(new RankingAndCrowdingDistanceComparator<>());
        // 将组件注册到algorithm
        algorithm = new NSGAIIBuilder<>(problem, crossover, mutation).setSelectionOperator(selection).setPopulationSize(10).setMaxEvaluations(10000).build();
        AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
        // 获取结果集
        return algorithm.getResult();
    }

    public static Double[] appropriateVolunteer() {
        List<DoubleSolution> population = calculate();
        double averageOfV1 = 0.0;
        double averageOfV2 = 0.0;
        for (DoubleSolution p : population) {
            averageOfV1 += p.getVariableValue(0);
            averageOfV2 += p.getVariableValue(1);
        }
        averageOfV1 /= population.size();
        averageOfV2 /= population.size();
        Double[] result = new Double[2];
        result[0] = averageOfV1;
        result[1] = averageOfV2;
        System.out.println(averageOfV1 + " " + averageOfV2);
        return result;
    }
}