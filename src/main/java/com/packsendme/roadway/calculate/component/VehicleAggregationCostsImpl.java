package com.packsendme.roadway.calculate.component;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.packsendme.roadway.calculate.dto.CalculatorDto;

public class VehicleAggregationCostsImpl implements IVehicleAggregationCosts {

	@Override
	public double weightVehicleCosts(String vehicle, Map<String, List<CalculatorDto>> weightCost_M) {
		double vlr_total = 0.0;
		try {
			if (weightCost_M != null) {
				for(Entry<String, List<CalculatorDto>> entry : weightCost_M.entrySet()) {
					String country = entry.getKey();
					List<CalculatorDto> weightCostL = weightCost_M.get(country);
					for(CalculatorDto calcObj : weightCostL) {
						if(vehicle.equals(calcObj.name)) {
							vlr_total = calcObj.value + vlr_total; 							
						}
					}
				}
			}
			return vlr_total;
		}
		catch (Exception e) {
			e.printStackTrace();
			return vlr_total;
		}
	}

	@Override
	public double distanceVehicleCosts(String vehicle, Map<String, List<CalculatorDto>> distanceCost_M) {
		double vlr_total = 0.0;
		try {
			if (distanceCost_M != null) {
				for(Entry<String, List<CalculatorDto>> entry : distanceCost_M.entrySet()) {
					String country = entry.getKey();
					List<CalculatorDto> distanceCostL = distanceCost_M.get(country);
					for(CalculatorDto calcObj : distanceCostL) {
						if(vehicle.equals(calcObj.name)) {
							vlr_total = calcObj.value + vlr_total; 							
						}
					}
				}
			}
			return vlr_total;
		}
		catch (Exception e) {
			e.printStackTrace();
			return vlr_total;
		}
	}

	@Override
	public double worktimeVehicleCosts(String vehicle, Map<String, List<CalculatorDto>> worktimeCost_M) {
		double vlr_total = 0.0;
		try {
			if (worktimeCost_M != null) {
				for(Entry<String, List<CalculatorDto>> entry : worktimeCost_M.entrySet()) {
					String country = entry.getKey();
					List<CalculatorDto> worktimeCostL = worktimeCost_M.get(country);
					for(CalculatorDto calcObj : worktimeCostL) {
						if(vehicle.equals(calcObj.name)) {
							vlr_total = calcObj.value + vlr_total; 							
						}
					}
				}
			}
			return vlr_total;
		}
		catch (Exception e) {
			e.printStackTrace();
			return vlr_total;
		}
	}

	@Override
	public double fuelConsumptionVehicleCosts(String vehicle, Map<String, List<CalculatorDto>> fuelConsumptionCost_M) {
		double vlr_total = 0.0;
		try {
			if (fuelConsumptionCost_M != null) {
				for(Entry<String, List<CalculatorDto>> entry : fuelConsumptionCost_M.entrySet()) {
					String country = entry.getKey();
					List<CalculatorDto> fuelCostL = fuelConsumptionCost_M.get(country);
					for(CalculatorDto calcObj : fuelCostL) {
						if(vehicle.equals(calcObj.name)) {
							vlr_total = calcObj.value + vlr_total; 							
						}
					}
				}
			}
			return vlr_total;
		}
		catch (Exception e) {
			e.printStackTrace();
			return vlr_total;
		}
	}

	@Override
	public double tollsVehicleCosts(Map<String, List<CalculatorDto>> tollsCost_M) {
		double vlr_total = 0.0;
		try {
			if (tollsCost_M != null) {
				for(Entry<String, List<CalculatorDto>> entry : tollsCost_M.entrySet()) {
					String country = entry.getKey();
					List<CalculatorDto> fuelCostL = tollsCost_M.get(country);
					for(CalculatorDto calcObj : fuelCostL) {
						vlr_total = calcObj.value + vlr_total; 							
					}
				}
			}
			return vlr_total;
		}
		catch (Exception e) {
			e.printStackTrace();
			return vlr_total;
		}
	}

	@Override
	public double dimensionVehicleCosts(String vehicle, Map<String, List<CalculatorDto>> dimensionCost_M) {
		double vlr_total = 0.0;
		try {
			if (dimensionCost_M != null) {
				for(Entry<String, List<CalculatorDto>> entry : dimensionCost_M.entrySet()) {
					String country = entry.getKey();
					List<CalculatorDto> dimensionCostL = dimensionCost_M.get(country);
					for(CalculatorDto calcObj : dimensionCostL) {
						if(vehicle.equals(calcObj.name)) {
							vlr_total = calcObj.value + vlr_total; 							
						}
					}
				}
			}
			return vlr_total;
		}
		catch (Exception e) {
			e.printStackTrace();
			return vlr_total;
		}
	}

}
