package com.topflight.assessment.mapper;

import com.topflight.assessment.model.RoomType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomTypesMapper implements RowMapper<RoomType> {
    @Override
    public RoomType mapRow(ResultSet rs, int rowNum) throws SQLException {
        RoomType room = new RoomType();
        room.setTypeName(rs.getString("TYPENAME"));
        room.setPricePerPerson(rs.getInt("PRICEPERPERSON"));
        room.setMaxPeople(rs.getInt("MAXPEOPLE"));
        room.setNumRoomsAvailable(rs.getInt("NUMROOMSAVAILABLE"));
        return room;
    }
}
