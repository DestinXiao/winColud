package com.example.wincloud.pojo;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class File {

    private Integer id;
    private String fileName;
    private Long fileSize;
    private Integer parentId;
    private Integer fileTypeId;
    private List<File> childFiles;
}
