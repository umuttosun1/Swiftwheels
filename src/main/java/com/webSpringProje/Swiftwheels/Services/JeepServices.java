package com.webSpringProje.Swiftwheels.Services;

import com.webSpringProje.Swiftwheels.DTO.JeepDTO;
import com.webSpringProje.Swiftwheels.Entity.Jeep;
import com.webSpringProje.Swiftwheels.Mapper.JeepMapper;
import com.webSpringProje.Swiftwheels.Repos.JeepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

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

    // JeepServices.java içine ekle

    public Jeep getJeepById(Long id) {
        return jeepRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Jeep bulunamadı: ID " + id));
    }

}
