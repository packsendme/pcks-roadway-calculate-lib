package com.packsendme.lib.roadwaycalculate.component.rulesinstance;

import com.packsendme.lib.common.response.dto.api.GoogleAPITrackingResponse_Dto;
import com.packsendme.lib.roadwaycalculate.component.Roadway_Abstract;
import com.packsendme.lib.simulation.request.dto.SimulationDataForCalculateRequest_Dto;

public class Car extends Roadway_Abstract{

	public Car(GoogleAPITrackingResponse_Dto simulationTrackingAPI, SimulationDataForCalculateRequest_Dto simulationData, String way) {
		super(simulationTrackingAPI, simulationData, way);
	}
		
}
