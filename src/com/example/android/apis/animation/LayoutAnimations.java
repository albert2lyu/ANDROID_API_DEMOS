/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.apis.animation;

// Need the following import to get access to the app resources, since this
// class is in a sub-package.
import android.animation.Animator;
import android.animation.ObjectAnimator;
import com.example.android.apis.R;

import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.LayoutTransition;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

/**
 * This application demonstrates how to use LayoutTransition to automate transition animations
 * as items are removed from or added to a container.
 */
/**
 * API动画效果之：给ViewGroup中的控件设置动画效果
 * 
 * @description：
 * @author ldm
 * @date 2016-5-9 上午9:28:52
 */
public class LayoutAnimations extends Activity {

	private int numButtons = 1;
	ViewGroup container = null;
	Animator defaultAppearingAnim, defaultDisappearingAnim;
	Animator defaultChangingAppearingAnim, defaultChangingDisappearingAnim;
	Animator customAppearingAnim, customDisappearingAnim;
	Animator customChangingAppearingAnim, customChangingDisappearingAnim;
	Animator currentAppearingAnim, currentDisappearingAnim;
	Animator currentChangingAppearingAnim, currentChangingDisappearingAnim;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_animations);

		container = new FixedGridLayout(this);
		container.setClipChildren(false);
		// 设置自定义ViewGroup中子控件的高度和宽度
		((FixedGridLayout) container).setCellHeight(90);
		((FixedGridLayout) container).setCellWidth(100);
		// LayoutTransition:布局过渡效果,这是Material
		// design的重要方面，帮助用户理解app的操作流程，用户切换视图时把不同组建有机结合起来
		final LayoutTransition transitioner = new LayoutTransition();
		// 通过setLayoutTransition()方法将对象设置进一个布局容器ViewGroup中
		container.setLayoutTransition(transitioner);
		/**
		 * 获取到动画效果 
		 * LayoutTransition.APPEARING： 当一个View在ViewGroup中出现时，对此View设置的动画。
		 * LayoutTransition.CHANGE_APPEARING： 当一个View在ViewGroup中出现时，对此View对其他View位置造成影响，对其他View设置的动画
		 * LayoutTransition.DISAPPEARING： 当一个View在ViewGroup中消失时，对此View设置的动画
		 * LayoutTransition.CHANGE_DISAPPEARING： 当一个View在ViewGroup中消失时，对此View对其他View位置造成影响，对其他View设置的动画
		 * LayoutTransition.CHANGE： 不是由于View出现或消失造成对其他View位置造成影响，然后对其他View设置的动画。
		 */
		defaultAppearingAnim = transitioner
				.getAnimator(LayoutTransition.APPEARING);
		defaultDisappearingAnim = transitioner
				.getAnimator(LayoutTransition.DISAPPEARING);
		defaultChangingAppearingAnim = transitioner
				.getAnimator(LayoutTransition.CHANGE_APPEARING);
		defaultChangingDisappearingAnim = transitioner
				.getAnimator(LayoutTransition.CHANGE_DISAPPEARING);
		createCustomAnimations(transitioner);
		currentAppearingAnim = defaultAppearingAnim;
		currentDisappearingAnim = defaultDisappearingAnim;
		currentChangingAppearingAnim = defaultChangingAppearingAnim;
		currentChangingDisappearingAnim = defaultChangingDisappearingAnim;

		ViewGroup parent = (ViewGroup) findViewById(R.id.parent);
		//代码中动态添加View
		parent.addView(container);
		parent.setClipChildren(false);
		Button addButton = (Button) findViewById(R.id.add_btn);
		addButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Button newButton = new Button(LayoutAnimations.this);
				newButton.setText(String.valueOf(numButtons++));
				newButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						//点击Button则移除对应Button
						container.removeView(v);
					}
				});
				container.addView(newButton,
						Math.min(1, container.getChildCount()));
			}
		});

		CheckBox customAnimCB = (CheckBox) findViewById(R.id.customAnimCB);
		customAnimCB
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						setupTransition(transitioner);
					}
				});

		// Check for disabled animations
		CheckBox appearingCB = (CheckBox) findViewById(R.id.appearingCB);
		appearingCB
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						setupTransition(transitioner);
					}
				});
		CheckBox disappearingCB = (CheckBox) findViewById(R.id.disappearingCB);
		disappearingCB
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						setupTransition(transitioner);
					}
				});
		CheckBox changingAppearingCB = (CheckBox) findViewById(R.id.changingAppearingCB);
		changingAppearingCB
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						setupTransition(transitioner);
					}
				});
		CheckBox changingDisappearingCB = (CheckBox) findViewById(R.id.changingDisappearingCB);
		changingDisappearingCB
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						setupTransition(transitioner);
					}
				});
	}

	/**
	 * 根据选择CheckBox对应的动画效果，设置动画
	 * 
	 * @description：
	 * @author ldm
	 * @date 2016-5-9 上午9:43:58
	 */
	private void setupTransition(LayoutTransition transition) {
		CheckBox customAnimCB = (CheckBox) findViewById(R.id.customAnimCB);
		CheckBox appearingCB = (CheckBox) findViewById(R.id.appearingCB);
		CheckBox disappearingCB = (CheckBox) findViewById(R.id.disappearingCB);
		CheckBox changingAppearingCB = (CheckBox) findViewById(R.id.changingAppearingCB);
		CheckBox changingDisappearingCB = (CheckBox) findViewById(R.id.changingDisappearingCB);
		transition.setAnimator(LayoutTransition.APPEARING, appearingCB
				.isChecked() ? (customAnimCB.isChecked() ? customAppearingAnim
				: defaultAppearingAnim) : null);
		transition
				.setAnimator(
						LayoutTransition.DISAPPEARING,
						disappearingCB.isChecked() ? (customAnimCB.isChecked() ? customDisappearingAnim
								: defaultDisappearingAnim)
								: null);
		transition
				.setAnimator(
						LayoutTransition.CHANGE_APPEARING,
						changingAppearingCB.isChecked() ? (customAnimCB
								.isChecked() ? customChangingAppearingAnim
								: defaultChangingAppearingAnim) : null);
		transition
				.setAnimator(
						LayoutTransition.CHANGE_DISAPPEARING,
						changingDisappearingCB.isChecked() ? (customAnimCB
								.isChecked() ? customChangingDisappearingAnim
								: defaultChangingDisappearingAnim) : null);
	}

	private void createCustomAnimations(LayoutTransition transition) {
		// Changing while Adding
		PropertyValuesHolder pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1);
		PropertyValuesHolder pvhTop = PropertyValuesHolder.ofInt("top", 0, 1);
		PropertyValuesHolder pvhRight = PropertyValuesHolder.ofInt("right", 0,
				1);
		PropertyValuesHolder pvhBottom = PropertyValuesHolder.ofInt("bottom",
				0, 1);
		PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofFloat("scaleX",
				1f, 0f, 1f);
		PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofFloat("scaleY",
				1f, 0f, 1f);
		customChangingAppearingAnim = ObjectAnimator.ofPropertyValuesHolder(
				this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX,
				pvhScaleY).setDuration(
				transition.getDuration(LayoutTransition.CHANGE_APPEARING));
		customChangingAppearingAnim.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setScaleX(1f);
				view.setScaleY(1f);
			}
		});

		// Changing while Removing
		Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
		Keyframe kf1 = Keyframe.ofFloat(.9999f, 360f);
		Keyframe kf2 = Keyframe.ofFloat(1f, 0f);
		PropertyValuesHolder pvhRotation = PropertyValuesHolder.ofKeyframe(
				"rotation", kf0, kf1, kf2);
		customChangingDisappearingAnim = ObjectAnimator
				.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight,
						pvhBottom, pvhRotation)
				.setDuration(
						transition
								.getDuration(LayoutTransition.CHANGE_DISAPPEARING));
		customChangingDisappearingAnim
				.addListener(new AnimatorListenerAdapter() {
					public void onAnimationEnd(Animator anim) {
						View view = (View) ((ObjectAnimator) anim).getTarget();
						view.setRotation(0f);
					}
				});

		// Adding
		customAppearingAnim = ObjectAnimator
				.ofFloat(null, "rotationY", 90f, 0f).setDuration(
						transition.getDuration(LayoutTransition.APPEARING));
		customAppearingAnim.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationY(0f);
			}
		});

		// Removing
		customDisappearingAnim = ObjectAnimator.ofFloat(null, "rotationX", 0f,
				90f).setDuration(
				transition.getDuration(LayoutTransition.DISAPPEARING));
		customDisappearingAnim.addListener(new AnimatorListenerAdapter() {
			public void onAnimationEnd(Animator anim) {
				View view = (View) ((ObjectAnimator) anim).getTarget();
				view.setRotationX(0f);
			}
		});

	}
}