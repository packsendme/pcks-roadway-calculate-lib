package com.packsendme.lib.roadwaycalculate.test.car;

import java.util.HashMap;
import java.util.Map;

import com.packsendme.lib.common.response.dto.api.GoogleAPITrackingResponse_Dto;
import com.packsendme.lib.common.response.dto.api.RoadwayTrackingResponse_Dto;

public class GoogleAPI_Test {
	


	public GoogleAPITrackingResponse_Dto generateGoogleAPI() {
		Map<String, RoadwayTrackingResponse_Dto> trackingRoadwayMap = new HashMap<String, RoadwayTrackingResponse_Dto>();
		
		GoogleAPITrackingResponse_Dto googleTrackingAPI = new GoogleAPITrackingResponse_Dto(); 
		googleTrackingAPI.status = true;
		googleTrackingAPI.distance_total = 9.0;
		googleTrackingAPI.duration = 1536;
		
		RoadwayTrackingResponse_Dto roadwayTracking1 = generateRoadwayTrackingBrasil();
		//RoadwayTrackingResponse_Dto roadwayTracking2 = generateRoadwayTrackingArgentina();

		trackingRoadwayMap.put("Brazil", roadwayTracking1);
	//trackingRoadwayMap.put("Argentina", roadwayTracking2);
		
		googleTrackingAPI.trackingRoadway = trackingRoadwayMap;
		
		return googleTrackingAPI;
	}
	
	
	public RoadwayTrackingResponse_Dto generateRoadwayTrackingBrasil() {
		RoadwayTrackingResponse_Dto roadwayTracking = new RoadwayTrackingResponse_Dto("Brazil", 0, 0.0, 9.0, 4.30, "R$", "KM");
		
		return roadwayTracking;
	}
	
	public RoadwayTrackingResponse_Dto generateRoadwayTrackingArgentina() {
		RoadwayTrackingResponse_Dto roadwayTracking = new RoadwayTrackingResponse_Dto("Argentina", 10, 12.80, 1373.0, 4.30, "R$", "KM");
		
		return roadwayTracking;
	}
		
}
