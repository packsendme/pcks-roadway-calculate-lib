package com.packsendme.roadway.calculate.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.packsendme.lib.common.constants.generic.Fuel_Constants;
import com.packsendme.lib.common.response.dto.api.GoogleAPITrackingResponse_Dto;
import com.packsendme.lib.common.response.dto.api.RoadwayTrackingResponse_Dto;
import com.packsendme.roadway.calculate.dto.CalculatorDto;
import com.packsendme.roadway.calculate.utility.CalculateUtility;
import com.packsendme.roadway.commons.dto.SimulationRoadwayDto;
import com.packsendme.roadway.commons.response.SimulationRoadwayResponse;
import com.packsendme.roadway.commons.entity.Costs;
import com.packsendme.roadway.commons.entity.Roadway;

public abstract class RoadwayRulesCosts {
	

	// =============== CORE - Calculator =============== // 
	
	public Map<String, List<CalculatorDto>> getDistance_Calculator(GoogleAPITrackingResponse_Dto googleAPI_Obj, Roadway roadwayBRE_Obj) {
		List<CalculatorDto> calculatorDto_L = new ArrayList<CalculatorDto>(); 
		CalculatorDto calcDto_Obj = null;
		Map<String, List<CalculatorDto>> distanceCost_M = new HashMap<String, List<CalculatorDto>>();
		System.out.println("---------------------------------------");
		System.out.println(" - getDistance_Calculator: START ---- ");
	
		try {
			for(Entry<String, RoadwayTrackingResponse_Dto> entry : googleAPI_Obj.trackingRoadway.entrySet()) {
				String country = entry.getKey();
				RoadwayTrackingResponse_Dto trackingResponseDto = googleAPI_Obj.trackingRoadway.get(country);
				
				System.out.println(" - getDistance_Calculator: Country ---- "+ country);
				
				List<Costs> distanceCountryL = roadwayBRE_Obj.costs.get(country);
				System.out.println(" - getDistance_Calculator: LIST ---- "+ distanceCountryL.size());
				System.out.println(" - getDistance_Calculator: country_distanceF ---- "+ trackingResponseDto.country_distanceF);

				for(Costs costsObj : distanceCountryL) {
					calcDto_Obj = new CalculatorDto();
					calcDto_Obj.name = costsObj.vehicle;
					calcDto_Obj.value = trackingResponseDto.country_distanceF * costsObj.distance_cost;
					calculatorDto_L.add(calcDto_Obj);
					System.out.println(" - getDistance_Calculator: VEICULO ---- "+ calcDto_Obj.name);
					System.out.println(" - getDistance_Calculator: VALOR ---- "+ calcDto_Obj.value);

				}
				distanceCost_M.put(country, calculatorDto_L);
			}
			System.out.println(" - getDistance_Calculator: FIM "+ distanceCost_M.size());
			System.out.println("---------------------------------------");
			System.out.println("");

			return distanceCost_M;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public Map<String, List<CalculatorDto>> getWeight_Calculator(Double weightFrom, Map<Integer, String> unity_weight, GoogleAPITrackingResponse_Dto googleAPI, Roadway roadwayBRE_Obj) {
		double weightCountry = 0.0;
		CalculateUtility weightConvert = new CalculateUtility();
		
		Map<String, List<CalculatorDto>> weightCost_M = new HashMap<String, List<CalculatorDto>>();
		List<CalculatorDto> calculatorDto_L = new ArrayList<CalculatorDto>(); 
		
		System.out.println("---------------------------------------");
		System.out.println(" - getWeight_Calculator: START ---- ");
		System.out.println(" - getWeight_Calculator: Weight ---- "+ weightFrom);
		System.out.println(" - getWeight_Calculator: unity_weight ---- "+ unity_weight);

		
		try {
			// Conversion Unit Measurement
			weightCountry = weightConvert.getAnalizeUnityWeight(weightFrom, unity_weight);
			System.out.println(" - getWeight_Calculator: weightFormat ---- "+ weightCountry);
			
			for(Entry<String, List<Costs>> entry : roadwayBRE_Obj.costs.entrySet()) {
				String country = entry.getKey();
				
				System.out.println(" - getWeight_Calculator: Country ---- "+ country);

	
				// Check MORE ONE country in tracking : If YES divid Weight for distance by country current
				if(googleAPI.trackingRoadway.size() > 1) {
					RoadwayTrackingResponse_Dto trackingResponseDto = googleAPI.trackingRoadway.get(country);
					weightCountry = weightCountry / trackingResponseDto.country_distanceF;
				}
				
				List<Costs> weightCountryL = roadwayBRE_Obj.costs.get(country);
				System.out.println(" - getWeight_Calculator: LIST-Country ---- "+ weightCountryL.size());

				for(Costs costsObj : weightCountryL) {
					CalculatorDto calcDto_Obj = new CalculatorDto();
					calcDto_Obj.name = costsObj.vehicle;
					calcDto_Obj.value = weightCountry * costsObj.weight_cost;
					calculatorDto_L.add(calcDto_Obj);
				
					System.out.println(" - getWeight_Calculator: VEICULO ---- "+ calcDto_Obj.name);
					System.out.println(" - getWeight_Calculator: VALOR ---- "+ calcDto_Obj.value);

				}
				weightCost_M.put(country, calculatorDto_L);
			}
			
			System.out.println(" - getWeight_Calculator: FIM "+ weightCost_M.size());
			System.out.println("---------------------------------------");
			System.out.println("");

			return weightCost_M;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<String, List<CalculatorDto>> getTolls_Calculator(GoogleAPITrackingResponse_Dto googleAPI_Obj) {
		List<CalculatorDto> calculatorDto_L = new ArrayList<CalculatorDto>(); 
		CalculatorDto calcDto_Obj = null;
		Map<String, List<CalculatorDto>> tollsCost_M = new HashMap<String, List<CalculatorDto>>();
		
		System.out.println("---------------------------------------");
		System.out.println(" - getTolls_Calculator: START ---- ");

		try {
			for(Entry<String, RoadwayTrackingResponse_Dto> entry : googleAPI_Obj.trackingRoadway.entrySet()) {
				String country = entry.getKey();
				calcDto_Obj = new CalculatorDto();
				
				System.out.println(" - getTolls_Calculator: Country ---- "+ country);

				RoadwayTrackingResponse_Dto trackingResponseDto = googleAPI_Obj.trackingRoadway.get(country);
				calcDto_Obj.name = trackingResponseDto.name_country;
				calcDto_Obj.value = trackingResponseDto.toll_amount * trackingResponseDto.toll_price; 
				
				System.out.println(" - getTolls_Calculator: VEICULO ---- "+ calcDto_Obj.name);
				System.out.println(" - getTolls_Calculator: VALOR ---- "+ calcDto_Obj.value);

				calculatorDto_L.add(calcDto_Obj);
				tollsCost_M.put(country, calculatorDto_L);
			}
			
			System.out.println(" - getWeight_Calculator: FIM "+ tollsCost_M.size());
			System.out.println("---------------------------------------");
			System.out.println("");

			return tollsCost_M;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<String, List<CalculatorDto>> getDimension_Calculator(Double height_DimensionFrom, Double width_DimensionFrom, Double length_DimensionFrom, 
			GoogleAPITrackingResponse_Dto googleAPI, Roadway roadwayBRE_Obj) {
		double dimensionCountry = 0.0;
		double heightDimension = 0.0;
		double widthDimension = 0.0;
		double lengthDimension = 0.0;
		
		Map<String, List<CalculatorDto>> dimensionCost_M = new HashMap<String, List<CalculatorDto>>();
		List<CalculatorDto> calculatorDto_L = new ArrayList<CalculatorDto>(); 
		
		System.out.println("---------------------------------------");
		System.out.println(" - getDimension_Calculator: START ---- ");

		try {
			for(Entry<String, List<Costs>> entry : roadwayBRE_Obj.costs.entrySet()) {
				String country = entry.getKey();
				dimensionCountry = 0.0;
				
				System.out.println(" - getDimension_Calculator: Country ---- "+ country);

				// Check more country in tracking : If YES divid Weight for distance by country current
				if(googleAPI.trackingRoadway.size() > 1) {
					RoadwayTrackingResponse_Dto trackingResponseDto = googleAPI.trackingRoadway.get(country);
					heightDimension = height_DimensionFrom / trackingResponseDto.country_distanceF;
					heightDimension = heightDimension * 100;
	
					widthDimension = width_DimensionFrom / trackingResponseDto.country_distanceF;
					widthDimension = widthDimension * 100;
					
					lengthDimension = length_DimensionFrom / trackingResponseDto.country_distanceF;
					lengthDimension = lengthDimension * 100;
	
				}
				else {
					heightDimension = height_DimensionFrom;
					widthDimension = width_DimensionFrom;
					lengthDimension = length_DimensionFrom;
				}
				
				List<Costs> weightCountryL = roadwayBRE_Obj.costs.get(country);
				for(Costs costsObj : weightCountryL) {
					CalculatorDto calcDto_Obj = new CalculatorDto();
					calcDto_Obj.name = costsObj.vehicle;
					dimensionCountry = (heightDimension * costsObj.heightDimension_cost) + (widthDimension * costsObj.widthDimension_cost) + (lengthDimension * costsObj.lengthDimension_cost); 
					
					System.out.println(" - getDimension_Calculator: VEICULO ---- "+ calcDto_Obj.name);
					System.out.println(" - getDimension_Calculator: VALOR ---- "+ dimensionCountry);

					calcDto_Obj.value = dimensionCountry;
					calculatorDto_L.add(calcDto_Obj);
				}
				dimensionCost_M.put(country, calculatorDto_L);
			}
			
			System.out.println(" - dimensionCost_M: FIM "+ dimensionCost_M.size());
			System.out.println("---------------------------------------");
			System.out.println("");

			return dimensionCost_M;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}



	public Map<String, List<CalculatorDto>> getWorktime_Calculator(GoogleAPITrackingResponse_Dto googleAPI_Obj, Roadway roadwayBRE_Obj) {
		List<CalculatorDto> calculatorDto_L = new ArrayList<CalculatorDto>(); 
		CalculatorDto calcDto_Obj = null;
		Map<String, List<CalculatorDto>> worktimeCost_M = new HashMap<String, List<CalculatorDto>>();

		System.out.println("---------------------------------------");
		System.out.println(" - getWorktime_Calculator: START ---- ");

		try {
			double durationByCountry = googleAPI_Obj.duration / googleAPI_Obj.trackingRoadway.size();
			for(Entry<String, RoadwayTrackingResponse_Dto> entry : googleAPI_Obj.trackingRoadway.entrySet()) {
				String country = entry.getKey();
				calcDto_Obj = new CalculatorDto();
	
				List<Costs> worktimeCountryL = roadwayBRE_Obj.costs.get(country);
				for(Costs costsObj : worktimeCountryL) {
					calcDto_Obj = new CalculatorDto();
					calcDto_Obj.name = costsObj.vehicle;
					calcDto_Obj.value = (durationByCountry / 60) * costsObj.worktime_cost;

					System.out.println(" - getWorktime_Calculator: VEICULO ---- "+ calcDto_Obj.name);
					System.out.println(" - getWorktime_Calculator: VALOR ---- "+ calcDto_Obj.value);
					
					calculatorDto_L.add(calcDto_Obj);
				}
				worktimeCost_M.put(country, calculatorDto_L);
			}
			
			System.out.println(" - getWorktime_Calculator: FIM "+ worktimeCost_M.size());
			System.out.println("---------------------------------------");
			System.out.println("");

			return worktimeCost_M;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Map<String, List<CalculatorDto>> getFuelConsumption_Calculator(GoogleAPITrackingResponse_Dto googleAPI_Obj, Roadway roadwayBRE_Obj) {
		List<CalculatorDto> calculatorDto_L = new ArrayList<CalculatorDto>(); 
		CalculatorDto calcDto_Obj = null;
		Map<String, List<CalculatorDto>> fuelCost_M = new HashMap<String, List<CalculatorDto>>();
		Double vlr_default = 0.00;
	
		System.out.println("---------------------------------------");
		System.out.println(" - getFuelConsumption_Calculator: START ---- ");

		try {
				for(Entry<String, RoadwayTrackingResponse_Dto> entry : googleAPI_Obj.trackingRoadway.entrySet()) {
					String country = entry.getKey();
					calcDto_Obj = new CalculatorDto();
					RoadwayTrackingResponse_Dto trackingResponseDto = googleAPI_Obj.trackingRoadway.get(country);
					
					List<Costs> fuelCountryL = roadwayBRE_Obj.costs.get(country);
					for(Costs costsObj : fuelCountryL) {
						calcDto_Obj = new CalculatorDto();
						calcDto_Obj.name = costsObj.vehicle;
						if((costsObj.fuel_type.equals(Fuel_Constants.GAS_FUEL)) || (costsObj.fuel_type.equals(Fuel_Constants.FLEX_FUEL))) {
							
							System.out.println(" ");
							System.out.println("---------------------------------------");
							System.out.println(" - getFuelConsumption_Calculator: country_distanceF ---- "+ trackingResponseDto.country_distanceF);
							System.out.println(" - getFuelConsumption_Calculator: average_consumption_cost ---- "+ costsObj.average_consumption_cost);
							System.out.println(" - getFuelConsumption_Calculator: fuelGasoline_price ---- "+ trackingResponseDto.fuelGasoline_price);
							System.out.println("---------------------------------------");

							
							calcDto_Obj.value = (trackingResponseDto.country_distanceF / costsObj.average_consumption_cost) * trackingResponseDto.fuelGasoline_price; 
							System.out.println(" - getFuelConsumption_Calculator: VEICULO ---- "+ calcDto_Obj.name);
							System.out.println(" - getFuelConsumption_Calculator: VALOR ---- "+ calcDto_Obj.value);

						}
						else if(costsObj.fuel_type.equals(Fuel_Constants.DIESEL_FUEL)) {
							calcDto_Obj.value = (trackingResponseDto.country_distanceF / costsObj.average_consumption_cost) * trackingResponseDto.fuelDiesel_price; 
						}
						else if(costsObj.fuel_type.equals(Fuel_Constants.ELECTRIC_FUEL)) {
							calcDto_Obj.value = vlr_default; 
						}
						
						calculatorDto_L.add(calcDto_Obj);
					}
					fuelCost_M.put(country, calculatorDto_L);
				}
				
				System.out.println(" - getFuelConsumption_Calculator: FIM "+ fuelCost_M.size());
				System.out.println("---------------------------------------");
				System.out.println("");

				return fuelCost_M;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	abstract public SimulationRoadwayResponse instanceRulesCosts(SimulationRoadwayDto requestData);

}
