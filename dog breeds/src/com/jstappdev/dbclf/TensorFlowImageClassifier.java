package com.jstappdev.dbclf;

/*
 * 版權聲明:
 * 2016年 TensorFlow 作者保留所有權利。
 * 2018年 Josef Steppan 所作修改版權歸其所有。
 *
 * 授權條款:
 * 使用本文件的程式碼需遵守 Apache License, Version 2.0。
 * 詳細資訊可參見 http://www.apache.org/licenses/LICENSE-2.0
 * 除非依法要求或書面同意，否則本軟體按"原樣"提供，不提供任何明示或暗示的保證或條件。
 * 請參閱授權條款以了解具體的法律條款和限制。
 * ==============================================================================
 */

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import org.tensorflow.Operation;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

/**
 * 一個使用 TensorFlow 進行圖片分類的專用分類器。
 */
public class TensorFlowImageClassifier implements Classifier {
    // 只返回最有可能的幾個結果，且置信度不低於這個閾值。
    private static final int MAX_RESULTS = 3;
    private static final float THRESHOLD = 0.1f;

    // 配置值。
    private String inputName;
    private String outputName;
    private int inputSize;
    private int imageMean;
    private float imageStd;

    // 預先分配的緩衝區。
    private Vector<String> labels = new Vector<String>();
    private int[] intValues;
    private float[] floatValues;
    private float[] outputs;
    private String[] outputNames;

    private boolean logStats = false;

    private TensorFlowInferenceInterface inferenceInterface;

    // 私有的無參數建構子，限制實例化。
    private TensorFlowImageClassifier() {
    }

    /**
     * 初始化一個用於分類圖片的本地 TensorFlow 會話。
     *
     * @param assetManager 用於加載資產的AssetManager。
     * @param modelFilename 模型 GraphDef 協議緩衝區的檔案路徑。
     * @param labels 標籤的字符串數組。
     * @param inputSize 輸入大小。假定輸入是 inputSize x inputSize 的方形影像。
     * @param imageMean 影像值的假定平均值。
     * @param imageStd 影像值的假定標準差。
     * @param inputName 影像輸入節點的標籤。
     * @param outputName 輸出節點的標籤。
     * @throws IOException
     */
    public static Classifier create(
            AssetManager assetManager,
            String modelFilename,
            String[] labels,
            int inputSize,
            int imageMean,
            float imageStd,
            String inputName,
            String outputName) {
        final TensorFlowImageClassifier c = new TensorFlowImageClassifier();
        c.inputName = inputName;
        c.outputName = outputName;

        // 將標籤名稱讀入內存。
        Collections.addAll(c.labels, labels);

        c.inferenceInterface = new TensorFlowInferenceInterface(assetManager, modelFilename);

        // 輸出的形狀為 [N, NUM_CLASSES]，其中 N 是批處理的大小。
        final Operation operation = c.inferenceInterface.graphOperation(outputName);
        final int numClasses = (int) operation.output(0).shape().size(1);

        // 理論上，inputSize 可以從輸入操作的形狀中獲取。然而，通常使用的 graphdef 中的 input 占位符節點並未指定形狀，
        // 因此必須作為參數傳入。
        c.inputSize = inputSize;
        c.imageMean = imageMean;
        c.imageStd = imageStd;

        // 預先分配緩衝區。
        c.outputNames = new String[]{outputName};
        c.intValues = new int[inputSize * inputSize];
        c.floatValues = new float[inputSize * inputSize * 3];
        c.outputs = new float[numClasses];

        return c;
    }


    @Override
    public List<Recognition> recognizeImage(final Bitmap bitmap) {

        // 從0-255整數對圖片數據進行預處理，轉換為基於提供參數的正規化浮點數。
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        for (int i = 0; i < intValues.length; ++i) {
            final int val = intValues[i];
            floatValues[i * 3 + 0] = (((val >> 16) & 0xFF) - imageMean) / imageStd;
            floatValues[i * 3 + 1] = (((val >> 8) & 0xFF) - imageMean) / imageStd;
            floatValues[i * 3 + 2] = ((val & 0xFF) - imageMean) / imageStd;
        }

        // 將輸入數據複製到 TensorFlow 中。
        inferenceInterface.feed(inputName, floatValues, 1, inputSize, inputSize, 3);

        // 執行推斷呼叫。
        inferenceInterface.run(outputNames, logStats);

        // 將輸出 Tensor 複製回輸出陣列。
        inferenceInterface.fetch(outputName, outputs);

        // 找到最佳的分類。
        PriorityQueue<Recognition> pq =
                new PriorityQueue<Recognition>(
                        3,
                        (lhs, rhs) -> {
                            // 故意反轉，以將高置信度放在隊列的前面。
                            return Float.compare(rhs.getConfidence(), lhs.getConfidence());
                        });

        for (int i = 0; i < outputs.length; ++i) {
            if (outputs[i] > THRESHOLD) {
                pq.add(
                        new Recognition(
                                "" + i, labels.size() > i ? labels.get(i) : "unknown", outputs[i], null));
            }
        }
        final ArrayList<Recognition> recognitions = new ArrayList<Recognition>();
        final int recognitionsSize = Math.min(pq.size(), MAX_RESULTS);
        for (int i = 0; i < recognitionsSize; ++i) {
            recognitions.add(pq.poll());
        }
        return recognitions;
    }
}
