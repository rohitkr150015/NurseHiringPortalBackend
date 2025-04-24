package com.nuhi.Nuhi.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuhi.Nuhi.dto.NurseProfileDTO;
import com.nuhi.Nuhi.model.NurseProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface NurseProfileMapper {

        @Mapping(target = "fullName", source = "user.firstName")
        @Mapping(target = "hourlyRate", source = "hourly_rate")
        NurseProfileDTO toDto(NurseProfile entity);

        @Mapping(target = "hourly_rate", source = "hourlyRate")
        @Mapping(target = "user", ignore = true)
        @Mapping(target = "nurseId", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "licenseNumber", ignore = true)
        NurseProfile toEntity(NurseProfileDTO dto);

        @Mapping(target = "user", ignore = true)
        @Mapping(target = "nurseId", ignore = true)
        @Mapping(target = "status", ignore = true)
        @Mapping(target = "createdAt", ignore = true)
        @Mapping(target = "licenseNumber", ignore = true)
        @Mapping(target = "hourly_rate", source = "hourlyRate")
        void updateEntityFromDto(NurseProfileDTO dto, @MappingTarget NurseProfile entity);
}




//@Mapping(target = "availability", expression = "java(mapAvailabilityToJson(dto.getAvailability()))")
//NurseProfile toEntity(NurseProfileDTO dto);
//
//@Mapping(target = "availability", expression = "java(mapJsonToAvailability(entity.getAvailability()))")
//NurseProfileDTO toDto(NurseProfile entity);
//
//@Mapping(target = "availability", expression = "java(mapAvailabilityToJson(dto.getAvailability()))")
//void updateEntityFromDto(NurseProfileDTO dto, @MappingTarget NurseProfile entity); // Renamed method
//
//default String mapAvailabilityToJson(Object availability) {
//    try {
//        if (availability instanceof String) return (String) availability;
//        return new ObjectMapper().writeValueAsString(availability);
//    } catch (JsonProcessingException e) {
//        throw new RuntimeException("Error mapping availability to JSON", e);
//    }
//}
//
//default Object mapJsonToAvailability(String availabilityJson) {
//    try {
//        return new ObjectMapper().readValue(availabilityJson, Object.class);
//    } catch (JsonProcessingException e) {
//        throw new RuntimeException("Error mapping JSON to availability", e);
//    }
//}