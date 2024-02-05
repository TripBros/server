package com.tripbros.server.user.dto;

import com.tripbros.server.enumerate.Sex;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EditUserInfoRequest(String password,
								  String nickname,
								  @NotNull Boolean leisurePreferFlag,
								  @NotNull Boolean planPreferFlag,
								  @NotNull Boolean adventurePreferFlag,
								  @NotNull Boolean vehiclePreferFlag,
								  @NotNull Boolean photoPreferFlag,
								  String profileImage) {
}
