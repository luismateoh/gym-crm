package com.luismateoh.gymcrm.service;

import static com.luismateoh.gymcrm.utils.Utils.convertToLocalDate;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.luismateoh.gymcrm.dao.TrainingDao;
import com.luismateoh.gymcrm.domain.Training;
import com.luismateoh.gymcrm.domain.TrainingType;
import com.luismateoh.gymcrm.dto.TrainingDTO;
import com.luismateoh.gymcrm.mapper.TrainingMapper;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {
    private final TrainingDao trainingDao;
    private final TrainingTypeService trainingTypeService;
    private static final TrainingMapper trainingMapper = TrainingMapper.INSTANCE;

    public TrainingService(TrainingDao trainingDao, TrainingTypeService trainingTypeService) {
        this.trainingDao = trainingDao;
        this.trainingTypeService = trainingTypeService;
    }

    public TrainingDTO addTraining(TrainingDTO trainingDTO) {
        Training training = trainingMapper.trainingDTOToTraining(trainingDTO);
        TrainingType trainingType = trainingTypeService.findByName(trainingDTO.getTrainingType().getTrainingTypeName())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid specialization: " + trainingDTO.getTrainingType().getTrainingTypeName()));
        training.setTrainingType(trainingType);
        Training savedTraining = trainingDao.saveOrUpdate(training);
        return trainingMapper.trainingToTrainingDTO(savedTraining);
    }

    public List<TrainingDTO> getTraineeTrainings(String username, Date fromDate, Date toDate, String trainerName,
                                                 String trainingType) {
        LocalDate localFromDate = convertToLocalDate(fromDate);
        LocalDate localToDate = convertToLocalDate(toDate);
        return trainingDao.findByTraineeUsernameAndCriteria(username, localFromDate, localToDate, trainerName,
                trainingType).stream().map(trainingMapper::trainingToTrainingDTO).toList();
    }

    public List<TrainingDTO> getTrainerTrainings(String username, Date fromDate, Date toDate, String traineeName) {
        LocalDate localFromDate = convertToLocalDate(fromDate);
        LocalDate localToDate = convertToLocalDate(toDate);
        return trainingDao.findByTrainerUsernameAndCriteria(username, localFromDate, localToDate, traineeName)
                .stream()
                .map(trainingMapper::trainingToTrainingDTO)
                .toList();
    }

    public List<TrainingDTO> findAllTrainings() {
        return trainingDao.findAll().stream().map(trainingMapper::trainingToTrainingDTO).toList();
    }
}
