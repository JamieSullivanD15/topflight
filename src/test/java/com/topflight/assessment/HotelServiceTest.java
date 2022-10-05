package com.topflight.assessment;

import com.topflight.assessment.model.Hotel;
import com.topflight.assessment.model.PaginatedHotels;
import com.topflight.assessment.model.RoomType;
import com.topflight.assessment.services.HotelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class HotelServiceTest {

    @Mock
    private HotelService hotelService;

    @Test
    public void testGetHotels() throws Exception {
        List<Hotel> hotels = getHotels();
        PaginatedHotels paginatedHotels = new PaginatedHotels();
        paginatedHotels.setHotels(hotels);
        paginatedHotels.setTotalResults(hotels.size());

        ResponseEntity<PaginatedHotels> response = ResponseEntity.ok(paginatedHotels);

        when(hotelService.getHotels("madrid", 0, 100, 5, 1, 5)).thenReturn(response);
        ResponseEntity<PaginatedHotels> responseEntity = hotelService.getHotels("madrid", 0, 100, 5, 1, 5);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).getTotalResults()).isEqualTo(hotels.size());
        assertThat(responseEntity.getBody().getHotels().containsAll(hotels));
    }

    @Test
    public void testGetCities() throws Exception {
        String city = "madrid";
        List<String> cities = new ArrayList<>();
        ResponseEntity<List<String>> response = ResponseEntity.ok(cities);
        cities.add(city);

        when(hotelService.getCities()).thenReturn(response);
        ResponseEntity<List<String>> responseEntity = hotelService.getCities();

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).size()).isEqualTo(cities.size());
        assertThat(responseEntity.getBody().containsAll(cities));
    }

    @Test
    public void testGetMaxPrice() throws Exception {
        Long price = 100L;
        ResponseEntity<Long> response = ResponseEntity.ok(price);

        when(hotelService.getMaxPrice()).thenReturn(response);
        ResponseEntity<Long> responseEntity = hotelService.getMaxPrice();

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(Objects.requireNonNull(responseEntity.getBody()).longValue()).isEqualTo(price);
    }

    private List<Hotel> getHotels() {
        List<Hotel> hotels = new ArrayList<>();
        Hotel hotel = new Hotel();
        hotel.setId(1);
        hotel.setName("Test hotel");
        hotel.setAddress("Test address");
        hotel.setCity("Test city");
        hotel.setStars(5);
        hotel.setPhotos(getPhotos());
        hotel.setRoomTypes(getRoomTypes());
        hotels.add(hotel);
        return hotels;
    }

    private List<String> getPhotos() {
        List<String> photos = new ArrayList<>();
        photos.add("Photo 1");
        photos.add("Photo 2");
        photos.add("Photo 3");
        return photos;
    }

    private List<RoomType> getRoomTypes() {
        List<RoomType> roomTypes = new ArrayList<>();
        RoomType room = new RoomType();
        room.setTypeName("Test room");
        room.setNumRoomsAvailable(5);
        room.setMaxPeople(2);
        room.setPricePerPerson(50);
        roomTypes.add(room);
        return roomTypes;
    }
}
