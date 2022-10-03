package com.topflight.assessment.mapper;

import com.topflight.assessment.model.Hotel;
import com.topflight.assessment.model.RoomType;
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
                hotel.setRoomTypes(addRoomTypes(rs, null));
                hotelMap.put(id, hotel);
            } else {
                hotel.setRoomTypes(addRoomTypes(rs, hotel));
            }

        }

        return new ArrayList<>(hotelMap.values());
    }

    private List<RoomType> addRoomTypes(ResultSet rs, Hotel hotel) throws SQLException, DataAccessException {
        List<RoomType> roomTypes = new ArrayList<>();
        RoomType roomType = new RoomType();

        if (hotel != null) {
            roomTypes = hotel.getRoomTypes();
        }

        roomType.setTypeName(rs.getString("TYPENAME"));
        roomType.setPricePerPerson(rs.getInt("PRICEPERPERSON"));
        roomType.setMaxPeople(rs.getInt("MAXPEOPLE"));
        roomType.setNumRoomsAvailable(rs.getInt("NUMROOMSAVAILABLE"));
        roomTypes.add(roomType);
        return roomTypes;
    }
}
