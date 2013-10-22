/*
 * Example class for building a classifier in Weka. 
 * 
 * @author johan.hagelback@bth.se
 */
package myclassifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import weka.classifiers.*;
import weka.core.*;

public class MyClassifier extends Classifier 
{
	private Map<String,Map<String, List<Double>>> attr_values_count;
	private Map<String,Map<String, List<Double>>> numericAtt;
	private int yes_count;
	private int no_count;
	
    /*
     * This is the method for training the classifier. Add the
     * code needed to train a classifier on the training set,
     * available in the data datastructure.
     */
    public void buildClassifier(Instances data){
    	// number of intstances
    	int num_inst = data.numInstances();
    	// number of Attributes
    	int num_attr = data.numAttributes();
    	numericAtt = new HashMap<String, Map<String, List<Double>>>(); 
    	yes_count = 0;
    	no_count = 0;
    	attr_values_count = new HashMap<String, Map<String, List<Double>>>();
    	
    	// each attribute of the datasets gets an entry in attr_values_count
    	for (int i = 0; i < num_attr-1; i++){
    		Map<String, List<Double>> new_map = new HashMap<String, List<Double>>();
    		attr_values_count.put(data.attribute(i).name(), new_map);
    		// if attribute is a numeric, it gets an entry in numericAtt
    		if (data.attribute(i).isNumeric()) {
    			List<Double> value_yes_list = new ArrayList<Double>();
    			List<Double> value_no_list = new ArrayList<Double>();
    			new_map.put("yes", value_yes_list);
    			new_map.put("no", value_no_list);
    			numericAtt.put(data.attribute(i).name(), new_map);
    		}
    	}
    	
    	// evaluating the data
    	for (int inst_nu = 0; inst_nu < num_inst; inst_nu++){
    		Instance inst = data.instance(inst_nu);
    		// count yes and no entries
    		if (inst.stringValue(num_attr-1).equals("yes")){
    			yes_count += 1;
    		} else {
    			no_count += 1;
    		}
    		for (int attr_nu = 0; attr_nu < num_attr-1; attr_nu++){
    			if (numericAtt.containsKey(inst.attribute(attr_nu).name())) {
    				countValuesNumerical(attr_nu, inst, num_attr);
    			} else {
    				countValuesNominal(attr_nu, inst, num_attr);
    			}
    		}
    	}
    	
    	// calculate standard daviations of yes and no of the numeric attributes 
    	for (String str: numericAtt.keySet()) {
    		Map<String, List<Double>> attr_map = attr_values_count.get(str);
    		List<Double> yes_count = attr_map.get("yes");
    		List<Double> no_count = attr_map.get("no");
    		List<Double> values = attr_map.get(str);
    		
    		Iterator<Double> iter_yes = yes_count.iterator();
    		Iterator<Double> iter_no = no_count.iterator();
    		double meandeviation_yes = 0;
    		double meandeviation_no = 0;
    		while (iter_yes.hasNext()) {
    			meandeviation_yes += Math.pow(iter_yes.next() - values.get(1), 2);
    		}
    		if (values.get(0) > 1) {
    			meandeviation_yes = Math.sqrt(meandeviation_yes/values.get(0) - 1);
    		}
    		
    		while (iter_no.hasNext()) {
    			meandeviation_no += Math.pow(iter_no.next() - values.get(3), 2);
    		}
    		if (values.get(2) > 1) {
    			meandeviation_no = Math.sqrt(meandeviation_no/values.get(2) - 1);
    		}

    		values.add(meandeviation_yes);
    		values.add(meandeviation_no);
    	}
    }
    
    
    /**
     * Evaluating of the nominal attribute, count the number of yes and no
     * 
     * @param attr_nu 	
     * @param inst 		
     * @param num_attr
     */
    private void countValuesNominal(int attr_nu, Instance inst, int num_attr){
    	Map<String, List<Double>> attr_map = attr_values_count.get(inst.attribute(attr_nu).name());
		String attr_val = inst.stringValue(attr_nu);
		if (!attr_map.containsKey(attr_val)){
			List<Double> yes_no_list = new ArrayList<Double>();
			yes_no_list.add((double) 0);
			yes_no_list.add((double) 0);
			attr_map.put(attr_val, yes_no_list);
		}
		if (inst.stringValue(num_attr-1).equals("yes")){
			attr_map.get(attr_val).set(0, attr_map.get(attr_val).get(0)+1); 
		} else {
			attr_map.get(attr_val).set(1, attr_map.get(attr_val).get(1)+1);
		}
    }

    /**
     * Evaluation of the numeric attribute
     * 
     * @param attr_nu
     * @param inst
     * @param num_attr
     */
    private void countValuesNumerical(int attr_nu, Instance inst, int num_attr) {
    	Map<String, List<Double>> attr_map = attr_values_count.get(inst.attribute(attr_nu).name());
    	String attr_name = inst.attribute(attr_nu).name();
		if (!attr_map.containsKey(attr_name)){
			List<Double> yes_no_list = new ArrayList<Double>();
			// Count of yes
			yes_no_list.add((double) 0);
			// average of yes
			yes_no_list.add((double) 0);
			// Count of no
			yes_no_list.add((double) 0);
			// average of no
			yes_no_list.add((double) 0);
			attr_map.put(attr_name, yes_no_list);
		}
		double count = 0;
		if (inst.stringValue(num_attr-1).equals("yes")) {
			count = attr_map.get(attr_name).get(0);
			// calculate the new average of yes
    		attr_map.get(attr_name).set(1, ((attr_map.get(attr_name).get(1) * count + inst.value(attr_nu)) / (count + 1)));
			attr_map.get(attr_name).set(0, count+1);
    		// safe all results of yes
    		numericAtt.get(attr_name).get("yes").add(inst.value(attr_nu));
    	} else {
    		count = attr_map.get(attr_name).get(2);
    		// calculate the new average of no
    		attr_map.get(attr_name).set(3, ((attr_map.get(attr_name).get(3) * count+ inst.value(attr_nu)) / (count + 1)));
    		attr_map.get(attr_name).set(2, count+1); 
    		// safe all result of no
    		numericAtt.get(attr_name).get("no").add(inst.value(attr_nu));
    	}
    }
    
    /*
     * This is the method for classifying the specified Instance.
     * Add the code needed to make a classification based on your
     * trained classifier.
     */
    public double classifyInstance(Instance inst) throws NoSupportForMissingValuesException {
    	double attr_affect_yes_prob = 1.0;
    	double attr_affect_no_prob = 1.0;
    	for (int attr_index = 0; attr_index < inst.numAttributes()-1; attr_index++){
    		
    		String attr_name = inst.attribute(attr_index).name();
    		if (numericAtt.containsKey(attr_name)) {
    			// calculate the densinity function of yes
    			double high_yes = (-1) * Math.pow(inst.value(attr_index) - numericAtt.get(attr_name).get(attr_name).get(1), 2) / (2 * Math.pow(numericAtt.get(attr_name).get(attr_name).get(4),2));
    			double down_yes = Math.sqrt(2 * Math.PI) * numericAtt.get(attr_name).get(attr_name).get(4);
    			attr_affect_yes_prob *= (1 / down_yes) * Math.pow(Math.E, high_yes) ; 
    			// calculate the densinity function of no
    			double high_no = (-1) * Math.pow(inst.value(attr_index) - numericAtt.get(attr_name).get(attr_name).get(3), 2) / (2 * Math.pow(numericAtt.get(attr_name).get(attr_name).get(5),2));
    			double down_no = Math.sqrt(2 * Math.PI) * numericAtt.get(attr_name).get(attr_name).get(5);
    			attr_affect_no_prob *= (1 / down_no) * Math.pow(Math.E, high_no) ; 
    		} else {
    			String attr_value = inst.stringValue(attr_index);
        		//value affect yes count / yes overall
        		attr_affect_yes_prob *= (double)attr_values_count.get(attr_name).get(attr_value).get(0)/(double)yes_count;
        		//value affect no count / no overall
        		attr_affect_no_prob *= (double)attr_values_count.get(attr_name).get(attr_value).get(1)/(double)no_count;
    		}
    		
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
            params[1] = "dataset/weather.numeric.arff";
            params[2] = "-x";
            params[3] = "10";
            System.out.println(Evaluation.evaluateModel(new MyClassifier(), params));
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    } 
}
