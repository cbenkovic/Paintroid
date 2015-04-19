/**
 *  Paintroid: An image manipulation application for Android.
 *  Copyright (C) 2010-2015 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.paintroid.dialog;

import java.util.ArrayList;

import org.catrobat.paintroid.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;


@SuppressLint("ValidFragment")
public final class RotationDialog extends DialogFragment implements
		OnClickListener, DialogInterface.OnClickListener {

	private static final String NOT_INITIALIZED_ERROR_MESSAGE = "BrushPickerDialog has not been initialized. Call init() first!";

	private static RotationDialog instance;
	private Context mContext;
	private ArrayList<OnRotationChangedListener> mRotationChangedListener;

	private int mRotationAngle;
	private boolean mSnappingIsActivated;
	private TextView mRotationAngleSeekBarText;
	private SeekBar mRotationAngleSeekBar;
	private RadioGroup mRotationAngleRadioGroup;
	private RadioButton mSelectedAngleRadioButton = null;
	private CheckBox mRotationSnappingCheckBox;
	private AlertDialog mRotationDialog;


	public interface OnRotationChangedListener {
		public void setAngle(int angle);
		public void setSnappingActivated(boolean activated);
	}

	@SuppressLint("ValidFragment")
	private RotationDialog(Context context) {

		mRotationChangedListener = new ArrayList<RotationDialog.OnRotationChangedListener>();
		mContext = context;
	}

	public static RotationDialog getInstance() {
		if (instance == null) {
			throw new IllegalStateException(NOT_INITIALIZED_ERROR_MESSAGE);
		}
		return instance;
	}

	public static void init(Context context) {
		instance = new RotationDialog(context);
	}

	public void setCurrentValues(int rotationAngle, boolean snappingIsActivated) {
		mRotationAngle = rotationAngle;
		mSnappingIsActivated = snappingIsActivated;
		/*
		mCurrentPaint = currentPaint;
		updateStrokeCap(currentPaint.getStrokeCap());
		updateStrokeChange((int) currentPaint.getStrokeWidth());
		*/
	}


	public void addRotationChangedListener(OnRotationChangedListener listener) {
		mRotationChangedListener.add(listener);
	}

	public void removeRotationChangedListener(OnRotationChangedListener listener) {
		mRotationChangedListener.remove(listener);
	}

	private void updateRotationAngle(int angle) {
		for (OnRotationChangedListener listener : mRotationChangedListener) {
			if (listener == null) {
				mRotationChangedListener.remove(listener);
			}
			listener.setAngle(angle);
		}
	}

	private void updateSnappingIsActivated(boolean activated) {
		for (OnRotationChangedListener listener : mRotationChangedListener) {
			if (listener == null) {
				mRotationChangedListener.remove(listener);
			}
			listener.setSnappingActivated(activated);
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		LayoutInflater inflater = getActivity().getLayoutInflater();
		AlertDialog.Builder builder = new CustomAlertDialogBuilder(mContext);
		builder.setTitle(R.string.dialog_rotation_settings_text);

		//final Activity act = (Activity) mContext;
		final View view = inflater.inflate(R.layout.dialog_rotation, null);

		mRotationAngleSeekBar = (SeekBar) view.findViewById(R.id.rotation_angle_seek_bar);
		mRotationAngleSeekBarText = (TextView) view.findViewById(R.id.rotation_angle_seek_bar_text);
		mRotationAngleRadioGroup = (RadioGroup) view.findViewById(R.id.rotation_angle_selection);
		mRotationSnappingCheckBox = (CheckBox) view.findViewById(R.id.rotation_snap_checkbox);

		// set dialog values
		/*
		mRotationAngleSeekBarText.setText((String.valueOf(mRotationAngle)));
		mRotationAngleSeekBar.setProgress(mRotationAngle);
		if (mSelectedAngleRadioButton != null) {
			mRotationAngleRadioGroup.check(mSelectedAngleRadioButton.getId());
		}
		mRotationSnappingCheckBox.setChecked(mSnappingIsActivated);
		*/


		mRotationAngleRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (mRotationAngleRadioGroup.getCheckedRadioButtonId() != -1) {
					mSelectedAngleRadioButton = (RadioButton) view.findViewById(checkedId);
					int selectedAngle = Integer.parseInt(mSelectedAngleRadioButton.getText().toString());
					updateRotationAngle(selectedAngle);
					mRotationAngleSeekBar.setProgress(selectedAngle);
				}
			}
		});

		mRotationAngleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				mRotationAngleSeekBarText.setText(String.valueOf(progress));
				if (fromUser) {
					updateRotationAngle(progress);
					mSelectedAngleRadioButton = null;
					mRotationAngleRadioGroup.clearCheck();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		mRotationSnappingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				updateSnappingIsActivated(isChecked);
			}
		});


		builder.setView(view);
		builder.setNeutralButton(R.string.done, this);

		return builder.create();

	}

	@Override
	public void onStart() {
		super.onStart();

		mRotationAngleSeekBarText.setText((String.valueOf(mRotationAngle)));
		mRotationAngleSeekBar.setProgress(mRotationAngle);
		if (mSelectedAngleRadioButton != null) {
			mRotationAngleRadioGroup.check(mSelectedAngleRadioButton.getId());
		}
		mRotationSnappingCheckBox.setChecked(mSnappingIsActivated);
	}

	@Override
	public void onClick(View v) {
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		switch (which) {
			case AlertDialog.BUTTON_NEUTRAL:
				dismiss();
				break;

		}
	}

}
