package com.packsendme.lib.roadwaycalculate.rulesinstance;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.packsendme.roadway.commons.dto.SimulationRoadwayDto;
import com.packsendme.roadway.commons.dto.CostsRoadwayDto;
import com.packsendme.roadway.commons.response.SimulationRoadwayResponse;
import com.packsendme.lib.roadwaycalculate.dto.CalculatorDto;
import com.packsendme.lib.roadwaycalculate.rules.RoadwayRulesCosts;
import com.packsendme.lib.roadwaycalculate.rules.VehicleAggregationCostsImpl;
import com.packsendme.lib.roadwaycalculate.total.TotalImpl;
import com.packsendme.lib.roadwaycalculate.utility.CalculateUtility;
import com.packsendme.roadway.commons.entity.Category;
import com.packsendme.roadway.commons.entity.Roadway;
import com.packsendme.roadway.commons.entity.Vehicle;

@Component
public class InstanceRuleCosts extends RoadwayRulesCosts{

	private Map<String, List<CalculatorDto>> weightCost_M = null;
	private Map<String, List<CalculatorDto>> distanceCost_M = null;
	private Map<String, List<CalculatorDto>> worktimeCost_M = null;
	private Map<String, List<CalculatorDto>> fuelConsumptionCost_M = null;
	private Map<String, List<CalculatorDto>> tollsCost_M = null;
	private Map<String, List<CalculatorDto>> dimensionCost_M = null;
	
	CalculateUtility weightConvertObj = new CalculateUtility();
	
	@Override
	public SimulationRoadwayResponse instanceRulesCosts(SimulationRoadwayDto requestData) {
		
		if(requestData.roadwayRule.tariffPlan.weight_plan == true) {
			if(requestData.weight_max == 0.0) {
				requestData.weight_max = requestData.roadwayRule.transport.weight_max;
				weightCost_M = this.getWeight_Calculator(requestData.weight_max, requestData.roadwayRule.transport.unity_weight, requestData.googleTracking, requestData.roadwayRule);
			}
			else {
				weightCost_M = this.getWeight_Calculator(requestData.weight_max, requestData.unity_weight, requestData.googleTracking, requestData.roadwayRule);
			}
		}
		
		if(requestData.roadwayRule.tariffPlan.distance_plan == true) {
			distanceCost_M = this.getDistance_Calculator(requestData.googleTracking, requestData.roadwayRule);
		}
		
		if(requestData.roadwayRule.tariffPlan.worktime_plan == true) {
			worktimeCost_M = this.getWorktime_Calculator(requestData.googleTracking, requestData.roadwayRule);
		}
		
		if(requestData.roadwayRule.tariffPlan.fuelconsumption_plan == true) {
			fuelConsumptionCost_M = this.getFuelConsumption_Calculator(requestData.googleTracking, requestData.roadwayRule);
		}
		
		if(requestData.roadwayRule.tariffPlan.tolls_plan == true) {
			tollsCost_M = this.getTolls_Calculator(requestData.googleTracking);
		}
		
		if(requestData.roadwayRule.tariffPlan.dimension_plan == true) {
			if((requestData.height_max == 0.0) && (requestData.length_max == 0.0) && (requestData.width_max == 0.0)) {
				requestData.height_max = requestData.roadwayRule.transport.heightDimension_max;
				requestData.width_max = requestData.roadwayRule.transport.widthDimension_max;
				requestData.length_max = requestData.roadwayRule.transport.lengthDimension_max;
			}
			dimensionCost_M = this.getDimension_Calculator(requestData.height_max, requestData.width_max, requestData.length_max, 
					requestData.googleTracking, requestData.roadwayRule);
		}
		
		if(requestData.roadwayRule.tariffPlan.antt_plan == true) {
			
		}
		
		SimulationRoadwayResponse simulationResulObj =  calcTotalCostsRoadway(requestData.roadwayRule,requestData);
 		return simulationResulObj;
	}
	
	private SimulationRoadwayResponse calcTotalCostsRoadway(Roadway roadwayBRE_Obj, SimulationRoadwayDto requestData) {
		DecimalFormat df2 = new DecimalFormat("#.##");
		SimulationRoadwayResponse simulationRoadwayResponse_Dto = null;
		VehicleAggregationCostsImpl vehicleAggregationObj = new VehicleAggregationCostsImpl();
		TotalImpl totalObj = new TotalImpl();
		List<CostsRoadwayDto> costsVehicle_L = new ArrayList<CostsRoadwayDto>();
		boolean statusVehiculoUnity = false;
		List<String> vehicleCheckRule = new ArrayList<String>();
		
		// Check vehicle based on rules
		for (Category catObj : roadwayBRE_Obj.categories) {
			for (Vehicle vehObj : catObj.vehicles) {

				System.out.println("");
				System.out.println("---------------------------------------");
				System.out.println("--- WEIGHT_MAX  BRE ------ "+ vehObj.weight_max);
				System.out.println("--- WEIGHT_MAX  REQ ------ "+ requestData.weight_max);
				System.out.println("");
				System.out.println("--- HEIGHT_MAX  BRE ------ "+ vehObj.height_dimension_max);
				System.out.println("--- HEIGHT_MAX  REQ ------ "+ requestData.height_max);
				System.out.println("");
				System.out.println("--- WIDTH_MAX  BRE ------ "+ vehObj.width_dimension_max);
				System.out.println("--- WIDTH_MAX  REQ ------ "+ requestData.width_max);
				System.out.println("");
				System.out.println("--- LENGTH_MAX  BRE ------ "+ vehObj.length_dimension_max);
				System.out.println("--- LENGTH_MAX  REQ ------ "+ requestData.length_max);
				System.out.println("---------------------------------------");
				System.out.println("");
				
				System.out.println("---------------------------------------");
				System.out.println("UNITY WEIGHT");
				
				// UnityMeas Vehicule
				int weightUnity_Vehicle = 0;
				for(Entry<Integer, String> entry : vehObj.unity_weight.entrySet()) {
					weightUnity_Vehicle = entry.getKey();
					System.out.println(" UNIDADE MEDIDA VEICULO "+ weightUnity_Vehicle);
				}

				// UnityMeas Carga
				int weightUnity_Load = 0;
				if(requestData.roadwayRule.transport.restriction == false) {
					for(Entry<Integer, String> entry : requestData.unity_weight.entrySet()) {
						weightUnity_Load = entry.getKey();
						System.out.println(" UNIDADE MEDIDA CARGA"+ weightUnity_Load);
					}
				}
				// UnityMeas Transport
				else if(requestData.roadwayRule.transport.restriction == true) {
					for(Entry<Integer, String> entry : requestData.roadwayRule.transport.unity_weight.entrySet()) {
						weightUnity_Load = entry.getKey();
						System.out.println(" UNIDADE MEDIDA CARGA"+ weightUnity_Load);
					}
				}
					
				System.out.println("---------------------------------------");
				System.out.println("");
				

				// Validation type vehicle Unity Measured. (GR / KG / T)  
				if((weightUnity_Vehicle == weightUnity_Load) && (vehObj.weight_max >= requestData.weight_max)) {
					statusVehiculoUnity = true;
				}
				else if(weightUnity_Vehicle > weightUnity_Load) {
					statusVehiculoUnity = true;
				}
						
				if(statusVehiculoUnity == true) {
					if((vehObj.height_dimension_max >= requestData.height_max) && (vehObj.width_dimension_max >= requestData.width_max) 
							&&(vehObj.length_dimension_max >= requestData.length_max)) {
						
						System.out.println("");
						System.out.println("---------------------------------------");
						System.out.println(" ENTROU 1-IF "+ vehObj.restriction );
						System.out.println(" ENTROU 2-IF "+ vehObj.distance_max);
						System.out.println(" ENTROU 3-IF "+ requestData.googleTracking.distance_total);
						System.out.println("---------------------------------------");

						
						if ((vehObj.restriction == true) && (requestData.googleTracking.distance_total < vehObj.distance_max)) {
							vehicleCheckRule.add(vehObj.category_vehicle);
							System.out.println("");
							System.out.println("---------------------------------------");
							System.out.println(" ENTROU "+ vehObj.category_vehicle);
							System.out.println("---------------------------------------");
							System.out.println("");
						}
						else if (vehObj.restriction == false) {
							vehicleCheckRule.add(vehObj.category_vehicle);
							System.out.println("");
							System.out.println("---------------------------------------");
							System.out.println(" ENTROU "+ vehObj.category_vehicle);
							System.out.println("---------------------------------------");
							System.out.println("");
						}
					
					}
				}
			}
		}
						
		System.out.println("---------------------------------------");
		System.out.println(" VEHICLE "+ vehicleCheckRule.size());
		System.out.println("---------------------------------------");

		for (String vehicle : vehicleCheckRule) {

			System.out.println("---------------------------------------");
			System.out.println(" VEHICLE CALC");
			System.out.println(" NAME"+ vehicle);
			System.out.println("---------------------------------------");

			// BaseCosts 
			double vlr_weight = vehicleAggregationObj.weightVehicleCosts(vehicle, weightCost_M);
			double vlr_dimension = vehicleAggregationObj.dimensionVehicleCosts(vehicle, dimensionCost_M);
			double vlr_distance = vehicleAggregationObj.distanceVehicleCosts(vehicle, distanceCost_M);
			double vlr_worktime = vehicleAggregationObj.worktimeVehicleCosts(vehicle, worktimeCost_M);
			double vlr_tolls = vehicleAggregationObj.tollsVehicleCosts(tollsCost_M);
			double vlr_fuelconsumption = vehicleAggregationObj.fuelConsumptionVehicleCosts(vehicle, fuelConsumptionCost_M);
			double cost_total_base = vlr_weight + vlr_dimension + vlr_distance + vlr_worktime + vlr_tolls + vlr_fuelconsumption; 

			// RiskCosts
			double vlr_fragile = totalObj.getFragileVlr_Total(cost_total_base, roadwayBRE_Obj);	
			double vlr_persishable = totalObj.getPersishableVlr_Total(cost_total_base, roadwayBRE_Obj);	
			double cost_total_risk = vlr_fragile + vlr_persishable;
					
			// TotalGeneral-Costs
			double vlr_reshipping = totalObj.getReshippingVlr_Total(cost_total_base, roadwayBRE_Obj);	
			double vlr_operationOwner = totalObj.getOperationOwner_Total(cost_total_base, roadwayBRE_Obj);
			double vlr_employeer = totalObj.getEmployeeVlr_Total(cost_total_base, roadwayBRE_Obj);
			double cost_total_US = totalObj.getCosts_Total(cost_total_base, cost_total_risk, vlr_operationOwner, vlr_employeer);
			
			
			// Exchange BaseCosts 
			String vlr_weightS = totalObj.getExchange_Total(requestData,vlr_weight);
			String vlr_dimensionS = totalObj.getExchange_Total(requestData,vlr_dimension);
			String vlr_distanceS = totalObj.getExchange_Total(requestData,vlr_distance);
			String vlr_worktimeS = totalObj.getExchange_Total(requestData,vlr_worktime);
			String vlr_tollsS = totalObj.getExchange_Total(requestData,vlr_tolls);
			String vlr_fuelconsumptionS = totalObj.getExchange_Total(requestData,vlr_fuelconsumption);
			String cost_total_baseS = totalObj.getExchange_Total(requestData, cost_total_base); 
			
			// Exchange RiskCosts 
			String vlr_fragileS = totalObj.getExchange_Total(requestData,vlr_fragile);	
			String vlr_persishableS = totalObj.getExchange_Total(requestData,vlr_persishable);
			String cost_total_riskS = totalObj.getExchange_Total(requestData,cost_total_risk);;

			
			// Total
			String vlr_reshippingS = totalObj.getExchange_Total(requestData,vlr_reshipping);	
			String vlr_operationOwnerS =  totalObj.getExchange_Total(requestData,vlr_operationOwner);
			String vlr_employeerS = totalObj.getExchange_Total(requestData, vlr_employeer);
			String cost_total_EX = totalObj.getExchange_Total(requestData, cost_total_US);

			
			System.out.println(" ");
			System.out.println("---------------------------------------");
			System.out.println("--- calcTotalCostsRoadway_ START ------ ");
			System.out.println("--- calcTotalCostsRoadway_ VEICULO ------ "+ vehicle);
			System.out.println(" - calcTotalCostsRoadway: totalCostsCore ---- "+ cost_total_base);
			System.out.println(" - calcTotalCostsRoadway: vlr_fragile ---- "+ vlr_fragile);
			System.out.println(" - calcTotalCostsRoadway: vlr_persishable ---- "+ vlr_persishable);
			System.out.println(" - calcTotalCostsRoadway: vlr_reshipping ---- "+ vlr_reshipping);
			System.out.println(" - calcTotalCostsRoadway: vlr_operationOwner ---- "+ vlr_operationOwner);
			System.out.println(" ");
			System.out.println(" - calcTotalCostsRoadway: cost_total_US ---- "+ cost_total_US);
			System.out.println(" ");
			System.out.println(" - calcTotalCostsRoadway: vlr_operationOwnerS ---- "+ vlr_operationOwnerS);
			System.out.println(" - calcTotalCostsRoadway: vlr_employeerS ---- "+ vlr_employeerS);
			System.out.println(" ");
			System.out.println(" ");
			System.out.println(" - calcTotalCostsRoadway: cost_total_EX ---- "+ cost_total_EX);
			System.out.println(" ");
			System.out.println(" ");
			System.out.println("---------------------------------------");

			
			CostsRoadwayDto costsVehicleObj = new CostsRoadwayDto(vehicle, vlr_weightS, vlr_dimensionS, vlr_distanceS, vlr_worktimeS,
					vlr_tollsS, vlr_fuelconsumptionS, cost_total_baseS, vlr_fragileS, vlr_persishableS, cost_total_riskS, vlr_reshippingS,
					vlr_operationOwnerS, vlr_employeerS, requestData.exchangeObj.toCurrent, df2.format(cost_total_US), cost_total_EX);
			costsVehicle_L.add(costsVehicleObj);
		}
	
		// Return Tolls-Total
		int tollsTotal_CountryAll = totalObj.getTolls_Amount(requestData.googleTracking);

		
		System.out.println(" ");
		System.out.println("---------------------------------------");
		System.out.println(" RESPONSE HTTP ---- ");
		System.out.println(" - TOLLS: TOTAL ---- "+ tollsTotal_CountryAll);
		System.out.println(" - DURANTION: TOTAL ---- "+ requestData.googleTracking.duration);
		System.out.println(" - DISTANCE: TOTAL ---- "+ requestData.googleTracking.distance_total);
		System.out.println(" - COSTSVEHICLE ---- "+ costsVehicle_L.size());
		System.out.println("---------------------------------------");
		System.out.println(" ");

		simulationRoadwayResponse_Dto = new SimulationRoadwayResponse(requestData.googleTracking.distance_total,requestData.googleTracking.duration,tollsTotal_CountryAll,null, costsVehicle_L, new Date());
		return simulationRoadwayResponse_Dto;
	}
}
