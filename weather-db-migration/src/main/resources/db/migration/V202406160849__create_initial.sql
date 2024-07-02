CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE location
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
        UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE weather_forecast
(
    id            BIGSERIAL PRIMARY KEY,
    date          DATE                                               NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    location_id   BIGINT
        REFERENCES location,
    provider_name VARCHAR(255)                                       NOT NULL,
    UNIQUE (date, location_id)
);

CREATE TABLE weather_forecast_details
(
    id              BIGSERIAL PRIMARY KEY,
    forecast_id     BIGINT                                             NOT NULL
        REFERENCES weather_forecast,
    temperature_min NUMERIC(5, 2),
    temperature_max NUMERIC(5, 2),
    phenomenon      VARCHAR(255),
    forecast_type   VARCHAR(10)                                        NOT NULL,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UNIQUE (forecast_id, forecast_type)
);

CREATE INDEX idx_location_name_trgm
    ON location USING gin (name gin_trgm_ops);

