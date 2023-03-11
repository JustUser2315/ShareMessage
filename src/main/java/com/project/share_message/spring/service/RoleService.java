package com.project.share_message.spring.service;

import com.project.share_message.spring.dto.RoleReadDto;
import com.project.share_message.spring.mapper.RoleEntityToReadMapper;
import com.project.share_message.spring.repository.RoleRepository;
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
