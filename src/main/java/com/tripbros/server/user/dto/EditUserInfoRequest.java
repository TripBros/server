package com.tripbros.server.user.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

public record EditUserInfoRequest(@Nullable String password,
								  @Nullable String nickname,
								  @NotNull Boolean leisurePreferFlag,
								  @NotNull Boolean planPreferFlag,
								  @NotNull Boolean adventurePreferFlag,
								  @NotNull Boolean vehiclePreferFlag,
								  @NotNull Boolean photoPreferFlag) {
}
