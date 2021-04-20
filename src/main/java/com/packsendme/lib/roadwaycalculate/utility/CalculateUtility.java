package com.packsendme.lib.roadwaycalculate.utility;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CalculateUtility {
	
	public Double getAnalizeUnityWeight(Double weight, Map<Integer, String> unityWeight) {
		Double weightVlr = 0.0;
		Double weightVlrFormat = 0.0;
		
		if(unityWeight.get(1) != null) {
			weightVlrFormat = weight;
		}
		else if(unityWeight.get(2) != null) {
			weightVlr = kilogramoToGrama(weight);
			weightVlrFormat = weightVlr * 1000;
		}
		else if(unityWeight.get(3) != null) {
			weightVlr = ToneladaToGrama(weight);
			weightVlrFormat = weightVlr * 1000000;
		}
		return weightVlrFormat;
	}
	
	public Double kilogramoToGrama(double weight) {
		return weight * 1000;
	}
	
	public Double ToneladaToGrama(double weight) {
		return weight * 1000000;
	}
	
	public Double GramaToKilograma(double weight) {
		return weight / 1000;
	}

	public Double GramaToToneleda(double weight) {
		return weight / 1000000;
	}

}
