package org.catrobat.paintroid.dialog;

import org.catrobat.paintroid.MainActivity;
import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ZoomControls;

public class ZoomListDialog extends BaseDialog {

	public static ZoomListDialog instance;

	private static final String NOT_INITIALIZED_ERROR_MESSAGE = "ZoomListDialog has not been initialized. Call init() first!";
	private MainActivity mParent;
	private final static float ZOOM_IN_SCALE = 1.75f;
	private int selection = 5;
	private Spinner zoomSelector;
	private ZoomControls zoomControls;

	public ZoomListDialog(Context context) {
		super(context);
		mParent = (MainActivity) context;
	}

	public static ZoomListDialog getInstance() {
		if (instance == null) {
			throw new IllegalStateException(NOT_INITIALIZED_ERROR_MESSAGE);
		}
		return instance;
	}

	public static void init(MainActivity mainActivity) {
		instance = new ZoomListDialog(mainActivity);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zoom_list);
		setTitle(R.string.dialog_zoom_list_title);
		setCanceledOnTouchOutside(true);

		zoomSelector = (Spinner) findViewById(R.id.zoomSelector);
		zoomSelector.setSelection(selection);

		zoomControls = (ZoomControls) findViewById(R.id.zoomControls);
		zoomControls.setOnZoomInClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				zoomIn();
			}
		});
		zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				zoomOut();
			}
		});

		Button submitBtn = (Button) findViewById(R.id.acceptBtn);
		submitBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				changeZoomLevel(v);
			}
		});
	}

	private void zoomOut() {
		float scale = 1 / ZOOM_IN_SCALE;
		PaintroidApplication.perspective.multiplyScale(scale);
		PaintroidApplication.perspective.translate(0, 0);

	}

	private void zoomIn() {
		float scale = ZOOM_IN_SCALE;
		PaintroidApplication.perspective.multiplyScale(scale);
		PaintroidApplication.perspective.translate(0, 0);
	}

	public void changeZoomLevel(View v) {
		int itemPosition = zoomSelector.getSelectedItemPosition();
		switch (itemPosition) {
		case 0:
			PaintroidApplication.perspective.setScale(5f);
			dismiss();
			break;
		case 1:
			PaintroidApplication.perspective.setScale(4f);
			dismiss();
			break;
		case 2:
			PaintroidApplication.perspective.setScale(3f);
			dismiss();
			break;
		case 3:
			PaintroidApplication.perspective.setScale(2f);
			dismiss();
			break;
		case 4:
			PaintroidApplication.perspective.setScale(1.5f);
			dismiss();
			break;
		case 5:
			PaintroidApplication.perspective.setScale(1f);// (0.85f);
			dismiss();
			break;
		case 6:
			PaintroidApplication.perspective.setScale(0.9f);
			dismiss();
			break;
		case 7:
			PaintroidApplication.perspective.setScale(0.8f);
			dismiss();
			break;
		case 8:
			PaintroidApplication.perspective.setScale(0.7f);
			dismiss();
			break;
		case 9:
			PaintroidApplication.perspective.setScale(0.6f);
			dismiss();
			break;
		case 10:
			PaintroidApplication.perspective.setScale(0.5f);
			dismiss();
			break;
		case 11:
			PaintroidApplication.perspective.setScale(0.4f);
			dismiss();
			break;
		case 12:
			PaintroidApplication.perspective.setScale(0.3f);
			dismiss();
			break;
		case 13:
			PaintroidApplication.perspective.setScale(0.2f);
			selection = 13;
			dismiss();
			break;
		case 14:
			PaintroidApplication.perspective.setScale(0.1f);
			selection = 14;
			dismiss();
			break;
		}

	}
}