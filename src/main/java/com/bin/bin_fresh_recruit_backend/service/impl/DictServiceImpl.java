package com.bin.bin_fresh_recruit_backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bin.bin_fresh_recruit_backend.mapper.DictMapper;
import com.bin.bin_fresh_recruit_backend.model.domain.Dict;
import com.bin.bin_fresh_recruit_backend.service.DictService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hongxiaobin
 * @description 针对表【t_dict(数据字典)】的数据库操作Service实现
 * @createDate 2023-11-04 15:34:12
 */
@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict>
        implements DictService {

    /**
     * 根据字典类型查询字典列表
     *
     * @param dictType 字典类型
     * @return 响应
     */
    @Override
    public List<String> getDictList(Integer dictType) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_type", dictType);
        List<Dict> dicts = this.list(dictQueryWrapper);
        List<String> result = new ArrayList<>();
        if (dicts == null || dicts.size() == 0) {
            return result;
        }
        for (Dict dict : dicts) {
            result.add(dict.getDictContent());
        }
        return result;
    }
}




