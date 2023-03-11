package com.project.share_message.spring.service;

import com.project.share_message.spring.dto.MessageEditDto;
import com.project.share_message.spring.dto.MessageReadDto;
import com.project.share_message.spring.entity.Message;
import com.project.share_message.spring.entity.QMessage;
import com.project.share_message.spring.filter.MessageFilter;
import com.project.share_message.spring.mapper.MessageEditToEntityMapper;
import com.project.share_message.spring.mapper.MessageEntityToReadMapper;
import com.project.share_message.spring.querydsl.QPredicates;
import com.project.share_message.spring.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {
    private final MessageEditToEntityMapper messageEditToEntityMapper;
    private final  MessageEntityToReadMapper messageEntityToReadMapper;
    private final  MessageRepository messageRepository;
    private final ImageService imageService;
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

    public List<MessageReadDto> showLastTen(){
       return messageRepository.findTop10()
                .stream().map(messageEntityToReadMapper::map)
                .toList();
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

    public Optional<byte[]> findPicture(Integer id) throws IOException {
        Optional<Message> mbMessage = messageRepository.findById(id);
        if(!mbMessage.isPresent()) {
            return Optional.empty();
        }
        return imageService.loadMessagePicture(mbMessage.get().getPicture());
    }
@Transactional
    public Optional<MessageReadDto> update(Integer id, MessageEditDto messageEditDto){
       return messageRepository.findById(id)
                .map(message->messageEditToEntityMapper.mapForUpdate(messageEditDto,message))
                .map(messageRepository::saveAndFlush)
                .map(messageEntityToReadMapper::map);

    }

    public Optional<MessageReadDto> findById(Integer id) {
        return messageRepository.findById(id)
                .map(messageEntityToReadMapper::map);
    }

    public boolean isLiked(Long userId, Integer messageId){
        return messageRepository.isLiked(userId, messageId)>0;
    }
}
