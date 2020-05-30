package com.packsendme.lib.roadwaycalculate.component;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

import com.packsendme.lib.common.constants.calculador.Calculate_Constants;
import com.packsendme.lib.common.constants.generic.MetricUnitMeasurement_Constants;
import com.packsendme.lib.common.response.dto.api.GoogleAPITrackingResponse_Dto;
import com.packsendme.lib.common.response.dto.api.RoadwayTrackingResponse_Dto;
import com.packsendme.lib.simulation.http.SimulationDataForCalculateRequest_Dto;
import com.packsendme.lib.simulation.roadway.RoadwayCalculatorResponse_Dto;
import com.packsendme.lib.simulation.roadway.RoadwayDataCalculatorResponse_Dto;
import com.packsendme.lib.utility.FormatValueMoney;
import com.packsendme.lib.utility.WeightConvert_Utility;
import com.packsendme.roadway.bre.rule.costs.model.RuleCosts_Model;
import com.packsendme.roadway.bre.rule.instance.model.RuleInstance_Model;

@Component
public abstract class Roadway_Abstract implements IRoadway_Costs {
		
	
	Map<String,RoadwayDataCalculatorResponse_Dto> roadwayDataCalculator = new HashMap<String,RoadwayDataCalculatorResponse_Dto>();
	WeightConvert_Utility weightConvert = new WeightConvert_Utility();
	FormatValueMoney valueFormatObj = new FormatValueMoney();
	
	
	private double vlr_packsend_total = 0.0;
	private double vlr_delivery_total = 0.0;
	
	public RoadwayCalculatorResponse_Dto roadwayCalculatorResponse;
	FormatValueMoney valueFormat = new FormatValueMoney();

	public Roadway_Abstract(GoogleAPITrackingResponse_Dto trackingAPI, SimulationDataForCalculateRequest_Dto simulationData, String way) {
		super();
		roadwayCalculatorResponse = analyzeRule_data(trackingAPI, simulationData, way);
	}

	@Override
	public RoadwayCalculatorResponse_Dto analyzeRule_data(GoogleAPITrackingResponse_Dto trackingAPI, SimulationDataForCalculateRequest_Dto simulationData, String way) {
		System.out.println(" ==============================|   analyzeRule_data  |==================================================");

		double total_parcial = 0.0, distance_cost = 0.0, weight_cost = 0.0, duration_cost = 0.0;
		int numberCountry = 0;
		
		RoadwayDataCalculatorResponse_Dto roadwayDataCalculatorObj = new RoadwayDataCalculatorResponse_Dto();
		RoadwayCalculatorResponse_Dto roadwayResponse = null;
		
		try {
		
			RuleInstance_Model ruleInstance = simulationData.roadwayBRE_cache.ruleInstance.get(way);
	
			System.out.println(" ------------------------------- ");
			System.out.println(" ------------- analyzeRule_data - Roadway_Abstract------------------ "+ way);
			System.out.println(" ------------- analyzeRule_data - weight_product ------------------ "+ simulationData.weight_product +" "+ ruleInstance.weight_max);
			System.out.println(" ------------- analyzeRule_data - distance_total------------------ "+ trackingAPI.distance_total +" "+ ruleInstance.distance_max);
	
			numberCountry = trackingAPI.trackingRoadway.size();
			System.out.println(" ------------- analyzeRule_data - Number Country ------------------ "+ numberCountry);
			System.out.println(" ------------------------------- ");
	
			
			if((simulationData.weight_productGr <= ruleInstance.weight_max) && (trackingAPI.distance_total <= ruleInstance.distance_max)){
	
				for(Entry<String, RoadwayTrackingResponse_Dto> entry : trackingAPI.trackingRoadway.entrySet()) {
	
					String country = entry.getKey();
					RoadwayTrackingResponse_Dto roadwayTrackingAPI_Dto  = entry.getValue(); 
					Map<String,RuleCosts_Model> ruleCosts = simulationData.roadwayBRE_cache.ruleCosts.get(country);
					RuleCosts_Model ruleCostsCache_Model = ruleCosts.get(way);
	
					System.out.println(" ");
					System.out.println(" ------------------------------- ");
	
					System.out.println(" ====== COUNTRIES START ========= "+ country);
	
					//Calculator Tolls 
					double tolls_vlr = getTollsCosts(roadwayTrackingAPI_Dto.toll_price, roadwayTrackingAPI_Dto.toll_amount);
					
					//Calculator Fuel 
					double fuel_vlr = getFuelCosts(roadwayTrackingAPI_Dto.fuel_price,roadwayTrackingAPI_Dto.country_distanceM,
							ruleCostsCache_Model.average_consumption_cost);
					vlr_delivery_total =  vlr_delivery_total + tolls_vlr + fuel_vlr;

					System.out.println(" (1) ");
					distance_cost = ruleCostsCache_Model.distance_cost;
					System.out.println(" (2) ");
					weight_cost = weight_cost + ruleCostsCache_Model.weight_cost;
					System.out.println(" (3) ");
					duration_cost = duration_cost + ruleCostsCache_Model.worktime_cost;
					System.out.println(" (4) ");

					roadwayDataCalculatorObj.fuel_total = getTotalRateExchange(fuel_vlr, simulationData);
					System.out.println(" (5) ");
					roadwayDataCalculatorObj.tolls_total = getTotalRateExchange(tolls_vlr, simulationData);
					System.out.println(" (6) ");
					roadwayDataCalculatorObj.distance = roadwayTrackingAPI_Dto.country_distanceF;
					System.out.println(" (7) ");
					roadwayDataCalculator.put(country, roadwayDataCalculatorObj);
					System.out.println(" (8) ");
					roadwayDataCalculatorObj = new RoadwayDataCalculatorResponse_Dto();
					tolls_vlr = 0.00;
					fuel_vlr = 0.00;
				}
				
				//Calculator Employer (Distance / TimeWork / weight)
				double vlr_employer_total = getEmployerCosts(trackingAPI.distance_total, distance_cost, simulationData.weight_product, weight_cost, trackingAPI.duration, duration_cost, simulationData.weight_measured_unit);
				total_parcial = vlr_employer_total + vlr_delivery_total;
				
				vlr_packsend_total = getPackSendMeCosts(total_parcial, simulationData);
				double vlrReshipping = getReshippingCosts(total_parcial, simulationData.roadwayBRE_cache.rate_reshipping);
				double vlrTotalDelivery = getTotalGeneralDelivery(total_parcial, vlr_packsend_total);
				
				System.out.println(" ");
				System.out.println(" ==============================|   T O T A L  -  G E R A L  |==================================================");
				System.out.println(" ");
				System.out.println(" ========================  analyzeRule_data :: total_parcial ================ "+ total_parcial);
				System.out.println(" ========================  analyzeRule_data :: EMPLOYER $ ================ "+ vlr_employer_total);
				System.out.println(" ========================  analyzeRule_data :: DELIVERY $ ================ "+ vlr_delivery_total);
				System.out.println(" ========================  analyzeRule_data :: PACKSEND $ ================ "+ vlr_packsend_total);
				System.out.println(" ========================  analyzeRule_data :: RESHIPPING $ ================ "+ vlrReshipping);
				System.out.println(" ========================  analyzeRule_data :: TOTAL $ ================ "+ vlrTotalDelivery);
				System.out.println(" ");
				System.out.println(" ");

				roadwayResponse = new RoadwayCalculatorResponse_Dto(
						getTotalRateExchange(vlr_employer_total, simulationData),
						getTotalRateExchange(vlr_packsend_total, simulationData),
						getTotalRateExchange(vlr_delivery_total, simulationData),
						getTotalRateExchange(vlrReshipping, simulationData),
						getTotalRateExchange(vlrTotalDelivery, simulationData),
						Calculate_Constants.STATUS_COSTS_SEND);
				/*
				roadwayResponse = new RoadwayCalculatorResponse_Dto(
						getTotalRateExchange(vlr_employer_total, simulationData),
						getTotalRateExchange(vlr_packsend_total, simulationData),
						getTotalRateExchange(vlr_delivery_total, simulationData),
						getTotalRateExchange(vlrReshipping, simulationData),
						getTotalRateExchange(vlrTotalDelivery, simulationData),
						Calculate_Constants.STATUS_COSTS_SEND, roadwayDataCalculator);*/
			}
			else {
				roadwayResponse = new RoadwayCalculatorResponse_Dto(
						Calculate_Constants.VALUE_DEFAUL_S, Calculate_Constants.VALUE_DEFAUL_S, Calculate_Constants.VALUE_DEFAUL_S, 
						Calculate_Constants.VALUE_DEFAUL_S, Calculate_Constants.VALUE_DEFAUL_S, Calculate_Constants.STATUS_COSTS_NSEND); 
/*				roadwayResponse = new RoadwayCalculatorResponse_Dto(
						Calculate_Constants.VALUE_DEFAUL_S, Calculate_Constants.VALUE_DEFAUL_S, Calculate_Constants.VALUE_DEFAUL_S, 
						Calculate_Constants.VALUE_DEFAUL_S, Calculate_Constants.VALUE_DEFAUL_S, Calculate_Constants.STATUS_COSTS_NSEND, null); */
			}
			return roadwayResponse;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		
	}

	//============================= TOTAL VALUE ================================================================ 

	@Override
	public double getTotalGeneralDelivery(double total_parcial, double vlr_packsend_total) {
		System.out.println(" ==============================|   getTotalGeneralDelivery  |==================================================");
		try {
			double totalGeneral_vlr = total_parcial + vlr_packsend_total;
			System.out.println(" ");
			System.out.println(" ================================================================================");
			System.out.println(" ========================  getTotalGeneralDelivery :: total_parcial ================ "+ total_parcial);
			System.out.println(" ========================  getTotalGeneralDelivery :: vlr_packsend_total ================ "+ vlr_packsend_total);
			System.out.println(" ========================  getTotalGeneralDelivery :: TOTAL-FORMAT ================ "+ totalGeneral_vlr);
			System.out.println(" ================================================================================");
			return totalGeneral_vlr;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	
	public String getTotalRateExchange(double total_vlr, SimulationDataForCalculateRequest_Dto simulation) {
		System.out.println(" ==============================|   getTotalRateExchange  |==================================================");

		try {
			double totalExchange_vlr = total_vlr * simulation.exchange_rate;
			System.out.println(" ");
			System.out.println(" ================================================================================");
			System.out.println(" ========================  getTotalRateExchange :: total_vlr ================ "+ total_vlr);
			System.out.println(" ========================  getTotalRateExchange :: TOTAL-FORMAT ================ "+ totalExchange_vlr);
			System.out.println(" ================================================================================");
			return valueFormatObj.formatDoubleStringInCurrency(totalExchange_vlr, simulation.locale_language, simulation.locale_country);
		}
		catch (Exception e) {
			e.printStackTrace();
			return "0.0";
		}
	}


	
	//============================= Costs Fix ( Tolls And Fuel ) ================================================================ 
	@Override
	public double getTollsCosts(double toll_price, int toll_amount) {
		System.out.println(" ==============================|   getTollsCosts  |==================================================");

		try {
			double tollsCosts_vlr = toll_price * toll_amount;
			System.out.println(" ");
			System.out.println(" =================================| T O L L S |===============================================");
			System.out.println(" ========================  getTolls :: toll_price ================ "+ toll_price);
			System.out.println(" ========================  getTolls :: toll_amount ================ "+ toll_amount);
			System.out.println(" ========================  getTolls :: TOTAL ================ "+ tollsCosts_vlr);
			System.out.println(" ================================================================================");
			System.out.println(" ");
			return tollsCosts_vlr;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	
	@Override
	public double getFuelCosts(double fuel_price, double country_distance, double average_consumption_cost) {
		System.out.println(" ==============================|   getFuelCosts  |==================================================");

		try {
			// Analisys if distance is more 1000 KM 
			double distanceByKM = country_distance / 1000;
			if(distanceByKM > 1000) {
				country_distance =  distanceByKM * 1000;
			}
			else {
				country_distance = distanceByKM;
			}
			
			double fuelCosts_vlr = (country_distance / average_consumption_cost) * fuel_price; 
			System.out.println(" ");
			System.out.println(" ===================================| F U E L |=============================================");
			System.out.println(" ========================  getFuelCosts :: fuel_average_cost ================ "+ fuel_price);
			System.out.println(" ========================  getFuelCosts :: country_distance ================ "+ country_distance);
			System.out.println(" ========================  getFuelCosts :: average_consumption_cost ================ "+ average_consumption_cost);
			System.out.println(" ========================  getFuelCosts :: TOTAL-FORMAT ================ "+ fuelCosts_vlr);
			System.out.println(" ================================================================================");
			System.out.println(" ");
			return fuelCosts_vlr;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

 
	//============================= Costs Provider ( PACKSEND ) ================================================================ 

	@Override
	public double getPackSendMeCosts(double total_parcial, SimulationDataForCalculateRequest_Dto simulationData) {
		System.out.println(" ==============================|   getPackSendMeCosts  |==================================================");

		try {
			double packsendme_vlr = (total_parcial * simulationData.percentage_packsend) / 100;
			return packsendme_vlr;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	
	@Override
	public double getReshippingCosts(double total_parcial, double percentage_Reshipping) {
		System.out.println(" ==============================|   getReshippingCosts  |==================================================");

		try {
			double vlr_reshipping = (total_parcial * percentage_Reshipping) / 100;
			return vlr_reshipping;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	//============================= Costs VARIABLE ( EMPLOYER ) ================================================================ 
	@Override
	public double getEmployerCosts(double distance_total, double distance_cost, double weight_simulation, double weight_cost, int duration, double duration_cost, String weight_measured_unit) {
		System.out.println(" ==============================|   getEmployerCosts  |==================================================");

		double employer_total_costs = 0.0;
		double weight_vlr = 0.0;
		double weightFormat_vlr = 0.0;
		double workTime_vlr = 0.0;
		
		// Employer Distance Calc
		try {
	
			// Analisys if distance is more 1000 KM 
			double distanceByKM = distance_total / 1000;
			if(distanceByKM > 1000) {
				distance_total =  distanceByKM * 1000;
			}
			else {
				distance_total = distanceByKM;
			}
			
			double distance_vlr = distance_total * distance_cost;
			
			System.out.println(" ");
			System.out.println(" ====================================| E M P L O Y E R |============================================");
			System.out.println(" ========================  getEmployerCosts :: distance_simulation ================ "+ distance_total);
			System.out.println(" ========================  getEmployerCosts :: distance_cost ================ "+ distance_cost);
			System.out.println(" ========================  getEmployerCosts :: weight_simulation ================ "+ weight_simulation);
			System.out.println(" ========================  getEmployerCosts :: weight_cost ================ "+ weight_cost);
			System.out.println(" ========================  getEmployerCosts :: duration ================ "+ duration);
			System.out.println(" ========================  getEmployerCosts :: duration_costs ================ "+ duration_cost);
			System.out.println(" ========================  getEmployerCosts :: weight_measured_unit ================ "+ weight_measured_unit);
			
			// Employer Weight Calc 
			if(weight_measured_unit.equals(MetricUnitMeasurement_Constants.grama_UnitMeasurement)) {
				weight_vlr = weight_simulation * weight_cost;
			}
			else if(weight_measured_unit.equals(MetricUnitMeasurement_Constants.kilograma_UnitMeasurement)) {
				weightFormat_vlr = weightConvert.kilogramoToGrama(weight_simulation);
				weightFormat_vlr = weightFormat_vlr / 100;
				System.out.println(" ========================  getEmployerCosts :: weight - convert ================ "+ weightFormat_vlr);
				weight_vlr = weightFormat_vlr * weight_cost;
			}
			else if(weight_measured_unit.equals(MetricUnitMeasurement_Constants.tonelada_UnitMeasurement)) {
				weightFormat_vlr = weightConvert.ToneladaToGrama(weight_simulation);
				weight_vlr = weightFormat_vlr * weight_cost / 1000;
			}                                                                
	
			// Employer WorkTime Calc 
			workTime_vlr = (duration / 60) * duration_cost;
	
			// TOTAL GENERAL
			employer_total_costs = distance_vlr + weight_vlr + workTime_vlr;
			
			System.out.println(" ========================  getEmployerCosts :: Employer TOTAL DISTANCE ================ "+ distance_vlr);
			System.out.println(" ========================  getEmployerCosts :: Employer TOTAL WEIGHT ================ "+ weight_vlr);
			System.out.println(" ========================  getEmployerCosts :: Employer TOTAL WORKTIME ================ "+ workTime_vlr);
			System.out.println(" ========================  getEmployerCosts :: Employer - TOTAL ================ "+ employer_total_costs);
			System.out.println(" ");
			System.out.println(" ================================================================================");
			System.out.println(" ");
	
			return employer_total_costs;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	

}
