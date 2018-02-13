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
package hero.core.operator.evaluator;

import hero.core.util.DataTable;
import java.util.ArrayList;

/**
 *
 * @author José Luis Risco Martín
 */
public abstract class AbstractPopPredictor {

    public abstract void updatePredictor(DataTable data, int idx);

    public static String generateClassHeader(Integer threadId) {
        StringBuilder currentJavaFile = new StringBuilder();
        currentJavaFile.append("import java.util.ArrayList;\n\n");
        currentJavaFile.append("public class PopPredictor").append(threadId).append(" extends hero.core.operator.evaluator.AbstractPopPredictor {\n");
        return currentJavaFile.toString();
    }

    public static String generateUpdatePredictor(ArrayList<String> phenotypes) {
        StringBuilder currentJavaFile = new StringBuilder();
        currentJavaFile.append("\tpublic void updatePredictor(hero.core.util.DataTable data, int idx) {\n");
        currentJavaFile.append("\t\ttry {\n"); // Try
        
        // SWITCH STRUCTURE
        currentJavaFile.append("\t\t\tswitch(idx) {\n"); // Switch
        for (int i = 0; i < phenotypes.size(); ++i) {
            currentJavaFile.append("\t\t\t\tcase ").append(i).append(":\n");
            currentJavaFile.append("\t\t\t\t\tfor(int i=0; i<data.getData().size(); ++i) {\n");
            currentJavaFile.append("\t\t\t\t\t\tdouble[] row = data.getData().get(i);\n");
            currentJavaFile.append("\t\t\t\t\t\trow[data.getPredictorColumn()] = predictorNum").append(i).append("(row);\n");
            currentJavaFile.append("\t\t\t\t\t\tif(!Double.isFinite(row[data.getPredictorColumn()])) {\n");
            currentJavaFile.append("\t\t\t\t\t\t\trow[data.getPredictorColumn()] = Double.POSITIVE_INFINITY;\n");
            currentJavaFile.append("\t\t\t\t\t\t}\n");
            currentJavaFile.append("\t\t\t\t\t}\n");
            currentJavaFile.append("\t\t\t\tbreak;\n");
        }
        currentJavaFile.append("\t\t\t\tdefault:\n");
        currentJavaFile.append("\t\t\t\t\tthrow new Exception(\"There is no case for this idx.\");\n");
        currentJavaFile.append("\t\t\t}\n"); // End switch
        currentJavaFile.append("\t\t}\n"); // End try
        currentJavaFile.append("\t\tcatch (Exception ee) {\n");
        currentJavaFile.append("\t\t\t// System.err.println(ee.getLocalizedMessage());\n");
        currentJavaFile.append("\t\t}\n"); // End catch
        currentJavaFile.append("\t}\n\n");
        
        // FUNCTIONS STRUCTURE
        for (int i = 0; i < phenotypes.size(); ++i) {
            String expression = phenotypes.get(i);
            currentJavaFile.append("\tpublic double predictorNum").append(i).append("(double[] v) {\n");
            currentJavaFile.append(expression);
            currentJavaFile.append("\t}\n\n");
        }
        

        return currentJavaFile.toString();
    }

/*    public static String generateComputeNewX(ArrayList<String> phenotypes) {
        StringBuilder currentJavaFile = new StringBuilder();
        currentJavaFile.append("\tpublic double[][] computeNewX(int idx, double[][] xx) {\n");
        currentJavaFile.append("\t\tdouble[][] xxNew = new double[xx.length][];\n");
        currentJavaFile.append("\t\ttry {\n"); // Try
        currentJavaFile.append("\t\t\tswitch(idx) {\n"); // Switch

        for (int i = 0; i < phenotypes.size(); ++i) {
            String multipleExpression = phenotypes.get(i);
            String[] parts = multipleExpression.split(";");
            currentJavaFile.append("\t\t\t\tcase ").append(i).append(":\n");
            currentJavaFile.append("\t\t\t\t\tfor(int i=0; i<xx.length; ++i) {\n");
            currentJavaFile.append("\t\t\t\t\t\tdouble[] x = xx[i];\n");
            currentJavaFile.append("\t\t\t\t\t\txxNew[i] = new double[").append(parts.length).append("];\n");
            for (int j = 0; j < parts.length; ++j) {
                currentJavaFile.append("\t\t\t\t\t\txxNew[i][").append(j).append("] = ").append(parts[j]).append(";\n");
                currentJavaFile.append("\t\t\t\t\t\tif(!Double.isFinite(xxNew[i][").append(j).append("])) {\n");
                currentJavaFile.append("\t\t\t\t\t\t\txxNew[i][").append(j).append("] = 2E3;\n");
                currentJavaFile.append("\t\t\t\t\t\t}\n");
            }
            currentJavaFile.append("\t\t\t\t\t}\n");
            currentJavaFile.append("\t\t\t\tbreak;\n");
        }
        currentJavaFile.append("\t\t\t\tdefault:\n");
        currentJavaFile.append("\t\t\t\t\txxNew = null;\n");
        currentJavaFile.append("\t\t\t}\n"); // End switch
        currentJavaFile.append("\t\t}\n"); // End try
        currentJavaFile.append("\t\tcatch (Exception ee) {\n");
        currentJavaFile.append("\t\t\t// System.err.println(ee.getLocalizedMessage());\n");
        currentJavaFile.append("\t\t\txxNew = null;\n");
        currentJavaFile.append("\t\t}\n"); // End catch
        currentJavaFile.append("\t\treturn xxNew;\n");
        currentJavaFile.append("\t}\n");

        return currentJavaFile.toString();
    }*/

    public static String generateClassFooter() {
        StringBuilder currentJavaFile = new StringBuilder();
        currentJavaFile.append("}\n");
        return currentJavaFile.toString();
    }

    public static String generateClassCode(Integer threadId, ArrayList<String> phenotypes) {
        StringBuilder javaCode = new StringBuilder();
        javaCode.append(generateClassHeader(threadId));
        javaCode.append(generateUpdatePredictor(phenotypes));
        //javaCode.append(generateComputeNewX(phenotypes));
        javaCode.append(generateClassFooter());
        return javaCode.toString();
    }
        public static String generateClassMainC(Integer threadId,ArrayList<String> phenotypes,Integer NumCol,String Path) {
        StringBuilder currentCFile = new StringBuilder();
        currentCFile.append("#include <stdlib.h>\n");
        currentCFile.append("#include <math.h>\n");
        currentCFile.append("#include <string.h>\n");
        currentCFile.append("#include <stdio.h>\n");
        currentCFile.append("#include \"PopPredictor1.h\"\n\n");
        currentCFile.append("#define COLUMN_SIZE ").append(NumCol).append("\n");
        currentCFile.append("#define NUM_IND ").append(phenotypes.size()).append("\n\n");
        currentCFile.append("FILE* dataTable,* fitTable;\n");
        currentCFile.append("char dataLine[100];\n");
        currentCFile.append("char *token;\n");
        currentCFile.append("double data[COLUMN_SIZE];\n");
        currentCFile.append("double fit[NUM_IND]; \n");
        currentCFile.append("int main(int argc, char** argv){\n");
        currentCFile.append("\tfunc_t func_array[NUM_IND]={\n\t\t");
        for (int i = 0; i < phenotypes.size()-1; ++i) {
            currentCFile.append("predictorNum").append(i).append(",");
            if(i%5==0){
            currentCFile.append("\n\t\t");
            }
        }
        currentCFile.append("predictorNum").append(phenotypes.size()-1).append("};\n\n");
        currentCFile.append("\tdataTable = fopen(\"").append(Path).append("\", \"r\");\n");
        currentCFile.append("\twhile(fscanf(dataTable, \"%[^\\n]%*c\",dataLine) == 1){\n");
        currentCFile.append("\t\ttoken = strtok(dataLine, \";\");\n");
        currentCFile.append("\t\tfor(int i=0;i<COLUMN_SIZE;i++){\n");
        currentCFile.append("\t\t\tdata[i]= atof(token);\n");
        currentCFile.append("\t\t\ttoken = strtok(NULL, \";\");}\n");
        currentCFile.append("\t\tfor(int j=0;j<NUM_IND;j++){\n");
        currentCFile.append("\t\t\tif(data[0]!=func_array[j](data)){\n");
        currentCFile.append("\t\t\tfit[j]++;}}}\n");
        currentCFile.append("\tfclose(dataTable);\n");
        currentCFile.append("\tfitTable = fopen(\"test/fitGESM.csv\", \"w+\");\n");
        currentCFile.append("\tfor(int k=0;k<NUM_IND;k++){\n");
        currentCFile.append("\t\tfprintf(fitTable, \"%.2f\\n\",fit[k]);}\n");
        currentCFile.append("\tfclose(fitTable);\n");
        currentCFile.append("return 0;}\n");
        return currentCFile.toString();      
    }
    public static String generateUpdatePredictorC(ArrayList<String> phenotypes) {
        StringBuilder currentCFile = new StringBuilder();
        // FUNCTIONS STRUCTURE
        currentCFile.append("#include <stdlib.h>\n");
        currentCFile.append("#include <math.h>\n");
        currentCFile.append("#include \"PopPredictor1.h\"\n");
        for (int i = 0; i < phenotypes.size(); ++i) {
            String expression = phenotypes.get(i);
            currentCFile.append("double predictorNum").append(i).append("(double* v) {\n");
            currentCFile.append(expression);
            currentCFile.append("}\n\n");
        }
        return currentCFile.toString();
    }
    public static String generateUpdatePredictorHeaderC(ArrayList<String> phenotypes) {
        StringBuilder currentCFile = new StringBuilder();
        // FUNCTIONS STRUCTURE
        currentCFile.append("#ifndef _POPPREDICTOR1_H\n");
        currentCFile.append("#define _POPPREDICTOR1_H\n");
        currentCFile.append("typedef double (*func_t)(double*);\n");
        for (int i = 0; i < phenotypes.size(); ++i) {
            currentCFile.append("double predictorNum").append(i).append("(double* v);\n");
        }
        currentCFile.append("#endif\n");
        return currentCFile.toString();
    }
}

