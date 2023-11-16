package com.jstappdev.dbclf;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class SimpleListActivity extends Activity {

    // 宣告一些成員變數
    private static String wikiLangSubDomain = "";
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, String> listDataChild;
    private ArrayList<String> recogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 設定應用程式的布局
        setContentView(R.layout.activity_list);

        // 獲取使用者設定的語言，以便顯示相應的內容
        final String lang = CameraActivity.preferredLanguageCode;
        if (CameraActivity.supportedLanguageCodes.contains(lang)) {
            wikiLangSubDomain = lang + ".";
        }

        // 從前一個 Activity 中取得傳遞過來的資料
        recogs = getIntent().getStringArrayListExtra("recogs");

        // 取得擴充式列表視圖
        expListView = findViewById(R.id.lvExp);

        // 準備擴充式列表的資料
        prepareListData();

        // 創建擴充式列表的適配器並設定
        final ExpandableListAdapter listAdapter = new ListAdapter(this, listDataHeader, listDataChild);
        expListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            // 在子項目點擊時觸發的事件
            final String title = listDataHeader.get(groupPosition);
            final String searchText = title.replace(" ", "+");

            final DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                // 點擊對話框按鈕時的處理
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        final String url = String.format("https://%swikipedia.org/w/index.php?search=%s&title=Special:Search", wikiLangSubDomain, searchText);

                        final Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };

            (new AlertDialog.Builder(this))
                    .setMessage(R.string.searchFor).setTitle(title)
                    .setNegativeButton(R.string.no, dialogClickListener)
                    .setPositiveButton(R.string.yes, dialogClickListener).show();

            return false;
        });

        expListView.setFastScrollEnabled(true);
        expListView.setAdapter(listAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        // 在 Activity 恢復時展開所有項目
        if (null != recogs)
            for (int i = 0; i < listDataHeader.size(); i++)
                expListView.expandGroup(i);
    }

    /*
     * 準備擴充式列表的資料
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // 加載列表標題和相應的文件名
        Collections.addAll(listDataHeader, getResources().getStringArray(R.array.breeds_array));
        final String[] fileNames = getResources().getStringArray(R.array.file_names);

        // 將文件名與列表標題對應起來
        for (int i = 0; i < listDataHeader.size(); i++) {
            listDataChild.put(listDataHeader.get(i), fileNames[i]);
        }

        // 如果有額外的識別資訊，則使用它來替換列表標題
        if (null != recogs) {
            listDataHeader = new ArrayList<>();
            listDataHeader.addAll(recogs);
            expListView.setFastScrollAlwaysVisible(false);
        } else {
            // 否則按字母順序排序列表標題
            Locale locale;

            if (CameraActivity.supportedLanguageCodes.contains(CameraActivity.preferredLanguageCode)) {
                locale = new Locale(CameraActivity.preferredLanguageCode);
            } else {
                locale = Locale.getDefault();
            }

            final Collator coll = Collator.getInstance(locale);
            coll.setStrength(Collator.PRIMARY);
            Collections.sort(listDataHeader, coll);
            expListView.setFastScrollAlwaysVisible(true);
        }
    }
}
