import {Vue} from 'vue-class-component'
import {AlertType, getAlertBootstrapClass} from '../models/alert-type'
import {WeatherService} from '../services/weather-service'
import {ApiError} from '../models/api-error'
import {WeatherForecastResponse} from "../models/weather-forecast";

export default class WeatherComponent extends Vue {
    alertMessage: string = ''
    alertType: AlertType | null = null
    apiService: WeatherService = new WeatherService()
    location: string = ''
    weatherForecast: WeatherForecastResponse | null = null;

    async fetchWeatherForecast() {
        try {
            this.weatherForecast = await this.apiService.getWeatherForecast(this.location)
        } catch (error) {
            this.handleApiError('Failed to load weather forecast. Please try again.', error)
        }
    }

    alertClass(): string {
        return getAlertBootstrapClass(this.alertType)
    }

    displayAlert(): boolean {
        return this.alertType !== null && this.alertMessage !== ''
    }

    private handleApiError(defaultMessage: string, error: any) {
        this.alertType = AlertType.ERROR
        if (error instanceof ApiError) {
            this.alertMessage = `${error.message}. ${error.debugMessage}: ${Object.entries(error.validationErrors)
                .map(([, message]) => message).join(', ')}`
        } else {
            this.alertMessage = defaultMessage
        }
    }

}
