package com.bin.bin_fresh_recruit_backend.model.request.fresh;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 投递简历请求
 *
 * @Author: hongxiaobin
 * @Time: 2023/12/2 16:52
 */
@Data
public class ResumeSendRequest implements Serializable {
    private static final long serialVersionUID = 444219453644207092L;

    @JsonProperty("com_id")
    private String comId;

    @JsonProperty("job_id")
    private String jobId;

    @JsonProperty("resume_id")
    private String resumeId;
}
