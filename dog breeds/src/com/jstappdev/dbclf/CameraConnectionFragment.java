package com.jstappdev.dbclf;

/*
 * Copyright 2016 The TensorFlow Authors. All Rights Reserved.
 * Modifications copyright (C) 2018 Josef Steppan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class CameraConnectionFragment extends Fragment {

    /**
     * 相機預覽大小將選擇為能夠容納 DESIRED_SIZE x DESIRED_SIZE 正方形的最小畫面大小。
     */
    private static final int MINIMUM_PREVIEW_SIZE = 299;

    /**
     * 從屏幕旋轉到JPEG方向的轉換。
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    /**
     * 用於防止應用在關閉相機之前退出的 {@link Semaphore}。
     */
    private final Semaphore cameraOpenCloseLock = new Semaphore(1);

    /**
     * TensorFlow期望的輸入大小（正方形位圖的寬度和高度）。
     */
    private final Size inputSize;

    /**
     * 用於此Fragment膨脹的佈局識別符。
     */
    private final int layout;

    private final CameraCaptureSession.CaptureCallback captureCallback =
            new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureProgressed(
                        final CameraCaptureSession session,
                        final CaptureRequest request,
                        final CaptureResult partialResult) {
                }

                @Override
                public void onCaptureCompleted(
                        final CameraCaptureSession session,
                        final CaptureRequest request,
                        final TotalCaptureResult result) {
                }
            };

    /**
     * 當前 {@link CameraDevice} 的ID。
     */
    private String cameraId;

    /**
     * 用於相機預覽的 {@link AutoFitTextureView}。
     */
    private AutoFitTextureView textureView;

    /**
     * 用於相機預覽的 {@link CameraCaptureSession }。
     */
    private CameraCaptureSession captureSession;

    /**
     * 對打開的 {@link CameraDevice} 的引用。
     */
    private CameraDevice cameraDevice;

    /**
     * 來自顯示的相機傳感器的旋轉（度）。
     */
    private Integer sensorOrientation;

    /**
     * 相機預覽的 {@link android.util.Size}。
     */
    private Size previewSize;

    /**
     * 用於運行不應該阻塞UI的任務的附加線程。
     */
    private HandlerThread backgroundThread;

    /**
     * 用於在後台運行任務的 {@link Handler}。
     */
    private Handler backgroundHandler;

    /**
     * 用於處理預覽幀捕獲的 {@link ImageReader}。
     */
    private ImageReader previewReader;

    /**
     * 用於相機預覽的 {@link CaptureRequest.Builder}。
     */
    private CaptureRequest.Builder previewRequestBuilder;

    /**
     * 由 {@link #previewRequestBuilder} 生成的 {@link CaptureRequest}。
     */
    private CaptureRequest previewRequest;

    /**
     * 用於在可用時接收幀的 {@link OnImageAvailableListener}。
     */
    private OnImageAvailableListener imageListener;

    /**
     * 當 {@link CameraDevice} 更改其狀態時調用的 {@link android.hardware.camera2.CameraDevice.StateCallback}。
     */
    private final CameraDevice.StateCallback stateCallback =
            new CameraDevice.StateCallback() {
                @Override
                public void onOpened(final CameraDevice cd) {
                    // 當相機打開時調用此方法。我們在這裡啟動相機預覽。
                    cameraDevice = cd;
                    createCameraPreviewSession();
                    cameraOpenCloseLock.release();
                }

                @Override
                public void onDisconnected(final CameraDevice cd) {
                    cameraOpenCloseLock.release();
                    cd.close();
                    cameraDevice = null;
                }

                @Override
                public void onError(final CameraDevice cd, final int error) {
                    cameraOpenCloseLock.release();
                    cd.close();
                    cameraDevice = null;
                    final Activity activity = getActivity();
                    if (null != activity) {
                        activity.finish();
                    }
                }
            };

    private ConnectionCallback cameraConnectionCallback;

    /**
     * {@link android.view.TextureView.SurfaceTextureListener} 處理 {@link TextureView} 上的幾個生命週期事件。
     */
    private final TextureView.SurfaceTextureListener surfaceTextureListener =
            new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(
                        final SurfaceTexture texture, final int width, final int height) {
                    openCamera(width, height);
                }

                @Override
                public void onSurfaceTextureSizeChanged(
                        final SurfaceTexture texture, final int width, final int height) {
                    configureTransform(width, height);
                }

                @Override
                public boolean onSurfaceTextureDestroyed(final SurfaceTexture texture) {
                    return true;
                }

                @Override
                public void onSurfaceTextureUpdated(final SurfaceTexture texture) {
                }
            };

    public CameraConnectionFragment() {
        this.layout = R.layout.camera_connection_fragment;
        this.inputSize = new Size(299, 299);
        this.cameraId = CameraActivity.cameraId;
    }

    /**
     * 給定相機支持的 {@code Size} 選擇，選擇寬度和高度至少與最小值相同的最小選項，或者如果可能，選擇一個確切的匹配。
     *
     * @param choices 相機支持的用於所需輸出類別的大小列表
     * @param width   所需最小寬度
     * @param height  所需最小高度
     * @return 最優 {@code Size}，如果沒有足夠大的，則是任意的一個
     */
    protected static Size chooseOptimalSize(final Size[] choices, final int width, final int height) {
        final int minSize = Math.max(Math.min(width, height), MINIMUM_PREVIEW_SIZE);
        final Size desiredSize = new Size(width, height);

        // 收集支持的分辨率，至少與預覽Surface一樣大
        boolean exactSizeFound = false;
        final List<Size> bigEnough = new ArrayList<Size>();

        for (final Size option : choices) {
            if (option.equals(desiredSize)) {
                // 設置大小，但不要馬上返回，以便仍然可以記錄剩餘的大小。
                exactSizeFound = true;
            }

            if (option.getHeight() >= minSize && option.getWidth() >= minSize) {
                bigEnough.add(option);
            }
        }

        if (exactSizeFound) {
            return desiredSize;
        }

        // 選擇這些中最小的，假設我們找到了
        if (bigEnough.size() > 0) {
            final Size chosenSize = Collections.min(bigEnough, new CompareSizesByArea());
            return chosenSize;
        } else {
            return choices[0];
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.cameraConnectionCallback = new ConnectionCallback() {
            @Override
            public void onPreviewSizeChosen(Size size, int rotation) {
                ((CameraActivity) requireContext()).onPreviewSizeChosen(size, rotation);
            }
        };

        this.imageListener = (CameraActivity) requireContext();
    }

    /**
     * 在UI線程上顯示 {@link Toast}。
     *
     * @param text 要顯示的消息
     */
    private void showToast(final String text) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(
                    () -> Toast.makeText(requireContext().getApplicationContext(), text, Toast.LENGTH_SHORT).show());
        }
    }

    @Override
    public View onCreateView(
            final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        return inflater.inflate(layout, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        textureView = view.findViewById(R.id.texture);
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();

        // 當屏幕關閉並重新打開時，SurfaceTexture已經可用，並且不會調用“onSurfaceTextureAvailable”。
        // 在這種情況下，我們可以從這裡打開相機並開始預覽（否則，我們等待SurfaceTextureListener中的Surface就緒）。
        if (textureView.isAvailable()) {
            openCamera(textureView.getWidth(), textureView.getHeight());
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    /**
     * 設置與相機相關的成員變量。
     */
    private void setUpCameraOutputs() {
        final Activity activity = getActivity();
        final CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            final CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

            final StreamConfigurationMap map =
                    characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

            sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);

            // 危險，W.R.! 嘗試使用太大的預覽大小可能會超過相機匯流排的帶寬限制，
            // 導致美麗的預覽但存儲了垃圾捕獲數據。
            previewSize =
                    chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                            inputSize.getWidth(),
                            inputSize.getHeight());

            // 我們將TextureView的寬高比設置為我們選擇的預覽大小。
            final int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                textureView.setAspectRatio(previewSize.getWidth(), previewSize.getHeight());
            } else {
                textureView.setAspectRatio(previewSize.getHeight(), previewSize.getWidth());
            }
        } catch (final CameraAccessException ignored) {
        } catch (final NullPointerException e) {
            // 使用Camera2API，但不支持運行此代碼的設備時，當前會拋出NPE。
            throw new RuntimeException(getString(R.string.camera_error));
        }

        cameraConnectionCallback.onPreviewSizeChosen(previewSize, sensorOrientation);
    }

    /**
     * Opens the camera specified by {@link CameraConnectionFragment#cameraId}.
     */
    private void openCamera(final int width, final int height) {
        setUpCameraOutputs();
        configureTransform(width, height);
        final Activity activity = getActivity();
        final CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            manager.openCamera(cameraId, stateCallback, backgroundHandler);
        } catch (final CameraAccessException e) {
        } catch (final InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    /**
     * Closes the current {@link CameraDevice}.
     */
    private void closeCamera() {
        try {
            cameraOpenCloseLock.acquire();
            if (null != captureSession) {
                captureSession.close();
                captureSession = null;
            }
            if (null != cameraDevice) {
                cameraDevice.close();
                cameraDevice = null;
            }
            if (null != previewReader) {
                previewReader.close();
                previewReader = null;
            }
        } catch (final InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            cameraOpenCloseLock.release();
        }
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        backgroundThread = new HandlerThread("ImageListener");
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        backgroundThread.quitSafely();
        try {
            backgroundThread.join();
            backgroundThread.quit();
            backgroundThread = null;
            backgroundHandler = null;
        } catch (final InterruptedException e) {
        }
    }

    /**
     * Creates a new {@link CameraCaptureSession} for camera preview.
     */
    private void createCameraPreviewSession() {
        try {
            final SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;

            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());

            // This is the output Surface we need to start preview.
            final Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            previewRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            previewRequestBuilder.addTarget(surface);

            // Create the reader for the preview frames.
            previewReader =
                    ImageReader.newInstance(
                            previewSize.getWidth(), previewSize.getHeight(), ImageFormat.YUV_420_888, 2);

            previewReader.setOnImageAvailableListener(imageListener, backgroundHandler);
            previewRequestBuilder.addTarget(previewReader.getSurface());

            // Here, we create a CameraCaptureSession for camera preview.
            cameraDevice.createCaptureSession(
                    Arrays.asList(surface, previewReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull final CameraCaptureSession cameraCaptureSession) {
                            // The camera is already closed
                            if (null == cameraDevice) {
                                return;
                            }

                            // When the session is ready, we start displaying the preview.
                            captureSession = cameraCaptureSession;
                            try {
                                // Auto focus should be continuous for camera preview.
                                previewRequestBuilder.set(
                                        CaptureRequest.CONTROL_AF_MODE,
                                        CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                // Flash is automatically enabled when necessary.
                                previewRequestBuilder.set(
                                        CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

                                // Finally, we start displaying the camera preview.
                                previewRequest = previewRequestBuilder.build();
                                /*
                                 * @throws IllegalStateException if this session is no longer active, either because the session
                                 *                               was explicitly closed, a new session has been created
                                 *                               or the camera device has been closed.
                                 */
                                // https://stackoverflow.com/questions/40146485/session-has-been-closed-further-changes-are-illegal
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (captureSession != null)
                                                captureSession.setRepeatingRequest(previewRequest,
                                                        captureCallback, null);
                                        } catch (CameraAccessException e) {
                                            showToast("Failed to start camera preview because it couldn't access camera");
                                        } catch (IllegalStateException e) {
                                            showToast("Failed to start camera preview.");
                                        }
                                    }
                                }, 500);

                            } catch (final Exception ignored) {
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull final CameraCaptureSession cameraCaptureSession) {
                            showToast("Failed");
                        }
                    },
                    null);
        } catch (final CameraAccessException ignored) {
        }
    }

    /**
     * Configures the necessary {@link android.graphics.Matrix} transformation to `mTextureView`.
     * This method should be called after the camera preview size is determined in
     * setUpCameraOutputs and also the size of `mTextureView` is fixed.
     *
     * @param viewWidth  The width of `mTextureView`
     * @param viewHeight The height of `mTextureView`
     */
    private void configureTransform(final int viewWidth, final int viewHeight) {
        final Activity activity = getActivity();
        if (null == textureView || null == previewSize || null == activity) {
            return;
        }
        final int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        final Matrix matrix = new Matrix();
        final RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        final RectF bufferRect = new RectF(0, 0, previewSize.getHeight(), previewSize.getWidth());
        final float centerX = viewRect.centerX();
        final float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            final float scale =
                    Math.max(
                            (float) viewHeight / previewSize.getHeight(),
                            (float) viewWidth / previewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        textureView.setTransform(matrix);
    }

    /**
     * Callback for Activities to use to initialize their data once the
     * selected preview size is known.
     */
    public interface ConnectionCallback {
        void onPreviewSizeChosen(Size size, int cameraRotation);
    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
    static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(final Size lhs, final Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum(
                    (long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
        }
    }

}
