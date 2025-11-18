package com.glamora_store.service.impl;

import com.glamora_store.dto.response.AttributeResponse;
import com.glamora_store.entity.Attribute;
import com.glamora_store.repository.AttributeRepository;
import com.glamora_store.service.AttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeServiceImpl implements AttributeService {

    private final AttributeRepository attributeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AttributeResponse> getAllAttributes() {
        List<Attribute> attributes = attributeRepository.findAllWithValues();

        return attributes.stream()
                .map(
                        attr -> AttributeResponse.builder()
                                .id(attr.getId())
                                .name(attr.getName())
                                .values(
                                        attr.getValues().stream()
                                                .map(
                                                        val -> AttributeResponse.AttributeValueResponse.builder()
                                                                .id(val.getId())
                                                                .value(val.getValue())
                                                                .build())
                                                .sorted(
                                                        (a, b) -> a.getValue().compareToIgnoreCase(b.getValue()))
                                                .collect(Collectors.toList()))
                                .build())
                .collect(Collectors.toList());
    }
}
