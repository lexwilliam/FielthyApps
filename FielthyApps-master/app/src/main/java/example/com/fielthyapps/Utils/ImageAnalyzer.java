package example.com.fielthyapps.Utils;

import static example.com.fielthyapps.Feature.Nutrition.FoodRecognitionActivity.HEIGHT;
import static example.com.fielthyapps.Feature.Nutrition.FoodRecognitionActivity.WIDTH;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Surface;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.image.ops.Rot90Op;
import org.tensorflow.lite.support.label.Category;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import example.com.fielthyapps.ml.LiteModelAiyVisionClassifierFoodV11;

public class ImageAnalyzer implements ImageAnalysis.Analyzer {

    private final Context ctx;
    private final RecognitionListener listener;
    private final @NonNull LiteModelAiyVisionClassifierFoodV11 model;

    public ImageAnalyzer(Context ctx, RecognitionListener listener) {
        this.ctx = ctx;
        this.listener = listener;
        try {
            this.model = LiteModelAiyVisionClassifierFoodV11.newInstance(ctx);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculate what rotation of an image is necessary before passing it to the model so as to
     * compensate for the device rotation.
     */
    private int calculateNecessaryRotation() {
        WindowManager windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        switch (windowManager.getDefaultDisplay().getRotation()) {
            case Surface.ROTATION_90:
                return 0;
            case Surface.ROTATION_270:
                return 2;
            case Surface.ROTATION_180:
                return 4;
            case Surface.ROTATION_0:
            default:
                return 3;
        }
    }

    /**
     * Analyze images from the camera stream using a Tensorflow Lite Model which performs
     * image classification of food in images.
     * Takes an ImageProxy as argument and returns the recognition results to a listener.
     */
    @OptIn(markerClass = ExperimentalGetImage.class)
    @SuppressLint("UnsafeExperimentalUsageError")
    @Override
    public void analyze(ImageProxy imageProxy) {
        List<Recognition> items = new ArrayList<>();

        // IMAGE PREPROCESSING
        ImageProcessor imageProcessor = new ImageProcessor.Builder()
                // Center crop the image
                .add(new ResizeWithCropOrPadOp(HEIGHT, WIDTH))
                // Rotate
                .add(new Rot90Op(calculateNecessaryRotation()))
                .build();
        TensorImage tImage = new TensorImage(DataType.FLOAT32);
        Bitmap bitmap = ImageUtils.toBitmap(Objects.requireNonNull(imageProxy.getImage()));
        tImage.load(bitmap);
        tImage = imageProcessor.process(tImage);

        // INFERENCE
        // Process the model
        LiteModelAiyVisionClassifierFoodV11.Outputs outputs = model.process(tImage);
        // Extract the recognition results
        List<Category> food = outputs.getProbabilityAsCategoryList();
        for (Category f : food) {
            items.add(new Recognition(f.getLabel(), f.getScore()));
        }

        // Sort the results by their confidence and return the three with the highest
        Collections.sort(items, (a, b) -> Float.compare(b.getConfidence(), a.getConfidence()));
        listener.onRecognition(items.get(0));

        // Close the image. This tells CameraX to feed the next image to the analyzer
        imageProxy.close();
    }
}
