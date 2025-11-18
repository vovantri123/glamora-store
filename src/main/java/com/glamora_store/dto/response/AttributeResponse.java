package com.glamora_store.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttributeResponse {
    private Long id;
    private String name;
    private List<AttributeValueResponse> values;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AttributeValueResponse {
        private Long id;
        private String value;
    }
}
