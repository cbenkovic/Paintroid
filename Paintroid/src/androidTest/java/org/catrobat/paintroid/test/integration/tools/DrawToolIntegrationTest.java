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

import android.graphics.PointF;

import org.catrobat.paintroid.test.integration.BaseIntegrationTestClass;
import org.catrobat.paintroid.test.utils.Utils;
import org.catrobat.paintroid.tools.ToolType;
import org.catrobat.paintroid.ui.DrawingSurface;
import org.junit.Before;

public class DrawToolIntegrationTest extends BaseIntegrationTestClass {

	private static final int MOVE_STEP_COUNT = 50;

	public DrawToolIntegrationTest() throws Exception {
		super();
	}

	@Override
	@Before
	protected void setUp() {
		super.setUp();
	}

	public void testDisableMultiTouchWhenDrawing() {
		assertTrue("Waiting for DrawingSurface", mSolo.waitForView(DrawingSurface.class, 1, TIMEOUT));
		selectTool(ToolType.BRUSH);

		PointF screenPoint = new PointF(mScreenWidth / 2, mScreenHeight / 2);
		PointF canvasPoint = Utils.getCanvasPointFromScreenPoint(screenPoint);

/*
		try {
			runTestOnUiThread(new Runnable() {
				@Override
				public void run() {
					mSolo.drag(mScreenWidth / 2, 0, mScreenHeight / 2, mScreenHeight / 2, MOVE_STEP_COUNT);
				}
			});
			mSolo.sleep(5000);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}


*/

	}

}
