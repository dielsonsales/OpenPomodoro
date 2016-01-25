package me.dielsonsales.app.openpomodoro.presentation.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import me.dielsonsales.app.openpomodoro.R;

public class TimePreference extends DialogPreference implements NumberPicker.OnValueChangeListener {
    private static final String TAG = "TimePreference";
    private static final String DEFAULT_VALUE = "00:00:00";

    public static final NumberPicker.Formatter TWO_DIGITS_FORMATTER =
            new NumberPicker.Formatter() {
                @Override
                public String format(int value) {
                    // TODO Auto-generated method stub
                    return String.format("%02d", value);
                }
            };

    // state
    private String mCurrentTime;

    // ui components
    private NumberPicker mHourPicker;
    private NumberPicker mMinutePicker;
    private NumberPicker mSecondPicker;

    public TimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.time_picker_widget);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue) {
            mCurrentTime = getPersistedString(DEFAULT_VALUE);
        } else {
            persistString(defaultValue.toString());
            mCurrentTime = defaultValue.toString();
        }
        setSummary(mCurrentTime);
    }

    @Override
    protected void onBindDialogView(View view) {
        String[] values = mCurrentTime.split(":");
        int hours = Integer.valueOf(values[0]);
        int minutes = Integer.valueOf(values[1]);
        int seconds = Integer.valueOf(values[2]);

        mHourPicker = (NumberPicker) view.findViewById(R.id.hour);
        mHourPicker.setMinValue(0);
        mHourPicker.setMaxValue(10);
        mHourPicker.setFormatter(TWO_DIGITS_FORMATTER);
        mHourPicker.setValue(hours);
        mHourPicker.setOnValueChangedListener(this); // min time is 5s

        mMinutePicker = (NumberPicker) view.findViewById(R.id.minute);
        mMinutePicker.setMinValue(0);
        mMinutePicker.setMaxValue(59);
        mMinutePicker.setFormatter(TWO_DIGITS_FORMATTER);
        mMinutePicker.setValue(minutes);
        mMinutePicker.setOnValueChangedListener(this); // min time is 5s

        mSecondPicker = (NumberPicker) view.findViewById(R.id.seconds);
        mSecondPicker.setMinValue(0); // see onValueChange method
        mSecondPicker.setMaxValue(59);
        mSecondPicker.setFormatter(TWO_DIGITS_FORMATTER);
        mSecondPicker.setValue(seconds);
        mSecondPicker.setOnValueChangedListener(this); // min time is 5s

        super.onBindDialogView(view);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int hours = mHourPicker.getValue();
            int minutes = mMinutePicker.getValue();
            int seconds = mSecondPicker.getValue();
            String stringValue = generatePrefString(hours, minutes, seconds);
            mCurrentTime = stringValue;
            persistString(stringValue);
            setSummary(stringValue);
        }
    }

    /**
     * Sets the second minValue to 5 if all the other NumberPickers are 0.
     */
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        if (mMinutePicker.getValue() == 0 && mHourPicker.getValue() == 0) {
            mSecondPicker.setMinValue(5);
        } else {
            mSecondPicker.setMinValue(0);
        }
    }

    // Private methods ---------------------------------------------------------

    private String generatePrefString(int hours, int minutes, int seconds) {
        String formattedString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        return formattedString;
    }
}
