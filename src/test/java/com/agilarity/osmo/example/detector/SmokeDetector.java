/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Joseph A. Cruz
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.agilarity.osmo.example.detector;

import static com.agilarity.osmo.example.detector.SafetyStatus.EMERGENCY;
import static com.agilarity.osmo.example.detector.SafetyStatus.SAFE;
import static com.agilarity.osmo.example.detector.SafetyStatus.WARNING;

public class SmokeDetector {

	private static final int MAX_WARNING = 14;
	private static final int MAX_SAFE = 6;

	public SafetyStatus detect(final int level) {
		if (level < MAX_SAFE) {
			return SAFE;
		} else if (level < MAX_WARNING) {
			return WARNING;
		} else {
			return EMERGENCY;
		}
	}
}
