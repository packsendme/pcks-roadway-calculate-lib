package com.packsendme.lib.roadwaycalculate.rules;

import java.util.List;
import java.util.Map;

import com.packsendme.lib.roadwaycalculate.dto.CalculatorDto;

public interface IVehicleAggregationCosts {

	public double weightVehicleCosts (String vehicle, Map<String, List<CalculatorDto>> weightCost_M);
	public double distanceVehicleCosts (String vehicle, Map<String, List<CalculatorDto>> distanceCost_M);
	public double worktimeVehicleCosts (String vehicle, Map<String, List<CalculatorDto>> worktimeCost_M);
	public double fuelConsumptionVehicleCosts (String vehicle, Map<String, List<CalculatorDto>> fuelConsumptionCost_M);
	public double tollsVehicleCosts (Map<String, List<CalculatorDto>> tollsCost_M );
	public double dimensionVehicleCosts (String vehicle, Map<String, List<CalculatorDto>> dimensionCost_M );
}
