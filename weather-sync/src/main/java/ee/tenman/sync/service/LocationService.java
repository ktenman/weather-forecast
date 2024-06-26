package ee.tenman.sync.service;

import ee.tenman.common.domain.Location;
import ee.tenman.common.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
	
	private final LocationRepository locationRepository;
	
	public Location getOrCreateLocation(String locationName) {
		return locationRepository.findByName(locationName)
				.orElseGet(() -> {
					Location newLocation = new Location();
					newLocation.setName(locationName);
					return locationRepository.saveAndFlush(newLocation);
				});
	}
}
