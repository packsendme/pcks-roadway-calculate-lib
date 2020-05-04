package com.packsendme.lib.roadwaycalculate.test.car.thirdCountries;

import java.util.HashMap;
import java.util.Map;

import com.packsendme.lib.common.response.dto.api.GoogleAPITrackingResponse_Dto;
import com.packsendme.lib.common.response.dto.api.RoadwayTrackingResponse_Dto;

public class GoogleAPI_Test {

	public GoogleAPITrackingResponse_Dto generateGoogleAPI() {
		Map<String, RoadwayTrackingResponse_Dto> trackingRoadwayMap = new HashMap<String, RoadwayTrackingResponse_Dto>();
		
		GoogleAPITrackingResponse_Dto googleTrackingAPI = new GoogleAPITrackingResponse_Dto(); 
		googleTrackingAPI.status = true;
		googleTrackingAPI.distance_total = 6204217.0;
		googleTrackingAPI.duration = 329645;
		
		RoadwayTrackingResponse_Dto roadway_Brasil = generateRoadwayTracking_Brasil();
		RoadwayTrackingResponse_Dto roadway_Ecuador = generateRoadwayTracking_Ecuador();
		RoadwayTrackingResponse_Dto roadway_Bolivia = generateRoadwayTracking_Bolivia();

		trackingRoadwayMap.put("Brazil", roadway_Brasil);
		trackingRoadwayMap.put("Ecuador", roadway_Ecuador);
		trackingRoadwayMap.put("Bolivia", roadway_Bolivia);
		
		googleTrackingAPI.trackingRoadway = trackingRoadwayMap;
		return googleTrackingAPI;
	}
	
	public RoadwayTrackingResponse_Dto generateRoadwayTracking_Brasil() {
		//String name_country, int toll_amount, Double toll_price, Double country_distanceF, Double country_distanceM, Double fuel_price, String currency_price, String unity_measurement_distance
		RoadwayTrackingResponse_Dto roadwayTracking = new RoadwayTrackingResponse_Dto("Brazil", 9, 0.84, 1.435, 1434696.0, 0.83, "R$", "KM");
		return roadwayTracking;
	}
	
	public RoadwayTrackingResponse_Dto generateRoadwayTracking_Ecuador() {			  
		RoadwayTrackingResponse_Dto roadwayTracking = new RoadwayTrackingResponse_Dto("Ecuador", 4, 0.84, 699.0, 699436.0, 0.83, "R$", "KM");
		return roadwayTracking;
	}
	
	public RoadwayTrackingResponse_Dto generateRoadwayTracking_Bolivia() {			  
		RoadwayTrackingResponse_Dto roadwayTracking = new RoadwayTrackingResponse_Dto("Bolivia", 10, 0.84, 1.579, 1579183.0, 0.83, "R$", "KM");
		return roadwayTracking;
	}	
	
	public RoadwayTrackingResponse_Dto generateRoadwayTracking_Peru() {			  
		RoadwayTrackingResponse_Dto roadwayTracking = new RoadwayTrackingResponse_Dto("Peru", 19, 0.84, 2.491, 2490908.0, 0.83, "R$", "KM");
		return roadwayTracking;
	}
		
}
