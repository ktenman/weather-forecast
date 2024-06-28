-- Add the provider_name column with a default value
ALTER TABLE weather_forecast
    ADD COLUMN provider_name VARCHAR(255) NOT NULL DEFAULT 'ILMATEENISTUS';

-- Update existing rows to set the provider_name to 'ILMATEENISTUS'
UPDATE weather_forecast
SET provider_name = 'ILMATEENISTUS'
WHERE provider_name IS NULL;

-- Remove the default value from the provider_name column
ALTER TABLE weather_forecast
    ALTER COLUMN provider_name DROP DEFAULT;
