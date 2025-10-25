package com.glamora_store.dto.response.common.product;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VariantAttributeResponse {
    private String attributeName;
    private String attributeValue;
}
