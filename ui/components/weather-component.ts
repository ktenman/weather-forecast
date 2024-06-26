import {computed, defineComponent, ref} from 'vue'
import {ApiError} from '../models/api-error'
import {WeatherForecastResponse} from '../models/weather-forecast'
import {WeatherService} from '../services/weather-service'
import {AlertType, getAlertBootstrapClass} from '../models/alert-type'

export default defineComponent({
    setup() {
        const alertMessage = ref('')
        const alertType = ref<AlertType | null>(null)
        const apiService = new WeatherService()
        const location = ref('')
        const weatherForecast = ref<WeatherForecastResponse | null>(null)

        const fetchWeatherForecast = async () => {
            try {
                weatherForecast.value = await apiService.getWeatherForecast(location.value)
            } catch (error) {
                handleApiError('Failed to load weather forecast. Please try again.', error)
            }
        }

        const alertClass = computed(() => {
            return getAlertBootstrapClass(alertType.value)
        })

        const displayAlert = computed(() => {
            return alertType.value !== null && alertMessage.value !== ''
        })

        const handleApiError = (defaultMessage: string, error: any) => {
            alertType.value = AlertType.ERROR
            if (error instanceof ApiError) {
                alertMessage.value = `${error.message}. ${error.debugMessage}: ${Object.entries(error.validationErrors)
                    .map(([, message]) => message)
                    .join(', ')}`
            } else {
                alertMessage.value = defaultMessage
            }
        }

        return {
            alertMessage,
            alertType,
            location,
            weatherForecast,
            fetchWeatherForecast,
            alertClass,
            displayAlert
        }
    }
})
