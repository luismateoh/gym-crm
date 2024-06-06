package com.luismateoh.gymcrm.mapper;

import com.luismateoh.gymcrm.domain.TrainingType;
import com.luismateoh.gymcrm.dto.TrainingTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TrainingTypeMapper {
    TrainingTypeMapper INSTANCE = Mappers.getMapper(TrainingTypeMapper.class);

    TrainingTypeDTO trainingTypeToTrainingTypeDTO(TrainingType trainingType);

    TrainingType trainingTypeDTOToTrainingType(TrainingTypeDTO trainingTypeDTO);
}
