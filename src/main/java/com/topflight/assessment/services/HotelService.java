package com.topflight.assessment.services;

import com.topflight.assessment.mapper.CitiesMapper;
import com.topflight.assessment.mapper.HotelExtractor;
import com.topflight.assessment.mapper.RoomTypesMapper;
import com.topflight.assessment.model.Hotel;
import com.topflight.assessment.model.RoomType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Iterator;
import java.util.List;

@RestController
@EnableWebMvc
@RequestMapping(path = "/hotels")
public class HotelService {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private Logger logger = LoggerFactory.getLogger(HotelService.class);

    @GetMapping(path = "/{city}/{minPrice}/{maxPrice}/{numPeople}")
    public ResponseEntity<List<Hotel>> getHotels(
            @PathVariable("city") String city,
            @PathVariable("minPrice") int minPrice,
            @PathVariable("maxPrice") int maxPrice,
            @PathVariable("numPeople") int numPeople
    ) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("city", city);
        List<Hotel> hotels = namedParameterJdbcTemplate.query(
                "SELECT h.*, p.url FROM hotels h "
                        + "LEFT OUTER JOIN photos p ON p.hotelId = h.id "
                        + "WHERE h.city = :city ORDER BY h.name ASC",
                namedParameters,
                new HotelExtractor()
        );

        if (hotels != null) {
            Iterator<Hotel> i = hotels.iterator();
            while (i.hasNext()) {
                Hotel hotel = i.next();
                List<RoomType> rooms = getRoomTypes(hotel.getId(), minPrice, maxPrice, numPeople);
                if (rooms.size() == 0) {
                    i.remove();
                } else {
                    hotel.setRoomTypes(rooms);
                }
            }
        }

        return ResponseEntity.ok(hotels);
    }

    @GetMapping(path = "/cities")
    public ResponseEntity<List<String>> getCities() {
        List<String> cities = namedParameterJdbcTemplate.query(
                "SELECT DISTINCT city FROM hotels ORDER BY city",
                new CitiesMapper()
        );

        return ResponseEntity.ok(cities);
    }

    @GetMapping(path = "/maxPrice")
    public ResponseEntity<Long> getMaxPrice() {
        Long price = namedParameterJdbcTemplate.queryForObject(
                "SELECT MAX(pricePerPerson) FROM roomtypes",
                new MapSqlParameterSource(),
                Long.class
        );

        return ResponseEntity.ok(price);
    }

    private List<RoomType> getRoomTypes(int hotelId , int minPrice, int maxPrice, int numPeople) {
        SqlParameterSource namedParameters = new MapSqlParameterSource()
                .addValue("hotelId", hotelId)
                .addValue("minPrice", minPrice)
                .addValue("maxPrice", maxPrice)
                .addValue("numPeople", numPeople);

        return namedParameterJdbcTemplate.query(
                "SELECT typeName, pricePerPerson, maxPeople, numRoomsAvailable FROM roomtypes WHERE hotelId = :hotelId "
                    + "AND pricePerPerson BETWEEN :minPrice AND :maxPrice AND maxPeople <= :numPeople",
                namedParameters,
                new RoomTypesMapper()
        );
    }
}


