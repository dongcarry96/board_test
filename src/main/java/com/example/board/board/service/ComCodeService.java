package com.example.board.board.service;

import com.example.board.board.domain.ComCode;
import com.example.board.board.repository.ComCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComCodeService {
    private final ComCodeRepository comCodeRepository;

    public List<ComCode> getPhoneCodes() {
        return comCodeRepository.findByIdCodeType("PHONE");
    }

    public List<ComCode> getBoardTypeCodes() {
        return comCodeRepository.findByIdCodeType("MENU");
    }
}
