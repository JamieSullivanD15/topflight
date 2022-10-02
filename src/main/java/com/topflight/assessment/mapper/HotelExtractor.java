package com.topflight.assessment.mapper;

import com.topflight.assessment.model.Hotel;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class HotelExtractor implements ResultSetExtractor<List<Hotel>> {
    @Override
    public List<Hotel> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Hotel> hotelMap = new LinkedHashMap<>();

        while (rs.next()) {
            Long id = rs.getLong("ID");
            Hotel hotel = hotelMap.get(id);

            if (hotel == null) {
                hotel = new Hotel();
                hotel.setId(id.intValue());
                hotel.setName(rs.getString("NAME"));
                hotel.setStars(rs.getInt("STARS"));
                hotel.setCity(rs.getString("CITY"));
                hotel.setAddress(rs.getString("ADDRESS"));
                List<String> photos = new ArrayList<>();
                photos.add(rs.getString("URL"));
                hotel.setPhotos(photos);
                hotelMap.put(id, hotel);
            } else {
                List<String> photos = hotel.getPhotos();
                photos.add(rs.getString("URL"));
                hotel.setPhotos(photos);
            }

        }

        return new ArrayList<>(hotelMap.values());
    }
}
