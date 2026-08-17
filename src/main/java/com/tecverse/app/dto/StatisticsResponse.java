package com.tecverse.app.dto;

public record StatisticsResponse(
        long participants,
        long speakers,
        long organizations,
        long sessions,
        long partners
) {
}
