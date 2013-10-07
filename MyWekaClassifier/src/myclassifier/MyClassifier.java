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
    	int num_inst = data.numInstances();
    	int num_attr = data.numAttributes();
    	numericAtt = new HashMap<String, Map<String, List<Double>>>(); 
    	yes_count = 0;
    	no_count = 0;
    	// list of maps, each map belongs to 1 attribute (same index as in data)
    	// map holds for each attribute value a list
    	// the list saves in the first position the amount of instances where the attribute belongs to yes (last attribute in data)
    	// and 2nd the amount of instances where the attribute belongs to no (last attribute in data)
    	// Version numerical attributes:	just one list for the attribute. 
    	//									1st position = average yes, 2nd position = average no
    	attr_values_count = new HashMap<String, Map<String, List<Double>>>();
    	
    	for (int i = 0; i < num_attr-1; i++){
    		Map<String, List<Double>> new_map = new HashMap<String, List<Double>>();
    		attr_values_count.put(data.attribute(i).name(), new_map);
    		if (data.attribute(i).isNumeric()) {
    			List<Double> value_yes_list = new ArrayList<Double>();
    			List<Double> value_no_list = new ArrayList<Double>();
    			new_map.put("yes", value_yes_list);
    			new_map.put("no", value_no_list);
    			numericAtt.put(data.attribute(i).name(), new_map);
    			System.out.println(data.attribute(i).name());
    		}
    	}
    	
    	
    	for (int inst_nu = 0; inst_nu < num_inst; inst_nu++){
    		Instance inst = data.instance(inst_nu);
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
    
    private void countValuesNominal(int attr_nu, Instance inst, int num_attr){
    	Map<String, List<Double>> attr_map = attr_values_count.get(inst.attribute(attr_nu).name());
		String attr_val = inst.stringValue(attr_nu);
		System.out.println(attr_val);
		if (!attr_map.containsKey(inst.attribute(attr_nu).name())){
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

    
    private void countValuesNumerical(int attr_nu, Instance inst, int num_attr) {
    	Map<String, List<Double>> attr_map = attr_values_count.get(inst.attribute(attr_nu).name());
    	String attr_name = inst.attribute(attr_nu).name();
		if (!attr_map.containsKey(attr_name)){
			List<Double> yes_no_list = new ArrayList<Double>();
			// Count of yes
			yes_no_list.add((double) 0);
			// sum of yes
			yes_no_list.add((double) 0);
			// Count of no
			yes_no_list.add((double) 0);
			// sum of no
			yes_no_list.add((double) 0);
			attr_map.put(attr_name, yes_no_list);
		}
		double count = 0;
		if (inst.stringValue(num_attr-1).equals("yes")) {
			count = attr_map.get(attr_name).get(0);
    		attr_map.get(attr_name).set(1, ((attr_map.get(attr_name).get(1) * count + inst.value(attr_nu)) / (count + 1)));
			attr_map.get(attr_name).set(0, count+1);
			System.out.println(inst.value(attr_nu));
    		// safe all results of yes
    		numericAtt.get(attr_name).get("yes").add(inst.value(attr_nu));
    	} else {
    		count = attr_map.get(attr_name).get(2);
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
//    	System.out.println(inst.stringValue(0) +", "+ inst.stringValue(1) + ", " + inst.stringValue(2) + ", " + inst.stringValue(3));
    	double attr_affect_yes_prob = 1.0;
    	double attr_affect_no_prob = 1.0;
    	for (int attr_index = 0; attr_index < inst.numAttributes()-1; attr_index++){
    		//System.out.println(inst.stringValue(attr_index) + " " + inst.attribute(attr_index).name() );
    		
    		String attr_name = inst.attribute(attr_index).name();
    		if (numericAtt.containsKey(attr_name)) {
    			double high_yes = (-1) * Math.pow(inst.value(attr_index) - numericAtt.get(attr_name).get(attr_name).get(1), 2) / (2 * Math.pow(numericAtt.get(attr_name).get(attr_name).get(4),2));
    			attr_affect_yes_prob *= 1 / (Math.sqrt(2 * Math.PI) * numericAtt.get(attr_name).get(attr_name).get(4)) * Math.pow(Math.E, high_yes) ; 
    			double high_no = (-1) * Math.pow(inst.value(attr_index) - numericAtt.get(attr_name).get(attr_name).get(3), 2) / (2 * Math.pow(numericAtt.get(attr_name).get(attr_name).get(5),2));
    			attr_affect_no_prob *= 1 / (Math.sqrt(2 * Math.PI) * numericAtt.get(attr_name).get(attr_name).get(5)) * Math.pow(Math.E, high_no) ; 
    		} else {
    			String attr_value = inst.stringValue(attr_index);
        		//value affect yes count / yes overall
        		attr_affect_yes_prob *= (double)attr_values_count.get(attr_name).get(attr_value).get(0)/(double)yes_count;
        		//value affect no count / no overall
        		attr_affect_no_prob *= (double)attr_values_count.get(attr_name).get(attr_value).get(1)/(double)no_count;
    		}
    		
    	//	Map<String, List<Double>> new_map = new HashMap<String, List<Double>>();
    	//	attr_values_count.put(data.attribute(i).name(), new_map);
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
