package com.packsendme.lib.roadwaycalculate.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.packsendme.lib.common.constants.generic.Fuel_Constants;
import com.packsendme.lib.common.response.dto.api.GoogleAPITrackingResponse_Dto;
import com.packsendme.lib.common.response.dto.api.RoadwayTrackingResponse_Dto;
import com.packsendme.lib.roadwaycalculate.dto.CalculatorDto;
import com.packsendme.roadbrewa.entity.Costs;
import com.packsendme.roadbrewa.entity.Roadway;

public abstract class RoadwayCalculate {
	

	// =============== CORE - Calculator =============== // 
	
	public Map<String, List<CalculatorDto>> getWeight_Calculator(double weightFrom, GoogleAPITrackingResponse_Dto googleAPI, Roadway roadwayBRE_Obj) {
		double weightCountry = 0.0;
		
		Map<String, List<CalculatorDto>> weightCost_M = new HashMap<String, List<CalculatorDto>>();
		List<CalculatorDto> calculatorDto_L = new ArrayList<CalculatorDto>(); 
		try {
			for(Entry<String, List<Costs>> entry : roadwayBRE_Obj.costs.entrySet()) {
				String country = entry.getKey();
	
				// Check more country in tracking : If YES divid Weight for distance by country current
				if(googleAPI.trackingRoadway.size() > 1) {
					RoadwayTrackingResponse_Dto trackingResponseDto = googleAPI.trackingRoadway.get(country);
					weightCountry = weightFrom / trackingResponseDto.country_distanceF;
				}
				else {
					weightCountry = weightFrom;
				}
				
				List<Costs> weightCountryL = roadwayBRE_Obj.costs.get(country);
				for(Costs costsObj : weightCountryL) {
					CalculatorDto calcDto_Obj = new CalculatorDto();
					calcDto_Obj.name = costsObj.vehicle;
					calcDto_Obj.value = weightCountry * costsObj.weight_cost;
					calculatorDto_L.add(calcDto_Obj);
				}
				weightCost_M.put(country, calculatorDto_L);
			}
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
		try {
			for(Entry<String, RoadwayTrackingResponse_Dto> entry : googleAPI_Obj.trackingRoadway.entrySet()) {
				String country = entry.getKey();
				calcDto_Obj = new CalculatorDto();
				RoadwayTrackingResponse_Dto trackingResponseDto = googleAPI_Obj.trackingRoadway.get(country);
				calcDto_Obj.name = trackingResponseDto.name_country;
				calcDto_Obj.value = trackingResponseDto.toll_price * trackingResponseDto.toll_amount; 
				calculatorDto_L.add(calcDto_Obj);
				tollsCost_M.put(country, calculatorDto_L);
			}
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
		try {
			for(Entry<String, List<Costs>> entry : roadwayBRE_Obj.costs.entrySet()) {
				String country = entry.getKey();
				dimensionCountry = 0.0;
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
					calcDto_Obj.value = dimensionCountry;
					calculatorDto_L.add(calcDto_Obj);
				}
				dimensionCost_M.put(country, calculatorDto_L);
			}
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
					calculatorDto_L.add(calcDto_Obj);
				}
				worktimeCost_M.put(country, calculatorDto_L);
			}
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
							calcDto_Obj.value = (trackingResponseDto.country_distanceF / costsObj.average_consumption_cost) * trackingResponseDto.fuelGasoline_price; 
						}
						else if(costsObj.fuel_type.equals(Fuel_Constants.DIESEL_FUEL)) {
							calcDto_Obj.value = (trackingResponseDto.country_distanceF / costsObj.average_consumption_cost) * trackingResponseDto.fuelDiesel_price; 
						}
						calculatorDto_L.add(calcDto_Obj);
					}
					fuelCost_M.put(country, calculatorDto_L);
				}
				return fuelCost_M;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// =============== TOTAL - RISK =============== // 
	
	public double getFragile_Calculator(Double costTotal_Core, Roadway roadwayBRE_Obj) {
		try {
			return (costTotal_Core * roadwayBRE_Obj.fragile_cost) / 100;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	public double getPersishable_Calculator(Double costTotal_Core, Roadway roadwayBRE_Obj) {
		try {
			return (costTotal_Core * roadwayBRE_Obj.persishable_cost) / 100; 
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	// =============== TOTAL - Calculator =============== // 

	public double getEmployee_Calculator(double costTotal_Core, Roadway roadwayBRE_Obj) {
		try {
			double vlr_employee = (costTotal_Core * roadwayBRE_Obj.employeer_cost) / 100;
			return vlr_employee;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	
	public double getOperationOwner_Calculator(double costTotal_Core, Roadway roadwayBRE_Obj) {
		try {
			double vlr_operation = (costTotal_Core * roadwayBRE_Obj.operation_cost) / 100;
			return vlr_operation;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	

	public double getReshippingCosts(double costTotal_Core, Roadway roadwayBRE_Obj) {
		try {
			double vlr_reshipping = (costTotal_Core * roadwayBRE_Obj.reshipping_cost) / 100;
			return vlr_reshipping;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}


	abstract public double calculatorProccess();

}
