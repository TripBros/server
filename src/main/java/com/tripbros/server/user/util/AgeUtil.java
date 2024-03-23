package com.tripbros.server.user.util;

import java.util.ArrayList;
import java.util.List;

import com.tripbros.server.board.domain.PreferAgeRange;

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

	public static List<String> preferAgeToString(PreferAgeRange preferAgeRange){
		List<String> preferList = new ArrayList<>();
		if(preferAgeRange.isTwentiesFlag())
			preferList.add("20대");
		if(preferAgeRange.isThirtiesFlag())
			preferList.add("30대");
		if(preferAgeRange.isFortiesFlag())
			preferList.add("40대");
		if(preferAgeRange.isFiftiesFlag())
			preferList.add("50대");
		if(preferAgeRange.isSixtiesAboveFlag())
			preferList.add("60대 이상");
		if(preferAgeRange.isUnrelatedFlag())
			preferList.add("나이 무관");

		return preferList;
	}
}
