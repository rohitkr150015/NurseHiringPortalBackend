package com.nuhi.Nuhi.config;


import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel ="spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MapStructConfig {
}
