/*
 * Example class for building a classifier in Weka. 
 * 
 * @author johan.hagelback@bth.se
 */
package myclassifier;

import weka.classifiers.*;
import weka.core.*;

public class MyClassifier extends Classifier 
{
    /*
     * This is the method for training the classifier. Add the
     * code needed to train a classifier on the training set,
     * available in the data datastructure.
     */
    public void buildClassifier(Instances data)
    {
  
    }
    
    /*
     * This is the method for classifying the specified Instance.
     * Add the code needed to make a classification based on your
     * trained classifier.
     */
    public double classifyInstance(Instance inst) throws NoSupportForMissingValuesException
    {
        int rnd = (int)(Math.random() * 2);
        return rnd;
    }
    
    /*
     * This is the main method for running the classifier. The
     * only thing you might need to change here is the path to
     * the data set.
     */
    public static void main(String[] args)
    {
        try 
        {
            String[] params = new String[4];
            params[0] = "-t";
            params[1] = "dataset/weather.nominal.arff";
            params[2] = "-x";
            params[3] = "10";
            System.out.println(Evaluation.evaluateModel(new MyClassifier(), params));
        }
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
    } 
}
