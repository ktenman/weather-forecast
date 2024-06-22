CREATE TABLE IF NOT EXISTS location
(
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (name)
);

-- Create a GIN index on the location name for faster text searches
CREATE INDEX IF NOT EXISTS idx_location_name_trgm ON location USING gin (name gin_trgm_ops);

-- Drop the weather_forecast_date_location_key constraint if it exists
ALTER TABLE IF EXISTS weather_forecast
    DROP CONSTRAINT IF EXISTS weather_forecast_date_location_key;

-- Drop the weather_forecast_pkey constraint and its dependent objects
ALTER TABLE IF EXISTS weather_forecast
    DROP CONSTRAINT IF EXISTS weather_forecast_pkey CASCADE;

-- Modify the weather_forecast table to reference the location table
ALTER TABLE weather_forecast
    DROP COLUMN IF EXISTS location,
    ADD COLUMN IF NOT EXISTS location_id BIGINT REFERENCES location (id);

-- Add the new unique constraint on weather_forecast
ALTER TABLE weather_forecast
    ADD CONSTRAINT weather_forecast_date_location_id_key UNIQUE (date, location_id);

-- Add the primary key constraint back to the weather_forecast table
ALTER TABLE weather_forecast
    ADD CONSTRAINT weather_forecast_pkey PRIMARY KEY (id);

-- Add the foreign key constraint back to the weather_forecast_details table
ALTER TABLE weather_forecast_details
    ADD CONSTRAINT weather_forecast_details_forecast_id_fkey
        FOREIGN KEY (forecast_id) REFERENCES weather_forecast (id);
