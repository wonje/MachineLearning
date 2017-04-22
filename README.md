# Judge whether selected movie review is positive or negative
This is machine learning class individual project. 
It uses two ways of classfication, perceptron classfication and naive bayes classfication.

This project is written in Java with Intellij IDE.

### Data set
The dataset, in the "txt_sentoken" folder, contains each of 1000 positive and negative movie review as .txt file.
In this step, the program converts data in .txt files to labeled data to use next step.

### Classification
These classifications uses 5-fold cross-validation.

#### Perceptron Classification
This classification uses binary classfication, positive and negative, using labeled data.

#### Naive Bayes Classification
This classification uses Naive Bayes classifier using labeled data with confusion matrix.

## How to run this code

To run this code, following this:
- Run following instruction in your shell
```
java -jar out\artifacts\CSE353_Assignment1_jar\CSE353_Assignment1.jar 
```

- Then, you will see the following questions:

```
Select processing options for your data processing
1. Frequency
2. Binary

>> You should type "1" or "2"
```

```
Select punctuation marks option
1. Punctuation
2. No Punctuation

>> You should type "1" or "2"
```

Then, this code works following the options you selected

# Example Output
## Frequency &amp; Punctuation

### Perceptron Classification

|   | **Precision** | **Recall** | **Accuracy** |
| --- | --- | --- | --- |
| **Maximum** | 85.75 | 85.75 | 85.75 |
| **Minimum** | 82.79 | 82.75 | 82.75 |
| **Average** | 84.26 | 84.15 | 84.15 |

### Naïve Bayes Classification

|   | **Precision** | **Recall** | **Accuracy** |
| --- | --- | --- | --- |
| **Maximum** | 84.27 | 83.50 | 83.50 |
| **Minimum** | 81.67 | 81.25 | 81.25 |
| **Average** | 82.93 | 82.45 | 82.45 |

## Frequency &amp; No Punctuation

### Perceptron Classification

|   | **Precision** | **Recall** | **Accuracy** |
| --- | --- | --- | --- |
| **Maximum** | 85.51 | 85.50 | 85.50 |
| **Minimum** | 82.00 | 82.00 | 82.00 |
| **Average** | 83.39 | 83.35 | 83.35 |

### Naïve Bayes Classification

|   | **Precision** | **Recall** | **Accuracy** |
| --- | --- | --- | --- |
| **Maximum** | 84.99 | 84.25 | 84.25 |
| **Minimum** | 82.10 | 81.75 | 81.75 |
| **Average** | 83.26 | 82.8 | 82.8 |

## Binary &amp; Punctuation

### Perceptron Classification

|   | **Precision** | **Recall** | **Accuracy** |
| --- | --- | --- | --- |
| **Maximum** | 84.56 | 84.25 | 84.25 |
| **Minimum** | 81.85 | 81.75 | 81.75 |
| **Average** | 83.31 | 83.10 | 83.10 |

### Naïve Bayes Classification

|   | **Precision** | **Recall** | **Accuracy** |
| --- | --- | --- | --- |
| **Maximum** | 84.56 | 84.25 | 84.25 |
| **Minimum** | 82.21 | 82.00 | 82.00 |
| **Average** | 83.17 | 83.00 | 83.00 |

## Binary &amp; No Punctuation

### Perceptron Classification

|   | **Precision** | **Recall** | **Accuracy** |
| --- | --- | --- | --- |
| **Maximum** | 84.56 | 84.25 | 84.25 |
| **Minimum** | 81.79 | 81.50 | 81.50 |
| **Average** | 82.83 | 82.65 | 82.65 |

### Naïve Bayes Classification

|   | **Precision** | **Recall** | **Accuracy** |
| --- | --- | --- | --- |
| **Maximum** | 84.56 | 84.25 | 84.25 |
| **Minimum** | 82.21 | 82.00 | 82.00 |
| **Average** | 83.17 | 83.00 | 83.00 |

## Replicability of experiment
All of weight elements in HashMap&lt;String, Double&gt; are set to random decimal number from -1 to 1 as #.#### format whenever each of new folds starts. Therefore, user can find that every tried experiment will display different results.

## Heuristics
For perceptron classification, the global error, which means sum of differences between actual response and desired response of each of data vectors, usually results 0 value nearby 40 iterations for each of folds on frequency mode. Therefore, the maximum number of iteration is set to 40 and the perceptron classification would converge or diverge from given data set.
