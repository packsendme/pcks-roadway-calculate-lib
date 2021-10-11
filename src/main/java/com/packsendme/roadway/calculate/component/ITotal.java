package com.packsendme.roadway.calculate.component;

import com.packsendme.cross.common.response.dto.api.GoogleAPITrackingResponse_Dto;
import com.packsendme.roadway.commons.dto.SimulationRoadwayDto;
import com.packsendme.roadway.commons.entity.Roadway;

public interface ITotal {
	public double getFragileVlr_Total(Double totalCostsCore, Roadway roadwayBRE_Obj);
	public double getPersishableVlr_Total(Double totalCostsCore, Roadway roadwayBRE_Obj);
	public double getReshippingVlr_Total(double totalCostsCore, Roadway roadwayBRE_Obj);
	public double getEmployeeVlr_Total(double totalCostsCore, Roadway roadwayBRE_Obj);
	public double getOperationOwner_Total(double totalCostsCore, Roadway roadwayBRE_Obj);
	public double getCosts_Total(double totalCostsBase, double totalCostsRisk, double vlr_operationOwner, double vlr_employeer);
	public String getExchange_Total(SimulationRoadwayDto simulationDto, double vlr_total);

	// Return Total Tolls
	public int getTolls_Amount(GoogleAPITrackingResponse_Dto googleAPI_Obj);

}
