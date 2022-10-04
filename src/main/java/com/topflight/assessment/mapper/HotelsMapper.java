package com.topflight.assessment.mapper;

import com.topflight.assessment.model.Hotel;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelsMapper implements RowMapper<Hotel> {
    @Override
    public Hotel mapRow(ResultSet rs, int rowNum) throws SQLException {
        Hotel hotel = new Hotel();
        hotel.setId(rs.getInt("ID"));
        hotel.setName(rs.getString("NAME"));
        hotel.setStars(rs.getInt("STARS"));
        hotel.setCity(rs.getString("CITY"));
        hotel.setAddress(rs.getString("ADDRESS"));
        return hotel;
    }
}