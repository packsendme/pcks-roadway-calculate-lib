package com.packsendme.lib.roadwaycalculate.component.rulesinstance;

import com.packsendme.lib.common.constants.way.Roadway_Constants;
import com.packsendme.lib.common.response.dto.api.GoogleAPITrackingResponse_Dto;
import com.packsendme.lib.roadwaycalculate.component.Roadway_Abstract;
import com.packsendme.lib.simulation.http.SimulationDataForCalculateRequest_Dto;

public class Truck extends Roadway_Abstract{

	public Truck(GoogleAPITrackingResponse_Dto simulationTrackingAPI, SimulationDataForCalculateRequest_Dto simulationData) {
		super(simulationTrackingAPI, simulationData, Roadway_Constants.ROADWAY_TRUCK);
	}
		
}
