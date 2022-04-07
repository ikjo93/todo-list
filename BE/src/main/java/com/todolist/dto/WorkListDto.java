package com.todolist.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WorkListDto {

    private String categoryName;
    private List<WorkDto> works;
}
