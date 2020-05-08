package com.packsendme.lib.roadwaycalculate.component;

import com.packsendme.lib.common.response.dto.api.GoogleAPITrackingResponse_Dto;
import com.packsendme.lib.simulation.http.SimulationDataForCalculateRequest_Dto;
import com.packsendme.lib.simulation.roadway.RoadwayCalculatorResponse_Dto;

public interface IRoadway_Costs {
	
	// Calculator Controller
	public RoadwayCalculatorResponse_Dto analyzeRule_data(GoogleAPITrackingResponse_Dto trackingAPI, SimulationDataForCalculateRequest_Dto simulationData, String way);
	
	public double getTotalGeneralDelivery(double total_parcial, double vlr_packsend_total);
	public String getTotalRateExchange(double total_vlr, SimulationDataForCalculateRequest_Dto simulation);
	
	// Calculator FIX_COSTS : Tolls And Fuel
	public double getTollsCosts(double toll_price, int toll_amount);
	public double getFuelCosts(double fuel_price, double country_distance, double average_consumption_cost);

	// Calculator PCK_COSTS : Packsend
	public double getPackSendMeCosts(double total_parcial, SimulationDataForCalculateRequest_Dto simulationData);

	// Calculator EMPLOYER : Distance/Weight And Reshipping
	
	public double getEmployerCosts(double distance_total, double distance_cost, double weight_simulation, double weight_cost, int duration, double duration_cost, String weight_measured_unit); 
	
	public double getReshippingCosts(double total_parcial, double percentage_Reshipping);
 

}
