package com.cupslicenew.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.cupslicenew.R;
import com.cupslicenew.core.BaseActivity;
import com.cupslicenew.core.BaseFragment;
import com.cupslicenew.core.helper.HelperGlobal;
import com.cupslicenew.core.helper.HelperImage;
import com.cupslicenew.core.view.ViewCoreCrop;
import com.cupslicenew.core.view.ViewItemMainMenu;
import com.cupslicenew.view.ViewButton;
import com.cupslicenew.view.ViewToastProcess;

import java.util.HashMap;
import java.util.Map;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.GPUImageOpacityFilter;

/**
 * Created by ekobudiarto on 7/19/16.
 */
public class FragmentEditorItemSupportMenu extends BaseFragment {

    View main_view;
    BaseActivity activity;
    HelperGlobal.FragmentInterface fragmentInterface;
    HelperGlobal.EditorBehavior editorBehavior;
    public static final String TAG_FRAGMENT_EDITOR_ITEM_SUPPORT_MENU = "tag:fragment-editor-item-support";

    LinearLayout linearItem, linearColorItem;
    ViewItemMainMenu adjustBrightness, adjustContrast, adjustSaturation, adjustBalance, adjustBW, adjustVignette,
    itemColor, itemOpacity, itemShadow, itemTextOpacity, itemTextColor, itemTextShadow, itemTextAlign;
    Map<String, String> parameter;
    String feature, selectedColor = "#FFFFFF", adjustFeature = "";
    ViewButton buttonCancel, buttonApply;
    SeekBar seekBar;
    int widthScreen = 0, percentageOpacity = 100;
    Bitmap bmpFeature, bmpSource, bmpTempFeature;
    String[] arrColor;
    HorizontalScrollView horizontalColorItem;
    ViewToastProcess toastProcess;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        main_view = inflater.inflate(R.layout.fragment_editor_item_support_menu, container, false);
        return main_view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity != null)
        {
            this.activity = (BaseActivity) activity;
            fragmentInterface = (HelperGlobal.FragmentInterface) this.activity;
            editorBehavior = (HelperGlobal.EditorBehavior) this.activity;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(activity != null)
        {
            init();
        }
    }

    private void init()
    {
        widthScreen = HelperGlobal.getScreenSize(activity, "w");
        arrColor = activity.getResources().getStringArray(R.array.color_item);
        linearItem = (LinearLayout) activity.findViewById(R.id.fragment_item_support_menu_linear_item);
        linearColorItem = (LinearLayout) activity.findViewById(R.id.fragment_editor_item_support_menu_linear_color);
        buttonCancel = (ViewButton) activity.findViewById(R.id.fragment_editor_item_support_menu_btn_cancel);
        buttonApply = (ViewButton) activity.findViewById(R.id.fragment_editor_item_support_menu_btn_apply);
        seekBar = (SeekBar) activity.findViewById(R.id.fragment_editor_item_support_menu_seekbar);
        horizontalColorItem = (HorizontalScrollView) activity.findViewById(R.id.fragment_editor_item_support_menu_horizontal_color);
        toastProcess = (ViewToastProcess) activity.findViewById(R.id.toast_process);

        buttonApply.getLayoutParams().width = widthScreen / 2;
        buttonCancel.getLayoutParams().width = widthScreen / 2;
        initItemMenu();
        addColor();
    }

    public void setBitmapFeature(Bitmap bmp)
    {
        this.bmpFeature = bmp;
        this.bmpTempFeature = bmp;
    }

    public void setBitmapSource(Bitmap src)
    {
        this.bmpSource = src;
    }

    @Override
    public void updateFragment() {
        super.updateFragment();
        selectedColor = "#FFFFFF";
        adjustFeature = "brightness";
        seekBar.setProgress(100);
        menuChecker();
        if(!FragmentEditorItemSupportMenu.this.isVisible())
        {
            show();
        }
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onApply();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                toastProcess.setMessage(Integer.toString(progress) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                toastProcess.newInstance();
                toastProcess.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                percentageOpacity = seekBar.getProgress();
                updateFeature();
                toastProcess.setVisibility(View.GONE);
            }
        });
    }

    private void initItemMenu()
    {
        adjustBrightness = new ViewItemMainMenu(activity);
        adjustBrightness.setColoringHover(true);
        adjustBrightness.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        adjustBrightness.setItemThumb(R.drawable.icon_support_brightness);
        adjustBrightness.setItemTitle("Bright");

        adjustContrast = new ViewItemMainMenu(activity);
        adjustContrast.setColoringHover(true);
        adjustContrast.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        adjustContrast.setItemThumb(R.drawable.icon_support_contrast);
        adjustContrast.setItemTitle("Contrast");

        adjustSaturation = new ViewItemMainMenu(activity);
        adjustSaturation.setColoringHover(true);
        adjustSaturation.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        adjustSaturation.setItemThumb(R.drawable.icon_support_saturation);
        adjustSaturation.setItemTitle("Saturation");

        adjustBalance = new ViewItemMainMenu(activity);
        adjustBalance.setColoringHover(true);
        adjustBalance.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        adjustBalance.setItemThumb(R.drawable.icon_support_balance);
        adjustBalance.setItemTitle("Balance");

        adjustBW = new ViewItemMainMenu(activity);
        adjustBW.setColoringHover(true);
        adjustBW.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        adjustBW.setItemThumb(R.drawable.icon_support_bw);
        adjustBW.setItemTitle("B&W");

        adjustVignette = new ViewItemMainMenu(activity);
        adjustVignette.setColoringHover(true);
        adjustVignette.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        adjustVignette.setItemThumb(R.drawable.icon_support_vignette);
        adjustVignette.setItemTitle("Vignette");

        itemColor = new ViewItemMainMenu(activity);
        itemColor.setColoringHover(true);
        itemColor.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        itemColor.setItemThumb(R.drawable.icon_support_color);
        itemColor.setItemTitle("Color");

        itemOpacity = new ViewItemMainMenu(activity);
        itemOpacity.setColoringHover(true);
        itemOpacity.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        itemOpacity.setItemThumb(R.drawable.icon_support_opacity);
        itemOpacity.setItemTitle("Opacity");

        itemShadow = new ViewItemMainMenu(activity);
        itemShadow.setColoringHover(true);
        itemShadow.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        itemShadow.setItemThumb(R.drawable.icon_support_shadow);
        itemShadow.setItemTitle("Shadow");

        itemTextColor = new ViewItemMainMenu(activity);
        itemTextColor.setColoringHover(true);
        itemTextColor.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        itemTextColor.setItemThumb(R.drawable.icon_support_text_color);
        itemTextColor.setItemTitle("Color");

        itemTextOpacity = new ViewItemMainMenu(activity);
        itemTextOpacity.setColoringHover(true);
        itemTextOpacity.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        itemTextOpacity.setItemThumb(R.drawable.icon_support_text_opacity);
        itemTextOpacity.setItemTitle("Opacity");

        itemTextShadow = new ViewItemMainMenu(activity);
        itemTextShadow.setColoringHover(true);
        itemTextShadow.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        itemTextShadow.setItemThumb(R.drawable.icon_support_text_shadow);
        itemTextShadow.setItemTitle("Shadow");

        itemTextAlign = new ViewItemMainMenu(activity);
        itemTextAlign.setColoringHover(true);
        itemTextAlign.setColor(HelperGlobal.getColor(activity, R.color.base_pink));
        itemTextAlign.setItemThumb(R.drawable.icon_support_text_align);
        itemTextAlign.setItemTitle("Align");

        linearItem.addView(adjustBrightness);
        linearItem.addView(adjustContrast);
        linearItem.addView(adjustSaturation);
        linearItem.addView(adjustBalance);
        linearItem.addView(adjustBW);
        linearItem.addView(adjustVignette);
        linearItem.addView(itemColor);
        linearItem.addView(itemOpacity);
        linearItem.addView(itemShadow);
        linearItem.addView(itemTextColor);
        linearItem.addView(itemTextOpacity);
        linearItem.addView(itemTextShadow);
        linearItem.addView(itemTextAlign);

        adjustBrightness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustBrightness.selected();
                adjustContrast.unselected();
                adjustSaturation.unselected();
                adjustBalance.unselected();
                adjustBW.unselected();
                adjustVignette.unselected();
                itemColor.unselected();
                itemOpacity.unselected();
                itemShadow.unselected();
                itemTextColor.unselected();
                itemTextOpacity.unselected();
                itemTextShadow.unselected();
                itemTextAlign.unselected();
                adjustFeature = "brightness";
                hideColorPalette();
                showSeekBar();
            }
        });
        adjustContrast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustBrightness.unselected();
                adjustContrast.selected();
                adjustSaturation.unselected();
                adjustBalance.unselected();
                adjustBW.unselected();
                adjustVignette.unselected();
                itemColor.unselected();
                itemOpacity.unselected();
                itemShadow.unselected();
                itemTextColor.unselected();
                itemTextOpacity.unselected();
                itemTextShadow.unselected();
                itemTextAlign.unselected();
                adjustFeature = "contrast";
                hideColorPalette();
                showSeekBar();
            }
        });
        adjustSaturation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustBrightness.unselected();
                adjustContrast.unselected();
                adjustSaturation.selected();
                adjustBalance.unselected();
                adjustBW.unselected();
                adjustVignette.unselected();
                itemColor.unselected();
                itemOpacity.unselected();
                itemShadow.unselected();
                itemTextColor.unselected();
                itemTextOpacity.unselected();
                itemTextShadow.unselected();
                itemTextAlign.unselected();
                adjustFeature = "saturation";
                hideColorPalette();
                showSeekBar();
            }
        });
        adjustBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustBrightness.unselected();
                adjustContrast.unselected();
                adjustSaturation.unselected();
                adjustBalance.selected();
                adjustBW.unselected();
                adjustVignette.unselected();
                itemColor.unselected();
                itemOpacity.unselected();
                itemShadow.unselected();
                itemTextColor.unselected();
                itemTextOpacity.unselected();
                itemTextShadow.unselected();
                itemTextAlign.unselected();
                adjustFeature = "balance";
                hideColorPalette();
                showSeekBar();
            }
        });
        adjustBW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustBrightness.unselected();
                adjustContrast.unselected();
                adjustSaturation.unselected();
                adjustBalance.unselected();
                adjustBW.selected();
                adjustVignette.unselected();
                itemColor.unselected();
                itemOpacity.unselected();
                itemShadow.unselected();
                itemTextColor.unselected();
                itemTextOpacity.unselected();
                itemTextShadow.unselected();
                itemTextAlign.unselected();
                adjustFeature = "bw";
                hideColorPalette();
                showSeekBar();
            }
        });
        adjustVignette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustBrightness.unselected();
                adjustContrast.unselected();
                adjustSaturation.unselected();
                adjustBalance.unselected();
                adjustBW.unselected();
                adjustVignette.selected();
                itemColor.unselected();
                itemOpacity.unselected();
                itemShadow.unselected();
                itemTextColor.unselected();
                itemTextOpacity.unselected();
                itemTextShadow.unselected();
                itemTextAlign.unselected();
                adjustFeature = "vignette";
                hideColorPalette();
                showSeekBar();
            }
        });
        itemColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustBrightness.unselected();
                adjustContrast.unselected();
                adjustSaturation.unselected();
                adjustBalance.unselected();
                adjustBW.unselected();
                adjustVignette.unselected();
                itemColor.selected();
                itemOpacity.unselected();
                itemShadow.unselected();
                itemTextColor.unselected();
                itemTextOpacity.unselected();
                itemTextShadow.unselected();
                itemTextAlign.unselected();
                showColorPalette();
                hideSeekBar();
            }
        });
        itemOpacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustBrightness.unselected();
                adjustContrast.unselected();
                adjustSaturation.unselected();
                adjustBalance.unselected();
                adjustBW.unselected();
                adjustVignette.unselected();
                itemColor.unselected();
                itemOpacity.selected();
                itemShadow.unselected();
                itemTextColor.unselected();
                itemTextOpacity.unselected();
                itemTextShadow.unselected();
                itemTextAlign.unselected();
                hideColorPalette();
                showSeekBar();
            }
        });
        itemShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustBrightness.unselected();
                adjustContrast.unselected();
                adjustSaturation.unselected();
                adjustBalance.unselected();
                adjustBW.unselected();
                adjustVignette.unselected();
                itemColor.unselected();
                itemOpacity.unselected();
                itemShadow.selected();
                itemTextColor.unselected();
                itemTextOpacity.unselected();
                itemTextShadow.unselected();
                itemTextAlign.unselected();
            }
        });
        itemTextColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustBrightness.unselected();
                adjustContrast.unselected();
                adjustSaturation.unselected();
                adjustBalance.unselected();
                adjustBW.unselected();
                adjustVignette.unselected();
                itemColor.unselected();
                itemOpacity.unselected();
                itemShadow.unselected();
                itemTextColor.selected();
                itemTextOpacity.unselected();
                itemTextShadow.unselected();
                itemTextAlign.unselected();
            }
        });
        itemTextOpacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustBrightness.unselected();
                adjustContrast.unselected();
                adjustSaturation.unselected();
                adjustBalance.unselected();
                adjustBW.unselected();
                adjustVignette.unselected();
                itemColor.unselected();
                itemOpacity.unselected();
                itemShadow.unselected();
                itemTextColor.unselected();
                itemTextOpacity.selected();
                itemTextShadow.unselected();
                itemTextAlign.unselected();
            }
        });
        itemTextShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustBrightness.unselected();
                adjustContrast.unselected();
                adjustSaturation.unselected();
                adjustBalance.unselected();
                adjustBW.unselected();
                adjustVignette.unselected();
                itemColor.unselected();
                itemOpacity.unselected();
                itemShadow.unselected();
                itemTextColor.unselected();
                itemTextOpacity.unselected();
                itemTextShadow.selected();
                itemTextAlign.unselected();
            }
        });
        itemTextAlign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustBrightness.unselected();
                adjustContrast.unselected();
                adjustSaturation.unselected();
                adjustBalance.unselected();
                adjustBW.unselected();
                adjustVignette.unselected();
                itemColor.unselected();
                itemOpacity.unselected();
                itemShadow.unselected();
                itemTextColor.unselected();
                itemTextOpacity.unselected();
                itemTextShadow.unselected();
                itemTextAlign.selected();
            }
        });
    }

    private void menuChecker()
    {
        parameter = getParameter();
        feature = parameter.get("feature");

        if(feature.equals("sticker"))
        {
            adjustBrightness.setVisibility(View.GONE);
            adjustContrast.setVisibility(View.GONE);
            adjustSaturation.setVisibility(View.GONE);
            adjustBalance.setVisibility(View.GONE);
            adjustBW.setVisibility(View.GONE);
            adjustVignette.setVisibility(View.GONE);
            itemTextColor.setVisibility(View.GONE);
            itemTextOpacity.setVisibility(View.GONE);
            itemTextShadow.setVisibility(View.GONE);
            itemTextAlign.setVisibility(View.GONE);
            itemColor.setVisibility(View.VISIBLE);
            itemOpacity.setVisibility(View.VISIBLE);
            itemColor.getLayoutParams().width = widthScreen / 2;
            itemOpacity.getLayoutParams().width = widthScreen / 2;
            itemColor.selected();
            itemOpacity.unselected();
            showColorPalette();
            hideSeekBar();
            //itemShadow.setVisibility(View.VISIBLE);
        }
        else if(feature.equals("filter"))
        {
            adjustBrightness.setVisibility(View.GONE);
            adjustContrast.setVisibility(View.GONE);
            adjustSaturation.setVisibility(View.GONE);
            adjustBalance.setVisibility(View.GONE);
            adjustBW.setVisibility(View.GONE);
            adjustVignette.setVisibility(View.GONE);
            itemShadow.setVisibility(View.GONE);
            itemColor.setVisibility(View.GONE);
            itemTextColor.setVisibility(View.GONE);
            itemTextOpacity.setVisibility(View.GONE);
            itemTextShadow.setVisibility(View.GONE);
            itemTextAlign.setVisibility(View.GONE);
            itemOpacity.setItemThumb(R.drawable.icon_menu_effect);
            itemOpacity.setItemTitle("Filter");
            itemOpacity.setVisibility(View.VISIBLE);
            itemOpacity.getLayoutParams().width = widthScreen;
            itemOpacity.selected();
            hideColorPalette();
            showSeekBar();
        }
        else if(feature.equals("frame"))
        {
            adjustBrightness.setVisibility(View.GONE);
            adjustContrast.setVisibility(View.GONE);
            adjustSaturation.setVisibility(View.GONE);
            adjustBalance.setVisibility(View.GONE);
            adjustBW.setVisibility(View.GONE);
            adjustVignette.setVisibility(View.GONE);
            itemShadow.setVisibility(View.GONE);
            itemTextColor.setVisibility(View.GONE);
            itemTextOpacity.setVisibility(View.GONE);
            itemTextShadow.setVisibility(View.GONE);
            itemTextAlign.setVisibility(View.GONE);
            itemColor.setVisibility(View.VISIBLE);
            itemOpacity.setVisibility(View.VISIBLE);
            itemColor.getLayoutParams().width = widthScreen / 2;
            itemOpacity.getLayoutParams().width = widthScreen / 2;
            itemColor.selected();
            itemOpacity.unselected();
            showColorPalette();
            hideSeekBar();
        }
        else if(feature.equals("text"))
        {
            adjustBrightness.setVisibility(View.GONE);
            adjustContrast.setVisibility(View.GONE);
            adjustSaturation.setVisibility(View.GONE);
            adjustBalance.setVisibility(View.GONE);
            adjustBW.setVisibility(View.GONE);
            adjustVignette.setVisibility(View.GONE);
            itemColor.setVisibility(View.GONE);
            itemShadow.setVisibility(View.GONE);
            itemOpacity.setVisibility(View.GONE);
            itemTextColor.setVisibility(View.VISIBLE);
            itemTextOpacity.setVisibility(View.VISIBLE);
            itemTextShadow.setVisibility(View.VISIBLE);
            itemTextAlign.setVisibility(View.VISIBLE);
            itemTextColor.getLayoutParams().width = widthScreen / 4;
            itemTextOpacity.getLayoutParams().width = widthScreen / 4;
            itemTextShadow.getLayoutParams().width = widthScreen / 4;
            itemTextAlign.getLayoutParams().width = widthScreen / 4;
        }
        else if(feature.equals("adjust"))
        {
            itemColor.setVisibility(View.GONE);
            itemOpacity.setVisibility(View.GONE);
            itemShadow.setVisibility(View.GONE);
            itemTextColor.setVisibility(View.GONE);
            itemTextOpacity.setVisibility(View.GONE);
            itemTextShadow.setVisibility(View.GONE);
            itemTextAlign.setVisibility(View.GONE);
            adjustBrightness.setVisibility(View.VISIBLE);
            adjustContrast.setVisibility(View.VISIBLE);
            adjustSaturation.setVisibility(View.VISIBLE);
            adjustBalance.setVisibility(View.VISIBLE);
            adjustBW.setVisibility(View.VISIBLE);
            adjustVignette.setVisibility(View.VISIBLE);
            adjustBrightness.selected();
            adjustContrast.unselected();
            adjustSaturation.unselected();
            adjustBalance.unselected();
            adjustBW.unselected();
            adjustVignette.unselected();
            hideColorPalette();
            showSeekBar();
        }
        else {
            itemColor.setVisibility(View.GONE);
            itemOpacity.setVisibility(View.GONE);
            itemShadow.setVisibility(View.GONE);
            itemTextColor.setVisibility(View.GONE);
            itemTextOpacity.setVisibility(View.GONE);
            itemTextShadow.setVisibility(View.GONE);
            itemTextAlign.setVisibility(View.GONE);
            adjustBrightness.setVisibility(View.GONE);
            adjustContrast.setVisibility(View.GONE);
            adjustSaturation.setVisibility(View.GONE);
            adjustBalance.setVisibility(View.GONE);
            adjustBW.setVisibility(View.GONE);
            adjustVignette.setVisibility(View.GONE);
        }
    }

    private void onCancel()
    {
        editorBehavior.onEditCancel();
    }

    private void onApply()
    {
        editorBehavior.onEditApply();
    }

    public void hide()
    {
        getChildFragmentManager().beginTransaction().
                hide(FragmentEditorItemSupportMenu.this).commit();
    }

    public void show()
    {
        getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slideup_menu, R.anim.slidedown_menu).
                show(FragmentEditorItemSupportMenu.this).commit();
    }

    private void addColor()
    {
        for(int i = 0;i < arrColor.length;i++)
        {
            FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
            param.width = (int) activity.getResources().getDimension(R.dimen.width_color_item);
            param.height = (int) activity.getResources().getDimension(R.dimen.height_color_item);
            FrameLayout frameItem = new FrameLayout(activity);
            frameItem.setLayoutParams(param);
            frameItem.setBackgroundColor(Color.parseColor(arrColor[i]));
            frameItem.setId(i);
            frameItem.setClickable(true);
            final int finalI = i;
            frameItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedColor = arrColor[finalI];
                    updateFeature();
                }
            });
            linearColorItem.addView(frameItem);
        }
    }



    private void showColorPalette()
    {
        horizontalColorItem.setVisibility(View.VISIBLE);
    }

    private void hideColorPalette()
    {
        horizontalColorItem.setVisibility(View.GONE);
    }

    private void showSeekBar()
    {
        seekBar.setVisibility(View.VISIBLE);
    }

    private void hideSeekBar()
    {
        seekBar.setVisibility(View.GONE);
    }

    private void updateFeature()
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("selectedColor", selectedColor);
        param.put("percentageOpacity", Integer.toString(percentageOpacity));
        param.put("adjustFeature", adjustFeature);
        editorBehavior.setOptionBlend(param);
        editorBehavior.updateFeature();
    }
}
