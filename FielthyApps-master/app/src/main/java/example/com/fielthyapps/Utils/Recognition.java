package example.com.fielthyapps.Utils;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;

public class Recognition {
    private final String label;
    private final float confidence;
    private final String probabilityString;

    /**
     * Constructor for Recognition item object with fields for the label and the probability.
     *
     * @param label      The label of the recognized item.
     * @param confidence The confidence score of the recognition.
     */
    @SuppressLint("DefaultLocale")
    public Recognition(String label, float confidence) {
        this.label = label;
        this.confidence = confidence;
        this.probabilityString = String.format("%.1f%%", confidence * 100.0f);
    }

    public String getLabel() {
        return label;
    }

    public float getConfidence() {
        return confidence;
    }

    public String getProbabilityString() {
        return probabilityString;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s / %s", label, probabilityString);
    }
}
