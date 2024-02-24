package com.tripbros.server.user.util;

public final class AgeUtil {
	public static String ageRange(Integer age){
		if(age >= 60) return "60대 이상";
		if(age >= 50) return "50대";
		if(age >= 40) return "40대";
		if(age >= 30) return "30대";
		if(age >= 20) return "20대";
		if(age >= 10) return "10대";
		return "기타";
	}
}
