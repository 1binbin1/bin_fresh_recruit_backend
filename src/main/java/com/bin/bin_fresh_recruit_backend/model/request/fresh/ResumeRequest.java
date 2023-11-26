package com.bin.bin_fresh_recruit_backend.model.request.fresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @Author: hongxiaobin
 * @Time: 2023/11/26 17:34
 */
@Data
public class ResumeRequest implements Serializable {
    private static final long serialVersionUID = -4229243524113912917L;

    /**
     * 简历
     */
    @JsonProperty("resume_id")
    String resumeId;
}
