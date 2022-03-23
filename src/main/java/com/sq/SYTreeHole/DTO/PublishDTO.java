package com.sq.SYTreeHole.DTO;

import com.sq.SYTreeHole.entity.Publish;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PublishDTO {
    private Publish publish;
    private List<String> imagesUrl;
}
