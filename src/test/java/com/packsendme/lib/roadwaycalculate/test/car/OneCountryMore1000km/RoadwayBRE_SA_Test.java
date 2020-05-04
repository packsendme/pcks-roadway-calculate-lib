package com.packsendme.lib.roadwaycalculate.test.car.OneCountryMore1000km;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packsendme.lib.common.constants.calculador.Currency_Constants;
import com.packsendme.lib.common.constants.generic.MetricUnitMeasurement_Constants;
import com.packsendme.lib.common.constants.way.Roadway_Constants;
import com.packsendme.roadway.bre.rule.costs.model.RuleCosts_Model;
import com.packsendme.roadway.bre.rule.instance.model.RuleInstance_Model;
import com.packsendme.roadway.bre.rule.model.RoadwayBRE_Model;

 public class RoadwayBRE_SA_Test {

	private String jsonSouthAmerica = null;
	private String url_json = "src/test/resources/RoadwayBRE_SA_V1.json";
	private String file_country_sa = "src/test/resources/Country_SA.txt";
	private String file_way_sa = "src/test/resources/Way.txt";
	
	private final String name_rule = "Roadway-SouthAmerica-BRE";

	// Rule-Instance
	RuleInstance_Model ruleInstance = new RuleInstance_Model();
	// Rule-Costs
	RuleCosts_Model ruleCosts = new RuleCosts_Model();
	
	
	public RoadwayBRE_Model generateJsonSouthAmerica() throws URISyntaxException, IOException {
		RoadwayBRE_Model roadwayBRE = new RoadwayBRE_Model();

		// RoadwayModel
		roadwayBRE.name_rule = name_rule;
		roadwayBRE.date_creation = new Date();
		roadwayBRE.date_change = null;
		roadwayBRE.rate_reshipping = 100.0;
		roadwayBRE.status = "Active";
		
		Map<String, RuleInstance_Model> ruleInstanceL = generateRuleInstance();
		Map<String,Map<String, RuleCosts_Model>> ruleCostsL = generateRuleCosts();
		
		roadwayBRE.ruleInstance = ruleInstanceL;
		roadwayBRE.ruleCosts = ruleCostsL;
		
		
		ObjectMapper mapper = new ObjectMapper();
		jsonSouthAmerica = mapper.writeValueAsString(roadwayBRE);
		System.out.println(jsonSouthAmerica);
   		Assert.notNull(jsonSouthAmerica);
   		//inputJsonFileSouthAmerica(jsonSouthAmerica);
   		
   		return roadwayBRE;
	}
	
	
	Map<String, RuleInstance_Model> generateRuleInstance() throws URISyntaxException, IOException {
		
		Map<String, RuleInstance_Model> ruleInstanceL = new HashMap<String, RuleInstance_Model>();
		RuleInstance_Model ruleInstance_Model = null;
		
		List<String> wayL = getWay();
		for(String way : wayL) {
			
			if(way.equals(Roadway_Constants.ROADWAY_BICYCLE)) {
				ruleInstance_Model = new RuleInstance_Model(Roadway_Constants.ROADWAY_BICYCLE, 5.0, 5.0, false,
						MetricUnitMeasurement_Constants.kilograma_UnitMeasurement,MetricUnitMeasurement_Constants.kilometro_UnitMeasurement);
			}
			else if(way.equals(Roadway_Constants.ROADWAY_CAR)) {
				ruleInstance_Model = new RuleInstance_Model(Roadway_Constants.ROADWAY_CAR, 9972759.0, 200.0, false,
						MetricUnitMeasurement_Constants.kilograma_UnitMeasurement,MetricUnitMeasurement_Constants.kilometro_UnitMeasurement);
			}
			else if(way.equals(Roadway_Constants.ROADWAY_MOTORCYCLE)) {
				ruleInstance_Model = new RuleInstance_Model(Roadway_Constants.ROADWAY_MOTORCYCLE, 8434.0, 100.0, false,
						MetricUnitMeasurement_Constants.kilograma_UnitMeasurement,MetricUnitMeasurement_Constants.kilometro_UnitMeasurement);
			}
			else if(way.equals(Roadway_Constants.ROADWAY_TRUCK)) {
				ruleInstance_Model = new RuleInstance_Model(Roadway_Constants.ROADWAY_TRUCK, 8434.0, 14.0, false,
						MetricUnitMeasurement_Constants.tonelada_UnitMeasurement,MetricUnitMeasurement_Constants.kilometro_UnitMeasurement);
			}
			else if(way.equals(Roadway_Constants.ROADWAY_WALKING)) {
				ruleInstance_Model = new RuleInstance_Model(Roadway_Constants.ROADWAY_WALKING, 2.0, 1.0, false,
						MetricUnitMeasurement_Constants.kilograma_UnitMeasurement,MetricUnitMeasurement_Constants.kilometro_UnitMeasurement);
			}
			ruleInstanceL.put(way, ruleInstance_Model);
		}
		return ruleInstanceL;
	}

	Map<String, Map<String, RuleCosts_Model>> generateRuleCosts() {
		RuleCosts_Model ruleCosts = null;
		Map<String,Map<String, RuleCosts_Model>> costsL = new HashMap<String,Map<String, RuleCosts_Model>>();
		
		List<String> countryL = getCountry();
		List<String> wayL = getWay();
		Map<String, RuleCosts_Model> costsCountryWayL = new HashMap<String,RuleCosts_Model>();

		for(String country : countryL) {
			for(String way : wayL) {
				
				/*
				 * RuleCosts_Model(Double weight_cost, Double distance_cost, Double worktime_cost, Double average_consumption_cost, 
				 * Double tolls_average_cost, Double fuel_average_cost, String currency_cost, Double rate_availability)
				 */
				if(way.equals(Roadway_Constants.ROADWAY_BICYCLE)) {
					ruleCosts = new RuleCosts_Model(0.1, 0.10, 0.10, 10.00, 2.21, 0.80, Currency_Constants.DOLLAR_CURRENCY_SYMBOL,0.0);
				}
				else if(way.equals(Roadway_Constants.ROADWAY_CAR)) {
					
					/*
					 * RuleCosts_Model(Double weight_cost, Double distance_cost, Double worktime_cost, Double average_consumption_cost, 
					 * Double tolls_average_cost, Double fuel_average_cost, String currency_cost, Double rate_availability)
					 */
				
					ruleCosts = new RuleCosts_Model(0.05, 0.05, 0.05, 10.00, 2.21, 0.80, Currency_Constants.DOLLAR_CURRENCY_SYMBOL,0.0);
				}
				else if(way.equals(Roadway_Constants.ROADWAY_MOTORCYCLE)) {
					ruleCosts = new RuleCosts_Model(0.1, 0.5, 0.5, 10.00, 2.21, 0.80, Currency_Constants.DOLLAR_CURRENCY_SYMBOL,0.0);
				}
				else if(way.equals(Roadway_Constants.ROADWAY_TRUCK)) {
					ruleCosts = new RuleCosts_Model(0.1, 0.10, 0.10, 10.00, 2.21, 0.80, Currency_Constants.DOLLAR_CURRENCY_SYMBOL,0.0);
				}
				else if(way.equals(Roadway_Constants.ROADWAY_WALKING)) {
					ruleCosts = new RuleCosts_Model(0.1, 0.10, 0.10, 10.00, 2.21, 0.80, Currency_Constants.DOLLAR_CURRENCY_SYMBOL,0.0);
				}
				costsCountryWayL.put(way,ruleCosts);
				ruleCosts = new RuleCosts_Model();
			}
			costsL.put(country, costsCountryWayL);
		}
		return costsL;
	}
	
	public List<String> getCountry() {
		List<String> countryL = new ArrayList<String>();
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(file_country_sa));
			String line = reader.readLine();
			while(line != null) {
				System.out.println(line);
				countryL.add(line);
				line = reader.readLine();
			}
			reader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return countryL;
	}

	public List<String> getWay() {
		List<String> wayL = new ArrayList<String>();
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(file_way_sa));
			String line = reader.readLine();
			while(line != null) {
				System.out.println(line);
				wayL.add(line);
				line = reader.readLine();
			}
			reader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return wayL;
	}

	void inputJsonFileSouthAmerica(String jsonSouthAmerica) throws IOException, URISyntaxException {
		ObjectMapper mapper = new ObjectMapper();
		File file = new File(url_json);
		if (file.length() != 0) {
			String absolutePath = file.getAbsolutePath();
			RoadwayBRE_Model obj = mapper.readValue(new File(absolutePath), RoadwayBRE_Model.class);
			Assert.notNull(obj);
		}
		else {
			try (FileWriter fileWriter = new FileWriter(url_json, true)) {
			    fileWriter.write(jsonSouthAmerica);
			}
		}
	}
}
