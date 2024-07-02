import axios, { AxiosError } from 'axios'
import { ApiError } from '../models/api-error'
import { WeatherForecastResponse } from '../models/weather-forecast'

export class WeatherService {
  async getWeatherForecast(location: string): Promise<WeatherForecastResponse> {
    try {
      const { data } = await axios.get<WeatherForecastResponse>('/api/weather/forecast', {
        params: { location },
      })
      return data
    } catch (error) {
      this.handleError(error as AxiosError<ApiError>)
    }
  }

  private handleError(error: AxiosError<ApiError>): never {
    if (axios.isAxiosError(error) && error.response) {
      throw new ApiError(
        error.response.status,
        error.response.data.message ?? 'Unknown error',
        error.response.data.debugMessage ?? 'An unknown error occurred. Is backend running?',
        error.response.data.validationErrors ?? {},
      )
    }
    throw error
  }
}
