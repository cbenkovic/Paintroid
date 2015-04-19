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

package org.catrobat.paintroid.test.integration.tools;

import org.catrobat.paintroid.PaintroidApplication;
import org.catrobat.paintroid.R;
import org.catrobat.paintroid.test.integration.BaseIntegrationTestClass;
import org.catrobat.paintroid.test.utils.PrivateAccess;
import org.catrobat.paintroid.tools.ToolType;
import org.catrobat.paintroid.tools.implementation.BaseToolWithRectangleShape;
import org.junit.After;
import org.junit.Before;

import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.robotium.solo.Condition;

public class BaseToolWithRectangleShapeTest extends BaseIntegrationTestClass {

	protected boolean mRotationViewsAreSet = false;
	protected View mButtonOpenDialog;
	protected View mButtonRotateLeft;
	protected View mButtonRotateRight;
	protected View mRadioButton30;
	protected View mRadioButton45;
	protected View mRadioButton90;
	protected SeekBar mAngleSeekBar;
	protected TextView mAngleSeekBarText;
	protected RadioGroup mAngleSelection;
	protected CheckBox mSnapCheckBox;

	public BaseToolWithRectangleShapeTest() throws Exception {
		super();
	}

	@Override
	@Before
	protected void setUp() {
		super.setUp();
	}

	@Override
	@After
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testRotationDialog() throws NoSuchFieldException, IllegalAccessException {
		selectTool(ToolType.RECT);
		openRotationSettingsDialog();

		// test default dialog values
		int noRadioBtnSelected = -1;
		assertTrue("Rotation dialog title not found", mSolo.searchText(
				mSolo.getString(R.string.dialog_rotation_settings_text)));
		assertEquals("No radio button should be selected", noRadioBtnSelected, mAngleSelection.getCheckedRadioButtonId());
		String expected = String.valueOf((Integer) PrivateAccess.getMemberValue(BaseToolWithRectangleShape.class,
				PaintroidApplication.currentTool, "DEFAULT_ROTATION_ANGLE"));
		assertEquals("Selected angle should be " + expected, expected, mAngleSeekBarText.getText().toString());
		assertFalse("Snapping checkbox should not be activated", mSnapCheckBox.isPressed());
		assertTrue("Done button text not found", mSolo.searchText(
				mSolo.getString(R.string.done)));

		// test different values
		mSolo.clickOnView(mRadioButton30);
		assertTrue("Radio button not pressed, 30 should be selected", mSolo.waitForText("30", 2, MEDIUM_TIMEOUT));
		assertEquals("Wrong radio button is pressed", mRadioButton30.getId(), mAngleSelection.getCheckedRadioButtonId());
		assertEquals("Wrong seek bar progress", mAngleSeekBar.getProgress(), 30);

		mSolo.clickOnView(mRadioButton45);
		assertTrue("Radio button not pressed, 45 should be selected", mSolo.waitForText("45", 2, MEDIUM_TIMEOUT));
		assertEquals("Wrong radio button is pressed", mRadioButton45.getId(), mAngleSelection.getCheckedRadioButtonId());
		assertEquals("Wrong seek bar progress", mAngleSeekBar.getProgress(), 45);

		mSolo.clickOnView(mRadioButton90);
		assertTrue("Radio button not pressed, 90 should be selected", mSolo.waitForText("90", 2, MEDIUM_TIMEOUT));
		assertEquals("Wrong radio button is pressed", mRadioButton90.getId(), mAngleSelection.getCheckedRadioButtonId());
		assertEquals("Wrong seek bar progress", mAngleSeekBar.getProgress(), 90);

		mSolo.clickOnView(mAngleSeekBar);
		assertTrue("Incorrect seek bar text", mSolo.waitForText(String.valueOf(mAngleSeekBar.getMax()/2)));
		assertEquals("No radio button should be selected", noRadioBtnSelected, mAngleSelection.getCheckedRadioButtonId());

		mSolo.clickOnView(mSnapCheckBox);
		mSolo.waitForCondition(new Condition() {
			@Override
			public boolean isSatisfied() {
				return mSnapCheckBox.isChecked();
			}
		}, MEDIUM_TIMEOUT);
		assertTrue("Checkbox should be checked", mSnapCheckBox.isChecked());

		// test if values are held after closing dialog and reopen it
		mSolo.clickOnView(mRadioButton45);
		assertTrue("Radio button not pressed, 45 should be selected", mSolo.waitForText("45", 2, MEDIUM_TIMEOUT));
		int checkedRadioBtnBefore = mAngleSelection.getCheckedRadioButtonId();
		int angleSeekBarProgressBefore = mAngleSeekBar.getProgress();
		String angleSeekBarTextBefore = mAngleSeekBarText.getText().toString();
		boolean snapCheckboxIsChecked = mSnapCheckBox.isChecked();

		mSolo.clickOnText(mSolo.getString(R.string.done));
		assertTrue("Rotation dialog does not close", mSolo.waitForDialogToClose());
		openRotationSettingsDialog();
		assertEquals("Wrong radio button is checked after reopen dialog",
				checkedRadioBtnBefore, mAngleSelection.getCheckedRadioButtonId());
		assertEquals("Incorrect angle seek bar progress after reopen dialog",
				angleSeekBarProgressBefore, mAngleSeekBar.getProgress());
		assertEquals("Wrong seek bar text after reopen dialog",
				angleSeekBarTextBefore, mAngleSeekBarText.getText().toString());
		assertEquals("checkbox should be checked after reopen dialog",
				snapCheckboxIsChecked, mSnapCheckBox.isChecked());
	}

	public void testRotateWithAngleBySelection() throws NoSuchFieldException, IllegalAccessException {
		selectTool(ToolType.RECT);
		initializeRotationSettingsDialog();

		// test right rotation button
		selectRotationAngle(mRadioButton45);
		float expectedValues[] = new float[] { 45, 90, 135, 180, -135, -90, -45, 0 };
		for (float expected : expectedValues) {
			mSolo.clickOnView(mButtonRotateRight);
			mSolo.sleep(SHORT_SLEEP);
			assertEquals("wrong box rotation value" + expected, expected, getBoxRotation());
		}

		// test left rotation button
		selectRotationAngle(mRadioButton30);
		mSolo.clickOnView(mButtonRotateLeft);
		mSolo.sleep(SHORT_SLEEP);
		assertEquals("wrong box rotation value", -30.0f, getBoxRotation());

		selectRotationAngle(mRadioButton90);
		mSolo.clickOnView(mButtonRotateLeft);
		mSolo.sleep(SHORT_SLEEP);
		assertEquals("wrong box rotation value", -120.0f, getBoxRotation());

		selectRotationAngle(mAngleSeekBar);
		mSolo.clickOnView(mButtonRotateLeft);
		mSolo.sleep(SHORT_SLEEP);
		assertEquals("wrong box rotation value", 60.0f, getBoxRotation());


	}

	public void testRotateWithAngleBySeekBar() {

	}

	public void testRotateBySnapping() {
		selectTool(ToolType.RECT);
		assertFalse("Rotate Angle Button should be shown", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertFalse("Rotate Left Button should be shown", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertFalse("Rotate Right Button should be shown", (getActivity().findViewById(R.id.rotation_btn_right) == null));

		mSolo.clickOnView(mButtonOpenDialog);
		mSolo.waitForDialogToOpen();



	}

	public void testRotationButtonsAreShownWithCorrectTools() {
		//mSolo.getView(R.id.rotation_btn_left)
		assertTrue("Rotate Angle Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertTrue("Rotate Left Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertTrue("Rotate Right Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_right) == null));

		selectTool(ToolType.ELLIPSE);
		assertFalse("Rotate Angle Button should be shown", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertFalse("Rotate Left Button should be shown", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertFalse("Rotate Right Button should be shown", (getActivity().findViewById(R.id.rotation_btn_right) == null));

		selectTool(ToolType.ERASER);
		assertTrue("Rotate Angle Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertTrue("Rotate Left Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertTrue("Rotate Right Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_right) == null));

		selectTool(ToolType.RECT);
		assertFalse("Rotate Angle Button should be shown", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertFalse("Rotate Left Button should be shown", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertFalse("Rotate Right Button should be shown", (getActivity().findViewById(R.id.rotation_btn_right) == null));

		selectTool(ToolType.FILL);
		assertTrue("Rotate Angle Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertTrue("Rotate Left Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertTrue("Rotate Right Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_right) == null));

		selectTool(ToolType.STAMP);
		assertFalse("Rotate Angle Button should be shown", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertFalse("Rotate Left Button should be shown", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertFalse("Rotate Right Button should be shown", (getActivity().findViewById(R.id.rotation_btn_right) == null));

		selectTool(ToolType.CROP);
		assertTrue("Rotate Angle Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_angle) == null));
		assertTrue("Rotate Left Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_left) == null));
		assertTrue("Rotate Right Button should not be visible", (getActivity().findViewById(R.id.rotation_btn_right) == null));

	}



	@Override
	protected void selectTool(ToolType toolType) {
		super.selectTool(toolType);

		mButtonOpenDialog = getActivity().findViewById(R.id.rotation_btn_angle);
		mButtonRotateLeft = getActivity().findViewById(R.id.rotation_btn_left);
		mButtonRotateRight = getActivity().findViewById(R.id.rotation_btn_right);

	}

	protected void initializeRotationSettingsDialog() {
		openRotationSettingsDialog();

		View view = mSolo.getView(R.id.layout_rotation);
		mAngleSelection = (RadioGroup) view.findViewById(R.id.rotation_angle_selection);
		mRadioButton30 = view.findViewById(R.id.rotation_rbtn_30);
		mRadioButton45 = view.findViewById(R.id.rotation_rbtn_45);
		mRadioButton90 = view.findViewById(R.id.rotation_rbtn_90);
		mAngleSeekBar = (SeekBar) view.findViewById(R.id.rotation_angle_seek_bar);
		mAngleSeekBarText = (TextView) view.findViewById(R.id.rotation_angle_seek_bar_text);
		mSnapCheckBox = (CheckBox) view.findViewById(R.id.rotation_snap_checkbox);
		mRotationViewsAreSet = true;

		closeRotationSettingsDialog();
	}

	protected void openRotationSettingsDialog() {
		mSolo.clickOnView(mButtonOpenDialog);
		mSolo.waitForDialogToOpen(MEDIUM_TIMEOUT);
		assertTrue("Rotation dialog title not found", mSolo.searchText(
				mSolo.getString(R.string.dialog_rotation_settings_text)));

		if (!mRotationViewsAreSet) {
			View view = mSolo.getView(R.id.layout_rotation);
			mAngleSelection = (RadioGroup) view.findViewById(R.id.rotation_angle_selection);
			mRadioButton30 = view.findViewById(R.id.rotation_rbtn_30);
			mRadioButton45 = view.findViewById(R.id.rotation_rbtn_45);
			mRadioButton90 = view.findViewById(R.id.rotation_rbtn_90);
			mAngleSeekBar = (SeekBar) view.findViewById(R.id.rotation_angle_seek_bar);
			mAngleSeekBarText = (TextView) view.findViewById(R.id.rotation_angle_seek_bar_text);
			mSnapCheckBox = (CheckBox) view.findViewById(R.id.rotation_snap_checkbox);
			mRotationViewsAreSet = true;
		}

	}

	protected void closeRotationSettingsDialog() {
		mSolo.clickOnText(mSolo.getString(R.string.done));
		assertTrue("Rotation dialog does not close", mSolo.waitForDialogToClose());
	}

	protected void selectRotationAngle(View view) {
		openRotationSettingsDialog();

		if (view.getId() == mRadioButton30.getId()) {
			mSolo.clickOnView(mRadioButton30);
			assertTrue("Radio button not pressed, 30 should be selected", mSolo.waitForText("30", 2, MEDIUM_TIMEOUT));
		}
		else if (view.getId() == mRadioButton45.getId()) {
			mSolo.clickOnView(mRadioButton45);
			assertTrue("Radio button not pressed, 45 should be selected", mSolo.waitForText("45", 2, MEDIUM_TIMEOUT));
		}
		else if (view.getId() == mRadioButton90.getId()) {
			mSolo.clickOnView(mRadioButton90);
			assertTrue("Radio button not pressed, 90 should be selected", mSolo.waitForText("90", 2, MEDIUM_TIMEOUT));
		}
		else if (view.getId() == mAngleSeekBar.getId()) {
			mSolo.clickOnView(mAngleSeekBar);
			int expectedAngle = mAngleSeekBar.getMax()/2;
			assertTrue("Wrong rotation angle, should be " + expectedAngle,
					mSolo.waitForText(String.valueOf(expectedAngle), 1, MEDIUM_TIMEOUT));
		}
		else {
			assertTrue("View not handled", false);
		}

		closeRotationSettingsDialog();
	}

	protected float getBoxRotation() throws NoSuchFieldException, IllegalAccessException {
		float boxRotation = ((Float) PrivateAccess.getMemberValue(BaseToolWithRectangleShape.class,
				PaintroidApplication.currentTool, "mBoxRotation")).floatValue();
		return boxRotation;
	}

}
