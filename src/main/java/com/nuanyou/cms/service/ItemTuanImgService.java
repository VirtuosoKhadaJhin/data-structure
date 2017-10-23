package com.nuanyou.cms.service;

import com.nuanyou.cms.entity.ItemTuanImg;

import java.util.List;

/**
 * Created by mylon.sun on 2017/10/23.
 */
public interface ItemTuanImgService {

    void deleteImgUrl(Long itemId);

    ItemTuanImg saveImgUrl(Long itemId, String imgUrl);

    List<ItemTuanImg> getItemTuanImgUrls(Long itemId);

}
