package com.packsendme.lib.roadwaycalculate.test.car.OneCountryMore1000km;

import java.util.HashMap;
import java.util.Map;

import com.packsendme.lib.common.response.dto.api.GoogleAPITrackingResponse_Dto;
import com.packsendme.lib.common.response.dto.api.RoadwayTrackingResponse_Dto;

public class GoogleAPI_Test {
	


	public GoogleAPITrackingResponse_Dto generateGoogleAPI() {
		Map<String, RoadwayTrackingResponse_Dto> trackingRoadwayMap = new HashMap<String, RoadwayTrackingResponse_Dto>();
		
		GoogleAPITrackingResponse_Dto googleTrackingAPI = new GoogleAPITrackingResponse_Dto(); 
		googleTrackingAPI.status = true;
		googleTrackingAPI.distance_total = 1544432.0;
		googleTrackingAPI.duration = 73072;
		
		RoadwayTrackingResponse_Dto roadwayTracking1 = generateRoadwayTrackingBrasil();
		trackingRoadwayMap.put("Brazil", roadwayTracking1);
		googleTrackingAPI.trackingRoadway = trackingRoadwayMap;
		return googleTrackingAPI;
	}
	
	public RoadwayTrackingResponse_Dto generateRoadwayTrackingBrasil() {
		//String name_country, int toll_amount, Double toll_price, Double country_distanceF, Double country_distanceM,Double fuel_price, String currency_price, String unity_measurement_distance
		RoadwayTrackingResponse_Dto roadwayTracking = new RoadwayTrackingResponse_Dto("Brazil", 10, 0.84, 1.544,  1544426.0, 0.83, "R$", "KM");
		
		return roadwayTracking;
	}
		
}
