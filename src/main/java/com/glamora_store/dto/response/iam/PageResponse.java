package com.glamora_store.dto.response.iam;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
  private List<T> content;
  private int pageNumber;
  private int totalPages;
  private int pageSize;
  private int numberOfElements;
  private long totalElements;
  private boolean first;
  private boolean last;

  public static <T> PageResponse<T> from(Page<T> page) {
    return new PageResponse<>(
      page.getContent(),
      page.getNumber(),
      page.getTotalPages(),
      page.getSize(),
      page.getNumberOfElements(),
      page.getTotalElements(),
      page.isFirst(),
      page.isLast());
  }
}
