package com.packsendme.lib.roadwaycalculate.component;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.packsendme.lib.common.response.dto.api.GoogleAPITrackingResponse_Dto;
import com.packsendme.lib.roadway.simulation.response.RoadwayCalculatorResponse_Dto;
import com.packsendme.lib.roadway.simulation.response.SimulationRoadwayResponse_Dto;
import com.packsendme.lib.roadwaycalculate.component.rulesinstance.Bicycle;
import com.packsendme.lib.roadwaycalculate.component.rulesinstance.Car;
import com.packsendme.lib.roadwaycalculate.component.rulesinstance.Motorcycle;
import com.packsendme.lib.roadwaycalculate.component.rulesinstance.Truck;
import com.packsendme.lib.roadwaycalculate.component.rulesinstance.Walking;

@Component
public class RoadwayInstanceCosts implements IRoadway_Instance{
	
	@Override
	public SimulationRoadwayResponse_Dto instanceRulesCosts(GoogleAPITrackingResponse_Dto simulationTrackingAPI, SimulationDataForCalculateRequest_Dto simulationData) {

		SimulationRoadwayResponse_Dto simulationRoadwayResponse = new SimulationRoadwayResponse_Dto();
		Map<String,RoadwayCalculatorResponse_Dto> roadwayMap = new HashMap<String, RoadwayCalculatorResponse_Dto>();
		
		//Roadway
		RoadwayCalculatorResponse_Dto carResponse = carInstance(simulationTrackingAPI,simulationData);
		roadwayMap.put(Roadway_Constants.ROADWAY_CAR, carResponse);

		//Bicycle
		RoadwayCalculatorResponse_Dto bicycleResponse = bicycleInstance(simulationTrackingAPI,simulationData);
		roadwayMap.put(Roadway_Constants.ROADWAY_BICYCLE, bicycleResponse);
		
		//Motorcycle
		RoadwayCalculatorResponse_Dto motorcycleResponse = motorcycleInstance(simulationTrackingAPI,simulationData);
		roadwayMap.put(Roadway_Constants.ROADWAY_MOTORCYCLE, motorcycleResponse);	
		
		//Truck
		RoadwayCalculatorResponse_Dto truckResponse = truckInstance(simulationTrackingAPI,simulationData);
		roadwayMap.put(Roadway_Constants.ROADWAY_TRUCK, truckResponse);
		
		//Walking
		RoadwayCalculatorResponse_Dto walkingResponse = walkingInstance(simulationTrackingAPI,simulationData);
		roadwayMap.put(Roadway_Constants.ROADWAY_WALKING, walkingResponse);
		
		simulationRoadwayResponse.roadway = roadwayMap;
		return simulationRoadwayResponse;
	}

	@Override
	public RoadwayCalculatorResponse_Dto bicycleInstance(GoogleAPITrackingResponse_Dto simulationTrackingAPI,
			SimulationDataForCalculateRequest_Dto simulationData) {
		Bicycle bicycle = new Bicycle(simulationTrackingAPI, simulationData); 
		RoadwayCalculatorResponse_Dto roadwayCalculatorResponse = bicycle.roadwayCalculatorResponse;
		return roadwayCalculatorResponse;
	}

	@Override
	public RoadwayCalculatorResponse_Dto carInstance(GoogleAPITrackingResponse_Dto simulationTrackingAPI,
			SimulationDataForCalculateRequest_Dto simulationData) {
		Car car = new Car(simulationTrackingAPI, simulationData); 
		RoadwayCalculatorResponse_Dto roadwayCalculatorResponse = car.roadwayCalculatorResponse;
		return roadwayCalculatorResponse;
	}

	@Override
	public RoadwayCalculatorResponse_Dto motorcycleInstance(GoogleAPITrackingResponse_Dto simulationTrackingAPI,
			SimulationDataForCalculateRequest_Dto simulationData) {
		Motorcycle motorcycle = new Motorcycle(simulationTrackingAPI, simulationData); 
		RoadwayCalculatorResponse_Dto roadwayCalculatorResponse = motorcycle.roadwayCalculatorResponse;
		return roadwayCalculatorResponse;
	}

	@Override
	public RoadwayCalculatorResponse_Dto truckInstance(GoogleAPITrackingResponse_Dto simulationTrackingAPI,
			SimulationDataForCalculateRequest_Dto simulationData) {
		Truck truck = new Truck(simulationTrackingAPI, simulationData); 
		RoadwayCalculatorResponse_Dto roadwayCalculatorResponse = truck.roadwayCalculatorResponse;
		return roadwayCalculatorResponse;
	}

	@Override
	public RoadwayCalculatorResponse_Dto walkingInstance(GoogleAPITrackingResponse_Dto simulationTrackingAPI,
			SimulationDataForCalculateRequest_Dto simulationData) {
		Walking walking = new Walking(simulationTrackingAPI, simulationData); 
		RoadwayCalculatorResponse_Dto roadwayCalculatorResponse = walking.roadwayCalculatorResponse;
		return roadwayCalculatorResponse;
	}
	
	
	

}
