package com.webSpringProje.Swiftwheels.Services;

import com.webSpringProje.Swiftwheels.DTO.JeepDTO;
import com.webSpringProje.Swiftwheels.Entity.Jeep;
import com.webSpringProje.Swiftwheels.Mapper.JeepMapper;
import com.webSpringProje.Swiftwheels.Repos.JeepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class JeepServices {

    @Autowired
    private JeepRepository jeepRepository;

    @Autowired
    private JeepMapper jeepMapper;

    public void saveJeep(JeepDTO dto) throws IOException {
        Jeep jeep = jeepMapper.toEntity(dto);
        jeepRepository.save(jeep);
    }

    public List<Jeep> getAllJeeps() {
        return jeepRepository.findAll();
    }
}
