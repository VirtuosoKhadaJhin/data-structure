package com.nuanyou.cms.config;

/**
 * Created by alan.ye on 17/1/11.
 */
public enum ImageSpec {

    Big43(640, 480, 100, "jpg", null),
    Big11(480, 480, 100, "jpg", null),

    Mid43(320, 240, 50, "jpg", null),
    Mid11(240, 240, 30, "jpg", null),

    Small43(160, 120, 15, "jpg", null),
    Small11(120, 120, 8, "jpg", null),

    ICON(60, 60, 5, "png", null),

    MerchantDetail(Big43, ImageSpec.CLIP),
    ItemShow(Big43, ImageSpec.CLIP),
    TuanShow(Big43, ImageSpec.CLIP),

    MerchantCatBG(ICON, ImageSpec.ZOMM),
    MerchantCatMap(ICON, ImageSpec.ZOMM),

    MerchantList(Mid43, ImageSpec.ZOMM),
    ItemList(Mid43, ImageSpec.ZOMM),

    Recommend(Mid43, ImageSpec.ZOMM),
    TuanList(Mid43, ImageSpec.ZOMM),

    FeatureGood(680, 300, 100, "jpg", ImageSpec.ZOMM),
    FeatureNormal(Mid11, ImageSpec.ZOMM),
    Quick(ICON, ImageSpec.ZOMM),
    BannerYoufu(Small43, ImageSpec.ZOMM),

    CulturalRecommendationsIndex(222, 200, 60, "jpg", ImageSpec.LIMIT),

    BannerIndex(720, 400, 150, "jpg", ImageSpec.LIMIT),
    BannerUp(338, 128, 80, "jpg", ImageSpec.LIMIT),
    BannerDown(270, 134, 50, "jpg", ImageSpec.LIMIT);

    public static final String CLIP = "CLIP";

    public static final String ZOMM = "ZOMM";

    public static final String LIMIT = "LIMIT";


    public final int width;

    public final int height;

    public final int dateSize;

    public final String format;

    public final String process;


    ImageSpec(int width, int height, int dateSize, String format, String process) {
        this.width = width;
        this.height = height;
        this.dateSize = dateSize * 1024;
        this.format = format;
        this.process = process;
    }

    ImageSpec(ImageSpec spec, int dateSize, String process) {
        this.width = spec.width;
        this.height = spec.height;
        this.dateSize = dateSize * 1024;
        this.format = spec.format;
        this.process = process;
    }

    ImageSpec(ImageSpec spec, String process) {
        this.width = spec.width;
        this.height = spec.height;
        this.dateSize = spec.dateSize;
        this.format = spec.format;
        this.process = process;
    }

}