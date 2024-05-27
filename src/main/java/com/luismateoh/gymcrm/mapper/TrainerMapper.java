package com.luismateoh.gymcrm.mapper;

import com.luismateoh.gymcrm.domain.Trainer;
import com.luismateoh.gymcrm.dto.TrainerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class, TrainingTypeMapper.class, TraineeMapper.class, TrainingMapper.class})
public interface TrainerMapper {
    TrainerMapper INSTANCE = Mappers.getMapper(TrainerMapper.class);

    TrainerDTO trainerToTrainerDTO(Trainer trainer);

    Trainer trainerDTOToTrainer(TrainerDTO trainerDTO);
}

