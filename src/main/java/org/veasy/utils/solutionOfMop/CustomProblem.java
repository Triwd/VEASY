package org.veasy.utils.solutionOfMop;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;

import java.util.ArrayList;
import java.util.List;

public class CustomProblem extends AbstractDoubleProblem {

    public CustomProblem() {
        setNumberOfVariables(2);
        setNumberOfObjectives(2);
        setName("VSV1");//Volunteer Selection Version 1

        List<Double> lowerLimit = new ArrayList<>(getNumberOfVariables());//决策变量个数
        List<Double> upperLimit = new ArrayList<>(getNumberOfVariables());//目标函数个数

        //设置决策变量定义域
        lowerLimit.add(0, 10.0);//时长
        upperLimit.add(0, 42.0);
        lowerLimit.add(1, 0.0);//时间
        upperLimit.add(1, (double) 60300.0);
        setLowerLimit(lowerLimit);
        setUpperLimit(upperLimit);
    }

    //计算解的适应度
    public void evaluate(DoubleSolution solution) {
        //
        double[] f = new double[getNumberOfObjectives()];
        //第一个目标函数值存于f[0]
        f[0] = evalH(solution);
        //第二个目标函数值存于f[1]
        f[1] = evalT(solution);
        //按索引写入函数值
        solution.setObjective(0, f[0]);
        solution.setObjective(1, f[1]);
    }


    //下面为目标函数
    private double evalH(DoubleSolution solution) {
        double h = solution.getVariableValue(0);
        double t = solution.getVariableValue(1);
        return h - t / 150.0;
    }

    private double evalT(DoubleSolution solution) {
        double t = solution.getVariableValue(1);
        double h = solution.getVariableValue(0);
        return t - h * 150.0;
    }
}
