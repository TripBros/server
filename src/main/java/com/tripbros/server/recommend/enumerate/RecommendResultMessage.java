package com.tripbros.server.recommend.enumerate;

public enum RecommendResultMessage {
	GET_ALL_RECOMMEND_LOCATES_SUCCESS("추천 지역 전체 조회 성공"),
	GET_A_RANDOM_RECOMMEND_LOCATE_SUCCESS("하나의 랜덤 추천 지역 조회 성공"),
	GET_ALL_RECOMMEND_PLACES_SUCCESS("선택 지역에 대한 추천 맛집 조회 성공"),
	UPDATE_BOOKMARK_PLACE_SUCCESS("추전 맛집 북마크 반영 성공"),
	GET_BOOKMARKED_PLACE_SUCCESS("북마크 한 맛집 조회 성공");

	private final String message;

	RecommendResultMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
