package com.nuanyou.cms.controller;

import com.nuanyou.cms.commons.APIResult;
import com.nuanyou.cms.component.FileClient;
import com.nuanyou.cms.config.ImageSpec;
import com.nuanyou.cms.dao.ItemDao;
import com.nuanyou.cms.dao.ItemDetailimgDao;
import com.nuanyou.cms.entity.Item;
import com.nuanyou.cms.entity.ItemDetailimg;
import com.nuanyou.cms.service.ItemDetailimgService;
import com.nuanyou.cms.util.BeanUtils;
import com.nuanyou.cms.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;


@Controller
@RequestMapping("itemDetailimg")
public class ItemDetailimgController {

    @Autowired
    private ItemDetailimgDao itemDetailimgDao;

    @Autowired
    private ItemDao itemDao;

    @Autowired
    private ItemDetailimgService itemDetailimgService;

    @Autowired
    @Qualifier("s3")
    private FileClient fileClient;

    @RequestMapping(path = "upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile[] files, @RequestParam("id") Long id) {
        for (MultipartFile file : files) {
            try {
                ImageUtils.File imgFile = ImageUtils.process(file.getInputStream(), ImageSpec.MerchantDetail);
                String fileType = imgFile.getFileType();
                InputStream is = new ByteArrayInputStream(imgFile.getData());
                String url = fileClient.uploadFile(is, fileType);

                ItemDetailimg entity = new ItemDetailimg();
                entity.setReferId(id);
                entity.setImgUrl(url);
                itemDetailimgService.saveNotNull(entity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:edit?referId=" + id;
    }

    @RequestMapping(path = "add", method = RequestMethod.POST)
    @ResponseBody
    public APIResult add(ItemDetailimg entity) {
        itemDetailimgService.saveNotNull(entity);
        return new APIResult();
    }

    @RequestMapping(path = "remove", method = RequestMethod.GET)
    @ResponseBody
    public APIResult remove(Long id) {
        itemDetailimgDao.delete(id);
        return new APIResult<>();
    }

    @RequestMapping(path = "top", method = RequestMethod.POST)
    @ResponseBody
    public APIResult top(Long id) {
        itemDetailimgService.setTop(id);
        return new APIResult<>();
    }

    @RequestMapping("edit")
    public String edit(ItemDetailimg entity, Model model) {
        List<ItemDetailimg> list = itemDetailimgService.find(entity);
        model.addAttribute("list", list);
        model.addAttribute("entity", entity);
        return "itemDetailimg/edit";
    }

    @RequestMapping("list")
    public String list(Item entity, @RequestParam(required = false, defaultValue = "1") int index, Model model) {
        Pageable pageable = new PageRequest(index - 1, 10, Sort.Direction.DESC, "id");

        ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("name", contains().ignoreCase()).withMatcher("kpname", contains().ignoreCase());

        BeanUtils.cleanEmpty(entity);
        Page<Item> page = itemDao.findAll(Example.of(entity, matcher), pageable);
        List<Item> content = page.getContent();
        for (Item item : content) {
            List<ItemDetailimg> list = itemDetailimgService.find(new ItemDetailimg(item.getId()));
            item.setDetailimgs(list);
        }

        model.addAttribute("entity", entity);
        model.addAttribute("page", page);
        return "itemDetailimg/list";
    }

}