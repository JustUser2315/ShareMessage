package com.example.sweater_letscode.spring.service;

import com.example.sweater_letscode.spring.dto.RoleReadDto;
import com.example.sweater_letscode.spring.mapper.RoleEntityToReadMapper;
import com.example.sweater_letscode.spring.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleEntityToReadMapper roleEntityToReadMapper;

    public List<RoleReadDto> findAll(){
       return roleRepository.findAll()
                .stream().map(roleEntityToReadMapper::map)
                .toList();
    }

}
