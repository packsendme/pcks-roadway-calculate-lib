package com.packsendme.lib.roadwaycalculate.total;

import com.packsendme.lib.roadway.simulation.request.SimulationRoadwayRequest_Dto;
import com.packsendme.lib.utility.FormatValueMoney;
import com.packsendme.roadbrewa.entity.Roadway;

public class TotalImpl implements ITotal {
	
	private FormatValueMoney valueFormatObj = new FormatValueMoney();

	@Override
	public double getFragileVlr_Total(Double totalCostsCore, Roadway roadwayBRE_Obj) {
		try {
			return (totalCostsCore * roadwayBRE_Obj.fragile_cost) / 100;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	@Override
	public double getPersishableVlr_Total(Double totalCostsCore, Roadway roadwayBRE_Obj) {
		try {
			return (totalCostsCore * roadwayBRE_Obj.persishable_cost) / 100; 
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	@Override
	public double getReshippingVlr_Total(double totalCostsCore, Roadway roadwayBRE_Obj) {
		try {
			return (totalCostsCore * roadwayBRE_Obj.reshipping_cost) / 100;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	@Override
	public double getEmployeeVlr_Total(double totalCostsCore, Roadway roadwayBRE_Obj) {
		try {
			return (totalCostsCore * roadwayBRE_Obj.employeer_cost) / 100;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	@Override
	public double getOperationOwner_Total(double totalCostsCore, Roadway roadwayBRE_Obj) {
		try {
			return (totalCostsCore * roadwayBRE_Obj.operation_cost) / 100;
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}

	@Override
	public double getCosts_Total(double totalCostsCore, double vlr_fragile, double vlr_persishable,
			double vlr_reshipping, double vlr_operationOwner, double vlr_employeer) {
		try {
			return (totalCostsCore+vlr_fragile+vlr_persishable+vlr_reshipping+vlr_operationOwner+vlr_employeer);
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	
	@Override
	public String getExchange_Total(SimulationRoadwayRequest_Dto simulationDto, double vlr_total) {
		try {
			double vlr_exchange = vlr_total * simulationDto.rate_exchange;
			return valueFormatObj.formatDoubleStringInCurrency(vlr_exchange,simulationDto.language_exchange, simulationDto.country_exchange);
		}
		catch (Exception e) {
			e.printStackTrace();
			return "0.0";
		}
	}


}
