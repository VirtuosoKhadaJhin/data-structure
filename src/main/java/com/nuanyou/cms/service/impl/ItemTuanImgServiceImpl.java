package com.nuanyou.cms.service.impl;

import com.google.common.collect.Lists;
import com.nuanyou.cms.dao.ItemTuanImgDao;
import com.nuanyou.cms.entity.ItemDetailimg;
import com.nuanyou.cms.entity.ItemTuanImg;
import com.nuanyou.cms.service.ItemTuanImgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by mylon.sun on 2017/10/23.
 */
@Service
public class ItemTuanImgServiceImpl implements ItemTuanImgService {

    @Autowired
    private ItemTuanImgDao itemTuanImgDao;

    @Override
    public void deleteImgUrl(Long itemId) {
        itemTuanImgDao.delete(itemId);
    }

    @Override
    public ItemTuanImg saveImgUrl(Long itemId, String imgUrl) {
        if(itemId != null){
            List<ItemTuanImg> itemTuanImgUrls = getItemTuanImgUrls(itemId);
            if (itemTuanImgUrls.size() >= 9) {
                return null;
            }
        }

        ItemTuanImg itemTuanImg = new ItemTuanImg();
        itemTuanImg.setImgUrl(imgUrl);
        itemTuanImg.setItemId(itemId);
        itemTuanImg.setCreateTime(new Date());
        itemTuanImg.setUpdateTime(new Date());

        ItemTuanImg result = itemTuanImgDao.save(itemTuanImg);
        return result;
    }

    @Override
    public List<ItemTuanImg> getItemTuanImgUrls(Long itemId) {
        if (itemId == null) {
            return Lists.newArrayList();
        }
        return itemTuanImgDao.findByItemId(itemId);
    }

    @Override
    public void setNewItemTuanImgs(Long id, List<String> detailimgs) {
        List<ItemTuanImg> itemTuanImgs = itemTuanImgDao.findByImgUrlIn(detailimgs);

        for (ItemTuanImg itemTuanImg : itemTuanImgs) {
            itemTuanImg.setItemId(id);
        }
        itemTuanImgDao.save(itemTuanImgs);
    }

}
