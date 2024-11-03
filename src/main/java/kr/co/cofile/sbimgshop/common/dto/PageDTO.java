package kr.co.cofile.sbimgshop.common.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageDTO<T> {
    private int page;
    private int size; // limit
    private int totalPages;
    private long totalElements;
    private List<T> content;
}