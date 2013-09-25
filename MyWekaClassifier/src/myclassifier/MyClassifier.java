/*
 * Example class for building a classifier in Weka. 
 * 
 * @author johan.hagelback@bth.se
 */
package myclassifier;

import java.util.HashMap;
import java.util.Map;

import weka.classifiers.*;
import weka.core.*;

public class MyClassifier extends Classifier 
{
	private Map<String, Double> values = new HashMap<String, Double>();
    /*
     * This is the method for training the classifier. Add the
     * code needed to train a classifier on the training set,
     * available in the data datastructure.
     */
    public void buildClassifier(Instances data){
    	values.put("T", (double)data.numInstances());
    	System.out.println(data.numAttributes());
    	Map<String, Integer> values = new HashMap<String, Integer>();
    	int number_attr = data.numAttributes();
    	
    	
    	int sunny = 0;
    	int rainy = 0;
    	int overcast = 0;
    	int hot = 0;
    	int mild = 0;
    	int cool = 0;
    	int high = 0;
    	int normal = 0;
    	int windy = 0;
    	int not_windy = 0;
    	
    	int sunny_yes = 0;
    	int rainy_yes = 0;
    	int overcast_yes = 0;
    	int hot_yes = 0;
    	int mild_yes = 0;
    	int cool_yes = 0;
    	int high_yes = 0;
    	int normal_yes = 0;
    	int windy_yes = 0;
    	int not_wind_yes = 0;
    	System.out.println(data.instance(0).stringValue(0));
    		
    		
    	
    	
    	
    }
    
    /*
     * This is the method for classifying the specified Instance.
     * Add the code needed to make a classification based on your
     * trained classifier.
     */
    public double classifyInstance(Instance inst) throws NoSupportForMissingValuesException {
        int rnd = (int)(Math.random() * 2);
        return rnd;
    }
    
    /*
     * This is the main method for running the classifier. The
     * only thing you might need to change here is the path to
     * the data set.
     */
    public static void main(String[] args){
        try {
            String[] params = new String[4];
            params[0] = "-t";
            params[1] = "dataset/weather.nominal.arff";
            params[2] = "-x";
            params[3] = "10";
            System.out.println(Evaluation.evaluateModel(new MyClassifier(), params));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    } 
}
