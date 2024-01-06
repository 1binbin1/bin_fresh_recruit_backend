package com.bin.bin_fresh_recruit_backend.service;

import com.bin.bin_fresh_recruit_backend.model.domain.Dict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author hongxiaobin
* @description 针对表【t_dict(数据字典)】的数据库操作Service
* @createDate 2023-11-04 15:34:12
*/
public interface DictService extends IService<Dict> {

    /**
     * 获取字典列表
     * @param dictType 字典类型
     * @return 响应
     */
    List<String> getDictList(Integer dictType);
}
