-- Insert training types
INSERT INTO "TRAINING_TYPES" ("TRAINING_TYPE_NAME") VALUES ('fitness'),('yoga'),('Zumba'),('stretching'),('resistance');

-- Insert users (trainers and trainees)
INSERT INTO "USERS" ("FIRST_NAME", "LAST_NAME", "USERNAME", "PASSWORD", "IS_ACTIVE") VALUES('Carlos', 'Gomez', 'carlos.gomez', 'password123', TRUE),('Ana', 'Martinez', 'ana.martinez', 'password123', TRUE),('Luis', 'Perez', 'luis.perez', 'password123', TRUE),('Maria', 'Rodriguez', 'maria.rodriguez', 'password123', TRUE),('Juan', 'Lopez', 'juan.lopez', 'password123', TRUE),('Sofia', 'Garcia', 'sofia.garcia', 'password123', TRUE);

-- Insert trainers
INSERT INTO "TRAINERS" ("USER_ID", "SPECIALIZATION_ID") VALUES ((SELECT "ID" FROM "USERS" WHERE "USERNAME" = 'carlos.gomez'), (SELECT "ID" FROM "TRAINING_TYPES" WHERE "TRAINING_TYPE_NAME" = 'fitness')), ((SELECT "ID" FROM "USERS" WHERE "USERNAME" = 'ana.martinez'), (SELECT "ID" FROM "TRAINING_TYPES" WHERE "TRAINING_TYPE_NAME" = 'yoga')), ((SELECT "ID" FROM "USERS" WHERE "USERNAME" = 'luis.perez'), (SELECT "ID" FROM "TRAINING_TYPES" WHERE "TRAINING_TYPE_NAME" = 'Zumba'));

-- Insert trainees
INSERT INTO "TRAINEES" ("USER_ID", "DATE_OF_BIRTH", "ADDRESS") VALUES ((SELECT "ID" FROM "USERS" WHERE "USERNAME" = 'maria.rodriguez'), '1990-01-01', 'Calle 123, Bogotá'), ((SELECT "ID" FROM "USERS" WHERE "USERNAME" = 'juan.lopez'), '1985-02-15', 'Carrera 45, Medellín'), ((SELECT "ID" FROM "USERS" WHERE "USERNAME" = 'sofia.garcia'), '1992-03-30', 'Avenida 7, Cali');

-- Insert trainings
INSERT INTO "TRAININGS" ("TRAINEE_ID", "TRAINER_ID", "TRAINING_TYPE_ID", "TRAINING_NAME", "TRAINING_DATE", "TRAINING_DURATION") VALUES ((SELECT "ID" FROM "TRAINEES" WHERE "USER_ID" = (SELECT "ID" FROM "USERS" WHERE "USERNAME" = 'maria.rodriguez')),(SELECT "ID" FROM "TRAINERS" WHERE "USER_ID" = (SELECT "ID" FROM "USERS" WHERE "USERNAME" = 'carlos.gomez')),(SELECT "ID" FROM "TRAINING_TYPES" WHERE "TRAINING_TYPE_NAME" = 'fitness'),'Morning Training', '2024-06-01', 60), ((SELECT "ID" FROM "TRAINEES" WHERE "USER_ID" = (SELECT "ID" FROM "USERS" WHERE "USERNAME" = 'juan.lopez')),(SELECT "ID" FROM "TRAINERS" WHERE "USER_ID" = (SELECT "ID" FROM "USERS" WHERE "USERNAME" = 'ana.martinez')),(SELECT "ID" FROM "TRAINING_TYPES" WHERE "TRAINING_TYPE_NAME" = 'yoga'),'Evening Yoga', '2024-06-02', 45), ((SELECT "ID" FROM "TRAINEES" WHERE "USER_ID" = (SELECT "ID" FROM "USERS" WHERE "USERNAME" = 'sofia.garcia')),(SELECT "ID" FROM "TRAINERS" WHERE "USER_ID" = (SELECT "ID" FROM "USERS" WHERE "USERNAME" = 'luis.perez')),(SELECT "ID" FROM "TRAINING_TYPES" WHERE "TRAINING_TYPE_NAME" = 'Zumba'),'Afternoon Zumba', '2024-06-03', 50);

-- Assign Trainees to Trainers
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (1, 1);
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (2, 2);
INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES (3, 3);