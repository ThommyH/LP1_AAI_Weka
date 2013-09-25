/*
 * Example class for building a classifier in Weka. 
 * 
 * @author johan.hagelback@bth.se
 */
package myclassifier;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weka.classifiers.*;
import weka.core.*;

public class MyClassifier extends Classifier 
{
	private ArrayList<Map<String, List<Integer>>> attr_values_count;
	private int yes_count;
	private int no_count;
	
    /*
     * This is the method for training the classifier. Add the
     * code needed to train a classifier on the training set,
     * available in the data datastructure.
     */
    public void buildClassifier(Instances data){
    	int num_inst = data.numInstances();
    	int num_attr = data.numAttributes();
    	yes_count = 0;
    	no_count = 0;
    	// list of maps, each map belongs to 1 attribute (same index as in data)
    	// map holds for each attribute value a list
    	// the list saves in the first position the amount of instances where the attribute belongs to yes (last attribute in data)
    	// and 2nd the amount of instances where the attribut belongs to no (last attribute in data)
    	attr_values_count = new ArrayList<Map<String, List<Integer>>>();
    	
    	for (int i = 0; i < num_attr-1; i++){
    		Map<String, List<Integer>> new_map = new HashMap<String, List<Integer>>();
    		attr_values_count.add(new_map);
    	}
    	
    	for (int inst_nu = 0; inst_nu < num_inst; inst_nu++){
    		Instance inst = data.instance(inst_nu);
    		if (inst.stringValue(num_attr-1).equals("yes")){
    			yes_count += 1;
    		} else {
    			no_count += 1;
    		}
    		for (int attr_nu = 0; attr_nu < num_attr-1; attr_nu++){
    			Map<String, List<Integer>> attr_map = attr_values_count.get(attr_nu);
    			String attr_val = inst.stringValue(attr_nu);
    			System.out.println(attr_val);
    			if (!attr_map.containsKey(attr_val)){
    				List<Integer> yes_no_list = new ArrayList<Integer>();
    				yes_no_list.add(0);
    				yes_no_list.add(0);
    				attr_map.put(attr_val, yes_no_list);
    			}
    			
    			if (inst.stringValue(num_attr-1).equals("yes")){
    				attr_map.get(attr_val).set(0, (Integer)attr_map.get(attr_val).get(0)+1); 
    			} else {
    				attr_map.get(attr_val).set(1, (Integer)attr_map.get(attr_val).get(1)+1);
    			}
    		}
    	}   	
    }
    
    /*
     * This is the method for classifying the specified Instance.
     * Add the code needed to make a classification based on your
     * trained classifier.
     */
    public double classifyInstance(Instance inst) throws NoSupportForMissingValuesException {
    	System.out.println(inst.stringValue(0) +", "+ inst.stringValue(1) + ", " + inst.stringValue(2) + ", " + inst.stringValue(3));
    	double attr_affect_yes_prob = 1.0;
    	double attr_affect_no_prob = 1.0;
    	for (int attr_index = 0; attr_index < inst.numAttributes()-1; attr_index++){
    		String attr_value = inst.stringValue(attr_index);
    		//value affect yes count / yes overall
    		attr_affect_yes_prob *= (double)attr_values_count.get(attr_index).get(attr_value).get(0)/(double)yes_count;
    		//value affect no count / no overall
    		attr_affect_no_prob *= (double)attr_values_count.get(attr_index).get(attr_value).get(1)/(double)no_count;
    	}
    	
        double yes_prob = ((double)yes_count/(double)(yes_count+no_count)) * attr_affect_yes_prob;
        double no_prob = ((double)no_count/(double)(yes_count+no_count)) * attr_affect_no_prob;
        if (yes_prob >= no_prob){
        	return 0;
        } else {
        	return 1;
    	}
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
