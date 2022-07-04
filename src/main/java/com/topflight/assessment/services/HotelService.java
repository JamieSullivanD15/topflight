package com.topflight.assessment.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@RestController
@EnableWebMvc
public class HotelService {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private Logger logger = LoggerFactory.getLogger(HotelService.class);

    @RequestMapping(path = "/hotel/{id}", method = RequestMethod.GET)
    public String ping(@PathVariable("id") String id) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
        String value = namedParameterJdbcTemplate.queryForObject(
                "select name from hotels where id = :id", namedParameters, String.class);
        return "Hotel Name: " + value;
    }
}


