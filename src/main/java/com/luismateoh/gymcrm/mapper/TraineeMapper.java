package com.luismateoh.gymcrm.mapper;

import com.luismateoh.gymcrm.domain.Trainee;
import com.luismateoh.gymcrm.dto.TraineeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class, TrainerMapper.class, TrainingMapper.class})
public interface TraineeMapper {
    TraineeMapper INSTANCE = Mappers.getMapper(TraineeMapper.class);

    TraineeDTO traineeToTraineeDTO(Trainee trainee);

    Trainee traineeDTOToTrainee(TraineeDTO traineeDTO);
}
