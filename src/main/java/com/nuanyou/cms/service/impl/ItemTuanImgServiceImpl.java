package com.nuanyou.cms.service.impl;

import com.nuanyou.cms.dao.ItemTuanImgDao;
import com.nuanyou.cms.entity.ItemTuanImg;
import com.nuanyou.cms.service.ItemTuanImgService;
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
        List<ItemTuanImg> itemTuanImgUrls = getItemTuanImgUrls(itemId);
        if (itemTuanImgUrls.size() >= 9) {
            return null;
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
        return itemTuanImgDao.findByItemId(itemId);
    }

}
