package com.packsendme.lib.roadwaycalculate.test.car.OneCountryless60km.copy;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.packsendme.lib.common.constants.generic.MetricUnitMeasurement_Constants;
import com.packsendme.lib.common.response.dto.api.GoogleAPITrackingResponse_Dto;
import com.packsendme.lib.roadwaycalculate.component.RoadwayInstanceCosts;
import com.packsendme.lib.simulation.http.SimulationDataForCalculateRequest_Dto;
import com.packsendme.lib.simulation.roadway.SimulationRoadwayResponse_Dto;
import com.packsendme.roadway.bre.rule.model.RoadwayBRE_Model;

 public class SimulationRoadway_SA_Test {

	private String jsonSouthAmerica = null;
	private String url_json = "src/test/resources/SimulationRoadway_SA_V1.json";

	RoadwayInstanceCosts roadwayInstance = new RoadwayInstanceCosts(); 
	
	@Test
	void simulationRoadway() throws URISyntaxException, IOException {
		SimulationRoadwayResponse_Dto simulationRoadwayResponse = new SimulationRoadwayResponse_Dto();
 
		
		// RoadwayBRE
		RoadwayBRE_SA_Test roadwayBRE_SA_Test = new RoadwayBRE_SA_Test();
		RoadwayBRE_Model roadwayBRE_cache = roadwayBRE_SA_Test.generateJsonSouthAmerica(); 

		// GoogleAPI
		GoogleAPI_Test googleAPI = new GoogleAPI_Test();
		GoogleAPITrackingResponse_Dto trackingAPI = googleAPI.generateGoogleAPI();
			
		SimulationDataForCalculateRequest_Dto simulationData = new SimulationDataForCalculateRequest_Dto(10.0, MetricUnitMeasurement_Constants.kilograma_UnitMeasurement, 
				"EXP", "pt", "BR", 5.34, 10.0, roadwayBRE_cache);
	
		simulationRoadwayResponse = roadwayInstance.instanceRulesCosts(trackingAPI, simulationData);
		
		ObjectMapper mapper = new ObjectMapper();
		jsonSouthAmerica = mapper.writeValueAsString(simulationRoadwayResponse);
		System.out.println(jsonSouthAmerica);
   		Assert.notNull(jsonSouthAmerica);
   		inputJsonFileSouthAmerica(jsonSouthAmerica);
   		
	}
	
	void inputJsonFileSouthAmerica(String jsonSouthAmerica) throws IOException, URISyntaxException {
		try (FileWriter fileWriter = new FileWriter(url_json, true)) {
		    fileWriter.write(jsonSouthAmerica);
		    fileWriter.write(System.getProperty("line.separator"));
		    fileWriter.close();
		}
	}
	
}
