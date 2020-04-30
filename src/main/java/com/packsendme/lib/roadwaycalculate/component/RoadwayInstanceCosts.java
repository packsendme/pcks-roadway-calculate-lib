package com.packsendme.lib.roadwaycalculate.component;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.packsendme.lib.common.constants.way.Roadway_Constants;
import com.packsendme.lib.common.response.dto.api.GoogleAPITrackingResponse_Dto;
import com.packsendme.lib.roadwaycalculate.component.rulesinstance.Car;
import com.packsendme.lib.simulation.request.dto.SimulationDataForCalculateRequest_Dto;
import com.packsendme.lib.simulation.way.response.dto.SimulationRoadwayResponse_Dto;
import com.packsendme.lib.simulation.way.roadway.response.dto.RoadwayCalculatorResponse_Dto;

@Component
public class RoadwayInstanceCosts{
	
	
	
	public SimulationRoadwayResponse_Dto instanceRulesCosts(GoogleAPITrackingResponse_Dto simulationTrackingAPI, SimulationDataForCalculateRequest_Dto simulationData) {

		SimulationRoadwayResponse_Dto simulationRoadwayResponse = new SimulationRoadwayResponse_Dto();
		Map<String,RoadwayCalculatorResponse_Dto> roadwayResponseMap = new HashMap<String, RoadwayCalculatorResponse_Dto>();
		
		Car car = new Car(simulationTrackingAPI, simulationData, Roadway_Constants.ROADWAY_CAR); 
		RoadwayCalculatorResponse_Dto roadwayCalculatorResponse = car.roadwayCalculatorResponse;
		
		roadwayResponseMap.put(Roadway_Constants.ROADWAY_CAR, roadwayCalculatorResponse);

		simulationRoadwayResponse.roadway = roadwayResponseMap;
		return simulationRoadwayResponse;
	}
	
	
	

}
