package com.packsendme.lib.roadwaycalculate.rulesinstance;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.packsendme.lib.common.response.dto.api.GoogleAPITrackingResponse_Dto;
import com.packsendme.lib.roadway.simulation.request.SimulationRoadwayRequest_Dto;
import com.packsendme.lib.roadway.simulation.response.CostsRoadway;
import com.packsendme.lib.roadway.simulation.response.SimulationRoadwayResponse;
import com.packsendme.lib.roadwaycalculate.dto.CalculatorDto;
import com.packsendme.lib.roadwaycalculate.rules.RoadwayRulesCosts;
import com.packsendme.lib.roadwaycalculate.rules.VehicleAggregationCostsImpl;
import com.packsendme.lib.roadwaycalculate.total.TotalImpl;
import com.packsendme.roadbrewa.entity.Category;
import com.packsendme.roadbrewa.entity.Roadway;
import com.packsendme.roadbrewa.entity.Vehicle;

@Component
public class InstanceRuleCosts extends RoadwayRulesCosts{

	private Map<String, List<CalculatorDto>> weightCost_M = null;
	private Map<String, List<CalculatorDto>> distanceCost_M = null;
	private Map<String, List<CalculatorDto>> worktimeCost_M = null;
	private Map<String, List<CalculatorDto>> fuelConsumptionCost_M = null;
	private Map<String, List<CalculatorDto>> tollsCost_M = null;
	private Map<String, List<CalculatorDto>> dimensionCost_M = null;
	
	
	@Override
	public SimulationRoadwayResponse instanceRulesCosts(GoogleAPITrackingResponse_Dto googleAPI, Roadway roadwayBRE,
			SimulationRoadwayRequest_Dto requestData) {
		
		if(roadwayBRE.tariffPlan.weight_plan == true) {
			weightCost_M = this.getWeight_Calculator(requestData.weight_max, googleAPI, roadwayBRE);
		}
		if(roadwayBRE.tariffPlan.distance_plan == true) {
			distanceCost_M = this.getDistance_Calculator(googleAPI, roadwayBRE);
		}
		if(roadwayBRE.tariffPlan.worktime_plan == true) {
			worktimeCost_M = this.getWorktime_Calculator(googleAPI, roadwayBRE);
		}
		if(roadwayBRE.tariffPlan.fuelconsumption_plan == true) {
			fuelConsumptionCost_M = this.getFuelConsumption_Calculator(googleAPI, roadwayBRE);
		}
		if(roadwayBRE.tariffPlan.tolls_plan == true) {
			tollsCost_M = this.getTolls_Calculator(googleAPI);
		}
		if(roadwayBRE.tariffPlan.dimension_plan == true) {
			dimensionCost_M = this.getDimension_Calculator(requestData.height_max, requestData.width_max, requestData.length_max, 
					googleAPI, roadwayBRE);
		}
		if(roadwayBRE.tariffPlan.antt_plan == true) {
			
		}
		
		calcTotalCostsRoadway(roadwayBRE,requestData);
 		return null;
	}
	
	private SimulationRoadwayResponse calcTotalCostsRoadway(Roadway roadwayBRE_Obj, SimulationRoadwayRequest_Dto requestData) {
		DecimalFormat df2 = new DecimalFormat("#.##");
		SimulationRoadwayResponse simulationRoadwayResponse_Dto = null;
		VehicleAggregationCostsImpl vehicleAggregationObj = new VehicleAggregationCostsImpl();
		TotalImpl totalObj = new TotalImpl();
		List<CostsRoadway> costsVehicle_L = new ArrayList<CostsRoadway>();
		
		List<String> vehicleCheckRule = new ArrayList<String>();
		
		// Check vehicle based on rules
		for (Category catObj : roadwayBRE_Obj.categories) {
			for (Vehicle vehObj : catObj.vehicles) {
				if((vehObj.weight_max >= requestData.weight_max) && (vehObj.height_dimension_max >= requestData.weight_max) 
					&&(vehObj.width_dimension_max >= requestData.width_max) &&(vehObj.length_dimension_max >= requestData.length_max)) {
					vehicleCheckRule.add(vehObj.category_vehicle);
				}
			}
		}
		
		for (String vehicle : vehicleCheckRule) {
			// Core-Costs 
			double vlr_weight = vehicleAggregationObj.weightVehicleCosts(vehicle, weightCost_M);
			double vlr_dimension = vehicleAggregationObj.dimensionVehicleCosts(vehicle, dimensionCost_M);
			double vlr_distance = vehicleAggregationObj.distanceVehicleCosts(vehicle, distanceCost_M);
			double vlr_worktime = vehicleAggregationObj.worktimeVehicleCosts(vehicle, worktimeCost_M);
			double vlr_tolls = vehicleAggregationObj.tollsVehicleCosts(tollsCost_M);
			double vlr_fuelconsumption = vehicleAggregationObj.fuelConsumptionVehicleCosts(vehicle, fuelConsumptionCost_M);

			// Total-Costs
			double totalCostsCore = vlr_weight + vlr_dimension + vlr_distance + vlr_worktime + vlr_tolls + vlr_fuelconsumption;
			double vlr_fragile = totalObj.getFragileVlr_Total(totalCostsCore, roadwayBRE_Obj);	
			double vlr_persishable = totalObj.getPersishableVlr_Total(totalCostsCore, roadwayBRE_Obj);	
			double vlr_reshipping = totalObj.getReshippingVlr_Total(totalCostsCore, roadwayBRE_Obj);	
			
			double vlr_operationOwner = totalObj.getOperationOwner_Total(totalCostsCore, roadwayBRE_Obj);
			double vlr_employeer = totalObj.getEmployeeVlr_Total(totalCostsCore, roadwayBRE_Obj);
			double cost_total_US = totalObj.getCosts_Total(totalCostsCore, vlr_fragile, vlr_persishable, vlr_reshipping, vlr_operationOwner, vlr_employeer);
				
			// Exchange Currency-Rate
			String vlr_operationOwnerS = totalObj.getExchange_Total(requestData, vlr_operationOwner);
			String vlr_employeerS = totalObj.getExchange_Total(requestData, vlr_employeer);
			String cost_total_EX = totalObj.getExchange_Total(requestData, cost_total_US);
			
			
			CostsRoadway costsVehicleObj = new CostsRoadway(vehicle, df2.format(vlr_weight), df2.format(vlr_dimension), df2.format(vlr_distance), df2.format(vlr_worktime),
					df2.format(vlr_tolls), df2.format(vlr_fuelconsumption), df2.format(vlr_fragile), df2.format(vlr_persishable), df2.format(vlr_reshipping),
					vlr_operationOwnerS, vlr_employeerS, requestData.currency_exchange, df2.format(cost_total_US), cost_total_EX);
			costsVehicle_L.add(costsVehicleObj);
		}
		simulationRoadwayResponse_Dto = new SimulationRoadwayResponse(requestData, costsVehicle_L, new Date());
		return simulationRoadwayResponse_Dto;
	}

 

}
