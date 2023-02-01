package com.example.sweater_letscode.spring.service;

import com.example.sweater_letscode.spring.dto.MessageEditDto;
import com.example.sweater_letscode.spring.dto.MessageReadDto;
import com.example.sweater_letscode.spring.entity.QMessage;
import com.example.sweater_letscode.spring.filter.MessageFilter;
import com.example.sweater_letscode.spring.mapper.MessageEditToEntityMapper;
import com.example.sweater_letscode.spring.mapper.MessageEntityToReadMapper;
import com.example.sweater_letscode.spring.querydsl.QPredicates;
import com.example.sweater_letscode.spring.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    private final MessageEditToEntityMapper messageEditToEntityMapper;
    private final  MessageEntityToReadMapper messageEntityToReadMapper;
    private final  MessageRepository messageRepository;
    public Page<MessageReadDto> findAllWithFilter(MessageFilter messageFilter, Pageable pageable){
        var predicate = QPredicates.builder()
                .add(messageFilter.getText(), QMessage.message.text::containsIgnoreCase)
                .add(messageFilter.getTag(), QMessage.message.tag::containsIgnoreCase)
                .build();

        if(predicate!=null){
            return messageRepository.findAll(predicate,pageable)
                    .map(messageEntityToReadMapper::map);
        }
        return messageRepository.findAll(pageable)
                .map(messageEntityToReadMapper::map);
    }
   @Transactional
    public MessageReadDto save(MessageEditDto messageEditDto){
       var savedMessage = messageRepository.save(messageEditToEntityMapper.map(messageEditDto));
       return messageEntityToReadMapper.map(savedMessage);
    }

    public List<MessageReadDto> findAllByAuthorId(Long id) {
        return messageRepository.findAllByAuthorId(id)
                .stream().map(messageEntityToReadMapper::map)
                .toList();
    }

    @Transactional
    public boolean delete(Integer id) {
        messageRepository.deleteById(id);
        messageRepository.flush();
        return messageRepository.findById(id).isEmpty();
    }
}
