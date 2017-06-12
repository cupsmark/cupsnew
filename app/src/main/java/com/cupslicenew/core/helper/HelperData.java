package com.cupslicenew.core.helper;

import android.content.Context;

import com.cupslicenew.core.model.MAds;
import com.cupslicenew.core.model.MEffect;
import com.cupslicenew.core.model.MEffectCategory;
import com.cupslicenew.core.model.MFrame;
import com.cupslicenew.core.model.MFrameCategory;
import com.cupslicenew.core.model.MSet;
import com.cupslicenew.core.model.MSticker;
import com.cupslicenew.core.model.MStickerCategory;

/**
 * Created by developer on 5/31/16.
 */
public class HelperData {

    //***** EFFECT CATEGORY *****//
    private int[] effect_category_id = new int[]{
            //1,2,3,
            1,2,3,4
    };
    private String[] effect_category_name = new String[]{
            //"Vintage",
            //"Portrait",
            //"Landscape",
            "Selfie","Lomo","Travel","Fade"
    };
    private String[] effect_category_file = new String[]{
            //"AECVintage.jpg","AECPortrait.jpg","AECLandscape.jpg",
            "AECSelfie.jpg","AECLomo.jpg","AECScenery.jpg","AECFade.jpg"
    };

    private String[] effect_category_status = new String[]{
            //"f","f","f",
            "f","f","f","f"
    };

    //***** EFFECT *****//
    private int[] effect_id = new int[]{
            //1,2,3,4,5,6,7,8,9,10, //vintage
            //11,12,13,14,15,16,17,18,19,  //portrait
            //21,22,23,24,25,26,27,28,29,30,  //Landscape
            31,32,33,34,35,36,37,38,39,40,  //selfie
            41,42,43,44,45,46,47,48,49,50,  //lomo
            51,52,53,54,55,56,57,58,59,60,  //travel
            61,62,63,64,65,66,67,68,69,70   //Fade
    };
    private String[] effect_name = new String[]{
            //"Vintage 1","Vintage 2","Vintage 3","Vintage 4","Vintage 5","Vintage 6","Vintage 7","Vintage 8","Vintage 9","Vintage 10",
            //"Portrait 1","Portrait 2","Portrait 3","Portrait 4","Portrait 5","Portrait 6","Portrait 7","Portrait 8","Portrait 9",
            //"Landscape 1","Landscape 2","Landscape 3","Landscape 4","Landscape 5","Landscape 6","Landscape 7","Landscape 8","Landscape 9","Landscape 10",
            "Fellow","Antique","Aquamarine","Arsenic","Xanada","Yawn","Capput","Chartra","Navona","Linen",
            "Napler","Pear","Emerald","Mulberry","Mikado","Misty","Mauve","Indigo","Bober","Ultramarine",
            "Olivine","Onyx","Almond","Jasmine","Ocean","Sky","Bless","Mint","Zeroda","Cadya",
            "Fog","Pewter","Slate","Porpoise","Fossil","Smoke","Ash","Pebble","Flint","Graphite"
    };
    private String[] effect_file = new String[]{
            //"vil1","vil2","vil3","vil4","vil5","vil6","vil7","vil8","vil9","vil10",
            //"po1","po2","po3","po4","po5","po6","po7","po8","po9",
            //"ln1","ln2","ln3","ln4","ln5","ln6","ln7","ln8","ln9","ln10",
            "ECSEFellow.jpg","ECSEAntique.jpg","ECSEAquamarine.jpg","ECSEArsenic.jpg","ECSEXanada.jpg","ECSEYawn.jpg","ECSECapput.jpg","ECSEChartra.jpg","ECSENavona.jpg","ECSELinen.jpg",
            "ECLNapler.jpg","ECLPear.jpg","ECLEmerald.jpg","ECLMulberry.jpg","ECLMikado.jpg","ECLMisty.jpg","ECLMauve.jpg","ECLIndigo.jpg","ECLBober.jpg","ECLUltramarine.jpg",
            "ECSOlivine.jpg","ECSOnyx.jpg","ECSAlmond.jpg","ECSJasmine.jpg","ECSOcean.jpg","ECSSky.jpg","ECSBless.jpg","ECSMint.jpg","ECSZeroda.jpg","ECSCadya.jpg",
            "ECFFog.png","ECFPewter.png","ECFSlate.png","ECFPorpoise.png","ECFFossil.png","ECFSmoke.png","ECFAsh.png","ECFPebble.png","ECFFlint.png","ECFGraphite.png"
    };

    private int[] effect_cat_id = new int[]{
            1,1,1,1,1,1,1,1,1,1,
            2,2,2,2,2,2,2,2,2,2,
            3,3,3,3,3,3,3,3,3,3,
            4,4,4,4,4,4,4,4,4,4
    };

    private String[] effect_status = new String[]{
            //"f","f","f","f","f","f","f","f","f","f",
            //"f","f","f","f","f","f","f","f","f",
            //"f","f","f","f","f","f","f","f","f","f",
            "f","f","f","f","f","f","f","f","f","f",
            "f","f","f","f","f","f","f","f","f","f",
            "f","f","f","f","f","f","f","f","f","f",
            "f","f","f","f","f","f","f","f","f","f"
    };


    //***** BADGE *****//
    private int[] sticker_id = new int[]{
            1,2,3,4,5,6,7,8,9,10,   //Sticker 1
            11,12,13,14,15,16,17,18,19,20,   //Sticker 2
            21,22,23,24,25,26,27,28,29,30,
    };
    private String[] sticker_name = new String[]{
            "StickerI1","StickerI2","StickerI3","StickerI4","StickerI5","StickerI6","StickerI7","StickerI8","StickerI9","StickerI10",
            "StickerII1","StickerII2","StickerII3","StickerII4","StickerII5","StickerII6","StickerII7","StickerII8","StickerII9","StickerII10",
            "StickerIII1","StickerIII2","StickerIII3","StickerIII4","StickerIII5","StickerIII6","StickerIII7","StickerIII8","StickerIII9","StickerIII10"
    };
    private String[] sticker_file = new String[]{
            "StickerI1.png","StickerI2.png","StickerI3.png","StickerI4.png","StickerI5.png","StickerI6.png","StickerI7.png","StickerI8.png","StickerI9.png","StickerI10.png",
            "StickerII1.png","StickerII2.png","StickerII3.png","StickerII4.png","StickerII5.png","StickerII6.png","StickerII7.png","StickerII8.png","StickerII9.png","StickerII10.png",
            "StickerIII1.png","StickerIII2.png","StickerIII3.png","StickerIII4.png","StickerIII5.png","StickerIII6.png","StickerIII7.png","StickerIII8.png","StickerIII9.png","StickerIII10.png",
    };
    private int[] sticker_cat_id = new int[]{
            1,1,1,1,1,1,1,1,1,1,
            2,2,2,2,2,2,2,2,2,2,
            3,3,3,3,3,3,3,3,3,3,
    };
    private String[] sticker_status = new String[]{
            "f","f","f","f","f","f","f","f","f","f",
            "f","f","f","f","f","f","f","f","f","f",
            "f","f","f","f","f","f","f","f","f","f"
    };

    private String[] sticker_color = new String[]{
            "yes","yes","yes","yes","yes","yes","yes","yes","yes","yes",
            "yes","yes","yes","yes","yes","yes","yes","yes","yes","yes",
            "yes","yes","yes","yes","yes","yes","yes","yes","yes","yes"
    };

    //***** BADGE CATEGORY *****//
    private int[] sticker_category_id = new int[]{
            1,2,3
    };
    private String[] sticker_category_name = new String[]{
            "Stickers 1","Stickers 2","Stickers 3"
    };
    private String[] sticker_category_file = new String[]{
            "ABCSticker_u17_1.png","ABCSticker_u17_2.png","ABCSticker_u17_3.png"
    };
    private String[] sticker_category_status = new String[]{
            "f","f","f"
    };


    //***** FRAME CATEGORY *****//
    private int[] frame_category_id = new int[]{
            1,4,5
    };
    private String[] frame_category_name = new String[]{
            "AFC1","AFC4","AFC5"
    };
    private String[] frame_category_file = new String[]{
            "AFC1.png","AFC4.png","AFC5.png"
    };
    private String[] frame_category_status = new String[]{
            "f","f","f"
    };


    //***** FRAME *****//
    private int[] frame_id = new int[]{
            1,2,3,4,5,6,7,8,9,//Frame 1
            37,38,39,40,41,42,43,44,45,46, //frame4
            47,48,49,50,51,52,53,54,55,56 //frame5
    };
    private String[] frame_name = new String[]{
            "F1frame1","F1frame2","F1frame3","F1frame4","F1frame5","F1frame6","F1frame7","F1frame8","F1frame9",
            "F4frame1","F4frame2","F4frame3","F4frame4","F4frame5","F4frame6","F4frame7","F4frame8","F4frame9","F4frame10",
            "F5frame1","F5frame2","F5frame3","F5frame4","F5frame5","F5frame6","F5frame7","F5frame8","F5frame9","F5frame10"
    };

    private String[] frame_file = new String[]{
            "F1frame1.png","F1frame2.png","F1frame3.png","F1frame4.png","F1frame5.png","F1frame6.png","F1frame7.png","F1frame8.png","F1frame9.png",
            "F4frame1.png","F4frame2.png","F4frame3.png","F4frame4.png","F4frame5.png","F4frame6.png","F4frame7.png","F4frame8.png","F4frame9.png","F4frame10.png",
            "F5frame1.png","F5frame2.png","F5frame3.png","F5frame4.png","F5frame5.png","F5frame6.png","F5frame7.png","F5frame8.png","F5frame9.png","F5frame10.png"
    };

    private int[] frame_cat_id = new int[]{
            1,1,1,1,1,1,1,1,1,
            4,4,4,4,4,4,4,4,4,4,
            5,5,5,5,5,5,5,5,5,5
    };
    private String[] frame_status = new String[]{
            "f","f","f","f","f","f","f","f","f",
            "f","f","f","f","f","f","f","f","f","f",
            "f","f","f","f","f","f","f","f","f","f"
    };

    private String[] frame_color = new String[]{
            "yes","yes","yes","yes","yes","yes","yes","yes","yes",
            "yes","yes","yes","yes","yes","yes","yes","yes","yes","yes",
            "yes","yes","yes","yes","yes","no","no","no","no","no"
    };


    private int[] set_id = new int[]{1};
    private String[] set_name = new String[]{"screen-new-feature"};
    private String[] set_value = new String[]{"0"};


    private Context mContext;
    public HelperData(Context context)
    {
        this.mContext = context;
    }

    public void run()
    {
        HelperDB dbHelper = new HelperDB(mContext);
        runEffectCategory(dbHelper);
        runEffect(dbHelper);
        runBadgeCategory(dbHelper);
        runBadge(dbHelper);
        runFrameCategory(dbHelper);
        runFrame(dbHelper);
        runAds(dbHelper);
        dbHelper.close();
    }

    private void runEffectCategory(HelperDB dbHelper)
    {
        try{
            if(dbHelper.getEffectCategoryCount() == 0){
                dbHelper.resetEffectCategory();
                for(int i = 0;i < effect_category_id.length;i++)
                {
                    MEffectCategory efcat = new MEffectCategory();
                    efcat.setEffectCategoryID(effect_category_id[i]);
                    efcat.setEffectCategoryName(effect_category_name[i]);
                    efcat.setEffectCategoryFile(effect_category_file[i]);
                    efcat.setEffectCategoryDesc("");
                    efcat.setEffectCategoryStatus(effect_category_status[i]);
                    dbHelper.addCategoryEffect(efcat);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void runEffect(HelperDB dbHelper)
    {
        try{
            if(dbHelper.getEffectCount() == 0){
                dbHelper.resetEffect();
                for(int i = 0;i < effect_id.length;i++)
                {
                    MEffect ef = new MEffect();
                    ef.setEffectID(effect_id[i]);
                    ef.setEffectName(effect_name[i]);
                    ef.setEffectDesc("");
                    ef.setEffectFile(effect_file[i]);
                    ef.setEffectCategoryID(effect_cat_id[i]);
                    ef.setEffectStatus(effect_status[i]);
                    dbHelper.addEffect(ef);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void runBadgeCategory(HelperDB dbHelper)
    {
        try{
            if(dbHelper.getStickerCategoryCount() == 0){
                dbHelper.resetStickerCategory();
                for(int i = 0;i < sticker_category_id.length;i++)
                {
                    MStickerCategory cat = new MStickerCategory();
                    cat.setStickerCategoryID(sticker_category_id[i]);
                    cat.setStickerCategoryName(sticker_category_name[i]);
                    cat.setStickerCategoryDesc("");
                    cat.setStickerCategoryUri(sticker_category_file[i]);
                    cat.setStickerCategoryStatus(sticker_category_status[i]);
                    cat.setStickerCategoryDate("2015-12-01 01:00:00");
                    dbHelper.addStickerCategory(cat);
                }
            }
            dbHelper.updateStickerCategoryDateNull();
            for(int i = 0;i < sticker_category_id.length;i++)
            {
                if(dbHelper.checkStickerCategoryByID(sticker_category_id[i]))
                {
                    dbHelper.updateStickerCategoryFile(sticker_category_id[i], sticker_category_file[i]);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void runBadge(HelperDB dbHelper)
    {
        try{
            if(dbHelper.getStickerCount() == 0){
                dbHelper.resetSticker();
                for(int i = 0;i < sticker_id.length;i++)
                {
                    MSticker sticker = new MSticker();
                    sticker.setStickerID(sticker_id[i]);
                    sticker.setStickerName(sticker_name[i]);
                    sticker.setStickerUri(sticker_file[i]);
                    sticker.setStickerCategoryID(sticker_cat_id[i]);
                    sticker.setStickerStatus(sticker_status[i]);
                    sticker.setStickerColor(sticker_color[i]);
                    dbHelper.addSticker(sticker);
                }
            }
            int[] cat_sticker = new int[]{31,32};
            for(int i = 0;i < cat_sticker.length;i++)
            {
                if(dbHelper.checkStickerByID(cat_sticker[i]))
                {
                    MSticker s = new MSticker();
                    s.setStickerID(cat_sticker[i]);
                    dbHelper.deleteSticker(s);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void runFrameCategory(HelperDB dbHelper)
    {
        try{
            if(dbHelper.getFrameCategoryCount() == 0){
                dbHelper.resetFrameCategory();
                for(int i = 0;i < frame_category_id.length;i++)
                {
                    MFrameCategory fcat = new MFrameCategory();
                    fcat.setFrameCategoryID(frame_category_id[i]);
                    fcat.setFrameCategoryName(frame_category_name[i]);
                    fcat.setFrameCategoryUri(frame_category_file[i]);
                    fcat.setFrameCategoryDesc("");
                    fcat.setFrameCategoryStatus(frame_category_status[i]);
                    dbHelper.addFrameCategory(fcat);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    private void runFrame(HelperDB dbHelper)
    {
        try{
            if(dbHelper.getFrameCount() == 0){
                dbHelper.resetFrame();
                for(int i = 0;i < frame_id.length;i++)
                {
                    MFrame frame = new MFrame();
                    frame.setFrameID(frame_id[i]);
                    frame.setFrameName(frame_name[i]);
                    frame.setFrameUri(frame_file[i]);
                    frame.setFrameDesc("");
                    frame.setFrameCategoryID(frame_cat_id[i]);
                    frame.setFrameStatus(frame_status[i]);
                    frame.setFrameColor(frame_color[i]);
                    dbHelper.addFrame(frame);
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void runAds(HelperDB helper)
    {
        long now = System.currentTimeMillis();
        long increment = 1000 * 60 * 60 * 24;
        long nextTime = now + increment;

        int count = helper.getAdsCount();
        if(count == 0)
        {
            MAds ads1 = new MAds();
            ads1.setAdsID(HelperGlobal.FULLSCREEN_ADS_ID);
            ads1.setAdsCategory("fullscreen");
            ads1.setAdsState("open");
            ads1.setAdsNextTime(Long.toString(System.currentTimeMillis()));
            ads1.setAdsOverlay("0");

            MAds ads2 = new MAds();
            ads2.setAdsID(HelperGlobal.POPUP_RATE_ID);
            ads2.setAdsCategory("");
            ads2.setAdsState("open");
            ads2.setAdsNextTime(Long.toString(nextTime));
            ads1.setAdsOverlay("0");
            helper.addAds(ads1);
            helper.addAds(ads2);
        }
    }


    public void runSet(HelperDB db)
    {
        try{
            for(int i = 0;i < set_id.length;i++)
            {
                MSet set = new MSet();
                set.setID(set_id[i]);
                set.setName(set_name[i]);
                set.setValue(set_value[i]);
                db.addSet(set);
            }
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
