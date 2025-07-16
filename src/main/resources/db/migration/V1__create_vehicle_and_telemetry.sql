-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Enable PostGIS extension
CREATE EXTENSION IF NOT EXISTS postgis;

CREATE TABLE vehicle
(
    id            UUID PRIMARY KEY,
    serial_number VARCHAR(255) NOT NULL UNIQUE,
    vehicle_type  VARCHAR(255) NOT NULL
);

-- create the single table for all vehicle telemetry
CREATE TABLE vehicle_telemetry
(
    -- discriminator Column
    vehicle_type                        VARCHAR(31)                 NOT NULL,

    -- common VehicleTelemetry
    id                                  UUID PRIMARY KEY,
    vehicle_id                          UUID                        NOT NULL,
    date_time                           TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    longitude                           DOUBLE PRECISION            NOT NULL,
    latitude                            DOUBLE PRECISION            NOT NULL,
    location                            GEOMETRY(Point, 4326),
    total_working_hours                 DOUBLE PRECISION,
    engine_speed                        INTEGER,
    engine_load                         INTEGER,
    ground_speed                        DOUBLE PRECISION,
    import_failed                       BOOLEAN                     NOT NULL DEFAULT FALSE,
    raw_csv_data                        JSONB,

    -- TractorTelemetry
    fuel_consumption                    DOUBLE PRECISION,
    ground_speed_radar                  DOUBLE PRECISION,
    coolant_temperature                 DOUBLE PRECISION,
    speed_front_pto                     INTEGER,
    speed_rear_pto                      INTEGER,
    current_gear_shift                  INTEGER,
    ambient_temperature                 DOUBLE PRECISION,
    parking_brake_status                VARCHAR(255),
    transverse_differential_lock_status VARCHAR(255),
    all_wheel_drive_status              VARCHAR(255),
    creeper_status                      VARCHAR(255),

    -- CombineTelemetry
    drum_speed                          INTEGER,
    fan_speed                           INTEGER,
    rotor_straw_walker_speed            INTEGER,
    separation_losses                   DOUBLE PRECISION,
    sieve_losses                        DOUBLE PRECISION,
    chopper_status                      VARCHAR(255),
    diesel_tank_level                   DOUBLE PRECISION,
    number_of_partial_widths            INTEGER,
    front_attachment_status             VARCHAR(255),
    max_number_of_partial_widths        INTEGER,
    feed_rake_speed                     INTEGER,
    working_position                    VARCHAR(255),
    grain_tank_unloading                VARCHAR(255),
    main_drive_status                   VARCHAR(255),
    concave_position                    INTEGER,
    upper_sieve_position                INTEGER,
    lower_sieve_position                INTEGER,
    grain_tank_70                       VARCHAR(255),
    grain_tank_100                      VARCHAR(255),
    grain_moisture_content              DOUBLE PRECISION,
    throughput                          DOUBLE PRECISION,
    radial_spreader_speed               INTEGER,
    grain_in_returns                    DOUBLE PRECISION,
    channel_position                    DOUBLE PRECISION,
    yield_measurement_status            VARCHAR(255),
    returns_auger_measurement           DOUBLE PRECISION,
    moisture_measurement_status         VARCHAR(255),
    type_of_crop                        VARCHAR(255),
    specific_crop_weight                INTEGER,
    auto_pilot_status                   VARCHAR(255),
    cruise_pilot_status                 VARCHAR(255),
    rate_of_work                        DOUBLE PRECISION,
    yield_value                         DOUBLE PRECISION,
    quantimeter_calibration_factor      DOUBLE PRECISION,
    separation_sensitivity              INTEGER,
    sieve_sensitivity                   INTEGER,

    -- FK
    CONSTRAINT fk_vehicle
        FOREIGN KEY (vehicle_id)
            REFERENCES vehicle (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,

    -- prevent duplicate telemetry
    CONSTRAINT uk_vehicle_telemetry_vehicle_datetime
        UNIQUE (vehicle_id, date_time)

);

-- indexes
CREATE INDEX idx_vehicle_type ON vehicle (vehicle_type);
CREATE INDEX idx_vehicle_telemetry_vehicle_id ON vehicle_telemetry (vehicle_id);
CREATE INDEX idx_vehicle_telemetry_date_time ON vehicle_telemetry (date_time);
CREATE INDEX idx_vehicle_telemetry_location ON vehicle_telemetry USING GIST (location);
