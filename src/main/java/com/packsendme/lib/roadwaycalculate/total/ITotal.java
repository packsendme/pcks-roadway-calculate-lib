package com.packsendme.lib.roadwaycalculate.total;

import com.packsendme.lib.roadway.simulation.request.SimulationRoadwayRequest_Dto;
import com.packsendme.roadbrewa.entity.Roadway;

public interface ITotal {
	public double getFragileVlr_Total(Double totalCostsCore, Roadway roadwayBRE_Obj);
	public double getPersishableVlr_Total(Double totalCostsCore, Roadway roadwayBRE_Obj);
	public double getReshippingVlr_Total(double totalCostsCore, Roadway roadwayBRE_Obj);
	public double getEmployeeVlr_Total(double totalCostsCore, Roadway roadwayBRE_Obj);
	public double getOperationOwner_Total(double totalCostsCore, Roadway roadwayBRE_Obj);
	public double getCosts_Total(double totalCostsCore, double vlr_fragile, double vlr_persishable, double vlr_reshipping, double vlr_operationOwner, double vlr_employeer);
	public String getExchange_Total(SimulationRoadwayRequest_Dto simulationDto, double vlr_total);

}
