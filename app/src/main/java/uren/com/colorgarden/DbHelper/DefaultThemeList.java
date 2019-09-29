package uren.com.colorgarden.DbHelper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import uren.com.colorgarden.R;
import uren.com.colorgarden.model.bean.ThemeBean;

import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_ANIMALS_1;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_ANIMALS_2;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_ANIMALS_3;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_ANIMALS_4;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_MANDALA_1;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_MANDALA_2;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_MANDALA_3;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_MANDALA_4;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_SEC_GARDEN_1;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_SEC_GARDEN_2;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_SEC_GARDEN_3;
import static uren.com.colorgarden.Constants.NumericConstants.DEF_CAT_SEC_GARDEN_4;
import static uren.com.colorgarden.Constants.StringConstants.DEF_FALSE;
import static uren.com.colorgarden.Constants.StringConstants.DEF_TRUE;

public class DefaultThemeList {

    public static List<ThemeBean.Theme> themeList = new ArrayList<>();

    public static List<ThemeBean.Theme> getThemeList(Context context){

        themeList.add(new ThemeBean.Theme(DEF_CAT_SEC_GARDEN_1, context.getString(R.string.secretGarden1), 0, "SecretGarden1", DEF_FALSE, ""));
        themeList.add(new ThemeBean.Theme(DEF_CAT_SEC_GARDEN_2, context.getString(R.string.secretGarden2), 0, "SecretGarden2", DEF_TRUE, ""));
        themeList.add(new ThemeBean.Theme(DEF_CAT_SEC_GARDEN_3, context.getString(R.string.secretGarden3), 0, "SecretGarden3", DEF_TRUE, ""));
        themeList.add(new ThemeBean.Theme(DEF_CAT_SEC_GARDEN_4, context.getString(R.string.secretGarden4), 0, "SecretGarden4", DEF_TRUE, "colorgarden4"));

        themeList.add(new ThemeBean.Theme(DEF_CAT_ANIMALS_1, context.getString(R.string.secretAnimals1), 0, "Animals1", DEF_TRUE, "animal1"));
        themeList.add(new ThemeBean.Theme(DEF_CAT_ANIMALS_2, context.getString(R.string.secretAnimals2), 0, "Animals2", DEF_TRUE, "animal2"));
        themeList.add(new ThemeBean.Theme(DEF_CAT_ANIMALS_3, context.getString(R.string.secretAnimals3), 0, "Animals3", DEF_TRUE, "animal3"));
        themeList.add(new ThemeBean.Theme(DEF_CAT_ANIMALS_4, context.getString(R.string.secretAnimals4), 0, "Animals4", DEF_TRUE, "animal4"));

        themeList.add(new ThemeBean.Theme(DEF_CAT_MANDALA_1, context.getString(R.string.mandala1), 0, "Mandala1", DEF_TRUE, "mandala1"));
        themeList.add(new ThemeBean.Theme(DEF_CAT_MANDALA_2, context.getString(R.string.mandala2), 0, "Mandala2", DEF_TRUE, "mandala2"));
        themeList.add(new ThemeBean.Theme(DEF_CAT_MANDALA_3, context.getString(R.string.mandala3), 0, "Mandala3", DEF_TRUE, "mandala3"));
        themeList.add(new ThemeBean.Theme(DEF_CAT_MANDALA_4, context.getString(R.string.mandala4), 0, "Mandala4", DEF_TRUE, "mandala4"));

        return themeList;
    }
}
