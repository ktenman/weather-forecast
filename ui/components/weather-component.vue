<template>
  <div class="container mt-3">
    <div class="row mt-4">
      <div class="col-md-5">
        <h3>Weather Forecast</h3>
        <form @submit.prevent="fetchWeatherForecast">
          <div class="input-group mb-3">
            <input
                id="location"
                v-model="location"
                class="form-control"
                placeholder="Search for location..."
                type="text"
            />
            <button class="btn btn-primary" type="submit">Search</button>
          </div>
        </form>
        <div v-if="weatherForecast && Object.keys(weatherForecast).length > 0">
          <div v-for="(forecastList, location) in weatherForecast" id="results" :key="location">
            <h5 class="location">{{ location }}</h5>
            <ul>
              <li v-for="forecast in forecastList" :key="forecast.date">
                {{ forecast.date }}: {{ forecast.temperatureMin }}-{{ forecast.temperatureMax }}&deg;C
              </li>
            </ul>
          </div>
        </div>
        <div v-else-if="weatherForecast && Object.keys(weatherForecast).length === 0" class="mt-3">
          <div class="alert alert-info" role="alert">
            No weather forecast data found for "{{ location }}"
          </div>
        </div>
        <div v-if="displayAlert()" class="mt-3">
          <div :class="['alert', alertClass()]" role="alert">
            {{ alertMessage }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./weather-component.ts"></script>
