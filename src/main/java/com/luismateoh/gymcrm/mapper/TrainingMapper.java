package com.luismateoh.gymcrm.mapper;

import com.luismateoh.gymcrm.domain.Training;
import com.luismateoh.gymcrm.dto.TrainingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {TraineeMapper.class, TrainerMapper.class, TrainingTypeMapper.class})
public interface TrainingMapper {
    TrainingMapper INSTANCE = Mappers.getMapper(TrainingMapper.class);

    TrainingDTO trainingToTrainingDTO(Training training);

    Training trainingDTOToTraining(TrainingDTO trainingDTO);
}
