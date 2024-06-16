CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE weather_forecast
(
    id         BIGSERIAL PRIMARY KEY,
    date       DATE         NOT NULL,
    location   VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (date, location)
);

CREATE TABLE weather_forecast_details
(
    id              BIGSERIAL PRIMARY KEY,
    forecast_id     BIGINT      NOT NULL REFERENCES weather_forecast (id),
    temperature_min DECIMAL(5, 2),
    temperature_max DECIMAL(5, 2),
    phenomenon      VARCHAR(255),
    forecast_type   VARCHAR(10) NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (forecast_id, forecast_type)
);

CREATE INDEX idx_weather_forecast_location_trgm ON weather_forecast USING gin (location gin_trgm_ops);
