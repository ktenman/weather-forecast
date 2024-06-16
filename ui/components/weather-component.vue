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
                type="text"
                placeholder="Search for location..."
            />
            <button class="btn btn-primary" type="submit">Search</button>
          </div>
        </form>
        <div v-if="weatherForecast">
          <div v-for="(forecastList, location) in weatherForecast" :key="location" id="results">
            <h5 class="location">{{ location }}</h5>
            <ul>
              <li v-for="forecast in forecastList" :key="forecast.date">
                {{ forecast.date }}: {{ forecast.temperatureMin }}-{{ forecast.temperatureMax }}&deg;C
              </li>
            </ul>
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
