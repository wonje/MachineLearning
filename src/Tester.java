import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Created by wonje on 3/12/2017.
 */
public class Tester {
    static HashMap<String, Integer> allDataSet = new HashMap<String, Integer>();
    static int theta = 0; // BOUND TO JUDGE POSITIVE OR NEGATIVE FOR CALCULATED OUTPUT
    static int MAX_ITER = 40; // MAXIMUM NUMBER OF ITERATION
    static double LEARNING_RATE = 0.1; // LEARNING RATE
    static double bias = 1; // BIAS

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        final int procs_opt; // 1.FREQUENCY 2.BINARY
        final int punc_opt; // 1.PUNCTUATION 2.NO PUNCTUATION
        // GET INPUT FOR DATA PROCESSING OPTION FOR FREQUENCY OR BINARY
        while (true) {
            System.out.println("Select processing options for your data processing\n1.Frequency\n2.Binary");
            String tried = input.nextLine();
            if (tried.equals("1") || tried.equals("2")) {
                procs_opt = Integer.valueOf(tried);
                break;
            }
            System.out.println("Wrong! Please type integer 1 or 2");
        }
        // GET INPUT FOR DATA PROCESSING OPTION FOR PUNCTUATION OR NO PUNCTUATION
        while (true) {
            System.out.println("Select punctuation marks option\n1.Punctuation\n2.No Punctuation");
            String tried = input.nextLine();
            if (tried.equals("1") || tried.equals("2")) {
                punc_opt = Integer.valueOf(tried);
                break;
            }
            System.out.println("Wrong! Please type integer 1 or 2");
        }
        System.out.print("You selected ");
        if (procs_opt == 1)
            System.out.print("Frequency ");
        else
            System.out.print("Binary ");
        if (punc_opt == 1)
            System.out.print("Punctuation ");
        else
            System.out.print("No Punctuation ");
        System.out.println("options for your data processing.");

        // VARIABLES FOR VECTOR
        ArrayList<HashMap> vectorForEachFiles = new ArrayList<HashMap>();
        // VARIABLES FOR FILE I/O
        StringBuilder sb = new StringBuilder();

        // DATA PROCESSING GETTING ALL OF DATA SET
        // READ EACH OF FILES
        // STEP 1 : SCAN ALL OF FILES TO MAKE DEFAULT DATA SET
        try (Stream<Path> paths = Files.walk(Paths.get("./txt_sentoken"))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    // OPEN FILE
                    try (BufferedReader br = new BufferedReader(new FileReader(filePath.toString()))) {
                        // INIT STRING BUILDER
                        sb.setLength(0);
                        // READ ALL LINES FROM THE SELECTED FILE
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            // IF NO PUNCTUATION MODE
                            if (punc_opt == 2)
                                line = puncEraser(line);
                            sb.append(line);
                        }
                        String[] tokens = sb.toString().split(" ");
                        for (int i = 0; i < tokens.length; i++) {
                            // SAVE DATA FOR ALL DATA SET
                            if (!allDataSet.containsKey(tokens[i])) {
                                allDataSet.put(tokens[i], 0);
                            }
                        }
                        // SET BIAS
                        allDataSet.put("BIAS", 0);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        // PRINT SIZE OF THE DEFAULT DATA SET
        System.out.println("Size of default data set : " + allDataSet.size());
        System.out.println("Data Processing...");
        // STEP 2 : SCAN ALL OF FILES FOR DATA PROCESSING
        try (Stream<Path> paths = Files.walk(Paths.get("./txt_sentoken"))) {
            paths.forEach(filePath -> {
                if (Files.isRegularFile(filePath)) {
                    // OPEN FILE
                    try (BufferedReader br = new BufferedReader(new FileReader(filePath.toString()))) {
                        // INIT STRING BUILDER
                        sb.setLength(0);
                        // READ ALL LINES FROM THE SELECTED FILE
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            // IF NO PUNCTUATION MODE
                            if (punc_opt == 2)
                                line = puncEraser(line);
                            sb.append(line);
                        }
                        // SET A VECTOR FOR PROCESSING TEXTS FROM EACH OF FILES
                        HashMap<String, Integer> currentVector = new HashMap<String, Integer>();
                        for (String key : allDataSet.keySet()) {
                            currentVector.put(key, 0);
                        }
                        // READ ALL OF TEXTS FROM EACH OF FILES
                        String[] tokens = sb.toString().split(" ");
                        for (int i = 0; i < tokens.length; i++) {
                            // BINARY MODE
                            if (procs_opt == 2 && currentVector.get(tokens[i]) == 1) {
                                continue;
                            }
                            currentVector.put(tokens[i], currentVector.get(tokens[i]) + 1);
                        }
                        // PUT ELEMENT TO SPECIFY POSITIVE OR NEGATIVE
                        if (filePath.toString().contains("neg")) {
                            currentVector.put("DESIRED RESP", -1);
                        } else {
                            currentVector.put("DESIRED RESP", 1);
                        }
                        // PUT BIAS
                        currentVector.put("BIAS", 1);
                        // SAVE CURRENT VECTOR TO VECTOR LIST
                        vectorForEachFiles.add(currentVector);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Data Processing Done...");
        System.out.println("Grouping for 5-fold Cross-Validation...");
        // GROUPING FOR 5-FOLD CROSS VALIDATION
        // 0 - 199 (GROUP 1) 200 - 399 (GROUP 2) 400 - 599 (GROUP 3) 600 - 799 (GROUP 4) 800 - 999 (GROUP 5)
        // 1000 - 1199 (GROUP 1) 1200 - 1399 (GROUP 2) 1400 - 1599 (GROUP 3) 1600 - 1799 (GROUP 4) 1800 - 1999 (GROUP 5)
        ArrayList[] groups = new ArrayList[5];
        for (int i = 0; i < 5; i++)
            groups[i] = new ArrayList<HashMap<String, Integer>>();
        for (int i = 0; i < 1000; i++) {
            // GROUP 1
            if (i < 200) {
                groups[0].add(vectorForEachFiles.get(i));
                groups[0].add(vectorForEachFiles.get(i + 1000));
            }
            // GROUP 2
            else if (i < 400) {
                groups[1].add(vectorForEachFiles.get(i));
                groups[1].add(vectorForEachFiles.get(i + 1000));
            }
            // GROUP 3
            else if (i < 600) {
                groups[2].add(vectorForEachFiles.get(i));
                groups[2].add(vectorForEachFiles.get(i + 1000));
            }
            // GROUP 4
            else if (i < 800) {
                groups[3].add(vectorForEachFiles.get(i));
                groups[3].add(vectorForEachFiles.get(i + 1000));
            }
            // GROUP 5
            else {
                groups[4].add(vectorForEachFiles.get(i));
                groups[4].add(vectorForEachFiles.get(i + 1000));
            }
        }
        System.out.println("Grouping for 5-fold Cross-Validation Done...");
        // PERCEPTRON CLASSIFICATION
        System.out.println("Perceptron Classification...");
        // VARIABLE SET
        double[] precision_pos = new double[5];
        double[] precision_neg = new double[5];
        double[] recall_pos = new double[5];
        double[] recall_neg = new double[5];
        double[] precision = new double[5];
        double[] recall = new double[5];
        double[] accuracy = new double[5];
        double precision_min = 100.0;
        double precision_max = 0.0;
        double recall_min = 100.0;
        double recall_max = 0.0;
        double accuracy_min = 100.0;
        double accuracy_max = 0.0;

        // WEIGHT PROCESSING
        HashMap<String, Double> weights = new HashMap<String, Double>();
        int output;
        double localError, globalError; // LOCAL ERROR = DESIRED OUTPUT - ACTUAL OUTPUT & GLOBAL ERROR = SUM OF LOCAL ERRORS
        int iteration;
        // WEIGHT PROCESSING USING 5-FOLD CROSS VALIDATION
        for (int testSet = 0; testSet < 5; testSet++) {
            // KEY SET FOR WEIGHT VECTOR
            for (String key : allDataSet.keySet()) {
                if (key.equals("BIAS")) {
                    weights.put(key, bias);
                    continue;
                }
                weights.put(key, randomNumber(-1, 1));
            }
            // STEP 1 : LEARNING WEIGHTS
            boolean escape = false;
            iteration = 0;
            do {
                iteration++;
                // PRINT CURRENT NUMBER OF ITERATION
                // System.out.println(iteration + "TH ITERATION");
                globalError = 0;
                for (int i = 0; i < 5; i++) {
                    // SKIP TEST SET
                    if (i == testSet) {
                        continue;
                    }
                    // TRAINING SETS
                    for (int j = 0; j < groups[i].size(); j++) {
                        HashMap<String, Integer> trainingVector = (HashMap<String, Integer>) groups[i].get(j);
                        // CALCULATE PREDICTED CLASS
                        output = calculateOutput(theta, weights, trainingVector);
                        // DIFFERENCE BETWEEN ACTUAL AND PREDICTED CLASS VALUES
                        localError = trainingVector.get("DESIRED RESP") - output;
                        // UPDATE WEIGHTS AND BIAS
                        for (String key : weights.keySet()) {
                            if (key.equals("BIAS"))
                                continue;
                            else
                                weights.put(key, weights.get(key) + LEARNING_RATE * localError * trainingVector.get(key));
                        }
                        // SUMMATION SQUARED ERROR (ERROR VALUE FOR ALL INSTANCES) --> EUCLIDEAN SUM-OF-SQUARES
                        globalError += (localError * localError);
                    }
                }
                // PRINT CURRENT GLOBAL ERROR VALUE
                // System.out.println("Current global error : " + globalError);
                if(globalError == 0)
                    escape = true;
            } while (!escape && iteration < MAX_ITER);
            // PRINT WHETHER THE CURRENT SET CONVERGES OR DIVERGES
            if (escape)
                System.out.println("Set " + (testSet + 1) + " converge!");
            else
                System.out.println("Set " + (testSet + 1) + " diverge!");

            // STEP 2 : TEST USING CONFUSION MATRIX
            double TP = 0;
            double FN = 0;
            double TN = 0;
            double FP = 0;
            for (int i = 0; i < groups[testSet].size(); i++) {
                HashMap<String, Integer> testVector = (HashMap<String, Integer>) groups[testSet].get(i);

                output = calculateOutput(theta, weights, testVector);
                testVector.put("ACTUAL RESP", output);
                // GET RATES
                if (testVector.get("DESIRED RESP") == 1) {
                    if (testVector.get("DESIRED RESP") == testVector.get("ACTUAL RESP"))
                        TP += 1;
                    else
                        FN += 1;
                } else {
                    if (testVector.get("DESIRED RESP") == testVector.get("ACTUAL RESP"))
                        TN += 1;
                    else
                        FP += 1;
                }
            }
            // STEP 3 : CALCULATE VALUES
            // PRECISION
            precision_pos[testSet] = (TP / (TP + FP)) * 100;
            precision_neg[testSet] = (TN / (TN + FN)) * 100;
            precision[testSet] = (precision_pos[testSet] + precision_neg[testSet]) / 2;
            // RECALL
            recall_pos[testSet] = (TP / (TP + FN)) * 100;
            recall_neg[testSet] = (TN / (TN + FP)) * 100;
            recall[testSet] = (recall_pos[testSet] + recall_neg[testSet]) / 2;
            // ACCURACY
            accuracy[testSet] = ((TP + TN) / (TP + TN + FP + FN)) * 100;
            // SET MAX AND MIN
            if(precision[testSet] > precision_max)
                precision_max = precision[testSet];
            if(precision[testSet] < precision_min)
                precision_min = precision[testSet];
            if(recall[testSet] > recall_max)
                recall_max = recall[testSet];
            if(recall[testSet] < recall_min)
                recall_min = recall[testSet];
            if(accuracy[testSet] > accuracy_max)
                accuracy_max = accuracy[testSet];
            if(accuracy[testSet] < accuracy_min)
                accuracy_min = accuracy[testSet];

            // PRINT CALCULATED VALUES FOR EACH OF SET
            System.out.println("Precision for Set " + (testSet + 1) + " : " + precision[testSet]);
            System.out.println("Recall for Set " + (testSet + 1) + " : " + recall[testSet]);
            System.out.println("Accuracy for Set " + (testSet + 1) + " : " + accuracy[testSet]);
        }
        // STEP 4 : CALCULATE AVERAGE VALUES FOR PERCEPTRON CLASSIFICATION
        double precision_avg = 0;
        double recall_avg = 0;
        double accuracy_avg = 0;
        for (int i = 0; i < 5; i++) {
            precision_avg += precision[i];
            recall_avg += recall[i];
            accuracy_avg += accuracy[i];
        }
        precision_avg = precision_avg / 5;
        recall_avg = recall_avg / 5;
        accuracy_avg = accuracy_avg / 5;

        // PRINT AVERAGE, MAXIMUM, AND MINIMUM VALUES
        System.out.println("Average Precision : " + precision_avg);
        System.out.println("Maximum Precision : " + precision_max);
        System.out.println("Minimum Precision : " + precision_min);
        System.out.println("Average Recall : " + recall_avg);
        System.out.println("Maximum Recall : " + recall_max);
        System.out.println("Minimum Recall : " + recall_min);
        System.out.println("Average Accuracy : " + accuracy_avg);
        System.out.println("Maximum Accuracy : " + accuracy_max);
        System.out.println("Minimum Accuracy : " + accuracy_min);
        System.out.println("Perceptron Classification Done...");

        // NAIVE BAYES CLASSIFICATION
        System.out.println("Naive Bayes Classification...");

        // VARIABLE SET
        precision_pos = new double[5];
        precision_neg = new double[5];
        recall_pos = new double[5];
        recall_neg = new double[5];
        precision = new double[5];
        recall = new double[5];
        accuracy = new double[5];
        precision_min = 100.0;
        precision_max = 0.0;
        recall_min = 100.0;
        recall_max = 0.0;
        accuracy_min = 100.0;
        accuracy_max = 0.0;

        // ERASE BIAS ELEMENT FOR EACH OF VECTORS
        for (HashMap<String, Integer> vector : vectorForEachFiles) {
            vector.remove("BIAS");
        }
        allDataSet.remove("BIAS");

        // MAKE LOOK UP TABLES
        HashMap<String, Double> lookUpTablePos = new HashMap<String, Double>();
        HashMap<String, Double> lookUpTableNeg = new HashMap<String, Double>();
        for (String key : allDataSet.keySet()) {
            lookUpTablePos.put(key, 0.0);
            lookUpTableNeg.put(key, 0.0);
        }

        // NAIVE BAYES CLASSIFICATION USING 5-FOLD CROSS VALIDATION
        for (int testSet = 0; testSet < 5; testSet++) {
            // STEP 1 : LEARNING PHASE
            for (int i = 0; i < 5; i++) {
                if (testSet == i)
                    continue;
                // TRAINING SETS FOR CONSTRUCTING LOOK UP TABLES
                for (int j = 0; j < groups[i].size(); j++) {
                    HashMap<String, Integer> trainingVector = (HashMap<String, Integer>) groups[i].get(j);
                    // POSITIVE VALUE
                    if (trainingVector.get("DESIRED RESP") == 1) {
                        for (String key : lookUpTablePos.keySet())
                            lookUpTablePos.put(key, lookUpTablePos.get(key) + (double) trainingVector.get(key));
                    }
                    // NEGATIVE VALUE
                    else {
                        for (String key : lookUpTableNeg.keySet())
                            lookUpTableNeg.put(key, lookUpTableNeg.get(key) + (double) trainingVector.get(key));
                    }
                }
            }
            // HANDLING ZERO VALUE
            for (String key : lookUpTablePos.keySet()) {
                lookUpTablePos.put(key, lookUpTablePos.get(key) + 1.0);
                lookUpTableNeg.put(key, lookUpTableNeg.get(key) + 1.0);
            }
            // DIVIDE EACH OF ELEMENTS OF TOTAL TRAINING SET NUMBER
            double totalPos = 0.0;
            double totalNeg = 0.0;
            // GET TOTAL
            for (String key : lookUpTablePos.keySet()) {
                totalPos += lookUpTablePos.get(key);
                totalNeg += lookUpTableNeg.get(key);
            }
            // DIVIDE EACH OF ELEMENTS OVER TOTAL
            for (String key : lookUpTablePos.keySet()) {
                lookUpTablePos.put(key, lookUpTablePos.get(key) / totalPos);
                lookUpTableNeg.put(key, lookUpTableNeg.get(key) / totalNeg);
            }
            // STEP 2 : TEST USING CONFUSION MATRIX
            double TP = 0;
            double FN = 0;
            double TN = 0;
            double FP = 0;
            for (int i = 0; i < groups[testSet].size(); i++) {
                HashMap<String, Integer> testVector = (HashMap<String, Integer>) groups[testSet].get(i);
                double posProb = Math.log(10);
                double negProb = Math.log(10);

                // CALCULATE POSITIVE AND NEGATIVE LOOK UP TABLES IF GIVEN DATA HAS THE KEY VALUE
                for (String key : lookUpTablePos.keySet()) {
                    if (testVector.get(key) > 0) {
                        posProb = posProb + Math.log(lookUpTablePos.get(key));
                        negProb = negProb + Math.log(lookUpTableNeg.get(key));
                    }
                }
                output = (posProb >= negProb) ? 1 : -1;
                testVector.put("ACTUAL RESP", output);
                // GET RATES
                if (testVector.get("DESIRED RESP") == 1) {
                    if (testVector.get("DESIRED RESP") == testVector.get("ACTUAL RESP"))
                        TP += 1;
                    else
                        FN += 1;
                } else {
                    if (testVector.get("DESIRED RESP") == testVector.get("ACTUAL RESP"))
                        TN += 1;
                    else
                        FP += 1;
                }
            }
            // STEP 3 : CALCULATE VALUES
            // PRECISION
            precision_pos[testSet] = (TP / (TP + FP)) * 100;
            precision_neg[testSet] = (TN / (TN + FN)) * 100;
            precision[testSet] = (precision_pos[testSet] + precision_neg[testSet]) / 2.0;
            // RECALL
            recall_pos[testSet] = (TP / (TP + FN)) * 100;
            recall_neg[testSet] = (TN / (TN + FP)) * 100;
            recall[testSet] = (recall_pos[testSet] + recall_neg[testSet]) / 2.0;
            // ACCURACY
            accuracy[testSet] = ((TP + TN) / (TP + TN + FP + FN)) * 100;
            // SET MAX AND MIN
            if(precision[testSet] > precision_max)
                precision_max = precision[testSet];
            if(precision[testSet] < precision_min)
                precision_min = precision[testSet];
            if(recall[testSet] > recall_max)
                recall_max = recall[testSet];
            if(recall[testSet] < recall_min)
                recall_min = recall[testSet];
            if(accuracy[testSet] > accuracy_max)
                accuracy_max = accuracy[testSet];
            if(accuracy[testSet] < accuracy_min)
                accuracy_min = accuracy[testSet];

            // PRINT CALCULATE VALUES FOR EACH OF SET
            System.out.println("Precision for Set " + (testSet + 1) + " : " + precision[testSet]);
            System.out.println("Recall for Set " + (testSet + 1) + " : " + recall[testSet]);
            System.out.println("Accuracy for Set " + (testSet + 1) + " : " + accuracy[testSet]);
        }
        // STEP 4 : CALCULATE AVERAGE VALUES FOR PERCEPTRON CLASSIFICATION
        precision_avg = 0.0;
        recall_avg = 0.0;
        accuracy_avg = 0.0;
        for (int i = 0; i < 5; i++) {
            precision_avg += precision[i];
            recall_avg += recall[i];
            accuracy_avg += accuracy[i];
        }
        precision_avg = precision_avg / 5.0;
        recall_avg = recall_avg / 5.0;
        accuracy_avg = accuracy_avg / 5.0;

        // PRINT AVERAGE, MAXIMUM, AND MINIMUM VALUES
        System.out.println("Average Precision : " + precision_avg);
        System.out.println("Maximum Precision : " + precision_max);
        System.out.println("Minimum Precision : " + precision_min);
        System.out.println("Average Recall : " + recall_avg);
        System.out.println("Maximum Recall : " + recall_max);
        System.out.println("Minimum Recall : " + recall_min);
        System.out.println("Average Accuracy : " + accuracy_avg);
        System.out.println("Maximum Accuracy : " + accuracy_max);
        System.out.println("Minimum Accuracy : " + accuracy_min);
        System.out.println("Naive Bayes Classification Done...");

    }

    // FUNCTION FOR CONVERTING PUNCTUATION TO EMPTY SPACE
    public static String puncEraser(String line) {
        for (int i = 0; i < line.length(); i++)
            if (line.charAt(i) == '!')
                line = line.substring(0, i) + ' ' + line.substring(i + 1);
        return line;
    }

    // FUNCTION FOR GENERATING RANDOM DOUBLE NUMBER FROM min TO max AS #.#### FORMAT
    public static double randomNumber(int min, int max) {
        DecimalFormat df = new DecimalFormat("#.####");
        double d = min + Math.random() * (max - min);
        String s = df.format(d);
        double x = Double.parseDouble(s);
        return x;
    }

    // FUNCTION FOR CALCULATING OUTPUT USING GIVEN WEIGHTS AND VECTOR
    public static int calculateOutput(int theta, HashMap<String, Double> weights, HashMap<String, Integer> vector) {
        double sum = 0;
        for (String key : weights.keySet()) {
            sum += vector.get(key) * weights.get(key); // INCLUDED BIAS CALCULATION
        }
        return (sum >= theta) ? 1 : -1;
    }

}
