package com.topflight.assessment.services;

import com.topflight.assessment.mapper.*;
import com.topflight.assessment.model.Hotel;
import com.topflight.assessment.model.PaginatedHotels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

@RestController
@EnableWebMvc
@RequestMapping(path = "/hotels")
@CrossOrigin(origins = "http://localhost:3000")
public class HotelService {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private Logger logger = LoggerFactory.getLogger(HotelService.class);

    @GetMapping(path = "")
    public ResponseEntity<PaginatedHotels> getHotels(
            @RequestParam(value = "city") String city,
            @RequestParam(value = "minPrice") int minPrice,
            @RequestParam(value = "maxPrice") int maxPrice,
            @RequestParam(value = "numPeople") int numPeople,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "3") int pageSize
    ) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("city", city)
                .addValue("minPrice", minPrice)
                .addValue("maxPrice", maxPrice)
                .addValue("numPeople", numPeople);

        List<Hotel> hotels = namedParameterJdbcTemplate.query(
                "SELECT h.*, r.typeName, r.pricePerPerson, r.maxPeople, r.numRoomsAvailable "
                        + "FROM hotels h LEFT OUTER JOIN roomTypes r ON r.hotelId = h.id "
                        + "WHERE h.city = :city "
                        + "AND r.pricePerPerson BETWEEN :minPrice AND :maxPrice AND r.maxPeople <= :numPeople "
                        + "ORDER BY h.name",
                params,
                new HotelExtractor()
        );

        List<String> totalResults = namedParameterJdbcTemplate.query(
                "SELECT DISTINCT h.id FROM hotels h "
                        + "LEFT OUTER JOIN roomTypes r ON r.hotelId = h.id "
                        + "WHERE h.city = :city "
                        + "AND r.pricePerPerson BETWEEN :minPrice AND :maxPrice AND r.maxPeople <= :numPeople",
                params,
                new TotalResultsMapper()
        );


        PaginatedHotels paginatedHotels = new PaginatedHotels();

        if (hotels != null && hotels.size() > 0) {
            for (Hotel hotel : hotels) {
                hotel.setPhotos(getPhotos(hotel.getId()));
            }

            int startIndex = (page - 1) * pageSize;
            int endIndex = Math.min(startIndex + pageSize, hotels.size());

            paginatedHotels.setHotels(hotels.subList(startIndex, endIndex));
            paginatedHotels.setTotalResults(totalResults.size());
        }

        return ResponseEntity.ok(paginatedHotels);
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

    private List<String> getPhotos(int hotelId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("hotelId", hotelId);
        return namedParameterJdbcTemplate.query(
                "SELECT url FROM photos WHERE hotelId = :hotelId",
                namedParameters,
                new PhotosMapper()
        );
    }
}


